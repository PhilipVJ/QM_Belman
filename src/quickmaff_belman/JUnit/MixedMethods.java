/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.JUnit;

import java.util.Calendar;
import java.util.Date;
import quickmaff_belman.be.BoardTask;

/**
 *
 * @author Bruger
 */
public class MixedMethods {

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

    public static String getDigitString(int digit) {
        String time;
        if (digit < 10) {
            time = "0" + digit;
        } else {
            time = "" + digit;
        }
        return time;
    }

    public static String getFileExtension(String file) {
        int lastIndexOfDot = file.lastIndexOf(".");
        return file.substring(lastIndexOfDot + 1);
    }

}
