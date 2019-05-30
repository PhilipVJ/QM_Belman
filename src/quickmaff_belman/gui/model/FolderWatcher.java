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
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import quickmaff_belman.be.FolderCheckResult;

/**
 *
 * @author Philip
 */
public class FolderWatcher implements Runnable {

    private WatchService wService = null;
    private Path path = null;
    private WatchKey watchKey;
    private String FOLDER_PATH = "DatabaseFiles";
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
                    Thread.sleep(1000);
                    try {
                        FolderCheckResult result = model.loadFile(file);
                        if (result.getNumberOfNewlyAddedFiles() > 0) {
                            setLabel(model.getResourceBundle().getString("loadfile"));
                        } else if (result.getNumberOfDuplicates() > 0) {
                            setLabel(model.getResourceBundle().getString("duplicateFile"));
                        }
                        else if(result.getNumberOfCorruptFiles()>0)
                        {
                          setLabel(model.getResourceBundle().getString("parseExceptionHeader"));  
                        }
                        else if(result.getNumberOfUnknownFiles()>0)
                        {
                            setLabel(model.getResourceBundle().getString("unknownFile"));  
                        }
                    } catch (IOException ex) {
                        ExceptionHandler.handleException(ex, model.getResourceBundle());
                    } catch (SQLException ex) {
                        connectionLost.set(true);
                    }
                    watchKey.reset();
                }
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
