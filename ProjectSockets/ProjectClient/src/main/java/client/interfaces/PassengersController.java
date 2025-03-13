package client.interfaces;

import common.domain.Agent;
import common.domain.Ticket;
import common.business.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PassengersController {

    private IService service;

    private Agent agent;

    private Ticket ticket;

    @FXML
    private VBox vbox;

    @FXML
    private Button bookButton;

    private List<TextField> passengers = new ArrayList<>();

    public void setController(IService service, Agent agent, Ticket ticket) {
        this.service = service;
        this.agent = agent;
        this.ticket = ticket;
        for(int i = 0; i < ticket.getNrSeats(); i++){
            Label label = new Label("Passenger " + (i + 1));
            TextField textField = new TextField();
            passengers.add(textField);
            vbox.getChildren().addAll(label, textField);

        }
    }

    @FXML
    void book(ActionEvent event) {
        if(passengers.stream().anyMatch(textField -> textField.getText().isEmpty())){
            System.out.println("Please fill in all fields");
            return;
        }
        try{
            for(TextField textField : passengers) {
                ticket.addTourist(textField.getText());
            }
            service.saveTicket(ticket);
//            service.notifyObservers();
            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.close();
        } catch (Exception e){
            System.out.println("Error while booking ticket");
        }
    }
}
