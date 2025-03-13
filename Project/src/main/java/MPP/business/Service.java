package MPP.business;

import MPP.domain.Agent;
import MPP.domain.Flight;
import MPP.domain.Ticket;
import MPP.domain.validators.TicketValidator;
import MPP.exceptions.ValidationException;
import MPP.repository.AgentDBRepository;
import MPP.repository.FlightDBRepository;
import MPP.repository.TicketDBRepository;
import MPP.utils.Observable;
import MPP.utils.Observer;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.StreamSupport;

public class Service implements Observable{

    private AgentDBRepository agentDBRepository;

    private FlightDBRepository flightDBRepository;

    private TicketDBRepository ticketDBRepository;

    private List<Observer> observerList = new ArrayList<>();

    public Service(Properties props){
        agentDBRepository = new AgentDBRepository(props);
        flightDBRepository = new FlightDBRepository(props);
        ticketDBRepository = new TicketDBRepository(props);
    }


    public Agent logInAgent(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new ServiceException("Username is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new ServiceException("Password is required.");
        }

        Optional<Agent> opAgent = agentDBRepository.findOneByUsername(username);
        if (opAgent.isEmpty())
            throw new ServiceException("No agent with that username");

        Agent agent = opAgent.get();
        if (Objects.equals(agent.getPassword(), password))
            return agent;

        throw new ServiceException("Incorrect password");
    }

    public int getAvailableSeats(Flight flight) {
        /* Available Seats for a certain flight*/
        int nrOfSeatsOccupied = 0;
        for (var ticket : ticketDBRepository.filterByFlight(flight.getId()))
            nrOfSeatsOccupied += ticket.getNrSeats();
        return flight.getNrOfSeats() - nrOfSeatsOccupied;
    }

    public List<Flight> getAllFlightsAvailable() {
        /* All Available flights*/
        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .filter(flight -> getAvailableSeats((Flight) flight) > 0)
                .toList();
    }


    public List<String> allFroms() {
        /* All departure airports */
        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .map(Flight::getFrom)
                .distinct()
                .toList();
    }

    public List<String> allTos() {
        /* All destinations airports */
        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .map(Flight::getTo)
                .distinct()
                .toList();
    }

    public List<Flight> searchFlights(String value, String value1, LocalDate value2) {
        if (value == null || value.isEmpty()) {
            throw new ServiceException("Departure location is required.");
        }
        if (value1 == null || value1.isEmpty()) {
            throw new ServiceException("Destination location is required.");
        }
        if (value2 == null) {
            throw new ServiceException("Date is required.");
        }

        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .filter(flight -> flight.getFrom().equals(value) && flight.getTo().equals(value1) && flight.getDate().toLocalDate().equals(value2))
                .toList();
    }

    public Ticket createBookFlight(Flight flight, String text, String text1, int i) {
        /* Create a Ticket based on a Flight detail */
        Ticket ticket = new Ticket(flight, text, text1, i);

        TicketValidator validator = TicketValidator.getInstance();
        try {
            validator.validate(ticket);
        } catch (ValidationException e) {
            throw new ServiceException(e.getMessage());
        }
        return ticket;
    }

    public void saveTicket(Ticket ticket) {
        // Validate ticket before saving
        TicketValidator validator = TicketValidator.getInstance();
        try {
            validator.validate(ticket);
        } catch (ValidationException e) {
            throw new ServiceException(e.getMessage());
        }

        ticketDBRepository.save(ticket);
    }

    @Override
    public void addObserver(Observer e) {
        /* Add observer */
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        /* Remove observer */
        observerList.remove(e);
    }

    @Override
    public void notifyObservers() {
        /* Notify Observer */
        for (Observer observer : observerList)
            observer.update();
    }
}
