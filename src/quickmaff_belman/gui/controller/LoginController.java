/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.dal.DbDAO;
import quickmaff_belman.dal.FileDAO;
import quickmaff_belman.gui.model.Model;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class LoginController implements Initializable
{

    @FXML
    private AnchorPane pane;
    private Model model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
     model = new Model(new BLLManager(new DatabaseFacade(new FileDAO(), new DbDAO())));
 
    }    

    private void loadFile() throws IOException, FileNotFoundException, ParseException {
        
        FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open JSON file");
            Stage stage = (Stage) pane.getScene().getWindow();
            File mediafile = fileChooser.showOpenDialog(stage);
            System.out.println(""+mediafile.getAbsolutePath());
            System.out.println(""+mediafile.getPath());
            if(mediafile!=null)
            {
                model.loadJSONfile(mediafile.getPath());
            }
    }
    
    public void initView(Stage stage)
    {

            stage.getScene().getAccelerators().put(
            new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN),
            new Runnable() {
                @FXML public void run() {

                    try {
                        loadFile();
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                });
    }
    
}
