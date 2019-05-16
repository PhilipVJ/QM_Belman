/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);

            String time = hour + ":" + minutes;
            String weekAndDay = Utility.dateConverter(cal.getTime());

            Platform.runLater(()
                    -> {
                clockLabel.setText(time + "\n" + weekAndDay);
            }
            );
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
