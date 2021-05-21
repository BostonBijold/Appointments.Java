package Controller;

import Model.ContactTotal;
import Model.Customer;
import Model.CustomerTotals;
import Model.MonthTotals;
import Utility.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;
/** The reports controller for the reports screen. */
public class ReportsController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Button Totals;

    @FXML
    private Button Contacts;

    @FXML
    private Button DeleteCustomer;

    @FXML
    private Button MainMenu;

    @FXML
    private TableView<MonthTotals> monthType;

    @FXML
    private TableColumn<MonthTotals, String> type;

    @FXML
    private TableColumn<MonthTotals, ?> month;

    @FXML
    private TableColumn<MonthTotals, ?> total;

    @FXML
    private TableView<ContactTotal> contactReport;

    @FXML
    private TableColumn<ContactTotal, ?> CRcontact;

    @FXML
    private TableColumn<ContactTotal, ?> CRappointment;

    @FXML
    private TableColumn<ContactTotal, ?> CRdescription;

    @FXML
    private TableColumn<ContactTotal, ?> CRstart;

    @FXML
    private TableColumn<ContactTotal, ?> CRend;

    @FXML
    private TableColumn<ContactTotal, ?> CRcustomer;

    @FXML
    private TableView<CustomerTotals> customerTotal;

    @FXML
    private TableColumn<?, ?> customerID;

    @FXML
    private TableColumn<?, ?> customerName;

    @FXML
    private TableColumn<?, ?> totalAppointments;

    /** Contact report display.
     * Sets the month total and customer total reports to invisible and displays the contact report.
     * @param event A UI button on screen.
     */

    @FXML
    public void OAContacts(ActionEvent event) {
        contactReport.setVisible(true);
        monthType.setVisible(false);
        customerTotal.setVisible(false);
        contactReport.setItems(ContactTotal.getContactTotal());
        CRcontact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        CRappointment.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        CRdescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        CRstart.setCellValueFactory(new PropertyValueFactory<>("Start"));
        CRend.setCellValueFactory(new PropertyValueFactory<>("End"));
        CRcustomer.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
    }

    /** Return to Main Menu.
     * Loads the menu screen.
     * @param event A button from the UI.
     * @throws IOException A thrown exception if the scene is unable to be loaded.
     */

    @FXML
    public void OAMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Month total report display.
     * Sets the contact total and customer total reports to invisible and displays the month report.
     * @param event A button on the UI.
     */
    @FXML
    public void OATotals(ActionEvent event) {
        monthType.setVisible(true);
        contactReport.setVisible(false);
        customerTotal.setVisible(false);
        monthType.setItems(MonthTotals.getTypeMonthReport());
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        month.setCellValueFactory(new PropertyValueFactory<>("Month"));
        total.setCellValueFactory(new PropertyValueFactory<>("Total"));

    }

    /** Customer total report display.
     * Sets the contact total and month total reports to invisible and displays the month report.
     * @param event A button on the UI.
     */
    public void OACustomerTotal(ActionEvent event) {
        customerTotal.setVisible(true);
        contactReport.setVisible(false);
        monthType.setVisible(false);
        customerTotal.setItems(CustomerTotals.getCustomerTotal());
        customerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        totalAppointments.setCellValueFactory(new PropertyValueFactory<>("Total"));

    }

    /** Report controller initializer.
     *  Loads and displays the Month total report.
     * @param url The location of the resources to initialize.
     * @param resourceBundle program resource bundle for initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ContactTotal.clearContactTotal();
        CustomerTotals.clearCustomerTotal();
        MonthTotals.clearMonthTotal();

        DBQuery.monthTotalQuery();
        DBQuery.contactReport();
        DBQuery.customerTotalsReport();

        monthType.setVisible(true);
        monthType.setItems(MonthTotals.getTypeMonthReport());
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        month.setCellValueFactory(new PropertyValueFactory<>("Month"));
        total.setCellValueFactory(new PropertyValueFactory<>("Total"));


    }
}
