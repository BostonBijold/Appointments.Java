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
/** Add Appointment controller. */
public class AddAppointmentController implements Initializable {



    @FXML
    private Button Cancel;

    @FXML
    private Button Save;

    @FXML
    private TextField TitleF;

    @FXML
    private TextField DescriptionF;

    @FXML
    private TextField LocationF;

    @FXML
    private TextField TypeF;

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


    /**
     * Cancels adding an appointment.
     * @param event Upon cancel button being clicked the user will be alerted with a conformation message that all entered data will be lost if canceled.
     */
    @FXML
    public void OACancel(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Countries.clearCountryList();
            Division.clearDivisions();
            Stage stage = (Stage) Cancel.getScene().getWindow();
            stage.close();

        }
    }

    /**
     * Saves the new appointment.
     * @param event  Uses a try/catch block to pull data from the input fields on the page.
     *               Uses errorWindow(#) to inform the user if a field is empty is invalid.Uses the DBQuery to add the
     *               new appointment to the database.
     * @throws SQLException Provides an error message if the insert is unsuccessful to have the user try again or check the internet connection.
     */
    @FXML
    public void OASave(ActionEvent event) throws SQLException {

        try {

            ContactCB.getSelectionModel().getSelectedItem();
            String title = TitleF.getText();
            String description = DescriptionF.getText();
            String location = LocationF.getText();
            String type = TypeF.getText();
            LocalDate startDate = StartDP.getValue();
            System.out.println(startDate);
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

            Appointment appointment = new Appointment(title, description, location, type, start, end,
                    customer.getCustomerID(), user.getUser_ID(), contact.getContactID());
            if (DBQuery.appointmentInsert(appointment) > 0) {
                DBQuery.appointmentsConnection();

                Stage stage = (Stage) Cancel.getScene().getWindow();
                stage.close();
            } else {
                errorWindow(4);
            }
        }catch (NullPointerException e){
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

    /** A change or selection of an appointment start date.
     * When a StartDate is selected or changed it first checks if the date is before today, displays a warning about scheduling before current date and changes the selection to the current date.
     * Does not allow for a selection of Weekends and blocks with an error message.
     * After valid date is selected the start times are created and available for selection.
     * @param event a datepicker on the program UI.
     */
    @FXML
    public void startDateChange(ActionEvent event) {
        LocalDate today = LocalDate.now();
        LocalDate start = StartDP.getValue();
        if(start.isBefore(LocalDate.now())){
            errorWindow(5);
            StartDP.setValue(LocalDate.now());
            return;
        }
        if(start.getDayOfWeek().equals(DayOfWeek.SUNDAY) || start.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            errorWindow(6);
            if(today.getDayOfWeek() == DayOfWeek.SATURDAY)
                StartDP.setValue(today.plusDays(2));
            else if (today.getDayOfWeek() == DayOfWeek.SUNDAY)
                StartDP.setValue(today.plusDays(1));
            return;
        }
        Date.clearTimes();
        Date.createTimes(StartDP.getValue(),0);
        Appointment.customerAppointments(StartDP.getValue());
        StartCB.setItems(Date.getAllTimes());

    }



    /** Add appointment initialization.
     * Initializes the Contact, Customer and User comboboxes on add appointment form.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactCB.setItems(Contact.getAllContacts());
        CustomerCB.setItems(Customer.getAllCustomers());
        UserCB.setItems(User.getUsers());


    }

    /** Add appointment error windows to be displayed.
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
}
