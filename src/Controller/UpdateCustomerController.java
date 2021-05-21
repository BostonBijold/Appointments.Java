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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
/** Update customer controller. */
public class UpdateCustomerController implements Initializable {


    @FXML
    private Button Cancel;

    @FXML
    private TextField IDField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCode;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button Save;

    @FXML
    private ComboBox<Countries> countryCombo;

    @FXML
    private ComboBox<Division> divisionCombo;

    @FXML
    private String CreatedBy;

    @FXML
    private LocalDateTime CreationDate;

    /**Created by.
     * Gets the logged in User to record in the database.
     * @return returns the logged in User.
     */
    public String getCreatedBy() {
        return CreatedBy;
    }

    /** Returns the date of update.
     * Returns the updated Datetime to record in the database.
     * @return Returns the current Date.
     */
    public LocalDateTime getCreationDate() {
        return CreationDate;
    }

    /** Sets the Created Date.
     * Sets the created DateTime as the current DateTime.
     * @param creationDate The current DateTIme.
     */
    public void setCreationDate(LocalDateTime creationDate) {
        CreationDate = creationDate;
    }

    /** Cancel add customer screen.
     * Asks if the user wants to clear all entered values, a canceled form closes the add customer screen without saving.
     * Clears the Country and division lists.
     * @param event A button on the UI to cancel the creation of a new customer.
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
    /** Saves a new customer.
     * Using a try/catch block data is retrived from the UI fields and comboboxes to create a new customer.
     * Error windows are displayed if data is missing stopping the save.
     * The new customer object added to the Database through the DBQuery statement.
     * @param event A button on the UI of the program that starts the saving process of the entered data.
     * @throws SQLException Displays a warning that the SQL query was unsuccessful and to try again or check the internet connection.
     */

    @FXML
    public void OASave(ActionEvent event) throws SQLException {


        try {
            String user = User.getLoggedIn();
            int ID = Integer.parseInt(IDField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            Division division = divisionCombo.getSelectionModel().getSelectedItem();
            Countries country = countryCombo.getSelectionModel().getSelectedItem();
            String code = postalCode.getText();
            String phone = phoneNumber.getText();
            LocalDateTime created = getCreationDate();

            String createdBy = getCreatedBy();
            LocalDateTime lastUpdate = LocalDateTime.now();
            if (name.isEmpty()) {
                errorWindow(2);
                return;
            }
            if (address.isEmpty()) {
                errorWindow(2);
                return;
            }
            if (code.isEmpty()) {
                errorWindow(2);
                return;
            }
            if (phone.isEmpty()) {
                errorWindow(2);
                return;
            }
            if (country == null) {
                errorWindow(1);
                return;
            }
            if (division == null) {
                errorWindow(3);
                return;
            }
            Customer newCustomer = new Customer(ID, name, address, code, phone, created, createdBy, lastUpdate, user, division);
            if (DBQuery.customerUpdate(newCustomer) > 0) {
                Customer.updateCustomer(newCustomer);
            }

            Stage stage = (Stage) Cancel.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Connection Error");
            alert.setContentText("Appointment could not be added due to a database connection error, please try again. ");
            alert.showAndWait();
        }

    }

    /** Country selection change.
     * Clears the combobox of first level division and repopulates the combobox with states or provinces from the newly selected country.
     * @param event A change in the combobox for selecting countries in the UI.
     */

    @FXML
    public void countryChange(ActionEvent event) {
        Division.clearCountryDivisions();
        Countries country = countryCombo.getSelectionModel().getSelectedItem();
        int countryID = country.getCountryID();
        if(Division.getCountrysDivisions(countryID).isEmpty())
            divisionCombo.setPromptText("N/A");
        divisionCombo.setItems(Division.getCountrysDivisions(countryID));
    }

    /** Retrieves selected customer from the customer screen.
     * @param customer A selected customer from the customer table.
     */

    public void receiveCustomer(Customer customer) {
        IDField.setText(String.valueOf(customer.getCustomerID()));
        nameField.setText(String.valueOf(customer.getCustomerName()));
        addressField.setText(String.valueOf(customer.getAddress()));
        postalCode.setText(String.valueOf(customer.getPostalCode()));
        phoneNumber.setText(String.valueOf(customer.getPhone()));
        countryCombo.setItems(Countries.getAllCountries());
        Countries countries = customer.getCountry();
        countryCombo.setValue(countries);
        divisionCombo.setItems(Division.getCountrysDivisions(countries.getCountryID()));
        divisionCombo.setValue(customer.getDivisiontest());
        CreatedBy =customer.getCreateBy();
        CreationDate = customer.getCreation();

    }

    /** Update customer error windows to be displayed.
     * Used in other functions to display warnings and error messages.
     * @param code provided from methods above to call a specific error message.
     */

    private void errorWindow(int code) {// error messages.
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please choose a country");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Data field is empty.");
            alert.showAndWait();
        }
        if (code == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Country and state/province not available.");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryCombo.setItems(Countries.getAllCountries());
        countryCombo.setPromptText("Choose a Country");
        divisionCombo.setPromptText("Province or State");

    }
}

