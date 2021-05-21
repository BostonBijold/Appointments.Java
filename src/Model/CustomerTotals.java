package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** Customer totals report class. */
public class CustomerTotals {

    private static ObservableList<CustomerTotals> customerTotalReport = FXCollections.observableArrayList();

    private int customerID;
    private String customerName;
    private int total;

    public CustomerTotals(int customerID, String customerName, int total) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.total = total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static void addtotal(CustomerTotals customerTotals){ customerTotalReport.add(customerTotals);}

    public static ObservableList<CustomerTotals> getCustomerTotal() {return customerTotalReport;}

    public static void clearCustomerTotal(){customerTotalReport.clear();}
}
