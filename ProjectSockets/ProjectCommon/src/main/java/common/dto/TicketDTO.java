package common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TicketDTO implements Serializable {

    private Long id;

    private Long flightId;

    private String from;

    private String to;

    private LocalDateTime date;

    private int nrOfSeats;

    private String clientName;

    private List<String> tourists;

    private String address;

    private int nrSeats;

    public TicketDTO(Long id, Long flightId, String from, String to, LocalDateTime date, int nrOfSeats, String clientName, List<String> tourists, String address, int nrSeats) {
        this.id = id;
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.date = date;
        this.nrOfSeats = nrOfSeats;
        this.clientName = clientName;
        this.tourists = tourists;
        this.address = address;
        this.nrSeats = nrSeats;
    }

    public TicketDTO(Long id, Long flightId, String from, String to, LocalDateTime date, int nrOfSeats, String clientName, String address, int nrSeats) {
        this.id = id;
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.date = date;
        this.nrOfSeats = nrOfSeats;
        this.clientName = clientName;
        this.address = address;
        this.nrSeats = nrSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(int nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getTourists() {
        return tourists;
    }

    public void setTourists(List<String> tourists) {
        this.tourists = tourists;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNrSeats() {
        return nrSeats;
    }

    public void setNrSeats(int nrSeats) {
        this.nrSeats = nrSeats;
    }
}
