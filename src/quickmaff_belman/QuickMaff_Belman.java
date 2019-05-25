/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.BelTimer;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.dal.DbConnection;
import quickmaff_belman.dal.DbUpdateDAO;
import quickmaff_belman.dal.FileDAO;
import quickmaff_belman.dal.OrderDAO;
import quickmaff_belman.gui.controller.LoginController;
import quickmaff_belman.gui.model.Model;
import quickmaff_belman.gui.model.Utility;

/**
 *
 * @author Philip
 */
public class QuickMaff_Belman extends Application {

    
    @Override
    public void start(Stage stage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/Login.fxml"));
            Parent root = loader.load();
            LoginController con = loader.getController();
            // Set up layers
            DbConnection connection = DbConnection.getInstance();
            FileDAO fDAO = new FileDAO();
            OrderDAO oDAO = new OrderDAO(connection);
            DbUpdateDAO uDAO = new DbUpdateDAO(connection);
            BelTimer timer = new BelTimer();
            Model model = new Model(new BLLManager(new DatabaseFacade(connection, fDAO, oDAO, uDAO, timer)));
            
            con.setStage(stage);
            con.setModel(model);
            con.createButtons();

            Scene scene = new Scene(root);
            stage.setFullScreen(true);
            stage.setScene(scene);

            // Makes sure all threads are closed upon exit
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode()==KeyCode.F){
                    stage.setFullScreen(true);
                    }
                }
            });
            stage.show();
            con.setGraphics();


        } catch (IOException ex) {
            Utility.createAlert(Alert.AlertType.ERROR, "Fejl", "Fil kunne ikke lokaliseres", "Programmet kunne ikke starte da der mangler en fil " + ex.getMessage());
        }

    }

}
