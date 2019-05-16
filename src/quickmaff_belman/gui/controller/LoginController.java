/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.gui.model.ExceptionHandler;

import quickmaff_belman.gui.model.Model;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class LoginController implements Initializable {

    @FXML
    private ImageView imgBackground;

    @FXML
    private ImageView imgBelmanLogo;
    @FXML
    private FlowPane flowPane;

    private Model model;
    private Stage stage;

    private ScheduledExecutorService executor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            model = new Model(new BLLManager(new DatabaseFacade()));
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
        createButtons();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void createButtons() {
        try {
            ArrayList<String> depNames = model.getDepartmentNames();
            for (String depName : depNames) {
                Button newButton = new Button();
                VBox vbox = new VBox();
                //sets size of text and position
                Label lbl = new Label();
                lbl.setText("" + depName);
                lbl.setMinWidth(173);
                lbl.setTranslateX(10);
                lbl.setTranslateY(-267);
                lbl.setFont(new Font("Arial", 24));
                lbl.setAlignment(Pos.CENTER);
                //sets prefered size of button to size of pictures
                newButton.setPrefHeight(300);
                newButton.setPrefWidth(191);
                newButton.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/button2Off.png);");
                //adds mouse clicked event to the button
                newButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (MouseEvent e)
                        -> {
                    try {
                        model.setDepartment(depName);
                        newButton.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/button2On.png);");
                        openMainView();
                    } catch (IOException ex) {
                        ExceptionHandler.handleException(ex, model.getResourceBundle());
                    }
                });
                vbox.getChildren().addAll(newButton, lbl);
                flowPane.getChildren().addAll(vbox);
                flowPane.setHgap(38);
                flowPane.setVgap(-5);

            }
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    public void openMainView() {

        executor = Executors.newScheduledThreadPool(1);

        Runnable openView = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/MainView.fxml"));
                        Parent root = loader.load();
                        MainViewController con = loader.getController();
                        con.setModel(model);
                        con.setStage(stage);
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        con.initView();
                        con.checkForUnloadedFiles();

                    } catch (IOException ex) {
                        ExceptionHandler.handleException(ex, model.getResourceBundle());
                    }
                });
            }
        };
        executor.schedule(openView, 2, TimeUnit.SECONDS);
    }

    public void setGraphics() {
        imgBackground.fitHeightProperty().bind(stage.heightProperty());
        imgBackground.fitWidthProperty().bind(stage.widthProperty());
        imgBelmanLogo.translateYProperty().bind(stage.heightProperty().multiply(0.1));
    }
}
