package common.jsonprotocol;

import common.dto.AgentDTO;
import common.dto.DTOUtils;
import common.domain.*;
import common.dto.FlightDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class JsonProtocolUtils {

    public static Response createErrorResponse(String errorMessage){
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Request createLoginRequest(String username, String password){
        /*Login Request */
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setAgent(new AgentDTO(username, password));
        return req;
    }

    public static Response createLoginResponse(Agent agent){
        /*Login Response*/
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        resp.setAgent(DTOUtils.getDTO(agent));
        return resp;
    }

    public static Request createGetAllFlightsRequest(){
        Request req = new Request();
        req.setType(RequestType.GET_AVAILABLE_FLIGHTS);
        return req;
    }

    public static Response createGetAllFlightsResponse(HashMap<Flight, Integer> flights) {
        /*Response contain information about flights DTO's */
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        FlightDTO[] flightDTOs = new FlightDTO[flights.size()];
        int i = 0;
        for (Flight flight : flights.keySet()) {
            FlightDTO flightDTO = DTOUtils.getToDTO(flight);
            flightDTO.setNrOfAvailableSeats(flights.get(flight));
            flightDTOs[i] = flightDTO;
            i++;
        }
        resp.setFlights(flightDTOs); //Adding flights to the response
        return resp;
    }

    public static Request createGetAllFromsRequest() {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_FROMS);
        return req;
    }

    public static Response createGetAllFromsResponse(List<String> froms) {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        resp.setData(DTOUtils.getArrayFromListOfString(froms)); // Adding the departure names
        return resp;
    }

    public static Request createGetAllTosRequest() {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_TOS);
        return req;
    }

    public static Response createGetAllTosResponse(List<String> tos){
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        resp.setData(DTOUtils.getArrayFromListOfString(tos));// Adding the destinations names
        return resp;
    }

    public static Request createSearchFlightsRequest(String from, String to, LocalDateTime date) {
        Request req = new Request();
        req.setType(RequestType.SEARCH_FLIGHTS);
        FlightDTO flightDTO = new FlightDTO(from, to, date);
        req.setFlight(flightDTO);
        return req;
    }

    public static Response createSearchFlightsResponse(HashMap<Flight, Integer> flights) {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        FlightDTO[] flightDTOs = new FlightDTO[flights.size()];
        int i = 0;
        for (Flight flight : flights.keySet()) {
            FlightDTO flightDTO = DTOUtils.getToDTO(flight);
            flightDTO.setNrOfAvailableSeats(flights.get(flight));
            flightDTOs[i] = flightDTO;
            i++;
        }
        resp.setFlights(flightDTOs);
        return resp;
    }

    public static Request createBookFlightRequest(Ticket ticket){
        Request req = new Request();
        req.setType(RequestType.BOOK_FLIGHT);
        req.setTicket(DTOUtils.getToDTO(ticket));
        return req;
    }

    public static Response createOkResponse(){
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Request createLogoutRequest(Agent agent){
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setAgent(DTOUtils.getDTO(agent));
        return req;
    }

    public static  Response createUpdateResponse(){
        Response resp = new Response();
        resp.setType(ResponseType.UPDATE);
        return resp;
    }
}
