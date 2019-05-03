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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane anchorPaneAll;
    @FXML
    private ImageView imgBelmanLogo;
    @FXML
    private ImageView iView;
    @FXML
    private ImageView imgBackground;
    @FXML
    private Label lblDepartment;
    @FXML
    private ImageView languageSwitch;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ImageView filter;

    private Model model;
    private Stage stage;
    private ExecutorService executor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        executor = Executors.newFixedThreadPool(2);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    private void changeLanguage(MouseEvent event) {

        Language language = model.changeLanguage();
        switch (language)
        {
            case DANISH:
                Image daImage = new Image("/quickmaff_belman/gui/view/images/knapSprogDK.png");
                languageSwitch.setImage(daImage);
                break;
            case ENGLISH:
                Image engImage = new Image("/quickmaff_belman/gui/view/images/knapSprogENG.png");
                languageSwitch.setImage(engImage);
                break;

        }
        setAllText();
    }

    public void initView() throws SQLException, IOException, InterruptedException {
        setGraphics();
        setAllText();
        stage.setFullScreen(true);
        // Setting up the board
        BoardMaker bMaker = new BoardMaker(flowPane, model, iView);
        executor.submit(bMaker);
        // Start the FolderWatcher looking for changes in the JSON folder
        FolderWatcher fWatcher = new FolderWatcher(model);
        executor.submit(fWatcher);
    }

    private void setAllText() {
        lblDepartment.setText(model.getDepartmentName());
    }

    @FXML
    private void filtering(MouseEvent event) {
        Language language = model.changeLanguage();

        switch (language)
        {
            case DANISH:
                Image buttonImage = new Image("/quickmaff_belman/gui/view/images/filterknap1.png");
                filter.setImage(buttonImage);
                break;
            case ENGLISH:
                Image pressImage = new Image("/quickmaff_belman/gui/view/images/filterknap2.png");
                filter.setImage(pressImage);
                break;
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setGraphics() {
        imgBelmanLogo.translateXProperty().bind(stage.widthProperty().multiply(0.5));
        imgBelmanLogo.translateYProperty().bind(stage.heightProperty().multiply(0.07));
        imgBackground.fitHeightProperty().bind(stage.heightProperty());
        imgBackground.fitWidthProperty().bind(stage.widthProperty());
        lblDepartment.translateXProperty().bind(stage.widthProperty().multiply(0.09));
        lblDepartment.translateYProperty().bind(stage.heightProperty().multiply(0.06));
        filter.translateXProperty().bind(stage.widthProperty().multiply(0.925));
        filter.translateYProperty().bind(stage.heightProperty().multiply(0.015));
        languageSwitch.translateXProperty().bind(stage.widthProperty().multiply(0.925));
        languageSwitch.translateYProperty().bind(stage.heightProperty().multiply(0.06));
    }

}
