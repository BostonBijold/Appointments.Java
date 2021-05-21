package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
/** Date class */
public class Date implements Initializable {

    private static ObservableList<LocalTime> allTimes = FXCollections.observableArrayList();

    /** Current Time method.
     * formats the LocalDateTime.now() to year month day hour minute second format.
     * @return Returns a formatted current date and time.
     */
    public static String currentTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatted = now.format(formatter);

        return formatted;
    }

    /** Date time formatter.
     * formats the provided LocalDateTime to year month day hour minute second format.
     * @param time LocalDateTime to be formatted.
     * @return Returns a formatted date and time.
     */
    public static String formattedTime(LocalDateTime time){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatted = time.format(formatter);

        return formatted;
    }

    /** Add Time.
     * Adds a LocalTime to the allTimes ObservableArrayList.
     * @param time LocalTime to Add to the alltimes list.
     */
    public static void addTime(LocalTime time){
        allTimes.add(time);
    }

    /**
     * Clear time method.
     * Clears both allTimes and updateTimes observableArrayLists.
     */
    public static void clearTimes() {allTimes.clear();}

    /**
     * Get alltimes method.
     * Gets ObservalbeArrayList of allTimes.
     * @return Returns a ObservableArrayList allTimes.
     */
    public static ObservableList<LocalTime> getAllTimes(){
        return allTimes;
    }



    /** Creates available times for appointment scheduling.
     * Creates the available time slots based on EasternStandardTIme and the provided date then converts them to local time of the computer.
     * @param date The date used to check if other appointments are scheduled on that date.
     * @param i An integer to differentiate from updating an appointment and adding an appointment.
     */
    public static void createTimes(LocalDate date, int i ) {

        clearTimes();
        LocalTime opening = LocalTime.of(8, 0);
        LocalTime closing = LocalTime.of(22,0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estOpen = ZonedDateTime.of(date, opening, estZoneId);
        ZonedDateTime estClose = ZonedDateTime.of(date, closing, estZoneId);

        ZoneId localZoneId = ZoneId.of(ZoneId.systemDefault().toString());
        ZonedDateTime localStartTime = ZonedDateTime.ofInstant(estOpen.toInstant(),localZoneId);
        ZonedDateTime localCloseTime = ZonedDateTime.ofInstant(estClose.toInstant(),localZoneId);

        LocalTime timeSlot = localStartTime.toLocalTime();
        LocalTime localClosing = localCloseTime.toLocalTime();


        while (timeSlot.isBefore(localClosing)){
            addTime(timeSlot);
            timeSlot = timeSlot.plusMinutes(30);
            }
    }

    /**
     * Appointment Time Remover Method.
     * Removes a time slot from the ObservableArrayList.
     * @param time LocalTime to have removed from list.
     */
    public static void appointmentTimeRemover(LocalTime time)   {
                allTimes.remove(time);
    }

    /**
     * Time insert method.
     * Inserts a Local time into a list.
     * @param time LocalTime to add to allTimes.
     */
    public static void insertTime(LocalTime time){ allTimes.add(time);}


    /**
     * UTC to Local Time Converter.
     * Uses a ZonedID and ZonedDateTime to convert the LocalDateTime from UTC to the local time of the system.
     * @param dateTimeToConvert LocalDateTime to be converted.
     * @return returns the converted LocalDateTime.
     */
    public static LocalDateTime UTCtoLocal(LocalDateTime dateTimeToConvert) {
        LocalDate UTCDate = dateTimeToConvert.toLocalDate();
        LocalTime UTCTime = dateTimeToConvert.toLocalTime();
        ZoneId UTCID = ZoneId.of("UTC");
        ZonedDateTime UTCDateTime = ZonedDateTime.of(UTCDate,UTCTime,UTCID);

        ZoneId localZoneId = ZoneId.of(ZoneId.systemDefault().toString());
        ZonedDateTime localStartTime = ZonedDateTime.ofInstant(UTCDateTime.toInstant(),localZoneId);

        LocalDate Ldate = localStartTime.toLocalDate();
        LocalTime LTime = localStartTime.toLocalTime();
        LocalDateTime LDateTime = LocalDateTime.of(Ldate,LTime);
        return LDateTime;

    }
    /**
     * Local to UTC Time Converter.
     * Uses a ZonedID and ZonedDateTime to convert the LocalDateTime from Local time to a UTC time.
     * @param dateTimeToConvert LocalDateTime to be converted.
     * @return returns the converted LocalDateTime.
     */
    public static LocalDateTime LocalToUTC(LocalDateTime dateTimeToConvert) {
        LocalDate LDate = dateTimeToConvert.toLocalDate();
        LocalTime LTime = dateTimeToConvert.toLocalTime();
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime LocalZoneDT = ZonedDateTime.of(LDate,LTime,localZoneId);

        ZoneId utcZID = ZoneId.of("UTC");
        ZonedDateTime localStartTime = ZonedDateTime.ofInstant(LocalZoneDT.toInstant(),utcZID);

        LocalDate utcDate = localStartTime.toLocalDate();
        LocalTime utcTime = localStartTime.toLocalTime();
        LocalDateTime utcDateTime = LocalDateTime.of(utcDate,utcTime);
        return utcDateTime;

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
