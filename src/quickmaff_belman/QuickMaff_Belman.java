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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.gui.controller.LoginController;
import quickmaff_belman.gui.model.Model;
import quickmaff_belman.gui.model.Utility;

/**
 *
 * @author Philip
 */
public class QuickMaff_Belman extends Application {

    public static void main(String[] args)
    {
        Application.launch(QuickMaff_Belman.class, args);
    }
    
    @Override
    public void start(Stage stage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/Login.fxml"));
            Parent root = loader.load();
            LoginController con = loader.getController();
            Model model = new Model(new BLLManager(new DatabaseFacade()));
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

            {

            }
            
            
        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN), new Runnable() {
            @Override
            public void run() {
                stage.setFullScreen(true);
            }
        });

            stage.show();
            con.setGraphics();


        } catch (IOException ex) {
            Utility.createAlert(Alert.AlertType.ERROR, "Fejl", "Fil kunne ikke lokaliseres", "Programmet kunne ikke starte da der mangler en fil " + ex.getMessage());
        }

    }

}
