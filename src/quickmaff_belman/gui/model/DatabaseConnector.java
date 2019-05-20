/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.io.IOException;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author Philip
 */
public class DatabaseConnector implements Runnable {

    private final Model model;
    private final IntegerProperty connected;

    public DatabaseConnector(Model model, IntegerProperty connected) {
        this.model = model;
        this.connected = connected;
    }

    @Override
    public void run() {
        boolean con;

        con = model.checkForDatabaseConnection();
        if (con == false) {
            connected.set(1);
        } else {
            connected.set(2);
        }

    }
}
