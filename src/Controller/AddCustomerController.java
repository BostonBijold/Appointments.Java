package Controller;

import Model.Countries;
import Model.Customer;
import Model.Division;
import Model.User;
import Utility.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
/** add Customer Controller */
public class AddCustomerController implements Initializable {



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

    /** Cancel add customer screen.
     * Asks if the user wants to clear all entered values, a canceled form closes the add customer screen without saving.
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
     * Using a try/catch block data is retrieved from the UI fields and comboboxes to create a new customer.
     * Error windows are displayed if data is missing stopping the save.
     * The new customer object added to the Database through the DBQuery statement.
     * @param event A button on the UI of the program that starts the saving process of the entered data.
     * @throws SQLException Displays a warning that the SQL query was unsuccessful and to try again or check the internet connection.
     */
    @FXML
    public void OASave(ActionEvent event) throws SQLException {

        String name;
        String address;
        String code;
        String phone;
        try {
            String user = User.getLoggedIn();
            name = nameField.getText();
            address = addressField.getText();
            Division division = divisionCombo.getSelectionModel().getSelectedItem();
            Countries country = countryCombo.getSelectionModel().getSelectedItem();
            code = postalCode.getText();
            phone = phoneNumber.getText();
            if(name.isEmpty()) {
                errorWindow(2);
                return;
            }
            if(address.isEmpty()) {
             errorWindow(2);
                return;
            }
            if(code.isEmpty()) {
                errorWindow(2);
                return;
             }
            if(phone.isEmpty()) {
                errorWindow(2);
                return;
             }
            if(country ==  null ){
                errorWindow(1);
                return;
            }
            if(division == null){
                errorWindow(3);
                return;
            }
            Customer newCustomer = new Customer( name, address, code, phone, division, user);

            if (DBQuery.customerInsert(newCustomer) > 0) {
                DBQuery.initialCustomerConnection();
            }
        else {
           errorWindow(4);
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
        divisionCombo.setItems(Division.getCountrysDivisions(countryID));
    }

    /** Add customer initialization.
     * Initializes and populates the country combobox and displays a message in the country and division combobox.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        countryCombo.setItems(Countries.getAllCountries());
        countryCombo.setPromptText("Choose a Country");
        divisionCombo.setPromptText("Province or State");

    }

    /** Add customer error windows to be displayed.
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
        if (code == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Connection Error");
            alert.setContentText("Customer could not be added due to a database connection error.");
            alert.showAndWait();
        }
    }
}
