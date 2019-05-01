/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.activation.FileDataSource;
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
    private GridPane gridPane;
    private ObservableList<String> dep = FXCollections.observableArrayList();
    private int counter = 0;
    private Stage stage;
    @FXML
    private FlowPane flowPane;
    
    
    

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
    
   

    private void loadFile() throws FileNotFoundException, ParseException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON file");
        Stage stage = (Stage) pane.getScene().getWindow();
        File mediafile = fileChooser.showOpenDialog(stage);

        if (mediafile != null) {
            try {
                boolean checkStatus = model.checkForDuplicateFile(mediafile);
                if (checkStatus == false) {
                    model.loadJSONfile(mediafile.getPath());
                    Utility.createAlert(Alert.AlertType.INFORMATION, "Vigtig besked", "Læsning af JSON fuldført",
                            "JSON filen er blevet læst og lagt op på databasen");
                } else {
                    Utility.createAlert(Alert.AlertType.ERROR, "Vigtig besked", "Indlæsning mislykkedes", "JSON filen er allerede indsat i databasen");
                }

            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            } catch (SQLException ex) {
                ExceptionHandler.handleException(ex);
            }

        }
    }

    public void initView(Stage stage) {

        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN), new Runnable() {
            public void run() {
                System.out.println("test");
                try {
                    loadFile();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
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

        return dep;
    }

    public void loadGrid() {

        ObservableList<String> depNames = getDepartmentNames();

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

            flowPane.getChildren().addAll(newButton);

        }
    }

    public void timer() {
        try {
            Thread.sleep(1000);

            Platform.runLater(() -> {
                try {
                    openMainView();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void openMainView() throws SQLException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/MainView.fxml"));
            Parent root = loader.load();
            MainViewController con = loader.getController();
            con.setModel(new Model(new BLLManager(new DatabaseFacade())));

            con.setStage(stage);

            Stage stage = (Stage) pane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setFullScreen(true);
            stage.setScene(scene);
            stage.show();
            
            con.initView();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
     

//    private void loadFile() throws IOException
//    {
//        
//    } 

}
