package Utility;

import Model.*;
import Model.Date;

import java.sql.*;
import java.time.LocalDateTime;

/** Database Query List */
public class DBQuery {

    private static Statement statement;
    private static PreparedStatement pstatement;

    /**Set Statement.
     * creates a statement to be used in a sql database.
     * @param conn Database connection
     * @throws SQLException a thrown exception if statement is not created.
     */
    //Create statement Object;
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    /**Get SQL Statement.
     *  Returns the statement.
     * @return Returns created statment from the Database.
     */
    // Return statement object
    public static Statement getStatement() {
        return statement;
    }

    /**Set Prepared Statement.
     * Creates a prepared statement to be used in the Database.
     * @param conn Database connection.
     * @param sqlStatement SQL statement to be used in the database.
     * @throws SQLException thrown if statement is unsuccessful.
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        pstatement = conn.prepareStatement(sqlStatement);
    }

    /** Get Prepared Statement.
     * Returns the Prepared statement.
     * @return returns the Prepared statement.
     */
    public static PreparedStatement getPreparedStatement() {
        return pstatement;
    }

    /** Customer Insert Statement.
     * Uses customer Object to create a SQL insert Query to add to the Database as a prepared statement.
     * @param customer Customer Object
     * @return returns the prepared statement update count.
     * @throws SQLException thrown if the prepared statement is unsuccessful.
     */
    public static int customerInsert(Customer customer) throws SQLException {
        String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Division_ID) VALUES(?,?,?,?,?,?)";
        Connection conn = DBConnection.getConnection();
        setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, User.getLoggedIn());
        ps.setInt(6, customer.getDivisionID());
        System.out.println(ps);
        ps.execute();
        return ps.getUpdateCount();
    }
    /** Customer update Statement.
     * Uses customer Object to create a SQL update Query to update a Database entry as a prepared statement.
     * @param customer Customer Object
     * @return returns the prepared statement update count.
     * @throws SQLException thrown if the prepared statement is unsuccessful.
     */
    public static int customerUpdate(Customer customer) throws SQLException {
        String insertStatement = "UPDATE customers SET Customer_Name = ?,  Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Updated_By = ?, Last_Update = now(), Division_ID = ?  WHERE Customer_ID = ?";
        Connection conn = DBConnection.getConnection();
        setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, User.getLoggedIn());
        ps.setInt(6, customer.getDivisionID());
        ps.setInt(7, customer.getCustomerID());
        System.out.println(ps);
        ps.execute();
        return ps.getUpdateCount();
    }

    /** Retrieve customer query.
     * Used to pull 0ne customer from the DB using the ID.
     * Used to update a customer by pulling the ID from the selected customer.
     * The update customer will populate the field with the data from the DB.
     * Saving will update the DB and the ObservableArrayList in this program.
     * This allows for the update and creation date to be saved.
     * @param ID Customer ID to retrieve correct customer.
     * @return Returns the requested customer object from the database.
     */

    public static Customer retrieveCustomer(int ID) {
        String selectStatement = "SELECT * FROM customers WHERE Customer_ID =" + ID;
        ResultSet rs;
        Statement statement = DBQuery.getStatement();

        try {
            statement.execute(selectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String code = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int DID = rs.getInt("Division_ID");
                Division division = Model.Division.getDivision(DID);
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updatedBy = rs.getString("Last_Updated_By");

                LocalDateTime createDateLDT = Date.UTCtoLocal(createDate.toLocalDateTime());
                LocalDateTime lastUpdateLDT = Date.UTCtoLocal(lastUpdate.toLocalDateTime());
                Customer customer = new Customer(customerID, name, address, code, phone, createDateLDT, createdBy, lastUpdateLDT, updatedBy, division);
                return customer;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /** Delete SQL statement.
     * Deletes an entry from the database.
     * @param table Table name for the database entry to delete.
     * @param column column name for the column condition to delete a entry.
     * @param condition The primary key for the table to delete the entry.
     * @return returns the update count.
     * @throws SQLException Thrown if the sql statement is unsuccessful.
     */
    public static int deleteStatement(String table, String column, int condition) throws SQLException {
        String deleteStatement = "DELETE FROM " + table + " WHERE " + column + " = " + condition;
        System.out.println(deleteStatement);
        Statement statement = DBQuery.getStatement();
        statement.execute(deleteStatement);
        return statement.getUpdateCount();
    }

    /** Select all customers from the Database.
     * Uses a select all from the customers table.
     * Uses the retrieved entries to create customer objects and add them to the observablearraylist.
     * Throws a SQL error if connection is unsuccessful.
     */
    public static void initialCustomerConnection() {

        String SelectStatement = "SELECT * FROM customers";
        ResultSet rs;
        Statement statement = getStatement();

        try {
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String code = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int DID = rs.getInt("Division_ID");
                Division division = Division.getDivision(DID);
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updatedBy = rs.getString("Last_Updated_By");

                LocalDateTime createDateLDT = Date.UTCtoLocal(createDate.toLocalDateTime());
                LocalDateTime lastUpdateLDT = Date.UTCtoLocal(lastUpdate.toLocalDateTime());
                Customer customer = new Customer(customerID, name, address, code, phone, createDateLDT, createdBy, lastUpdateLDT, updatedBy, division);
                Customer.AddCustomer(customer);
            }
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /** Selects all appointments from the Database.
     * Uses a select all from the appointments table.
     * Uses the retrieved entries to create appointment objects and add them to the observablearraylist.
     * Throws a SQL error if connection is unsuccessful.
     */
    public static void appointmentsConnection() {

        String SelectStatement = "SELECT * FROM appointments";
        ResultSet rs;
        Statement statement = getStatement();

        try {
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updatedBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                LocalDateTime createDateLDT = Date.UTCtoLocal(createDate.toLocalDateTime());
                LocalDateTime lastUpdateLDT = Date.UTCtoLocal(lastUpdate.toLocalDateTime());
                LocalDateTime startLDT = start.toLocalDateTime();
                LocalDateTime endLDT = end.toLocalDateTime();
                Appointment newAppointment = new Appointment(appointmentID, title, description, location, type, startLDT, endLDT,
                        createDateLDT, createdBy, lastUpdateLDT, updatedBy, customerID, userID, contactID);

                Appointment.addAppointment(newAppointment);
            }
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /** Retrieve a specific appointment.
     * Uses a select all statement to retrieve all data for a specific appointment.
     * Creates a appointment object from the result set.
     * @param ID int ID to select a specific appointment.
     * @return returns appointment object.
     */

    public static Appointment retrieveAppointment(int ID) {
        String selectStatement = "SELECT * FROM appointments WHERE Appointment_ID =" + ID;
        ResultSet rs;
        Statement statement = DBQuery.getStatement();

        try {
            statement.execute(selectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updatedBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                LocalDateTime createDateLDT = createDate.toLocalDateTime();
                LocalDateTime lastUpdateLDT = lastUpdate.toLocalDateTime();
                LocalDateTime startLDT = start.toLocalDateTime();
                LocalDateTime endLDT = end.toLocalDateTime();
                Appointment updateAppointment = new Appointment(appointmentID, title, description, location, type, startLDT, endLDT,
                        createDateLDT, createdBy, lastUpdateLDT, updatedBy, customerID, userID, contactID);

                return updateAppointment;
            }
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    /** Select all contacts from the Database.
     * Uses a select all from the contacts table.
     * Uses the retrieved entries to create contact objects and add them to the observablearraylist.
     * Throws a SQL error if connection is unsuccessful.
     */

    public static void contactInitialization() {
        String selectStatement = "SELECT * FROM contacts";
        ResultSet rs;
        Statement statement = getStatement();

        try {
            statement.execute(selectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(id, name, email);
                Contact.addContact(contact);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Appointment Insert Statement.
     * Uses appointment Object to create a SQL insert Query to add to the Database as a prepared statement.
     * @param appointment Appointment Object
     * @return returns the prepared statement update count.
     * @throws SQLException thrown if the prepared statement is unsuccessful.
     */
    public static int appointmentInsert(Appointment appointment) throws SQLException {
        String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Created_By," +
                " Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getConnection();
        setPreparedStatement(conn, insertStatement);
        LocalDateTime t = appointment.getStart();
        PreparedStatement ps = getPreparedStatement();

        try {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            Timestamp startT = Timestamp.valueOf(appointment.getStart());
            ps.setTimestamp(5, startT);
            Timestamp end = Timestamp.valueOf(appointment.getEnd());
            ps.setTimestamp(6, end);
            ps.setString(7, User.getLoggedIn());
            ps.setString(8, User.getLoggedIn());
            ps.setInt(9, appointment.getCustomerID());
            ps.setInt(10, appointment.getUserID());
            ps.setInt(11, appointment.getContactID());
            System.out.println(ps);
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ps.getUpdateCount();
    }

    /** Appointment update Statement.
     * Uses Appointment Object to create a SQL update Query to update a Database entry as a prepared statement.
     * @param appointment Appointment Object
     * @return returns the prepared statement update count.
     * @throws SQLException thrown if the prepared statement is unsuccessful.
     */

    public static int appointmentUpdate(Appointment appointment) throws SQLException {
        String insertStatement = "UPDATE appointments SET Title = ?,  Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = now(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?" +
                "  WHERE Appointment_ID = ?";
        Connection conn = DBConnection.getConnection();
        setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        Timestamp startT = Timestamp.valueOf(Date.LocalToUTC(appointment.getStart()));
        ps.setTimestamp(5, startT);
        Timestamp end = Timestamp.valueOf(Date.LocalToUTC(appointment.getEnd()));
        ps.setTimestamp(6, end);
        ps.setString(7, User.getLoggedIn());
        ps.setInt(8, appointment.getCustomerID());
        ps.setInt(9, appointment.getUserID());
        ps.setInt(10, appointment.getContactID());
        ps.setInt(11, appointment.getAppointmentID());
        System.out.println(ps);
        ps.execute();
        return ps.getUpdateCount();
    }

    /** Month Total SQL Query.
     * Uses a select all statement to pull from the month_totals table.
     * Uses result set to create MonthTotals objects.
     */
    public static void monthTotalQuery() {

        String SelectStatement = "SELECT * FROM month_totals";
        ResultSet rs;
        Statement statement = getStatement();

        try {
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int month = rs.getInt("m");
                String type = rs.getString("Type");
                int count = rs.getInt("count(*)");
                String m = MonthTotals.monthName(month);
                MonthTotals monthTotals = new MonthTotals(type,count,m);
                MonthTotals.addMonthTotal(monthTotals);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /** Contact Total SQL Query.
     * Uses a select all statement to pull from the contact_total table.
     * Uses result set to create ContactTotal objects.
     */
    public static void contactReport(){
        String SelectStatement = "SELECT * FROM contact_total";
        ResultSet rs;
        Statement statement = getStatement();

        try {
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int contact = rs.getInt("Contact_ID");
                int appointment = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customer = rs.getInt("Customer_ID");
                LocalDateTime Start = start.toLocalDateTime();
                LocalDateTime End = end.toLocalDateTime();
                ContactTotal contactTotal = new ContactTotal(contact, appointment,title,description,Start,End,customer);
                ContactTotal.addContactTotal(contactTotal);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /** Customer Total SQL Query.
     * Uses a select all statement to pull from the customerTotals table.
     * Uses result set to create CustomerTotals objects.
     */
    public static void customerTotalsReport(){

        String SelectStatement = "SELECT * FROM customerTotals";
        ResultSet rs;
        Statement statement = getStatement();

        try{
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                int total = rs.getInt("count");
                CustomerTotals report = new CustomerTotals(customerID,customerName,total);
                CustomerTotals.addtotal(report);

            }
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}