package MPP.interfaces;

import MPP.business.Service;
import MPP.domain.Agent;
import MPP.domain.Flight;
import MPP.utils.Observer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AgentMenuController implements Initializable, Observer {

    private Service service;

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
    private ObservableList<Flight> results = FXCollections.observableArrayList();

    public void setController(Service service, Agent agent){
        this.service = service;
        this.agent = agent;
        from.addAll(service.allFroms());
        to.addAll(service.allTos());
        comboBox1.setItems(from);
        comboBox2.setItems(to);
        update();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<Flight, String>("from"));
        col2.setCellValueFactory(new PropertyValueFactory<Flight, String>("to"));
        col3.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("date"));
        col4.setCellValueFactory(flight -> new SimpleIntegerProperty(service.getAvailableSeats(flight.getValue())).asObject());
        table1.setItems(allFLightsAvailable);
        col5.setCellValueFactory(new PropertyValueFactory<Flight, String>("from"));
        col6.setCellValueFactory(new PropertyValueFactory<Flight, String>("to"));
        col7.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("date"));
        col8.setCellValueFactory(flight -> new SimpleIntegerProperty(service.getAvailableSeats(flight.getValue())).asObject());
        table2.setItems(results);
        comboBox1.setItems(from);
        comboBox2.setItems(to);
    }

    public void update(){
        allFLightsAvailable = FXCollections.observableArrayList(service.getAllFlightsAvailable());
        table1.setItems(allFLightsAvailable);
        table1.refresh();
        //table1 - i store all Flights Available
        if(comboBox1.getValue() != null && comboBox2.getValue() != null && datePicker.getValue() != null)
            results = FXCollections.observableArrayList(service.searchFlights(comboBox1.getValue(), comboBox2.getValue(), datePicker.getValue()));
        //table 2 i have only the flights i searched
        table2.setItems(results);
        table2.refresh();
    }
    @FXML
    void LogOut(ActionEvent event) {
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.close();

        try{
            //After the logout  i will reload the Login page in case i need another agent to login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogInf.fxml"));
            Parent root = loader.load();

            LogInController controller = loader.getController();
            controller.setService(service);

            Scene scene = new Scene(root);
            stage.setTitle("Log In");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Error while logging out");
        }
    }

    @FXML
    void Search(ActionEvent event) {
        /* Search a flight based on departure , destination and the date value */
        if(comboBox1.getValue() == null || comboBox2.getValue() == null || datePicker.getValue() == null){
            System.out.println("Please fill in all fields");
            return;
        }
        results = FXCollections.observableArrayList(service.searchFlights(comboBox1.getValue(), comboBox2.getValue(), datePicker.getValue()));
        table2.setItems(results);
    }

    @FXML
    void Book(ActionEvent event) {
        /* Book a selected flight by adding names address etc */
        if(table2.getSelectionModel().getSelectedItem() == null){
            System.out.println("Please select a flight");
            return;
        }
        Flight flight = table2.getSelectionModel().getSelectedItem();
        try{
            // Load the booking window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookFlightf.fxml"));
            Parent root = loader.load();

            BookFlightController controller = loader.getController();
            controller.setController(service, agent, flight);

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
