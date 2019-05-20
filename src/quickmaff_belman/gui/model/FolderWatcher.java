/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.FileWrapper;

/**
 *
 * @author Philip
 */
public class FolderWatcher implements Runnable {

    private WatchService wService = null;
    private Path path = null;
    private WatchKey watchKey;
    private String FOLDER_PATH = "JSON";
    private Model model;
    private Label infoBar;
    private BooleanProperty connectionLost;

    public FolderWatcher(Model model, Label infoBar, BooleanProperty connectionLost) {
        try {
            this.wService = FileSystems.getDefault().newWatchService();
            this.path = Paths.get(FOLDER_PATH);
            watchKey = path.register(wService, StandardWatchEventKinds.ENTRY_CREATE);
            this.model = model;
            this.infoBar = infoBar;
            this.connectionLost = connectionLost;

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex, model.getResourceBundle());
        }
    }

    @Override
    public void run() {
        try {
            while ((watchKey = wService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    String filePath = event.context().toString();
                    File file = new File(FOLDER_PATH + "/" + filePath);
                    FileWrapper wrappedFile;
                    try {
                        wrappedFile = new FileWrapper(file);
                        boolean checkStatus = model.checkForDuplicateFile(wrappedFile);
                        if (checkStatus == false) {
                            model.loadJSONfile(wrappedFile);
                            setLabel(model.getResourceBundle().getString("loadfile"));
                        } else {
                            setLabel(model.getResourceBundle().getString("duplicateFile"));
                        }
                    } catch (IOException ex) {
                        ExceptionHandler.handleException(ex, model.getResourceBundle());
                    } catch (SQLException ex) {
                        connectionLost.set(true);
                    } catch (ParseException ex) {
                        try {
                            model.addCorruptFileToLog();
                            setLabel(model.getResourceBundle().getString("parseExceptionHeader"));
                        } catch (SQLException ex1) {
                            connectionLost.set(true);
                        }
                    }
                }
                watchKey.reset();
            }

        } catch (InterruptedException ex) {
            return;
        }
    }

    private void setLabel(String text) {
        Platform.runLater(() -> {
            infoBar.setText(text);
        });
    }
}
