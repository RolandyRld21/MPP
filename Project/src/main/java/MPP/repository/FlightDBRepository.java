package MPP.repository;

import MPP.domain.Flight;

import MPP.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class FlightDBRepository implements IFlightRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public FlightDBRepository(Properties props) {
        logger.info("Initializing FlightDBRepository with properties: {} ", dbUtils);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Flight> findOne(Long id) {
        logger.traceEntry("finding flight with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("select * from Flights where id = ?")){
            statement.setLong(1, id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    String from = result.getString("flightFrom");
                    String to = result.getString("flightTo");

                    String strDate = result.getString("date");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime date = LocalDateTime.parse(strDate, formatter);

                    int nrOfSeats = result.getInt("nrOfSeats");
                    Flight flight = new Flight(from, to, date, nrOfSeats);
                    flight.setId(id);
                    logger.traceExit();
                    return Optional.of(flight);
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
    public Iterable<Flight> getAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        Set<Flight> flights = new HashSet<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Flights")){
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String from = result.getString("flightFrom");
                    String to = result.getString("flightTo");
                    String strDate = result.getString("date");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime date = LocalDateTime.parse(strDate, formatter);

                    int nrOfSeats = result.getInt("nrOfSeats");
                    Flight flight = new Flight(from, to, date, nrOfSeats);
                    flight.setId(id);
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(flights);
        return flights;
    }

    @Override
    public void save(Flight entity) {
        logger.traceEntry("saving flight {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Flights (flightFrom, flightTo, date, nrOfSeats) values (?,?,?,?)")){
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String strDate = entity.getDate().format(formatter);
            statement.setString(3, strDate);

            statement.setInt(4, entity.getNrOfSeats());
            int result = statement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting flight with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Flights where id = ?")){
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Flight entity) {
        logger.traceEntry("updating flight {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("update Flights set flightFrom = ?, flightTo = ?, date = ?, nrOfSeats = ? where id = ?")){
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String strDate = entity.getDate().format(formatter);
            statement.setString(3, strDate);

            statement.setInt(4, entity.getNrOfSeats());
            statement.setLong(5, entity.getId());
            int result = statement.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }
}
