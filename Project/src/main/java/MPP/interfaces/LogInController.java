package MPP.interfaces;

import MPP.business.Service;
import MPP.domain.Agent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {

    private Service service;

    @FXML
    private Button logInButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void LogIn(ActionEvent event) {
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            System.out.println("Please fill in all fields");
            return;
        }
        try{
            Agent agent = service.logInAgent(usernameField.getText(), passwordField.getText());

            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AgentMenuf.fxml"));
            Parent root = loader.load();

            AgentMenuController controller = loader.getController();
            controller.setController(service, agent);
            service.addObserver(controller);

            Scene scene =new Scene(root, root.prefWidth(1), root.prefHeight(1));
            stage.setTitle("Agent Menu!");
            stage.setScene(scene);
            stage.show();


        } catch (Exception e){
            System.out.println("Error while logging in");
        }
    }

    public void setService(Service service) {
        this.service = service;
    }
}
