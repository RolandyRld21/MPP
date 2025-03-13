package common.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlightDTO implements Serializable {

    private Long id;

    private String from;

    private String to;

    private LocalDateTime date;

    private int nrOfSeats;

    private int nrOfAvailableSeats;

    public FlightDTO(Long id, String from, String to, LocalDateTime date, int nrOfSeats, int nrOfAvailableSeats) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.nrOfSeats = nrOfSeats;
        this.nrOfAvailableSeats = nrOfAvailableSeats;
    }

    public FlightDTO(Long id, String from, String to, LocalDateTime date, int nrOfSeats) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.nrOfSeats = nrOfSeats;
    }

    public FlightDTO(String from, String to, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNrOfAvailableSeats() {
        return nrOfAvailableSeats;
    }

    public void setNrOfAvailableSeats(int nrOfAvailableSeats) {
        this.nrOfAvailableSeats = nrOfAvailableSeats;
    }
}
