package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** Month Total Report class. */
public class MonthTotals {

    private static ObservableList<MonthTotals> typeMonthReport = FXCollections.observableArrayList();

    private String type;
    private int total;
    private String month;

    public MonthTotals(String type, int total, String month) {
        this.type = type;
        this.total = total;
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public static ObservableList<MonthTotals> getTypeMonthReport() {
        return typeMonthReport;
    }

    public static void addMonthTotal(MonthTotals monthTotals){
        typeMonthReport.add(monthTotals);

    }

    /** Month Name Converter.
     * Method takes the month integer and converts it into a string month name.
     * @param m Month int to be converted to String.
     * @return String version of month.
     */

    public static String monthName(int m){
        String month;
        switch (m){
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + m);
        }
        return month;
    }

    public static void clearMonthTotal(){typeMonthReport.clear();}
}
