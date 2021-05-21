package Controller;

import Model.Date;
import Model.User;
import Utility.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**The controller for the Login page, The first UI seen. */
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private Button Enter;

    @FXML
    private Button Exit;

    @FXML
    private TextField UsernameField;

    @FXML
    private TextField PasswordField;

    @FXML
    private Label UserName;

    @FXML
    private Label Password;

    @FXML
    private Label Lable;

    @FXML
    private Label DateandTime;

    @FXML
    private Label Region;

    /** User log in Check upon hitting enter.
     * Checks if  username and password match valid entries from the database to log in.
     *  Prints to a .txt file attempted username, timestamp, and if the log in was successful.
     *  Displays an error message in French or English if the Login attempt fails.
     * @param event A UI button to enter the program.
     * @throws IOException an exception thrown if the program can not load the next portion of code.
     */
    @FXML
    public void OAEnter(ActionEvent event) throws IOException {
        String fileName = "login_activity.txt", activity;
        FileWriter fWriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fWriter);

        String UserName = UsernameField.getText();
        String Password = PasswordField.getText();

        if(User.lookUpUserName(UserName) >0 && User.lookUpPassword(Password) >0) {
            activity = "Attempted UserName: " + UserName + " Timestamp: " + LocalDateTime.now().withNano(0) + " Login was successful.";
            outputFile.println(activity);
            User.setLoggedIn(UserName);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            activity = "Attempted UserName: " +  UserName + " TimeStamp: " + LocalDateTime.now().withNano(0) + " Login was unsuccessful.";
            outputFile.println(activity);
            User.setLoggedIn(UserName);
            if(Locale.getDefault().getLanguage().equals("fr"))
                errorWindow(2);
            else
                errorWindow(1);
        }
        outputFile.close();
    }


    /**
     * Program exit.
     * Closes the program from the UI and Disconnects the DataBase connection.
     * @param event A UI Button.
     * @throws SQLException Exception thrown if the Database connection is unable to be disconnected.
     */

    @FXML
    public void OAExit(ActionEvent event) throws SQLException {
        DBConnection.closeConnection();
        System.exit(0);    }

    /**
     * Username and Password error message.
     * @param code an int provided by the method call to specify which error window to use.
     */
    public  void errorWindow(int code){
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("The Username or Password do not match.");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("connexion Erreur");
            alert.setContentText("Le nom d'utilisateur ou le mot de passe ne correspondent pas.");
            alert.showAndWait();
        }
    }

    /**
     * Login initializer.
     * Initializes the controller, sets the Date and Time on the UI, and checks the language of the user's system.
     * @param url The location of the resources to initialize.
     * @param resourceBundle Loads the Language change bundle if the default system language is french.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateandTime.setText(Date.currentTime());
        ZoneId region = ZoneId.of(TimeZone.getDefault().getID());
        Region.setText(region.toString());
        //System.out.println(Locale.getDefault());

        ResourceBundle rb = ResourceBundle.getBundle("Bundles_Lambda/File_fr", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")){
            Enter.setText(rb.getString("Enter"));
            Exit.setText(rb.getString("Exit"));
            UserName.setText(rb.getString("Username"));
            Password.setText(rb.getString("Password"));
            Lable.setText(rb.getString("Login"));

        }
        //System.out.println(Locale.getDefault());
        //System.out.println(ZoneId.of(TimeZone.getDefault().getID()));

    }
}
