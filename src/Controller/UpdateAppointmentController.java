package Controller;

import Model.*;
import Utility.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
/** Update appointment controller screen. */
public class UpdateAppointmentController implements Initializable {


    @FXML
    private Button Cancel;

    @FXML
    private TextField appointmentID;

    @FXML
    private TextField TitleF;

    @FXML
    private TextField DescriptionF;

    @FXML
    private TextField LocationF;

    @FXML
    private TextField TypeF;

    @FXML
    private Button Save;

    @FXML
    private ComboBox<Customer> CustomerCB;

    @FXML
    private ComboBox<User> UserCB;

    @FXML
    private ComboBox<Contact> ContactCB;

    @FXML
    private DatePicker StartDP;

    @FXML
    private ComboBox<LocalTime> StartCB;

    /** Cancel Update appointment.
     * Informs the user that the cancellation will delete all of their input data.
     * Closes the window upon cancellation.
     * @param event A button on the UI.
     */

    @FXML
    public void OACancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) Cancel.getScene().getWindow();
            stage.close();

        }
    }

    /** Save the updated appointment.
     * Pulls the data from the UI fields to create a new appointment object.
     * Displays error windows if data is missing.
     * Uses the new object in a SQL statement to update the database, upon a successful update the program redownload any
     * appointments not in the program already.
     * @param event A button on the UI.
     */
    @FXML
    public void OASave(ActionEvent event) {
        try {

            ContactCB.getSelectionModel().getSelectedItem();
            int appID = Integer.parseInt(appointmentID.getText());
            String title = TitleF.getText();
            String description = DescriptionF.getText();
            String location = LocationF.getText();
            String type = TypeF.getText();
            LocalDate startDate = StartDP.getValue();
            LocalTime startTime = StartCB.getSelectionModel().getSelectedItem();
            LocalTime endTime = startTime.plusMinutes(30);
            Customer customer = CustomerCB.getSelectionModel().getSelectedItem();
            User user = UserCB.getSelectionModel().getSelectedItem();
            Contact contact = ContactCB.getSelectionModel().getSelectedItem();
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = LocalDateTime.of(startDate, endTime);
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (user == null){
                errorWindow(3);
                return;
            }
            if (customer == null) {
                errorWindow(3);
                return;
            }
            if (contact == null) {
                errorWindow(3);
                return;
            }
            if(startDate.isBefore(LocalDate.now())){
                errorWindow(5);
                return;
            }

            Appointment appointment = new Appointment(appID, title, description, location, type, start, end,
                    customer.getCustomerID(), user.getUser_ID(), contact.getContactID());
            if (DBQuery.appointmentUpdate(appointment) > 0) {
                DBQuery.appointmentsConnection();
                Stage stage = (Stage) Cancel.getScene().getWindow();
                stage.close();
            } else {
                errorWindow(4);
            }
        }catch (NullPointerException | SQLException e){
            System.out.println(e.getMessage());
            if(e.getMessage().equals("time")) {
                errorWindow(2);
            }
            else if(e.getMessage().equals("date")){
                errorWindow(2);
            }
            else
                errorWindow(3);
        }
    }

    /** A change or selection of the appointment's start date.
     * Tests if the selected date is before today, displays a warning and switches the date to the current date if true.
     * Checks if the date is a Saturday or Sunday and add days till the date is the next monday after displaying a warning about selecting a weekday.
     * Populates the StartTime Combobox with available times and inserts the updated appointment original time back into the selectable list.
     * @param event A UI button on screen.
     */

    @FXML
    public void startDateChange(ActionEvent event) {

        try {
            LocalDate today = LocalDate.now();
            LocalDate start = StartDP.getValue();
            if (start.isBefore(LocalDate.now())) {
                errorWindow(5);
                StartDP.setValue(LocalDate.now());
                return;
            }
            if (start.getDayOfWeek().equals(DayOfWeek.SUNDAY) || start.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                errorWindow(6);
                if (today.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    StartDP.setValue(today.plusDays(2));
                } else if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    StartDP.setValue(today.plusDays(1));
                }
                return;
            }
            int appID = Integer.parseInt(appointmentID.getText());
            Appointment appointment = Appointment.getAppointment(appID);
            LocalDate appStart = appointment.getStart().toLocalDate();
            LocalTime appStartTime = appointment.getStart().toLocalTime();


            Date.createTimes(StartDP.getValue(), 1);
            Appointment.customerAppointments(StartDP.getValue());
            Date.insertTime(appStartTime);
            if(appStart.equals(StartDP.getValue()))
                StartCB.setValue(appStartTime);
            StartCB.setItems(Date.getAllTimes());
        }
        catch (Exception x) {
            System.out.println(x.getCause());

        }
    }

    /** Retrieves selected appointment from the Main Menu screen.
     * Clears and resets the available appointment times.
     * @param appointment A selected appointment from the appointments table.
     */

    public void receiveAppointment(Appointment appointment) {
        appointmentID.setText(String.valueOf(appointment.getAppointmentID()));
        TitleF.setText(String.valueOf(appointment.getTitle()));
        DescriptionF.setText(String.valueOf(appointment.getDescription()));
        LocationF.setText(String.valueOf(appointment.getLocation()));
        TypeF.setText(String.valueOf(appointment.getType()));
        Contact contact = Contact.getContact(appointment.getContactID());
        ContactCB.setValue(contact);
        Customer customer = Customer.getCustomer(appointment.getCustomerID());
        CustomerCB.setValue(customer);
        User user = User.getUser(appointment.getUserID());
        UserCB.setValue(user);
        LocalDateTime start = appointment.getStart();
        StartDP.setValue(start.toLocalDate());
        StartCB.setValue(start.toLocalTime());

        Date.clearTimes();
        Date.createTimes(StartDP.getValue(), 1);

        Appointment.customerAppointments(StartDP.getValue());
        StartCB.setItems(Date.getAllTimes());


    }
    /** Update appointment error windows to be displayed.
     * Used in other functions to display warnings and error messages.
     * @param code provided from methods above to call a specific error message.
     */
    private void errorWindow(int code) {// error messages.
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("A data field is empty.");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select starting and ending dates and times.");
            alert.showAndWait();
        }

        if (code == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select a user, customer, and contact.");
            alert.showAndWait();
        }
        if (code == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Connection Error");
            alert.setContentText("Appointment could not be added due to a database connection error, please try again. ");
            alert.showAndWait();
        }
        if (code == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please pick a date and Time after the current date and time.");
            alert.showAndWait();
        }
        if (code == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please pick a weekday to add an appointment.");
            alert.showAndWait();
        }
    }

    /** Update appointment initializer.
     * Populates the comboboxes for selecting customers, contacts, and users.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerCB.setItems(Customer.getAllCustomers());
        ContactCB.setItems(Contact.getAllContacts());
        UserCB.setItems(User.getUsers());

    }
}

