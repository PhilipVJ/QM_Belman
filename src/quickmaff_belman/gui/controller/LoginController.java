/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.dal.OrderDAO;
import quickmaff_belman.dal.FileDAO;
import quickmaff_belman.dal.WorkerDAO;
import quickmaff_belman.gui.model.ExceptionHandler;
import quickmaff_belman.gui.model.Model;
import quickmaff_belman.gui.model.Utility;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane pane;
    private Model model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new Model(new BLLManager(new DatabaseFacade(new FileDAO(), new OrderDAO(), new WorkerDAO())));

    }

    private void loadFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON file");
        Stage stage = (Stage) pane.getScene().getWindow();
        File mediafile = fileChooser.showOpenDialog(stage);
        
        if (mediafile != null) {
            try {
                model.loadJSONfile(mediafile.getPath());
                Utility.createAlert(Alert.AlertType.INFORMATION, "Vigtig besked", "Læsning af JSON fuldført", 
                        "JSON filen er blevet læst og lagt op på databasen");
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            } catch (ParseException ex) {
                ExceptionHandler.handleException(ex);
            }
        }
    }

    public void initView(Stage stage) {

        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN),new Runnable() 
        {
            public void run() {
                loadFile();
            }
        });
    }

}
