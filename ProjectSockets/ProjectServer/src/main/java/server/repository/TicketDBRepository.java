package server.repository;

import common.domain.Flight;
import common.domain.Ticket;
import server.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class TicketDBRepository implements ITicketRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public TicketDBRepository(Properties props){
        logger.info("Initializing TicketDBRepository with properties: {} ", dbUtils);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Ticket> filterByFlight(Long flightId) {
        logger.traceEntry("filtering tickets by flight with id {}", flightId);
        Connection connection = dbUtils.getConnection();
        Set<Ticket> tickets = new HashSet<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Tickets where flightId = ?")){
            statement.setLong(1, flightId);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String clientName = result.getString("clientName");
                    String address = result.getString("address");
                    int nrSeats = result.getInt("nrSeats");
                    Flight flight = null;
                    try(PreparedStatement statement1 = connection.prepareStatement("select * from Flights where id = ?")){
                        statement1.setLong(1, flightId);
                        try(ResultSet result1 = statement1.executeQuery()){
                            if(result1.next()){
                                String from = result1.getString("flightFrom");
                                String to = result1.getString("flightTo");

                                String strDate = result1.getString("date");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime date = LocalDateTime.parse(strDate, formatter);

                                int nrOfSeats = result1.getInt("nrOfSeats");
                                flight = new Flight(from, to, date, nrOfSeats);
                                flight.setId(flightId);
                            }
                        }
                    }
                    Ticket ticket = new Ticket(flight, clientName, address, nrSeats);
                    ticket.setId(id);
                    try(PreparedStatement statement2 = connection.prepareStatement("select * from Tourists where ticketId = ?")){
                        statement2.setLong(1, id);
                        try(ResultSet result2 = statement2.executeQuery()){
                            while(result2.next()){
                                ticket.addTourist(result2.getString("touristName"));
                            }
                        }
                    }
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return tickets;
    }

    @Override
    public Optional<Ticket> findOne(Long id) {
        logger.traceEntry("finding ticket with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("select * from Tickets where id = ?")){
            statement.setLong(1, id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    Long flightId = result.getLong("flightId");
                    String clientName = result.getString("clientName");
                    String address = result.getString("address");
                    int nrSeats = result.getInt("nrSeats");
                    Flight flight = null;
                    try(PreparedStatement statement1 = connection.prepareStatement("select * from Flights where id = ?")){
                        try(ResultSet result1 = statement1.executeQuery()){
                            if(result1.next()){
                                String from = result1.getString("flightFrom");
                                String to = result1.getString("flightTo");

                                String strDate = result.getString("date");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime date = LocalDateTime.parse(strDate, formatter);

                                int nrOfSeats = result1.getInt("nrOfSeats");
                                flight = new Flight(from, to, date, nrOfSeats);
                                flight.setId(flightId);
                            }
                        }
                    }
                    Ticket ticket = new Ticket(flight, clientName, address, nrSeats);
                    ticket.setId(id);
                    try(PreparedStatement statement2 = connection.prepareStatement("select * from Tourists where ticketId = ?")){
                        statement2.setLong(1, id);
                        try(ResultSet result2 = statement2.executeQuery()){
                            while(result2.next()){
                                ticket.addTourist(result2.getString("touristName"));
                            }
                        }
                    }
                    logger.traceExit();
                    return Optional.of(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> getAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        Set<Ticket> tickets = new HashSet<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Tickets")){
            try(ResultSet result = statement.executeQuery()){
                while(result.next()) {
                    Long id = result.getLong("id");
                    Long flightId = result.getLong("flightId");
                    String clientName = result.getString("clientName");
                    String address = result.getString("address");
                    int nrSeats = result.getInt("nrSeats");
                    Flight flight = null;
                    try (PreparedStatement statement1 = connection.prepareStatement("select * from Flights where id = ?")) {
                        try (ResultSet result1 = statement1.executeQuery()) {
                            if (result1.next()) {
                                String from = result1.getString("flightFrom");
                                String to = result1.getString("flightTo");

                                String strDate = result.getString("date");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime date = LocalDateTime.parse(strDate, formatter);

                                int nrOfSeats = result1.getInt("nrOfSeats");
                                flight = new Flight(from, to, date, nrOfSeats);
                                flight.setId(flightId);
                            }
                        }
                    }
                    Ticket ticket = new Ticket(flight, clientName, address, nrSeats);
                    ticket.setId(id);
                    try (PreparedStatement statement2 = connection.prepareStatement("select * from Tourists where ticketId = ?")) {
                        statement2.setLong(1, id);
                        try (ResultSet result2 = statement2.executeQuery()) {
                            while (result2.next()) {
                                ticket.addTourist(result2.getString("touristName"));
                            }
                        }
                    }
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        return tickets;
    }

    @Override
    public Long save(Ticket entity) {
        logger.traceEntry("saving ticket {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Tickets (flightId, clientName, address, nrSeats) values (?,?,?,?)")) {
            statement.setLong(1, entity.getFlight().getId());
            statement.setString(2, entity.getClientName());
            statement.setString(3, entity.getAddress());
            statement.setInt(4, entity.getNrSeats());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while(resultSet.next())
            {
                entity.setId(resultSet.getLong(1));
            }
            try(PreparedStatement statement1 = connection.prepareStatement("insert into Tourists (ticketId, touristName) values (?,?)")) {
                for (String tourist : entity.getTourists()) {
                    statement1.setLong(1, entity.getId());
                    statement1.setString(2, tourist);
                    statement1.executeUpdate();
                }
            }
            logger.trace("Saved ticket {}", entity);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public void delete(Long id) {
        Connection connection = dbUtils.getConnection();
        logger.traceEntry("deleting ticket with {}", id);
        try(PreparedStatement statement = connection.prepareStatement("delete from Tickets where id = ?")){
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.trace("Deleted ticket with id {}", id);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Ticket entity) {
        Connection connection = dbUtils.getConnection();
        logger.traceEntry("updating ticket {}", entity);
        try(PreparedStatement statement = connection.prepareStatement("update Tickets set flightId = ?, clientName = ?, address = ?, nrSeats = ? where id = ?")){
            statement.setLong(1, entity.getFlight().getId());
            statement.setString(2, entity.getClientName());
            statement.setString(3, entity.getAddress());
            statement.setInt(4, entity.getNrSeats());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while(resultSet.next())
            {
                entity.setId(resultSet.getLong(1));
            }
            try(PreparedStatement statement1 = connection.prepareStatement("delete from Tourists where ticketId = ?")){
                statement1.setLong(1, entity.getId());
                statement1.executeUpdate();
                try(PreparedStatement statement2 = connection.prepareStatement("insert into Tourists (ticketId, touristName) values (?,?)")){
                    for (String tourist : entity.getTourists()) {
                        statement2.setLong(1, entity.getId());
                        statement2.setString(2, tourist);
                        statement2.executeUpdate();
                    }
                }
            }
            logger.trace("Updated ticket {}", entity);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
    }
}
