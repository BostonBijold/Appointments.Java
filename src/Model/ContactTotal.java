package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
/** Contact Total Report Class. */
public class ContactTotal {

    private static ObservableList<ContactTotal> contactTotal = FXCollections.observableArrayList();

    private int contactID;
    private int appointmentID;
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;

    public ContactTotal(int contactID, int appointmentID, String title, String description,
                        LocalDateTime start, LocalDateTime end, int customerID) {
        this.contactID = contactID;
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
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

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public static void addContactTotal(ContactTotal contact){
        contactTotal.add(contact);
    }

    public static ObservableList<ContactTotal> getContactTotal(){
        return contactTotal;
    }

    public static void clearContactTotal(){contactTotal.clear();}
}
