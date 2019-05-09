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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BluePainter;
import quickmaff_belman.be.ColorfulPainter;
import quickmaff_belman.be.GreenPainter;
import quickmaff_belman.be.ITaskPainter;
import quickmaff_belman.be.RedPainter;
import quickmaff_belman.be.YellowPainter;
import quickmaff_belman.gui.model.BoardMaker;
import quickmaff_belman.gui.model.ExceptionHandler;
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
    private ImageView imgBackground;
    @FXML
    private ImageView languageSwitch;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ImageView filter;

    private Model model;
    private Stage stage;
    private ExecutorService bMakerExecutor;
    private ExecutorService fWatcherExecutor;

    @FXML
    private Label infoBar;

    private ScheduledExecutorService labelWatcher;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView logo;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label departmentName;
    @FXML
    private StackPane display;

    private int filterOption = 1;

    private Image greenFilter;
    private Image redFilter;
    private Image yellowFilter;
    private Image blueFilter;
    private Image offFilter;

    private BooleanProperty isLoading;
    @FXML
    private VBox filterBox;
    @FXML
    private ImageView filterSwitch;

    private ITaskPainter currentFilter;

    private Image filterGlow = new Image("/quickmaff_belman/gui/view/images/on2.png");
    private Image filterGlowOff = new Image("/quickmaff_belman/gui/view/images/on.png");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bMakerExecutor = Executors.newSingleThreadExecutor();
        fWatcherExecutor = Executors.newSingleThreadExecutor();
        labelWatcher = Executors.newScheduledThreadPool(1);

        currentFilter = new ColorfulPainter();

        startLabelResetter();
        //set Images
        greenFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1.png");
        yellowFilter = new Image("/quickmaff_belman/gui/view/images/filterknap2.png");
        redFilter = new Image("/quickmaff_belman/gui/view/images/filterknap3.png");
        blueFilter = new Image("/quickmaff_belman/gui/view/images/filterknap4.png");
        offFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1Off.png");

        filterGlow = new Image("/quickmaff_belman/gui/view/images/on2.png");
        filterGlowOff = new Image("/quickmaff_belman/gui/view/images/on.png");

        isLoading = new SimpleBooleanProperty(false);

        isLoading.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isLoading.get()) {
                    Platform.runLater(()
                            -> {
                        infoBar.setText(model.getResourceBundle().getString("loading"));
                    });

                } else if (!isLoading.get()) {
                    Platform.runLater(()
                            -> {
                        if (flowPane.getChildren().isEmpty()) {
                            infoBar.setText(model.getResourceBundle().getString("noTasks"));
                        } else {
                            infoBar.setText("");
                        }
                    });
                }
            }

        });

    }

    public void setModel(Model model) {
        this.model = model;
    }

    private void checkForEmptyFlowPane() {

    }

    @FXML
    private void changeLanguage(MouseEvent event) {

        Language language = model.changeLanguage();
        switch (language) {
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

    /**
     * When the info label is updated it will be reset after 5 seconds
     */
    private void startLabelResetter() {
        infoBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!isLoading.get()) {
                    Runnable resetter = new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(()
                                    -> {
                                infoBar.setText("");
                            });
                        }
                    };
                    labelWatcher.schedule(resetter, 10, TimeUnit.SECONDS);
                }

            }
        });

    }

    public void initView() {
        try {
            stage.setFullScreen(true);
            setGraphics();
            setAllText();
            // Setting up the board
            BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, currentFilter, isLoading);
            bMakerExecutor.submit(bMaker);
            // Start the FolderWatcher looking for changes in the JSON folder
            FolderWatcher fWatcher = new FolderWatcher(model, infoBar);
            fWatcherExecutor.submit(fWatcher);

            // Makes the application go back to the login screen with a certain key combination
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN), new Runnable() {
                @Override
                public void run() {
                    logOut();
                }

            });
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        } catch (InterruptedException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/Login.fxml"));
            Parent root = loader.load();
            LoginController con = loader.getController();
            con.setStage(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
//            stage.show();
            con.setGraphics();
   
            shutDownThreads();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    private void shutDownThreads() {
        bMakerExecutor.shutdown();
        fWatcherExecutor.shutdown();
        labelWatcher.shutdown();
         bMakerExecutor.shutdownNow();
        fWatcherExecutor.shutdownNow();
        labelWatcher.shutdownNow();
    }

    private void setAllText() {
        departmentName.setText(model.getDepartmentName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setGraphics() {

        imgBackground.fitHeightProperty().bind(stage.heightProperty());
        imgBackground.fitWidthProperty().bind(stage.widthProperty());

        flowPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());

    }

    public void checkForUnloadedFiles() {
        int numberOfAddedFiles;
        try {
            numberOfAddedFiles = model.checkForUnLoadedFiles();
            if (numberOfAddedFiles > 0) {
                infoBar.setText(model.getResourceBundle().getString("addedNewFiles") + numberOfAddedFiles);
            }
        } catch (IOException ex) {
            infoBar.setText(model.getResourceBundle().getString("fileMissingHeader"));

        } catch (SQLException ex) {
            infoBar.setText(model.getResourceBundle().getString("sqlExceptionHeader"));

        } catch (ParseException ex) {
            infoBar.setText(model.getResourceBundle().getString("parseExceptionHeader"));
        }

    }

    @FXML
    private void setFilterOption(MouseEvent event) {
        if (filterOption == 5) {
            filterOption = 1;
        } else {
            filterOption++;
        }

        switch (filterOption) {
            case 1:
                filter.setImage(offFilter);
                currentFilter = new ColorfulPainter();
                break;
            case 2:
                filter.setImage(greenFilter);
                currentFilter = new GreenPainter();
                break;
            case 3:
                filter.setImage(yellowFilter);
                currentFilter = new YellowPainter();
                break;
            case 4:
                filter.setImage(redFilter);
                currentFilter = new RedPainter();
                break;
            case 5:
                filter.setImage(blueFilter);
                currentFilter = new BluePainter();
                break;
        }

    }

    private void restartBoardMaker(ITaskPainter chosenFilter) {
        // Shut down the current thread
        bMakerExecutor.shutdown();
        bMakerExecutor.shutdownNow();
        // Make a new thread with a new runnable
        bMakerExecutor = Executors.newSingleThreadExecutor();
        flowPane.getChildren().clear();
        infoBar.setText(model.getResourceBundle().getString("loading"));
        BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, chosenFilter, isLoading);
        bMakerExecutor.submit(bMaker);
    }

    @FXML
    private void filter(MouseEvent event) {

        filterSwitch.setImage(filterGlow);
        ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {

                    filterSwitch.setImage(filterGlowOff);

                });
            }

        };
        thread.schedule(run, 1, TimeUnit.SECONDS);
        restartBoardMaker(currentFilter);

    }

}
