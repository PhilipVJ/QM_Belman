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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
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

        dep.add("a");
        dep.add("b");
        dep.add("c");
        dep.add("d");
        dep.add("e");
        dep.add("f");
        dep.add("g");
        dep.add("h");
        dep.add("i");
        dep.add("j");
        dep.add("k");
        dep.add("l");
        dep.add("m");
        dep.add("n");
        dep.add("o");
        dep.add("p");
        dep.add("q");
        dep.add("r");

        return dep;
    }

    public void buttonGenerator() {

        ObservableList<String> depNames = getDepartmentNames();

        int i = 0; //column index
        int j = 0; //row index

        for (String buttonName : depNames) {
            Button newButton = new Button(buttonName);
            newButton.setBorder(Border.EMPTY);
            newButton.setPrefHeight(25);
            newButton.setPrefWidth(200);
            //adds mouse clicked event to the button
            newButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/Authentication.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    AuthenticationController hc = fxmlLoader.getController();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            //adds button to gridpane with coordinates
            gridPane.add(newButton, j, i);
            //makes sure to get the right coordinates for column and row.
            j++;
            if (j == 2) {
                j = 0;
                i++;
            }
        }
    }

}
