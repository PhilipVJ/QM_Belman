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
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.taskpainter.BluePainter;
import quickmaff_belman.be.taskpainter.ColorfulPainter;
import quickmaff_belman.be.Filter;
import quickmaff_belman.be.taskpainter.GreenPainter;
import quickmaff_belman.be.taskpainter.ITaskPainter;
import quickmaff_belman.be.Log;
import quickmaff_belman.be.taskpainter.RedPainter;
import quickmaff_belman.be.taskpainter.YellowPainter;
import quickmaff_belman.gui.model.BoardMaker;
import quickmaff_belman.gui.model.Clock;
import quickmaff_belman.gui.model.ExceptionHandler;
import quickmaff_belman.gui.model.FolderWatcher;
import quickmaff_belman.gui.model.Language;
import quickmaff_belman.gui.model.Model;
import quickmaff_belman.gui.model.Utility;
import quickmaff_belman.gui.model.WorkerFilterOption;

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
    @FXML
    private Label infoBar;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label departmentName;
    @FXML
    private ImageView filterSwitch;
    @FXML
    private TextField searchbar;
    @FXML
    private RadioButton activeWorkers;
    @FXML
    private RadioButton nonActiveWorkers;
    @FXML
    private RadioButton showAll;
    @FXML
    private Label clock;

    private Model model;
    private Stage stage;
    private TableView<Log> tvLog;
    private int filterOption = 1;
    private Image greenFilter;
    private Image redFilter;
    private Image yellowFilter;
    private Image blueFilter;
    private Image offFilter;
    private BooleanProperty isLoading;
    private ITaskPainter paintFilter;
    private Filter chosenFilter;
    private Image filterGlow;
    private Image filterGlowOff;
    private ExecutorService fWatcherExecutor;
    private ExecutorService clockExecutor;
    private ExecutorService bMakerExecutor;
    private ScheduledExecutorService labelWatcher;

    private WorkerFilterOption wOption;
    private StackPane stackPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        searchbar.setFocusTraversable(false);
        // Make the filter radio buttons into a group
        ToggleGroup radioGroup = new ToggleGroup();
        activeWorkers.setToggleGroup(radioGroup);
        nonActiveWorkers.setToggleGroup(radioGroup);
        showAll.setSelected(true);
        showAll.setToggleGroup(radioGroup);

        //Adds a listener to the group
        radioGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> changeWorkerFilterOption(newVal));

        bMakerExecutor = Executors.newSingleThreadExecutor();
        fWatcherExecutor = Executors.newSingleThreadExecutor();
        labelWatcher = Executors.newScheduledThreadPool(1);
        clockExecutor = Executors.newSingleThreadExecutor();
        // Sets the filters to default
        paintFilter = new ColorfulPainter();
        wOption = WorkerFilterOption.SHOWALL;
        
        startLabelResetter();
        //set Images
        greenFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1.png");
        yellowFilter = new Image("/quickmaff_belman/gui/view/images/filterknap2.png");
        redFilter = new Image("/quickmaff_belman/gui/view/images/filterknap3.png");
        blueFilter = new Image("/quickmaff_belman/gui/view/images/filterknap4.png");
        offFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1Off.png");
        filterGlow = new Image("/quickmaff_belman/gui/view/images/on2.png");
        filterGlowOff = new Image("/quickmaff_belman/gui/view/images/on.png");

        addKeybindToLogView();
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
            Filter filter = new Filter(WorkerFilterOption.SHOWALL);
            BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, paintFilter, isLoading, infoBar, filter);
            bMakerExecutor.submit(bMaker);
            // Start the FolderWatcher looking for changes in the JSON folder
            FolderWatcher fWatcher = new FolderWatcher(model, infoBar);
            fWatcherExecutor.submit(fWatcher);

            Clock clockSetter = new Clock(clock);
            clockExecutor.submit(clockSetter);

            // Makes the application go back to the login screen with a certain key combination
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN), new Runnable() {
                @Override
                public void run() {
                    logOut();
                }
            });

        } catch (IOException ex) {
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
        clockExecutor.shutdown();
        clockExecutor.shutdownNow();
    }

    private void setAllText() {
        departmentName.setText(model.getDepartmentName());
        activeWorkers.setText(model.getResourceBundle().getString("activeWorkersRadio"));
        nonActiveWorkers.setText(model.getResourceBundle().getString("nonActiveWorkersRadio"));
        showAll.setText(model.getResourceBundle().getString("disable"));
        searchbar.setPromptText(model.getResourceBundle().getString("search"));
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
                paintFilter = new ColorfulPainter();
                break;
            case 2:
                filter.setImage(greenFilter);
                paintFilter = new GreenPainter();
                break;
            case 3:
                filter.setImage(yellowFilter);
                paintFilter = new YellowPainter();
                break;
            case 4:
                filter.setImage(redFilter);
                paintFilter = new RedPainter();
                break;
            case 5:
                filter.setImage(blueFilter);
                paintFilter = new BluePainter();
                break;
        }
    }

    private void restartBoardMaker() {

        // Shut down the current thread
        bMakerExecutor.shutdown();
        bMakerExecutor.shutdownNow();
        // Make a new thread with a new runnable
        bMakerExecutor = Executors.newSingleThreadExecutor();

        flowPane.getChildren().clear();
        infoBar.setText(model.getResourceBundle().getString("loading"));
        BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, paintFilter, isLoading, infoBar, chosenFilter);
        bMakerExecutor.submit(bMaker);
    }

    @FXML
    private void filter(MouseEvent event) {
        String searchWord = searchbar.getText();
        chosenFilter = new Filter(wOption, searchWord);
        filterSwitch.setImage(filterGlow);
        //Make the switch turn of its glow after 1 second
        ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(()
                        -> {
                    filterSwitch.setImage(filterGlowOff);
                }
                );

            }
        };
        thread.schedule(run, 1, TimeUnit.SECONDS);
        restartBoardMaker();

    }

    private void changeWorkerFilterOption(Toggle newVal) {
        String fxId = Utility.getFXIDfromToggle(newVal);

        switch (fxId) {
            case "activeWorkers":
                wOption = WorkerFilterOption.ACTIVEWORKERS;
                break;
            case "nonActiveWorkers":
                wOption = WorkerFilterOption.NONACTIVEWORKERS;
                break;
            case "showAll":
                wOption = WorkerFilterOption.SHOWALL;
                break;
        }
    }

    private void makeLogView() {

        try {
            TableColumn<Log, Integer> logIDCol = new TableColumn<>("Log ID");
            logIDCol.setCellValueFactory(new PropertyValueFactory("logID"));

            TableColumn<Log, Date> activityDateCol = new TableColumn<>("activityDate");
            activityDateCol.setCellValueFactory(new PropertyValueFactory("activityDate"));

            TableColumn<Log, String> activityCol = new TableColumn<>("activity");
            activityCol.setCellValueFactory(new PropertyValueFactory("activity"));

            TableColumn<Log, String> descriptionCol = new TableColumn<>("description");
            descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));

            TableColumn<Log, String> departmentNameCol = new TableColumn<>("departmentName");
            departmentNameCol.setCellValueFactory(new PropertyValueFactory("departmentName"));

            activityDateCol.setText(model.getResourceBundle().getString("activityDate"));
            activityCol.setText(model.getResourceBundle().getString("activity"));
            descriptionCol.setText(model.getResourceBundle().getString("description"));
            departmentNameCol.setText(model.getResourceBundle().getString("departmentName"));

            ArrayList<Log> logs = model.getAllLogs();
            ObservableList<Log> tvLogs = FXCollections.observableArrayList(logs);
            tvLog = new TableView<>();
            tvLog.setItems(tvLogs);
            tvLog.getColumns().addAll(logIDCol, activityDateCol, activityCol, descriptionCol, departmentNameCol);
            tvLog.columnResizePolicyProperty().set(CONSTRAINED_RESIZE_POLICY);

            stackPane = new StackPane();
            stackPane.getChildren().add(tvLog);

            stackPane.prefHeightProperty().bind(anchorPane.heightProperty());
            stackPane.prefWidthProperty().bind(anchorPane.widthProperty());
            anchorPane.getChildren().add(stackPane);
        } catch (SQLException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    public void clearLogView() {
        anchorPane.getChildren().remove(stackPane);
    }

    private void addKeybindToLogView() {
        anchorPane.setOnKeyPressed(
                event -> {
                    switch (event.getCode()) {
                        case F11: {
                            makeLogView();
                            break;
                        }
                        case F12:
                            clearLogView();
                            break;
                    }
                }
        );
    }
}
