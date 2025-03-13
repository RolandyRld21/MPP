package client.interfaces;

import common.business.IObserver;
import common.business.ProjectException;
import common.domain.*;
import common.business.IService;
import common.utils.Observer;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AgentMenuController implements Initializable, IObserver {

    private IService service;

    private Agent agent;

    @FXML
    private TableColumn<Flight, String> col1;

    @FXML
    private TableColumn<Flight, String> col2;

    @FXML
    private TableColumn<Flight, LocalDateTime> col3;

    @FXML
    private TableColumn<Flight, Integer> col4;

    @FXML
    private TableColumn<Flight, String> col5;

    @FXML
    private TableColumn<Flight, String> col6;

    @FXML
    private TableColumn<Flight, LocalDateTime> col7;

    @FXML
    private TableColumn<Flight, Integer> col8;

    @FXML
    private ComboBox<String> comboBox1;

    @FXML
    private ComboBox<String> comboBox2;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button logOutButton;

    @FXML
    private Button serchButton;

    @FXML
    private Button bookButton;

    @FXML
    private TableView<Flight> table1;

    @FXML
    private TableView<Flight> table2;

    private ObservableList<String> from = FXCollections.observableArrayList();
    private ObservableList<String> to = FXCollections.observableArrayList();
    private ObservableList<Flight> allFLightsAvailable = FXCollections.observableArrayList();

    private HashMap<Flight, Integer> flightsMap;
    private HashMap<Flight, Integer> searhcedFlights;
    private ObservableList<Flight> results = FXCollections.observableArrayList();

    public void setController(IService service, Agent agent){
        this.service = service;
        this.agent = agent;
        try{
            from.addAll(service.allFroms());
            to.addAll(service.allTos());
        } catch (Exception e){
            System.out.println("Error while setting controller");
        }
        comboBox1.setItems(from);
        comboBox2.setItems(to);
        load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<Flight, String>("from"));
        col2.setCellValueFactory(new PropertyValueFactory<Flight, String>("to"));
        col3.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("date"));
        col4.setCellValueFactory(flight -> new SimpleIntegerProperty(flightsMap.get(flight.getValue())).asObject());
        col5.setCellValueFactory(new PropertyValueFactory<Flight, String>("from"));
        col6.setCellValueFactory(new PropertyValueFactory<Flight, String>("to"));
        col7.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("date"));
        col8.setCellValueFactory(flight -> new SimpleIntegerProperty(searhcedFlights.get(flight.getValue())).asObject());
        table2.setItems(results);
        comboBox1.setItems(from);
        comboBox2.setItems(to);
    }

    public void load(){
        try{
            flightsMap = service.getAllFlightsAvailable();
            allFLightsAvailable = FXCollections.observableArrayList(flightsMap.keySet());
            table1.setItems(allFLightsAvailable);
            table1.refresh();
        }catch (ProjectException e){
            System.out.println("Error while updating flights");
        }
    }

    public void update(){
        Platform.runLater(()->{
            try{
                flightsMap = service.getAllFlightsAvailable();
                allFLightsAvailable = FXCollections.observableArrayList(flightsMap.keySet());
                table1.setItems(allFLightsAvailable);
                table1.refresh();
                if(comboBox1.getValue() != null && comboBox2.getValue() != null && datePicker.getValue() != null){
                    searhcedFlights = service.searchFlights(comboBox1.getValue(), comboBox2.getValue(), datePicker.getValue().atStartOfDay());
                    results = FXCollections.observableArrayList(searhcedFlights.keySet());
                };
                table2.setItems(results);
                table2.refresh();
            }catch (ProjectException e){
                System.out.println("Error while updating flights");
            }
        });
    }
    @FXML
    void LogOut(ActionEvent event) {
        try {
            service.logOutAgent(agent, this);
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogInf.fxml"));
            Parent root = loader.load();

            LogInController controller = loader.getController();
            controller.setService(service);

            Scene scene = new Scene(root);
            stage.setTitle("Log In");
            stage.setScene(scene);
            stage.show();
        }catch (ProjectException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Error while logging out");
        }
    }

    @FXML
    void Search(ActionEvent event) {
        if(comboBox1.getValue() == null || comboBox2.getValue() == null || datePicker.getValue() == null){
            System.out.println("Please fill in all fields");
            return;
        }
        try {
            searhcedFlights = service.searchFlights(comboBox1.getValue(), comboBox2.getValue(), datePicker.getValue().atStartOfDay());
            results = FXCollections.observableArrayList(searhcedFlights.keySet());
            table2.setItems(results);
        }catch (ProjectException e){
            System.out.println("Error while searching flights");
        }
    }

    @FXML
    void Book(ActionEvent event) {
        if(table2.getSelectionModel().getSelectedItem() == null){
            System.out.println("Please select a flight");
            return;
        }
        Flight flight = table2.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookFlightf.fxml"));
            Parent root = loader.load();

            BookFlightController controller = loader.getController();
            controller.setController(service, agent, flight, searhcedFlights.get(flight));

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Log In");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Error while booking flight");
        }
    }
}
