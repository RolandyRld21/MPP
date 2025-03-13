package MPP;

import MPP.domain.*;
import MPP.repository.AgentDBRepository;
import MPP.repository.FlightDBRepository;
import MPP.repository.TicketDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        AgentDBRepository agentRepository = new AgentDBRepository(props);
        FlightDBRepository flightRepository = new FlightDBRepository(props);
        TicketDBRepository ticketRepository = new TicketDBRepository(props);

        agentRepository.save(new Agent("agent1", "password1", "agency1"));
        agentRepository.save(new Agent("agent2", "password2", "agency2"));
        flightRepository.save(new Flight("Cluj", "Bucuresti", LocalDateTime.now() , 100));
        ticketRepository.save(new Ticket(flightRepository.findOne(8L).get(), "C", new ArrayList<String>(Arrays.asList("A", "B")), "client1", 1));
        System.out.println(agentRepository.getAll());
        System.out.println(flightRepository.getAll());
        System.out.println(ticketRepository.getAll());

    }
}