package MPP.interfaces;

import MPP.business.Service;
import MPP.domain.Agent;
import MPP.domain.Flight;
import MPP.domain.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookFlightController {

    private Service service;

    private Agent agent;

    private Flight flight;

    @FXML
    private TextField addressField;

    @FXML
    private Label bookingFlight;

    @FXML
    private TextField clientField;

    @FXML
    private TextField nOSField;

    @FXML
    private Button nextButton;

    public void setController(Service service, Agent agent, Flight flight){
        this.service = service;
        this.agent = agent;
        this.flight = flight;
        bookingFlight.setText("Booking flight from: " + flight.getFrom() + " to: " + flight.getTo() + " on: " + flight.getDate());
    }

    @FXML
    void Next(ActionEvent event) {
        /* Book flight for a client with an address and the number of seats, and then read all the names for the number of seats */
        if(addressField.getText().isEmpty() || clientField.getText().isEmpty() || nOSField.getText().isEmpty()){
            System.out.println("Please fill in all fields");
            return;
        }
        try{
            if(Integer.parseInt(nOSField.getText()) <= 0 || service.getAvailableSeats(flight) < Integer.parseInt(nOSField.getText())){
                System.out.println("Number of seats invalid");
                return;
            }

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.close();

            Ticket ticket = service.createBookFlight(flight, clientField.getText(), addressField.getText(), Integer.parseInt(nOSField.getText()));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Passengersf.fxml"));
            Parent root = loader.load();

            PassengersController controller = loader.getController();
            controller.setController(service, agent, ticket);

            Scene scene = new Scene(root, root.prefWidth(1), root.prefHeight(1));
            stage.setScene(scene);
            stage.show();


        } catch (Exception e){
            System.out.println("Error while booking flight: " + e.getMessage());
        }
    }

}
