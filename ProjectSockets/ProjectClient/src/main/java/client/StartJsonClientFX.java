package client;

import client.interfaces.LogInController;
import common.business.IService;
import common.jsonprotocol.ProjectServicesJsonProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application{

    private Stage primaryStage;

    private static int defaultChatPort = 30053;

    private static String defaultServer = "localhost";

    private IService server;

    public void start(Stage primaryStage) throws Exception{
        System.out.println("In start");
        Properties clientProps = new Properties();
        try{
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/projectclient.properties"));
            System.out.println("Client properties set.");
            clientProps.list(System.out);
        }catch (IOException e){
            System.err.println("Cannot find projectclient.properties " + e);
            return;
        }
        String serverIP =clientProps.getProperty("project.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try{
            serverPort = Integer.parseInt(clientProps.getProperty("project.server.port"));
        }catch (NumberFormatException ex){
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        /* Server  */
        IService server = new ProjectServicesJsonProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogIn.fxml"));
        Parent root = loader.load();

        LogInController controller = loader.getController();

        controller.setService(server);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();
    }
}
