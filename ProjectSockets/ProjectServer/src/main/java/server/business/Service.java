package server.business;

import common.business.IObserver;
import common.business.ProjectException;
import common.domain.*;
import server.repository.*;
import common.business.IService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

public class Service implements IService{

    private IAgentRepository agentDBRepository;

    private IFlightRepository flightDBRepository;

    private ITicketRepository ticketDBRepository;

    private Map<Long, IObserver> loggedClients = new HashMap<>();

    public Service(IAgentRepository agentRepository, IFlightRepository flightRepository, ITicketRepository ticketRepository){
        this.agentDBRepository = agentRepository;
        this.flightDBRepository = flightRepository;
        this.ticketDBRepository = ticketRepository;
    }


    public Agent logInAgent(String username, String password, IObserver client) throws ProjectException {
        Optional<Agent> opAgent = agentDBRepository.findOneByUsername(username);
        if (opAgent.isEmpty())
            throw new ServiceException("No agent with that username");
        Agent agent = opAgent.get();
        if (Objects.equals(agent.getPassword(), password)) {
            if(loggedClients.get(agent.getId())!=null)
                throw new ProjectException("Agent already logged in.");
            loggedClients.put(agent.getId(), client);
            return agent;
        }
        else{
            throw new ServiceException("Incorrect password");
        }
    }

    @Override
    public void logOutAgent(Agent agent, IObserver client) throws ProjectException {
        IObserver localClient = loggedClients.remove(agent.getId());
        if (localClient == null)
            throw new ProjectException("Agent " + agent.getUsername() +" not logged in.");
    }

    public int getAvailableSeats(Flight flight) throws ProjectException{
        int nrOfSeatsOccupied = 0;
        for (Ticket ticket : ticketDBRepository.filterByFlight(flight.getId()))
            nrOfSeatsOccupied += ticket.getNrSeats();
        return flight.getNrOfSeats() - nrOfSeatsOccupied;
    }

    public HashMap<Flight, Integer> getAllFlightsAvailable() throws ProjectException{
        HashMap<Flight, Integer> flightsAvailable = new HashMap<>();
        for(Flight flight : flightDBRepository.getAll()){
            int availableSeats = getAvailableSeats(flight);
            if(availableSeats > 0){
                flightsAvailable.put(flight, availableSeats);
            }
        }
        return flightsAvailable;
    }


    public List<String> allFroms() throws ProjectException{
        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .map(Flight::getFrom)
                .distinct()
                .toList();
    }

    public List<String> allTos() throws ProjectException{
        return StreamSupport.stream(flightDBRepository.getAll().spliterator(), false)
                .map(Flight::getTo)
                .distinct()
                .toList();
    }

    @Override
    public HashMap<Flight, Integer> searchFlights(String from, String to, LocalDateTime date) throws ProjectException{
        HashMap<Flight, Integer> filterFlights = new HashMap<>();
        for(Flight flight : flightDBRepository.getAll()){
            if(flight.getFrom().equals(from) && flight.getTo().equals(to) && flight.getDate().getYear() == date.getYear() && flight.getDate().getMonth() == date.getMonth() && flight.getDate().getDayOfMonth() == date.getDayOfMonth()){
                int availableSeats = getAvailableSeats(flight);
               filterFlights.put(flight, availableSeats);
            }
        }
        return filterFlights;
    }

    public void saveTicket(Ticket ticket) throws ProjectException{

        ticketDBRepository.save(ticket);
        notifyAgents();
    }

    private final int defaultThreadsNo = 5;
    private void notifyAgents() throws ProjectException{
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
                for(IObserver client : loggedClients.values()){
                    executor.execute(() -> {
                        try {
                            client.update();
                        } catch (ProjectException e) {
                            System.err.println("Error notifying agent " + e);
                        }
                    });
                }
                executor.shutdown();
    }
}
