package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** Contact Class */
public class Contact {

    private int contactID;
    private String contactName;
    private String contactEmail;
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int ID) {
        this.contactID = contactID;
    }

    /** Get Contact.
     * Returns a contact if the given ID matches.
     * @param ID a provided contact ID to match and return a contact.
     * @return Returns the matching contact or null if non exists.
     */
    public static Contact getContact(int ID){
        for(Contact contact: allContacts){
            if(contact.getContactID() == ID)
                return contact;
        }
        return null;
    }

    /** ToString Override.
     *  Overrides the toString Method to display the contactID and Contact name in a ComboBox.
     * @return Returns the ContactID and ContactName.
     */
    @Override
    public String toString(){
        return(contactID + ": " + contactName);
    }

    public static void addContact(Contact contact) { allContacts.add(contact);}
    public static ObservableList<Contact> getAllContacts() {return allContacts;}
}
