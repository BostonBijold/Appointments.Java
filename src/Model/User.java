package Model;

import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/** User Class */
public class User {

    private int User_ID;
    private String User_Name;
    private String Password;
    private String Create_Date;
    private String Created_By;
    private String Last_Update;
    private String Last_Updated_By;
    private static String loggedIn;

    private static ObservableList<Model.User> Users = FXCollections.observableArrayList();

    public User(int user_ID, String user_Name, String password) {
        User_ID = user_ID;
        User_Name = user_Name;
        Password = password;

    }

    public static String getLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(String logged) {
        loggedIn = logged;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public static void addUser(User user){
        Users.add(user);
    }

    /**User name Lookup.
     * Loops through all the users to find a match.
     * @param Name String entered as a usernameLogin attempt.
     * @return int returned to verify if login is successful.
     */
    public static int lookUpUserName(String Name){
        for(User user : Users){

            if(user.getUser_Name().equals(Name))
                return 1;
        }
        return 0;
    }

    /**User password Lookup.
     * Loops through all the users to find a matching password.
     * @param password String entered as a password Login attempt.
     * @return int returned to verify if login is successful.
     */
    public static int lookUpPassword(String password){
        for(User user : Users){
            if(user.getPassword().equals(password))
                return 1;
        }
        return 0;
    }

    /**User database connection.
     * Uses SQL statement to download Usernames and Passwords from the database and creates user Objects.
     * @throws SQLException if Query is unsuccessful.
     */
    public static void initialUserConnection() throws SQLException {
        String userSelectStatement = "SELECT * FROM users";
        ResultSet rs;
        Statement statement = DBQuery.getStatement();
        statement.execute(userSelectStatement);
        rs = statement.getResultSet();

        while(rs.next()){
            int UserID = rs.getInt("User_ID");
            String UserName = rs.getString("User_Name");
            String Password = rs.getString("Password");
            User userLogin = new User(UserID, UserName, Password);
            User.addUser(userLogin);

        }
    }

    /**Get User.
     * Loops through Users to match User ID with int argument.
     * @param ID int argument to pull a User.
     * @return returns a User.
     */
    public static User getUser(int ID){
        for(User user: Users)
            if(user.getUser_ID()== ID)
                return user;
            return null;
    }
    /** toString Override.
     * Overrides the toString method to return a specified values.
     * @return Returns UserID and UserName.
     */
    @Override
    public String toString(){
        return(User_ID + ": " + User_Name);
    }

    /**Get Users.
     * Gets User ObservableArrayList.
     * @return returns User OAL.
     */
    public static ObservableList<User> getUsers(){
        return Users;
    }
}
