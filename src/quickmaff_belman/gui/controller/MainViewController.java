/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import quickmaff_belman.gui.model.BoardMaker;
import quickmaff_belman.gui.model.FolderWatcher;
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
    private ImageView Filter;

    @FXML
    private FlowPane flowPane;
    private Stage stage;
    private ExecutorService executor;
    @FXML
    private ImageView iView;
    @FXML
    private Label infoBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        executor = Executors.newFixedThreadPool(2);
        startLabelResetter();
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

    public void initView() throws SQLException, IOException, InterruptedException {
        setGraphics();
        setAllText();
        // Setting up the board
        BoardMaker bMaker = new BoardMaker(flowPane, model, iView);
        executor.submit(bMaker);
        // Start the FolderWatcher looking for changes in the JSON folder
        FolderWatcher fWatcher = new FolderWatcher(model, infoBar);
        executor.submit(fWatcher);
    }

    private void setAllText() {
        department.setText(model.getResourceBundle().getString("department"));
    }

    @FXML
    private void filtering(MouseEvent event) {
        Language language = model.changeLanguage();

        switch (language) {
            case DANISH:
                Image buttonImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap.png");
                Filter.setImage(buttonImage);
                break;
            case ENGLISH:
                Image pressImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap-tryk.png");
                Filter.setImage(pressImage);
                break;
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setGraphics() {

        flowPane.prefWidthProperty().bind(stage.widthProperty().subtract(615));

    }

    private void startLabelResetter() {
        infoBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                Runnable resetter = new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            infoBar.setText("");
                        });
                    }
                };
              executor.schedule(resetter, 5000, TimeUnit.MILLISECONDS);
            }

        });

    }

}
