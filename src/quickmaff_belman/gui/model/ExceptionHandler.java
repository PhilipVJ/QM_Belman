/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;

public class ExceptionHandler {

    public static void handleException(Exception ex, ResourceBundle languagePack) {
        switch (ex.getClass().getSimpleName()) {
            case "SQLServerException":
                handleSqlServerException(languagePack);
                break;
            case "SQLException":
                handleSqlException(languagePack);
                break;
            case "FileNotFoundException":
                handleIOException(languagePack);
                break;
            case "ParseException":
                handleParseException(languagePack);
                break;
            case "IOException":
                handleIOException(languagePack);
                break;
            case "InterruptedException":
                handleInterruptedException(languagePack);
                break;
            default:
                handleUnknownError(languagePack);
        }
    }

    private static void handleSqlServerException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("sqlServerException"), lang.getString("contactSupport"));
    }

    private static void handleSqlException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("sqlExceptionHeader"), lang.getString("contactSupport"));
    }

    private static void handleIOException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("fileMissingHeader"), lang.getString("fileMissingText"));
    }

    private static void handleUnknownError(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"),lang.getString("unknownErrorHeader"), lang.getString("unknownErrorText"));
    }

    private static void handleParseException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("parseExceptionHeader"), lang.getString("unknownErrorText"));
    }

    private static void handleInterruptedException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("systemErrorHeader"), lang.getString("systemErrorText"));
    }

}
