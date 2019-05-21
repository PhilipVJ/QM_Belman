/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quickmaff_belman.gui.model.DatabaseConnector;
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

    private Label loadingLabel;

    private IntegerProperty connected;

    private ExecutorService service;
    @FXML
    private AnchorPane anPane;

    private StackPane loadingPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        service = Executors.newSingleThreadExecutor();
        connected = new SimpleIntegerProperty();
        connected.set(0);
        connected.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                
                int value = (int) newValue;
                if (value == 2) {
                    Platform.runLater(()-> {openMainView();});
                } else if (value == 1) {
                    Platform.runLater(()-> {connected.set(0);setClickToRetry();});
                }
            }
        });
        
    }

    private void setClickToRetry() {
        anPane.getChildren().remove(loadingPane);
        Label noConnection = new Label(model.getResourceBundle().getString("noConnection"));
        noConnection.setFont(new Font("Arial", 24));
        noConnection.setTranslateY(150);
        Image image = new Image("/quickmaff_belman/gui/view/images/noconnection.png");
        ImageView view = new ImageView(image);

        StackPane pane = new StackPane();
        pane.prefWidthProperty().bind(anPane.widthProperty());
        pane.prefHeightProperty().bind(anPane.heightProperty());

        Button retry = new Button(model.getResourceBundle().getString("connect"));
        retry.setFont(new Font("Arial", 24));
        retry.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anPane.getChildren().remove(pane);
                connectToDatabase();
            }

        });
        retry.setTranslateY(300);
        pane.getChildren().addAll(view, noConnection, retry);
        anPane.getChildren().add(pane);
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
                Label lbl = makeLabel(depName);
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
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(()->{flowPane.getChildren().clear();connectToDatabase();});
                            };
                        };
                        timer.schedule(task, 1000);     
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

    private Label makeLabel(String depName) {
        Label lbl = new Label();
        lbl.setText("" + depName);
        lbl.setMinWidth(173);
        lbl.setTranslateX(10);
        lbl.setTranslateY(-267);
        lbl.setFont(new Font("Arial", 24));
        lbl.setAlignment(Pos.CENTER);
        return lbl;
    }

    private void connectToDatabase() {
        Image loading = new Image("/quickmaff_belman/gui/view/images/loading.gif");
        ImageView view = new ImageView(loading);
        loadingPane = new StackPane();
        loadingPane.prefWidthProperty().bind(anPane.widthProperty());
        loadingPane.prefHeightProperty().bind(anPane.heightProperty());
        loadingLabel = new Label(model.getResourceBundle().getString("connecting"));
        loadingLabel.setFont(new Font("Arial", 24));
        loadingLabel.setTranslateY(150);
        loadingPane.getChildren().addAll(view, loadingLabel);
        anPane.getChildren().add(loadingPane);
        DatabaseConnector connector = new DatabaseConnector(model, connected);
        service.submit(connector);
    }

    public void openMainView() {
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
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGraphics() {
        imgBackground.fitHeightProperty().bind(stage.heightProperty());
        imgBackground.fitWidthProperty().bind(stage.widthProperty());
        imgBelmanLogo.translateYProperty().bind(stage.heightProperty().multiply(0.1));
    }


    public void setModel(Model model) {
        this.model = model;
    }
}
