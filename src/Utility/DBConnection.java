package Utility;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/** Database connection class. */
public class DBConnection {

    //database connection


    //JDBC URL Parts;
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com/WJ07XdY";
    //possible ip address 24.10.225.82

    //JDBC URL
    private static final String jdbcurl = protocol + vendorName + serverName;

    //Driver reference
    private static final String mySQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    private static final String userName = "U07XdY";
    private static final String password = "53689156730";

    /** Database connection.
     * getConnection allows for the whole program to use one connection by checking if there is a connection before preforming
     * actions, if it is connected it uses the initial connection.
     * @return
     */
    public static Connection getConnection(){
        if(conn != null)
            return conn;
        return startConnection();
    }

    /**Start connection.
     * Uses getConnection() to connect to the Database, enters username and password to start the connection.
     *
     * @return returns the connection.
     */
    private static Connection startConnection() {
        try {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcurl, userName, password);
            System.out.println("Connection was successful.");
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**Close database connection.
     * Closes the connection and prints to terminal that the closure was successful.
     * @throws SQLException thrown if the connection closure is unsuccessful.
     */
    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection Closed.");
    }
}
