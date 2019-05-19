/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ExceptionHandler {

    public static void handleException(Exception ex, ResourceBundle languagePack) {
        switch (ex.getClass().getSimpleName()) {

            case "NoSuchFileException":
                handleNoSuchFileException(languagePack);
                break;
            default:
                handleUnknownError(languagePack);
        }
    }

    private static void handleNoSuchFileException(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"), lang.getString("fileMissingHeader"), lang.getString("fileMissingText"));
    }

    private static void handleUnknownError(ResourceBundle lang) {
        Utility.createAlert(AlertType.ERROR, lang.getString("error"),lang.getString("unknownErrorHeader"), lang.getString("unknownErrorText"));
    }

}
