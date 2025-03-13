package common.domain;

import java.util.ArrayList;
import java.util.List;

public class Ticket extends Entity<Long>{

    private Flight flight;

    private String clientName;

    private List<String> tourists;

    private String address;

    private int nrSeats;

    public Ticket(Flight flight, String clientName, List<String> tourists, String address, int nrSeats) {
        this.flight = flight;
        this.clientName = clientName;
        this.tourists = tourists;
        this.address = address;
        this.nrSeats = nrSeats;
    }

    public Ticket(Flight flight, String clientName, String address, int nrSeats) {
        this.flight = flight;
        this.clientName = clientName;
        tourists =  new ArrayList<>();
        this.address = address;
        this.nrSeats = nrSeats;
    }


        public Flight getFlight() {
            return flight;
        }

        public void setFlight(Flight flight) {
            this.flight = flight;
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

        public void addTourist(String touristName) {
            tourists.add(touristName);
        }
}
