/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.Calendar;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author Philip
 */
public class Clock implements Runnable {

    private Calendar cal;
    private final Label clockLabel;

    public Clock(Label clockLabel) {
        this.clockLabel = clockLabel;
    }

    @Override
    public void run() {
        while (true) {
            cal = Calendar.getInstance();

            String hour = getDigitString(cal.get(Calendar.HOUR_OF_DAY));
            String sec = getDigitString(cal.get(Calendar.SECOND));
            String minutes = getDigitString(cal.get(Calendar.MINUTE));
            String time = hour + ":" + minutes + ":" + sec;
            String weekAndDay = Utility.dateConverter(cal.getTime());

            Platform.runLater(()-> {clockLabel.setText(time + "\n" + weekAndDay);});
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    private String getDigitString(int digit) {
        String time;
        if (digit < 10) {
            time = "0" + digit;
        } else {
            time = "" + digit;
        }
        return time;
    }

}

