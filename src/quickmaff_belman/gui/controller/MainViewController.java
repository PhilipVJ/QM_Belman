/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import com.jfoenix.controls.JFXScrollPane;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import quickmaff_belman.gui.model.Language;
import quickmaff_belman.gui.model.Model;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class MainViewController implements Initializable {

    @FXML
    private ImageView languageSwitch;
    private Model model;
    @FXML
    private Label department;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        anchorPane.setPrefHeight(6000);

    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    private void changeLanguage(MouseEvent event) {

        Language language = model.changeLanguage();
        switch (language) {
            case DANISH:
                Image daImage = new Image("/quickmaff_belman/gui/view/images/toggle.png");
                languageSwitch.setImage(daImage);
                break;
            case ENGLISH:
                Image engImage = new Image("/quickmaff_belman/gui/view/images/toggle2.png");
                languageSwitch.setImage(engImage);
                break;

        }

        setAllText();

    }

    public void initView() {
        setAllText();
        testScroll();
    }

    public void testScroll() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            names.add("Button: " + i);
        }

        int i = 0; //column index
        int j = 0; //row index

        for (String buttonName : names) {
            Image daImage = new Image("/quickmaff_belman/gui/view/images/postit2.png");
            ImageView view = new ImageView(daImage);

            view.setPreserveRatio(true);
            view.setFitWidth(160);
            view.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("Opening task " + buttonName);
            });
            HBox box = new HBox(view);
            box.setAlignment(Pos.CENTER);
            grid.add(box, j, i);
            j++;
            if (j == 3) {
                j = 0;
                i++;
            }
        }

    }

    private void setAllText() {
        department.setText(model.getResourceBundle().getString("department"));
    }

}
