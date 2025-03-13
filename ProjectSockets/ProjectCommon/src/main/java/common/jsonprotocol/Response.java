package common.jsonprotocol;

import common.dto.AgentDTO;
import common.dto.FlightDTO;
import common.dto.TicketDTO;

import java.util.List;

public class Response {

    private ResponseType type;

    private AgentDTO agent;

    private FlightDTO[] flights;

    private TicketDTO ticket;

    private String[] data;

    private String errorMessage;

    public Response() {
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public AgentDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }

    public FlightDTO[] getFlights() {
        return flights;
    }

    public void setFlights(FlightDTO[] flights) {
        this.flights = flights;
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
