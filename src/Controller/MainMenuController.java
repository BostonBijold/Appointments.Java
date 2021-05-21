package Controller;

import Model.Appointment;
import Model.Customer;
import Model.Date;
import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;
/** The main menu screen that displays the scheduled appointments. */
public class MainMenuController implements Initializable {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;


    @FXML
    private Button WeekView;

    @FXML
    private Button MonthView;

    @FXML
    private Button Customers;

    @FXML
    private Button AddAppointment;

    @FXML
    private Button UpdateAppointment;

    @FXML
    private Button DeleteAppointment;

    @FXML
    private Button Reports;

    @FXML
    private Button Logout;

    @FXML
    private TableView<Appointment> Appointments;

    @FXML
    private TableColumn<Appointment, ?> AppointmentID;

    @FXML
    private TableColumn<Appointment, ?> Title;

    @FXML
    private TableColumn<Appointment, ?> Description;

    @FXML
    private TableColumn<Appointment, ?> Location;

    @FXML
    private TableColumn<Appointment, ?> Type;

    @FXML
    private TableColumn<Appointment, ?> Start;

    @FXML
    private TableColumn<Appointment, ?> End;

    @FXML
    private TableColumn<Appointment, ?> Created;

    @FXML
    private TableColumn<Appointment, ?> CreatedBy;

    @FXML
    private TableColumn<Appointment, ?> LastUpdate;

    @FXML
    private TableColumn<Appointment, ?> UpdateBy;

    @FXML
    private TableColumn<Appointment, ?> CustomerID;

    @FXML
    private TableColumn<Appointment, ?> UserID;

    @FXML
    private TableColumn<Appointment, ?> ContactID;

    /** Add appointment window loader.
     * AddAppointment loads the add new appointment screen over the main menu screen.
     * @param event A UI button on the screen.
     * @throws IOException A thrown exception if the scene is unable to be loaded.
     */
    @FXML
    public void OAAddAppointment(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/AddAppointment.fxml"));
        Parent add = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(add));
        stage.show();
    }

    /** Customer screen loader.
     * Customers loads the customer page replacing the main menu page.
     * @param event A UI button on screen.
     * @throws IOException A thrown exception if the scene is unable to be loaded.
     */
    @FXML
    public void OACustomers(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    /** Delete appointment.
     * Uses the DBQuery class to delete a appointment on the program and the database after checking if the User is sure they want to.
     * Uses the selected appointment's ID to use the SQL query to delete the appointment.
     * If deletion is completed a warning will display to inform the user.
     * If deletion fails due to database connection a warning is displayed informing the user.
     * A warning is displayed if no appointment is selected.
     * @param event a UI button on the screen.
     */
    @FXML
    public void OADeleteAppointment(ActionEvent event) {
        try {
            Appointment appointment = Appointments.getSelectionModel().getSelectedItem();

            int appointmentID = appointment.getAppointmentID();
            Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete this appointment " + appointmentID + ", do you wish to continue?");
            Optional<ButtonType> result = warning.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                try {
                    String table = "appointments";
                    String column = "Appointment_ID";
                    int delete = DBQuery.deleteStatement(table, column, appointment.getAppointmentID());
                    if (delete > 0) {
                        Appointment.deleteAppointment(appointment);
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Appointment Deleted.");
                        alert.setContentText("Appointment " + appointmentID + " was delete.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Appointment deletion error.");
                        alert.setContentText("Connection to the database to delete Appointment " + appointmentID + " was unsuccessful. Please try again.");
                        alert.showAndWait();
                    }
                } catch (NullPointerException | SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No appointment selected.");
                    alert.setContentText("Please select a appointment to Delete.");
                    alert.showAndWait();
                }
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select a appointment to Delete.");
            alert.showAndWait();
        }
    }

    /** User Logout.
     * Logs the user out and loads the Log in page again.
     * OA stands for On Action
     * @param event a UI button.
     * @throws IOException a thrown exception if the scene is unable to be loaded.
     */
    @FXML
    public void OALogout(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Monthly appointment view.
     * Shows only the appointments in the current month.
     * @param event a UI button on the screen.
     */
    @FXML
    public void OAMonthView(ActionEvent event) {
        Appointment.clearMonthlyAppointment();
        Appointment.setMonthAppointments();
        Appointments.setItems(Appointment.getMonthAppointments());

    }


    /** Default appointment view.
     * Returns the displayed appointments to the default view.
     * @param event a UI button on the screen.
     */
    @FXML
    public void OADefault(ActionEvent event) {
        Appointments.setItems(Appointment.getAllAppointments());
    }

    /** Report screen loader.
     * Loads the Reports page, replacing the main menu screen.
     * @param event A UI button on screen.
     * @throws IOException a thrown exception if the scene is unable to be loaded.
     */
    @FXML
    public void OAReports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Update appointment screen loader.
     * Receives the onscreen selected appointment and populates the Update appointment screen that loads over the main menu screen.
     * Uses a SQL Query to load the database appointment to avoid error in loading an incorrect version of a appointment.
     * Throws a error message if there is no appointment selected.
     * @param event A UI button on the screen that requires a selected appointment.
     */
    @FXML
    public void OAUpdateAppointment(ActionEvent event)  {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View/UpdateAppointment.fxml"));
            Parent add = fxmlLoader.load();
            UpdateAppointmentController updateAppointment = fxmlLoader.getController();
            Appointment appointment = Appointments.getSelectionModel().getSelectedItem();
            int ID = appointment.getAppointmentID();
            Appointment app = DBQuery.retrieveAppointment(ID);
            updateAppointment.receiveAppointment(app);
            Stage stage = new Stage();
            stage.setScene(new Scene(add));
            stage.show();


        }catch (IOException | NullPointerException e){
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select a appointment to update.");
            alert.showAndWait();}
    }
    /** Weekly appointment view.
     * Shows only the appointments for the next 7 days.
     * @param event A UI button on the screen.
     */
    @FXML
    public void OAWeekView(ActionEvent event) {
        Appointment.clearWeeklyAppointments();
        Appointment.setWeekAppointments();
        Appointments.setItems(Appointment.getWeekAppointments());
    }

    /** Initializes the main menu page and 15 min appointment alert.
     * Loads the allAppointments ObservableArrayList from the Appointments class and populates the table with each appointment as a row.
     * Upon initialization the controller, checks if an appointment is starting in 15 minutes and shows a warning screen with the clients details if there is an appointment.
     * If no appointment is scheduled within 15 minutes a display is shown.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allAppointments = Appointment.getAllAppointments();
        Appointments.setItems(allAppointments);
        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        Location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        End.setCellValueFactory(new PropertyValueFactory<>("End"));
        Created.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        CreatedBy.setCellValueFactory(new PropertyValueFactory<>("CreateBy"));
        LastUpdate.setCellValueFactory(new PropertyValueFactory<>("LastUpdate"));
        UpdateBy.setCellValueFactory(new PropertyValueFactory<>("LastUpdateBy"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        UserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        ContactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        int i = 0;

        for(Appointment appointment:allAppointments){
            LocalDateTime start = appointment.getStart();
            LocalDateTime current = LocalDateTime.now();
            long timeDifference = ChronoUnit.MINUTES.between(current,start);
            Customer customer = Customer.getCustomer(appointment.getCustomerID());
            String name = customer.getCustomerName();
            if(timeDifference > 0 && timeDifference < 30){
                i +=1;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("You have an upcoming appointment.");
                alert.setContentText("In " + timeDifference + " minutes there is an appointment with " + name + ".");
                alert.showAndWait();}

        }
        if(i < 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Upcoming Appointments");
            alert.setHeaderText("You have no upcoming appointments.");
            alert.setContentText("There is no appointment scheduled within the next 15 minutes.");
            alert.showAndWait();}


        }

    }

