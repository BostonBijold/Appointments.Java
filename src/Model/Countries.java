package Model;

import Bundles_Lambda.CountryLB;
import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
/** Countries class */
public class Countries implements Initializable {

    private String CountryName;
    private int CountryID;

    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    public Countries(String country, int countryID) {
        CountryName = country;
        CountryID = countryID;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String country) {
        CountryName = country;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }
    public static void clearCountryList() {
        allCountries.clear();
    }

    public static ObservableList<Countries> getAllCountries() {
        return allCountries;
    }

    /** toString Override.
     * Overrides the toString method to return a speciified value.
     * @return Returns the country name.
     */
    @Override
    public String toString(){
        return(CountryName);
    }

    /** Country Database Connection.
     *  Creates a SQL Query and saves the result as Countries objects in an ObservableArrayList.
     *  Uses a Lambda statement Filter to remove all countries except the USA Canada and the UK.
     *  Throws a SQL Exception if the connection fails.
     */
    public static void countryConnection() {
        String SelectStatement = "SELECT * FROM countries";
        ResultSet rs;
        Statement statement = DBQuery.getStatement();

        try{
            statement.execute(SelectStatement);
            rs = statement.getResultSet();
            while (rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries country = new Countries(countryName, countryID);
                CountryLB filter = n ->{
                    int i = 0;
                    if(n == 38)
                        i +=1;
                    if(n == 230)
                        i += 1;
                    if(n == 231)
                        i +=1;
                    return i;

                };
                if(filter.filterCountry(countryID)> 0) {
                    allCountries.add(country);
                    //System.out.println(country);
                }

            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
