package MPP;

import MPP.business.Service;
import MPP.interfaces.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GUIApplication extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogInf.fxml"));
            Parent root = loader.load();

            LogInController controller = loader.getController();

            Properties props=new Properties();
            try {
                props.load(new FileReader("bd.config"));
            } catch (IOException e) {
                System.out.println("Cannot find bd.config "+e);
            }

            Service service = new Service(props);

            controller.setService(service);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Log In");
            primaryStage.show();

        } catch (Exception e){
            System.out.println("Error while starting app " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
