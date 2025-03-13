package common.jsonprotocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.business.IObserver;
import common.business.IService;
import common.business.ProjectException;
import common.domain.Agent;
import common.domain.Flight;
import common.domain.Ticket;
import common.dto.DTOUtils;
import common.dto.FlightDTO;
import common.dto.TicketDTO;
import common.utils.LocalDateTimeTypeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ProjectClientJsonWorker implements Runnable, IObserver{

    private IService server;

    private Socket connection;

    private BufferedReader input;

    private PrintWriter output;

    private Gson gsonFormatter;

    private volatile boolean connected;

    public ProjectClientJsonWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(connected){
            try{
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if(response!= null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
        }catch (IOException e){
            System.out.println("Error "+e);
        }
    }

    public void update() throws ProjectException{
        sendResponse(JsonProtocolUtils.createUpdateResponse());
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.getType());
            String username = DTOUtils.getUsernameFromDTO(request.getAgent());
            String password = DTOUtils.getPasswordFromDTO(request.getAgent());
            try {
                Agent agent = server.logInAgent(username, password, this);
                return JsonProtocolUtils.createLoginResponse(agent);
            } catch (ProjectException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_AVAILABLE_FLIGHTS) {
            System.out.println("Get available flights request ...");
            try {
                HashMap<Flight, Integer> flights = server.getAllFlightsAvailable();
                return JsonProtocolUtils.createGetAllFlightsResponse(flights);
            } catch (ProjectException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_ALL_FROMS) {
            System.out.println("Get all froms request ...");
            try {
                return JsonProtocolUtils.createGetAllFromsResponse(server.allFroms());
            } catch (ProjectException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_ALL_TOS) {
            System.out.println("Get all tos request ...");
            try {
                return JsonProtocolUtils.createGetAllTosResponse(server.allTos());
            } catch (ProjectException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.SEARCH_FLIGHTS) {
            System.out.println("Search flights request ...");
            try{
                FlightDTO flightDTO = request.getFlight();
                String from = flightDTO.getFrom();
                String to = flightDTO.getTo();
                LocalDateTime departure = flightDTO.getDate();
                return JsonProtocolUtils.createSearchFlightsResponse(server.searchFlights(from, to, departure));
            }catch (ProjectException e){
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.BOOK_FLIGHT) {
            System.out.println("Book flight request ...");
            try{
                Ticket ticket = DTOUtils.getFromDTO(request.getTicket());
                server.saveTicket(ticket);
                return JsonProtocolUtils.createOkResponse();
            }catch (ProjectException e){
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.LOGOUT) {
            System.out.println("Logout request ...");
            try {
                Agent agent = DTOUtils.getFromDTO(request.getAgent());
                server.logOutAgent(agent, this);
                connected=false;
                return JsonProtocolUtils.createOkResponse();
            } catch (ProjectException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response){
        String responseString = gsonFormatter.toJson(response);
        System.out.println("Sending response " + responseString);
        synchronized (output){
            output.println(responseString);
            output.flush();
        }
    }
}
