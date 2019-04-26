/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author nikla
 */
public class ExceptionHandler {

    public static void handleException(Exception ex) {
        switch (ex.getClass().getSimpleName()) {
            case "SQLServerException":
                handleSqlServerException();
                break;
            case "SQLException":
                handleSqlException();
                break;
            case "FileNotFoundException":
                handleIOException();
                break;
            case "ParseException":
                handleParseException();
                break;
            default:
                handleUnknownError();
        }
    }

    private static void handleSqlServerException() {
        Utility.createAlert(AlertType.ERROR,"Fejl","Der kan ikke oprettes forbindelse til serveren", "Kontakt support eller prøv igen senere!");
    }

    private static void handleSqlException() {
        Utility.createAlert(AlertType.ERROR,"Fejl","Der er problemer i databasen", "Kontakt support eller prøv igen senere!");
    }

    private static void handleIOException() {
        Utility.createAlert(AlertType.ERROR,"Fejl","En fil kunne ikke lokaliseres", "Kontakt venligst support eller kig i manualen");
    }

    private static void handleUnknownError() {
        Utility.createAlert(AlertType.ERROR,"Fejl","Ukendt fejl opstod", "Der opstod en ukendt fejl i systemet.");
    }

    private static void handleParseException() {
        Utility.createAlert(AlertType.ERROR,"Fejl","Fejl læsning af JSON fil", "Kontakt venligst support");
    }

}
