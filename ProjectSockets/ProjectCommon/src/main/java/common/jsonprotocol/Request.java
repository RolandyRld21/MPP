package common.jsonprotocol;

import common.dto.AgentDTO;
import common.dto.FlightDTO;
import common.dto.TicketDTO;

import java.util.Arrays;

public class Request {

    private RequestType type;

    private AgentDTO agent;

    private FlightDTO flight;

    private TicketDTO ticket;

    private String[] data;

    public Request() {
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public AgentDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
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
}
