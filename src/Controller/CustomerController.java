package Controller;

import Bundles_Lambda.AdditionLB;
import Model.Appointment;
import Model.Countries;
import Model.Customer;
import Model.Division;
import Utility.DBQuery;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
/** The customer portion of the UI.  */
public class CustomerController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private TableView<Customer> CustomerTable;

    @FXML
    private TableColumn<Customer, Integer> customerID;

    @FXML
    private TableColumn<Customer, String> CustomerName;

    @FXML
    private TableColumn<Customer, String> Address;

    @FXML
    private TableColumn<Customer, String> Code;

    @FXML
    private TableColumn<Customer, String> DivisionT;

    @FXML
    private TableColumn<Customer, String> Country;

    @FXML
    private TableColumn<Customer, String> PhoneNumber;

    @FXML
    private TableColumn<?, ?> createDate;

    @FXML
    private TableColumn<Customer, String> CreateBy;

    @FXML
    private TableColumn<?, ?> updateDate;

    @FXML
    private TableColumn<?, ?> updateBy;

    @FXML
    private Button AddCustomer;

    @FXML
    private Button UpdateCustomer;

    @FXML
    private Button DeleteCustomer;

    @FXML
    private Button MainMenu;

    /**
     * Displays a add customer scene over the customer UI.
     * @param event Button from the UI.
     * @throws IOException a thrown exception if the scene is unable to be loaded.
     */

    @FXML
    public void OAAddCustomer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/AddCustomer.fxml"));
        Parent add = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(add));
        stage.show();

    }

    /**
     * Deletes a customer from the Database and program.
     * Pulls the selected customer from the table and displays a warning if no customer is selected.
     * Checks if the selected customer has any appointments and alerts that the customer cannot be deleted until the appointments are removed.
     * If no appointments exist a conformation window is displayed confirming the deletion of the customer.
     * LAMBDA expression additionLB used to simplify the addition process for deleting an appointment.
     * @param event Button from the UI.
     */
    @FXML
    public void OADeleteCustomer(ActionEvent event) {
        try {
            ObservableList<Appointment> appointments = Appointment.getAllAppointments();
            Customer customer = CustomerTable.getSelectionModel().getSelectedItem();
            int id = customer.getCustomerID();

            AdditionLB add = (x) -> x =+1;

            int i = 0;
            for (Appointment app : appointments) {
                if (app.getCustomerID() == id)
                    i = add.addition(i);

            }
            if (i >= 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Unable to delete customer.");
                alert.setContentText("Customer " + id + " can not be deleted. Please delete all of their appointments first.");
                alert.showAndWait();
            } else {
                Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete this customer " + id + ", do you wish to continue?");
                Optional<ButtonType> result = warning.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        String table = "customers";
                        String column = "Customer_ID";
                        int delete = DBQuery.deleteStatement(table, column, customer.getCustomerID());
                        if (delete > 0) {
                            Customer.deleteCustomer(customer);
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Customer Deleted.");
                            alert.setContentText("Customer " + id + " was delete.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Customer deletion error.");
                            alert.setContentText("Connection to the database to delete customer " + id + " was un successful. Please try again.");
                            alert.showAndWait();
                        }
                    } catch (NullPointerException | SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("No customer selected.");
                        alert.setContentText("Please select a customer to Delete.");
                        alert.showAndWait();
                    }
                }
            }
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to Delete.");
            alert.showAndWait();
        }
    }

    /**
     * Return to the main menu.
     * @param event A button on the UI that returns the user to the main menu.
     * @throws IOException a exception for if the program is unable to return to the main screen.
     */

    @FXML
    public void OAMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));// loads the addProduct screen
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Loads the customer update screen.
     * Uses a try/catch to load the update customer screen with a selected customer from the customer table.
     * To ensure the database date matches the program's data a query is used to retrieve the customer from the database through DBQuery.
     * A error message is displayed if no customer is selected.
     * @param event a button on the UI that needs a selected customer to update.
     */
    @FXML
    public void OAUpdateCustomer(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View/UpdateCustomer.fxml"));
            Parent add = fxmlLoader.load();
            UpdateCustomerController updateCustomer = fxmlLoader.getController();
            Customer customer = CustomerTable.getSelectionModel().getSelectedItem();
            updateCustomer.receiveCustomer(DBQuery.retrieveCustomer(customer.getCustomerID()));
            Stage stage = new Stage();
            stage.setScene(new Scene(add));
            stage.show();


        } catch (IOException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        }
    }

    /**
     * Customer screen Initialization.
     * Pulls data from the customer class's ObservableArrayList and the Getter methods for each customer to populate the table.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerTable.setItems(Customer.getAllCustomers());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Code.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        DivisionT.setCellValueFactory(new PropertyValueFactory<>("DivisionName"));
        Country.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        PhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDate.setCellValueFactory(new PropertyValueFactory<>("creation"));
        CreateBy.setCellValueFactory(new PropertyValueFactory<>("createBy"));
        updateDate.setCellValueFactory(new PropertyValueFactory<>("LastUpdate"));
        updateBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));

    }
}
