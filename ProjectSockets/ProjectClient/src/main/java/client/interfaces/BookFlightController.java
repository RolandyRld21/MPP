package client.interfaces;

import common.domain.Agent;
import common.domain.Flight;
import common.domain.Ticket;
import common.business.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookFlightController {

    private IService service;

    private Agent agent;

    private Flight flight;

    private Integer maxSeats;

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

    public void setController(IService service, Agent agent, Flight flight, Integer maxSeats){
        this.service = service;
        this.agent = agent;
        this.flight = flight;
        this.maxSeats = maxSeats;
        bookingFlight.setText("Booking flight from: " + flight.getFrom() + " to: " + flight.getTo() + " on: " + flight.getDate());
    }

    @FXML
    void Next(ActionEvent event) {
        if(addressField.getText().isEmpty() || clientField.getText().isEmpty() || nOSField.getText().isEmpty()){
            System.out.println("Please fill in all fields");
            return;
        }
        try{
            if(Integer.parseInt(nOSField.getText()) <= 0 || maxSeats < Integer.parseInt(nOSField.getText())){
                System.out.println("Number of seats invalid");
                return;
            }

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.close();

            Ticket ticket = new Ticket(flight, clientField.getText(), addressField.getText(), Integer.parseInt(nOSField.getText()));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Passengers.fxml"));
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
