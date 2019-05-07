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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.ColorfulPainter;
import quickmaff_belman.be.ITaskPainter;
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
public class MainViewController implements Initializable
{

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        bMakerExecutor = Executors.newSingleThreadExecutor();
        fWatcherExecutor = Executors.newSingleThreadExecutor();
        labelWatcher = Executors.newScheduledThreadPool(1);

        startLabelResetter();
        //set Images
        greenFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1.png");
        yellowFilter = new Image("/quickmaff_belman/gui/view/images/filterknap2.png");
        redFilter = new Image("/quickmaff_belman/gui/view/images/filterknap3.png");
        blueFilter = new Image("/quickmaff_belman/gui/view/images/filterknap4.png");
        offFilter = new Image("/quickmaff_belman/gui/view/images/filterknap1Off.png");

    }

    public void setModel(Model model)
    {
        this.model = model;
    }

    @FXML
    private void changeLanguage(MouseEvent event)
    {

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

    /**
     * When the info label is updated it will be reset after 5 seconds
     */
    private void startLabelResetter()
    {
        infoBar.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                Runnable resetter = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Platform.runLater(() ->
                        {
                            infoBar.setText("");
                        });
                    }
                };
                labelWatcher.schedule(resetter, 10, TimeUnit.SECONDS);
            }

        });

    }

    public void initView()
    {
        try
        {
            stage.setFullScreen(true);
            setGraphics();
            setAllText();

            // Setting up the board
            ColorfulPainter paint = new ColorfulPainter();
            BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, paint);
            bMakerExecutor.submit(bMaker);
            // Start the FolderWatcher looking for changes in the JSON folder
            FolderWatcher fWatcher = new FolderWatcher(model, infoBar);
            fWatcherExecutor.submit(fWatcher);
        } catch (IOException ex)
        {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        } catch (InterruptedException ex)
        {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    private void setAllText()
    {
        departmentName.setText(model.getDepartmentName());
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    private void setGraphics()
    {

        imgBackground.fitHeightProperty().bind(stage.heightProperty());
        imgBackground.fitWidthProperty().bind(stage.widthProperty());

        flowPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());

    }

    public void checkForUnloadedFiles()
    {
        int numberOfAddedFiles;
        try
        {
            numberOfAddedFiles = model.checkForUnLoadedFiles();
            if (numberOfAddedFiles > 0)
            {
                infoBar.setText(model.getResourceBundle().getString("addedNewFiles") + numberOfAddedFiles);
            }
        } catch (IOException ex)
        {
            infoBar.setText(model.getResourceBundle().getString("fileMissingHeader"));

        } catch (SQLException ex)
        {
            infoBar.setText(model.getResourceBundle().getString("sqlExceptionHeader"));

        } catch (ParseException ex)
        {
            infoBar.setText(model.getResourceBundle().getString("parseExceptionHeader"));
        }

    }

    @FXML
    private void filtering(MouseEvent event)
    {
        if (filterOption == 5)
        {
            filterOption = 1;
        } else
        {
            filterOption++;
        }

        switch (filterOption)
        {
            case 1:
                filter.setImage(offFilter);
                break;
            case 2:
                filter.setImage(greenFilter);
                break;
            case 3:
                filter.setImage(yellowFilter);
                break;
            case 4:
                filter.setImage(redFilter);
                break;
            case 5:
                filter.setImage(blueFilter);
                break;
        }
        System.out.println(filterOption);
    }

    private void restartBoardMaker(ITaskPainter chosenFilter)
    {
      bMakerExecutor.shutdown();
      bMakerExecutor.shutdownNow();
      BoardMaker bMaker = new BoardMaker(flowPane, model, anchorPane, chosenFilter);
      bMakerExecutor.submit(bMaker);
    }
}
