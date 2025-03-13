package MPP.domain;

import java.time.LocalDateTime;

public class Flight extends Entity<Long>{

    private String from;

    private String to;

    private LocalDateTime date;

    private int nrOfSeats;

    public Flight(String from, String to, LocalDateTime date, int nrOfSeats) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.nrOfSeats = nrOfSeats;
    }

    public String getFrom(){ return from; }

    public void setFrom(String from){ this.from = from; }

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
}
