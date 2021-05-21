package Model;

import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.IDN;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
/** Customer Class */
public class Customer implements Initializable {

    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String createBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    private int divisionID;
    private Division division;
    private String divisionName;
    private String countryName;
    private Countries country;
    private LocalDateTime creation;

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer( String customerName, String address, String postalCode, String phone, Division division,
                     String createBy) {

        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        this.createBy = createBy;
        countryName = setCountryName(division.getCountryID());
        country = setCountry(division.getCountryID());
        divisionName = division.getDivisionName();
        divisionID = division.getDivisionID();
    }


    public Customer(int customerID, String customerName, String address, String postalCode, String phone, LocalDateTime creation,
                    String createBy, LocalDateTime lastUpdate, String lastUpdateBy,
                    Division division) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.creation = creation;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.division = division;
        countryName = setCountryName(division.getCountryID());
        country = setCountry(division.getCountryID());
        divisionName = division.getDivisionName();
        divisionID = division.getDivisionID();
    }

    /** Customer Country setter.
     * Loops through all countries with a countryID to match and returns the match.
     * @param countryid A provided int CountryID
     * @return  Returns a country with a matching country ID.
     */

    public Countries setCountry(int countryid){
        for(Countries countries: Countries.getAllCountries()){
            if(countryid == countries.getCountryID())
                country = countries;
        }
        return country;
    }
    public Countries getCountry(){
        return country;
    }
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivision(int division) {
        divisionID = division;
    }

    /** Add Customer method.
     * Loops through current customers and adds the provided object if it is not currently recorded in the observableArrayList.
     * @param customer A customer object to add if missing from the list.
     */

    public static void AddCustomer(Customer customer){
        int i = 0;
        for(Customer c : allCustomers)

            if(c.getCustomerID() == customer.getCustomerID())
                i += 1;
            if(i == 0)
                allCustomers.add(customer);
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        divisionName = divisionName;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Model.Division getDivisiontest() {
        return division;
    }

    public void setDivisiontest(Model.Division divisiontest) {
        this.division = divisiontest;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String country) {
        this.countryName = country;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getLastupdate() {
        return lastUpdate;
    }

    public String getLastUpdateDate(){
        return Date.formattedTime(lastUpdate);
    }
    public String getCreationDate(){
        return Date.formattedTime(creation);
    }

    /**Country Name setter.
     * Verifies division ID and sets the customer's country based on the ID.
     * @param division a Division's country ID provided during call.
     * @return Returns the country name.
     */

    public String setCountryName(int division) {
        String countryName;
        if (division == 38)
            countryName = "Canada";
        else if (division == 230)
            countryName = "United Kingdom";
        else if (division == 231)
            countryName = "United States";
        else
            countryName = "N/A";

        return countryName;
    }

    /**Customer Update method.
     * Loops through customers till an customerID match is found, updates the customer Object upon a match.
     * @param customer a Customer object provided to update the current record.
     */
    public static void updateCustomer(Customer customer){
        int i = -1;
        for(Customer c : allCustomers){
            i++;
            if(c.getCustomerID() == customer.getCustomerID())
                allCustomers.set(i, customer);
        }
    }

    /**Customer getter.
     * Loops through all customers to return a requested customer object.
     * @param ID a customer ID to request a customer object.
     * @return returns the requested customer.
     */

    public static Customer getCustomer(int ID){
        for(Customer customer: allCustomers)
            if(customer.getCustomerID() == ID)
                return customer;
            return null;
    }

    public static void deleteCustomer(Customer customer){allCustomers.remove(customer);}

    public static ObservableList<Customer> getAllCustomers(){ return allCustomers;}

    /** toString Override.
     * Overrides the toString method to display customerID and CustomerName.
     * @return Returns a String. customerID : customerName.
     */
    @Override
    public String toString(){
        return(customerID + ": " + customerName);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
