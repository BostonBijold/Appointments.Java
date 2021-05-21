package Main;

import Model.*;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** This program is a scheduling program for a consultation service for accountants.*/

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

/** This is the main method. This connect to the DataBase to pull data. */
    public static void main(String[] args) throws SQLException {

        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);

        User.initialUserConnection();
        Division.divisionConnection();
        Countries.countryConnection();
        DBQuery.initialCustomerConnection();
        DBQuery.appointmentsConnection();
        DBQuery.contactInitialization();
        System.out.println(Date.currentTime());

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime utcToday = Date.LocalToUTC(today);
        //System.out.println(utcToday);
        //System.out.println("*****");
        //System.out.println(Date.UTCtoLocal(utcToday));


        launch(args);
        DBConnection.closeConnection();


    }
}
