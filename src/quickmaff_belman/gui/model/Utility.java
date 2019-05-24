/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.Calendar;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Toggle;
import quickmaff_belman.be.BoardTask;

public final class Utility {

    private Utility() {
    }

    public static void createAlert(AlertType type, String title, String header, String content) {
        Platform.runLater(()
                -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.OK && type == AlertType.ERROR) {
                Platform.exit();
                System.exit(0);
            }
        }
        );
    }

    public static String getFXIDfromToggle(Toggle newVal) {
        int indexOfEquals = newVal.toString().indexOf("=");
        int indexOfComma = newVal.toString().indexOf(",");
        String fxId = newVal.toString().substring(indexOfEquals + 1, indexOfComma);
        return fxId;
    }

    public static double getPercentageTimeLeft(BoardTask bTask) {
        double startTime = bTask.getStartDate().getTime();
        Date endDate = bTask.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        double endTime = calendar.getTimeInMillis();
        double totalTime = endTime - startTime;
        double currentTime = System.currentTimeMillis();
        double timePassedSinceStart = currentTime - startTime;
        double percantage = (timePassedSinceStart / totalTime);
        System.out.println(""+percantage);
        return percantage;
    }

    public static String dateConverter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String output = "[";
        output += cal.get(Calendar.WEEK_OF_YEAR) + ":";
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            output += "7]";
        } else {
            output += (day - 1) + "]";
        }
        return output;
    }

    public static String getFileExtension(String file) {
        int lastIndexOfDot = file.lastIndexOf(".");
        return file.substring(lastIndexOfDot + 1);
    }

    public static Date csvStringToDate(String deliveryTime) {
        int lastIndexOfSlash = deliveryTime.lastIndexOf("/");
        String subString = deliveryTime.substring(0, lastIndexOfSlash + 5);
        String[] split = subString.split("/");
        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.YEAR, year);
        Date toReturn = date.getTime();
        return toReturn;
    }

}
