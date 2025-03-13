package server.repository;

import common.domain.Agent;
import server.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class AgentDBRepository implements IAgentRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public AgentDBRepository(Properties props) {
        logger.info("Initializing AgentDBRepository with properties: {} ",dbUtils);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Optional<Agent> findOne(Long id) {
        logger.traceEntry("finding agent with id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("select * from Agents where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String password = result.getString("password");
                    String agency = result.getString("agency");
                    Agent agent = new Agent(username, password, agency);
                    agent.setId(id);
                    logger.traceExit(agent);
                    return Optional.of(agent);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<Agent> getAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        Set<Agent> agents = new HashSet<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Agents")){
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    String agency = result.getString("agency");
                    Agent agent = new Agent(username, password, agency);
                    agent.setId(id);
                    agents.add(agent);
                }
            }
        } catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return agents;
    }

    @Override
    public Long save(Agent entity) {
        logger.traceEntry("saving agent {}",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Agents(username, password, agency) values (?, ?, ?)")){
            statement.setString(1, entity.getUsername());
            statement.setString(2,entity.getPassword());
            statement.setString(3, entity.getAgency());
            int result = statement.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting agent with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Agents where id = ?")){
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Agent entity) {
        logger.traceEntry("updating agent {}",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("update Agents set username = ?, password = ?, agency = ? where id = ?")){
            statement.setString(1, entity.getUsername());
            statement.setString(2,entity.getPassword());
            statement.setString(3, entity.getAgency());
            statement.setLong(4, entity.getId());
            int result = statement.executeUpdate();
            logger.trace("Updated {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public Optional<Agent> findOneByUsername(String username) {
        logger.traceEntry("finding agent with username {}", username);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("select * from Agents where username = ?")){
            statement.setString(1, username);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    Long id = result.getLong("id");
                    String password = result.getString("password");
                    String agency = result.getString("agency");
                    Agent agent = new Agent(username, password, agency);
                    agent.setId(id);
                    logger.traceExit(agent);
                    return Optional.of(agent);
                }

            }
        } catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return Optional.empty();
    }
}
