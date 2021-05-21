package Model;

import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
/** Division Class */
public class Division implements Initializable {


    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<Division> countrysDivisions = FXCollections.observableArrayList();

    private int DivisionID;
    private String DivisionName;
    private int CountryID;

    public Division(int divisionID, String division, int countryID) {
        DivisionID = divisionID;
        DivisionName = division;
        CountryID = countryID;
    }

    public int getDivisionID() {
        return DivisionID;
    }

    public void setDivisionID(int divisionID) {
        DivisionID = divisionID;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String division) {
        DivisionName = division;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }
    public static ObservableList<Division> getAllDivisions(){
        return allDivisions;
    }

    /** toString Override.
     * Overrides the toString method to display DivisionName.
     * @return Returns a String DivisionName.
     */
    @Override
    public String toString(){
        return(DivisionName);
    }

    /**
     * Get Country's Divisions.
     * Loops though all Divisions by ID and returns matching Divisions for the country.
     * @param id CountryID to request divisions of specific country.
     * @return returns the requested divisions of the country.
     */
    public static ObservableList<Division> getCountrysDivisions(int id) {
        for(Division division : allDivisions) {
            if (division.getCountryID() == id) {
                countrysDivisions.add(division);
            }
        }

        return countrysDivisions;
    }

    /**
     * Clear country Division list.
     * Clears the country division list.
     */
    public static void clearCountryDivisions(){
        countrysDivisions.clear();
    }

    /** Division Database Connection.
     * Uses a SQL Query to Download all first level division from the Database. Populates the allDivisions ObservableArrayList with new division objects.
     * Throws a SQL exception if the connection is unable to download.
     */
    public static void divisionConnection() {

        String SelectStatement = "SELECT * FROM first_level_divisions";
        ResultSet rs;
        Statement statement = DBQuery.getStatement();

        try {
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                Division newDivision = new Division(divisionID,division,countryID);
                allDivisions.add(newDivision);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get Division.
     * Loops through allDivisions to return a division based on an ID.
     * @param divisionID the requested object's ID.
     * @return returns the requested Division.
     */
    public static Division getDivision(int divisionID) {
        for(Division division: allDivisions){
            if(divisionID == division.getDivisionID())
                return division;
        }
        return null;
    }

    /** Clear allDivisions.
     * Clears allDivisions ObservableArrayList.
     */
    public static void clearDivisions() {
        allDivisions.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
