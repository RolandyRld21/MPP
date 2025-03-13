package common.business;

import common.domain.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface IService{

    Agent logInAgent(String username, String password, IObserver client) throws ProjectException;

    void logOutAgent(Agent agent, IObserver client) throws ProjectException;

    HashMap<Flight, Integer> getAllFlightsAvailable() throws ProjectException;

    List<String> allFroms() throws ProjectException;

    List<String> allTos() throws ProjectException;

    HashMap<Flight, Integer> searchFlights(String from, String to, LocalDateTime date) throws ProjectException;

    public void saveTicket(Ticket ticket) throws ProjectException;
}
