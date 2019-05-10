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
import javafx.scene.control.Label;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.FileWrapper;

/**
 *
 * @author Philip
 */
public class FolderWatcher implements Runnable {

    private final WatchService wService;
    private final Path path;
    private WatchKey watchKey;
    private final String FOLDER_PATH = "JSON";
    private final Model model;
    private final Label infoBar;

    public FolderWatcher(Model model, Label infoBar) throws IOException {
        this.wService = FileSystems.getDefault().newWatchService();
        this.path = Paths.get(FOLDER_PATH);
        watchKey = path.register(wService, StandardWatchEventKinds.ENTRY_CREATE);
        this.model = model;
        this.infoBar = infoBar;
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
                            System.out.println("LOADED FILE");
                            setLabel(model.getResourceBundle().getString("loadfile"));
                        } else {
                            setLabel(model.getResourceBundle().getString("duplicateFile"));
                        }
                    } catch (IOException ex) {
                        setLabel(model.getResourceBundle().getString("fileMissingHeader"));
                    } catch (SQLException ex) {
                        setLabel(model.getResourceBundle().getString("sqlExceptionHeader"));
                    } catch (ParseException ex) {
                        setLabel(model.getResourceBundle().getString("parseExceptionHeader"));
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
