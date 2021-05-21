package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;
/** Appointment class. */
public class Appointment implements Initializable {


    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();


    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    private int customerID;
    private int userID;
    private int contactID;

    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, LocalDateTime createDate, String createBy, LocalDateTime lastUpdate,
                       String lastUpdateBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    public Appointment(String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, int customerID, int userID, int contactID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, int customerID, int userID, int contactID) {
        this.appointmentID= appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Add appointment method.
     * Checks if the provided appointmnetn is already in the allAppointments ObservableArrayList and add it if it is not included.
     * @param appointment an Appointment object.
     */

    public static void addAppointment(Appointment appointment){
        int i =0;
        for(Appointment a : allAppointments)
            if(a.getAppointmentID() == appointment.getAppointmentID())
                i +=1;
            if(i == 0)
                allAppointments.add(appointment);
    }

    /** Customer appointment.
     * Loops though the list of appointment and removes available times from the add or update appointment page.
     * @param newAppointment A provided LocalDate to check against scheduled appointments.
     */
    public static void customerAppointments(LocalDate newAppointment) {
        for (Appointment a : allAppointments) {
            if (a.getStart().toLocalDate().isEqual(newAppointment)) {
                Date.appointmentTimeRemover( a.getStart().toLocalTime());
            }
        }
    }


    /** Month Appointment List.
     * populates a ObservableArrayList with all appointments in the current month.
     */
    public static void setMonthAppointments(){
        Month month = LocalDateTime.now().getMonth();
        for(Appointment a : allAppointments){
            if(a.start.getMonth() == month) {
                monthAppointments.add(a);
            }
        }
    }

    /** Month Appointment Clear.
     * Clears the monthAppointment ObservableArrayList.
     */
    public static void clearMonthlyAppointment (){monthAppointments.clear();}

    /** Current Week Appointments.
     * creates an ObservableArrayList of all appointments in the next seven days.
     */
    public static void setWeekAppointments(){

        LocalDate date = LocalDateTime.now().toLocalDate();
        LocalDate sevenDays = date.plusDays(7);
        for(Appointment a : allAppointments){
            LocalDate start = a.getStart().toLocalDate();
            if(start.isAfter(date) && start.isBefore(sevenDays)) {

                weekAppointments.add(a);
            }
        }
    }

    /** Week appointment clearer.
     * Clears the weekly appointments list.
     */
    public static void clearWeeklyAppointments(){weekAppointments.clear();}

    public static ObservableList<Appointment> getMonthAppointments(){
        return monthAppointments;
    }

    public static ObservableList<Appointment> getWeekAppointments(){
        return weekAppointments;
    }

    public static ObservableList<Appointment> getCustomerAppointments(){
        return customerAppointments;
    }

    public static ObservableList getAllAppointments() {return allAppointments;}

    /** Appointment Deleter.
     * Deletes an appointment from all available lists.
     * @param appointment
     */
    public static void deleteAppointment (Appointment appointment){
        allAppointments.remove(appointment);
        weekAppointments.remove(appointment);
        monthAppointments.remove(appointment);
    }

    /** Appointment return.
     * Loops through all appointments and returns an appointment with a matching appointment ID.
     * @param id Appointment ID to match an appointment record.
     * @return Returns an appointment that matches the provided ID or returns Null if non exists.
     */
    public static Appointment getAppointment (int id){
        for(Appointment app : allAppointments)
            if(app.getAppointmentID() == id)
                return app;
            return null;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
