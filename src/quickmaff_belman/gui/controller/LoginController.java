/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
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
    @FXML
    private GridPane gridPane;
    private ObservableList<String> dep = FXCollections.observableArrayList();
    private  int counter = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        buttonGenerator();
        try {
            model = new Model(new BLLManager(new DatabaseFacade()));
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }

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
            } catch (SQLException ex) {
                ExceptionHandler.handleException(ex);
            }

        }
    }

    public void initView(Stage stage) {

        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN), new Runnable() {
            public void run() {
                loadFile();
            }
        });
    }

    public ObservableList<String> getDepartmentNames() {

        dep.add("Department 1");
        dep.add("Department 2");
        dep.add("Department 3");
        dep.add("Department 4");
        dep.add("Department 5");
        dep.add("Department 6");
        dep.add("Department 7");
        dep.add("Department 7");
        dep.add("Department 7");
        dep.add("Department 7");
        dep.add("Department 7");
        dep.add("Department 7");
        



        return dep;
    }

    public void loadGrid() {

        ObservableList<String> depNames = getDepartmentNames();
        int i = 0; //column index
        int j = 0; //row index
        
        for (String depName : depNames) {
            Button newButton = new Button(depName);            
            //sets size of text
            Font font = new Font(22);
            newButton.setFont(font);
            //sets prefered size of button = size of pictures
            newButton.setPrefHeight(203);
            newButton.setPrefWidth(206);
            newButton.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/ButtonOFF.png);");           
            //adds mouse clicked event to the button
            newButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                
                newButton.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/ButtonON.png);");
                //makes new thread for the timer
                Thread t = new Thread(() -> {
                timer();
                 });
                 t.start();
            });            
            //adds button to gridpane with coordinates            
            gridPane.add(newButton, j, i);
            //makes sure to get the right coordinates for column and row.
            j++;
            if (j == 3) {
                j = 0;
                i++;
            }
        }  
    }
    
   public void timer(){
         try {
            Thread.sleep(1000); 
            
            Platform.runLater(() -> { 
                openMainView();
            });
            }catch (InterruptedException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
    }  
   
    public void openMainView(){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/MainView.fxml"));
            Parent root = loader.load();
            MainViewController con = loader.getController();
            con.setModel(new Model(new BLLManager(new DatabaseFacade())));
            con.initView();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
            }catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }   


}
