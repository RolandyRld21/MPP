package common.jsonprotocol;

import com.google.gson.GsonBuilder;
import common.business.*;
import common.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import common.domain.Agent;
import common.domain.Flight;
import common.domain.Ticket;
import common.dto.DTOUtils;
import common.utils.LocalDateTimeTypeAdapter;
import common.utils.Observer;


public class ProjectServicesJsonProxy implements IService{

    private String host;

    private int port;

    private IObserver client;

    private BufferedReader input;

    private PrintWriter output;

    private Gson gsonFormatter;

    private Socket connection;

    private BlockingQueue<Response> qresponses;

    private volatile boolean finished;

    public ProjectServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws ProjectException {
        try {
            gsonFormatter = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(Request request) throws ProjectException{
        String reqLine = gsonFormatter.toJson(request);
        try{
            output.println(reqLine);
            output.flush();
        }catch (Exception e){
            throw new ProjectException("Error sending object " + e);
        }
    }

    private Response readResponse() throws ProjectException{
        Response response = null;
        try{
            response = qresponses.take();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Agent logInAgent(String username, String password, IObserver client) throws ProjectException {
        initializeConnection();

        Request req = JsonProtocolUtils.createLoginRequest(username, password);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            Agent agent = DTOUtils.getFromDTO(response.getAgent());
            this.client = client;
            return agent;
        }
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            closeConnection();
            throw new ProjectException(err);
        }

        return null;
    }

    public void logOutAgent(Agent agent, IObserver client) throws ProjectException {
        Request req = JsonProtocolUtils.createLogoutRequest(agent);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
        closeConnection();
    }

    @Override
    public HashMap<Flight, Integer> getAllFlightsAvailable() throws ProjectException {
        Request req = JsonProtocolUtils.createGetAllFlightsRequest();
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            HashMap<Flight, Integer> flightsAvailable = new HashMap<>();
            for(FlightDTO flightDTO : response.getFlights()){
                Flight flight = DTOUtils.getFromDTO(flightDTO);
                int availableSeats = DTOUtils.getAvailableSeatsFromDTO(flightDTO);
                flightsAvailable.put(flight, availableSeats);
            }
            return flightsAvailable;
        }
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
        HashMap<Flight, Integer> flights = new HashMap<>();
        for (FlightDTO flightDTO : response.getFlights()){
            Flight flight = DTOUtils.getFromDTO(flightDTO);
            int availableSeats = DTOUtils.getAvailableSeatsFromDTO(flightDTO);
            flights.put(flight, availableSeats);
        }
        return flights;
    }

    @Override
    public List<String> allFroms() throws ProjectException {
        Request req = JsonProtocolUtils.createGetAllFromsRequest();
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            return DTOUtils.getListOfStringFromArray(response.getData());
        }
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
        return null;
    }

    @Override
    public List<String> allTos() throws ProjectException{
        Request req = JsonProtocolUtils.createGetAllTosRequest();
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            return DTOUtils.getListOfStringFromArray(response.getData());
        }
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
        return null;
    }

    @Override
    public HashMap<Flight, Integer> searchFlights(String from, String to, LocalDateTime date) throws ProjectException {
        Request req = JsonProtocolUtils.createSearchFlightsRequest(from, to, date);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            HashMap<Flight, Integer> searchedFlights = new HashMap<>();
            for(FlightDTO flightDTO : response.getFlights()){
                Flight flight = DTOUtils.getFromDTO(flightDTO);
                int availableSeats = DTOUtils.getAvailableSeatsFromDTO(flightDTO);
                searchedFlights.put(flight, availableSeats);
            }
            return searchedFlights;
        }
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
        return null;
    }

    @Override
    public void saveTicket(Ticket ticket) throws ProjectException{
        Request req = JsonProtocolUtils.createBookFlightRequest(ticket);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new ProjectException(err);
        }
    }

    private boolean isUpdate(Response response){
        return response.getType() == ResponseType.UPDATE;
    }

    private void handleUpdate(Response response) {
        if(response.getType() == ResponseType.UPDATE){
            System.out.println("Update received");
            try{
                client.update();
            }catch (ProjectException e){
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable{
        public void run(){
            while(!finished){
                try{
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if(isUpdate(response)){
                        handleUpdate(response);
                    }else{
                        try{
                            qresponses.put(response);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
