package server;

import common.business.IService;
import common.utils.AbstractServer;
import common.utils.ProjectJsonConcurrentServer;
import common.utils.ServerException;
import server.business.Service;
import server.repository.*;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int dafaultPort = 30053;
    public static void main(String[] args){
        Properties serverProps = new Properties();
        try{
            serverProps.load(StartJsonServer.class.getResourceAsStream("/projectserver.properties"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        } catch (IOException e){
            System.err.println("Cannot find projectserver.properties " + e);
            return;
        }
        IAgentRepository agentRepository = new AgentDBRepository(serverProps);
        IFlightRepository flightRepository = new FlightDBRepository(serverProps);
        ITicketRepository ticketRepository = new TicketDBRepository(serverProps);
        IService projectServerImpl = new Service(agentRepository, flightRepository, ticketRepository);
        int projectServerPort = dafaultPort;
        try{
            projectServerPort = Integer.parseInt(serverProps.getProperty("project.server.port"));
        }catch(NumberFormatException nef){
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + dafaultPort);
        }
        AbstractServer server = new ProjectJsonConcurrentServer(projectServerPort, projectServerImpl);
        try{
            server.start();
        }catch (ServerException e){
            System.err.println("Error starting the server" + e.getMessage());
        }

    }
}
