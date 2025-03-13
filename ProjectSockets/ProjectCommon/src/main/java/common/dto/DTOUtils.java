package common.dto;

import common.domain.*;

import java.util.List;

public class DTOUtils {

    public static String getUsernameFromDTO(AgentDTO userDTO){
        return userDTO.getUsername();
    }

    public static String getPasswordFromDTO(AgentDTO userDTO){
        return userDTO.getPassword();
    }

    public static AgentDTO getUsernameAndPasswordtoDTO(String username, String password){
        return new AgentDTO(username, password);
    }

    public static Agent getFromDTO(AgentDTO userDTO){
        Agent agent = new Agent(userDTO.getUsername(), userDTO.getPassword(), userDTO.getAgency());
        agent.setId(userDTO.getId());
        return agent;
    }

    public static AgentDTO getDTO(Agent agent){
        return new AgentDTO(agent.getId(), agent.getUsername(), agent.getPassword(), agent.getAgency());
    }

    public static Flight getFromDTO(FlightDTO flightDTO){
        Flight flight = new Flight(flightDTO.getFrom(), flightDTO.getTo(), flightDTO.getDate(), flightDTO.getNrOfSeats());
        flight.setId(flightDTO.getId());
        return flight;
    }

    public static FlightDTO getToDTO(Flight flight){
        return new FlightDTO(flight.getId(), flight.getFrom(), flight.getTo(), flight.getDate(), flight.getNrOfSeats());
    }

    public static int getAvailableSeatsFromDTO(FlightDTO flightDTO){
        return flightDTO.getNrOfAvailableSeats();
    }

    public static FlightDTO getAvailableSeatsToDTO(FlightDTO flightDTO, int nrOfAvailableSeats){
        return new FlightDTO(flightDTO.getId(), flightDTO.getFrom(), flightDTO.getTo(), flightDTO.getDate(), flightDTO.getNrOfSeats(), nrOfAvailableSeats);
    }

    public static TicketDTO getToDTO(Ticket ticket){
        return new TicketDTO(ticket.getId(), ticket.getFlight().getId(), ticket.getFlight().getFrom(), ticket.getFlight().getTo(), ticket.getFlight().getDate(), ticket.getFlight().getNrOfSeats(), ticket.getClientName(), ticket.getTourists(), ticket.getAddress(), ticket.getNrSeats());
    }

    public static Ticket getFromDTO(TicketDTO ticketDTO){
        Flight flight = new Flight(ticketDTO.getFrom(), ticketDTO.getTo(), ticketDTO.getDate(), ticketDTO.getNrOfSeats());
        flight.setId(ticketDTO.getFlightId());
        Ticket ticket = new Ticket(flight, ticketDTO.getClientName(), ticketDTO.getTourists(), ticketDTO.getAddress(), ticketDTO.getNrSeats());
        ticket.setId(ticketDTO.getId());
        return ticket;
    }

    public static FlightDTO[] getToDTO(Flight[] flights){
        FlightDTO[] flightDTOS = new FlightDTO[flights.length];
        for(int i = 0; i < flights.length; i++){
            flightDTOS[i] = getToDTO(flights[i]);
        }
        return flightDTOS;
    }

    public static Flight[] getFromDTO(FlightDTO[] flightDTOS){
        Flight[] flights = new Flight[flightDTOS.length];
        for(int i = 0; i < flightDTOS.length; i++){
            flights[i] = getFromDTO(flightDTOS[i]);
        }
        return flights;
    }

    public static List<String> getListOfStringFromArray(String[] strings){
        return List.of(strings);
    }

    public static String[] getArrayFromListOfString(List<String> strings){
        return strings.toArray(new String[0]);
    }

}
