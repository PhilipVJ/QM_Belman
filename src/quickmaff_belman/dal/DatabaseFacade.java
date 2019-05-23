/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;
import quickmaff_belman.be.Worker;
import quickmaff_belman.gui.model.Utility;

public class DatabaseFacade {

    private final FileDAO fDAO;
    private final OrderDAO oDAO;
    private final DbUpdateDAO uDAO;
    private final BelTimer bTimer;
    private final DbConnection con;

    public DatabaseFacade() throws IOException {
        con = DbConnection.getInstance();
        fDAO = new FileDAO();
        oDAO = new OrderDAO(con);
        uDAO = new DbUpdateDAO(con);
        bTimer = new BelTimer();
    }

    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
        ArrayList<BoardTask> allBoardTasks = oDAO.getAllBoardTasks(departmentName, offset);
        for (BoardTask boardTask : allBoardTasks) {
            Worker worker = bTimer.getActiveWorker(boardTask.getOrderNumber());
            boardTask.setActiveWorker(worker);
        }
        return allBoardTasks;
    }

    public FolderCheckResult checkForUnloadedFiles(String department) throws IOException, SQLException, FileNotFoundException {

        File[] allFiles = fDAO.getAllFolderFiles();
        FolderCheckResult result = loadFile(department, allFiles);
        return result;
    }

    public void setCompleteTask(int taskID, String departmentName) throws SQLException {
        oDAO.setCompleteTask(taskID, departmentName);
    }

    public ArrayList<Log> getAllLogs() throws SQLException {
        return oDAO.getAllLogs();
    }

    public boolean checkForDatabaseConnection() {

        try (Connection connection = con.getConnection()) {
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public void addCorruptFileToLog(String department) throws SQLException {
        uDAO.addCorruptFilesToLog(1, department);
    }

    public FolderCheckResult loadFile(String department, File... files) throws IOException, SQLException {
        int numberOfNewFilesAdded = 0;
        int numberOfCorruptFiles = 0;
        int numberOfDuplicates = 0;

        for (File file : files) {

            if (Utility.getFileExtension(file.getPath()).equals("txt")) {
                try {
                    DataContainer jFile = fDAO.getDataFromJSON(file.getPath());
                    if (!uDAO.checkForDuplicateFile(jFile.hashCode())) {
                        uDAO.updateDatabaseWithFile(jFile, department);
                        numberOfNewFilesAdded++;
                    } else {
                        numberOfDuplicates++;
                    }
                } catch (ParseException ex) {
                    numberOfCorruptFiles++;
                    ;
                }
            }
            if (Utility.getFileExtension(file.getPath()).equals("csv")) {
                DataContainer cFile;
                try {
                    cFile = fDAO.getDataFromCSV(file.getPath());

                    if (!uDAO.checkForDuplicateFile(cFile.hashCode())) {
                        uDAO.updateDatabaseWithFile(cFile, department);
                        numberOfNewFilesAdded++;
                    } else {
                        numberOfDuplicates++;
                    }
                } catch (Exception ex) {
                    numberOfCorruptFiles++;
                }
            }

            if (Utility.getFileExtension(file.getPath()).equals("xlsx")) {
                DataContainer xFile;
                try {
                    xFile = fDAO.getDataFromExcel(file.getPath());
               
                if (!uDAO.checkForDuplicateFile(xFile.hashCode())) {
                    uDAO.updateDatabaseWithFile(xFile, department);
                    numberOfNewFilesAdded++;
                } else {
                    numberOfDuplicates++;
                }
                 } catch (Exception ex) {
                    numberOfCorruptFiles++;
                }
            }

        }
        FolderCheckResult result = new FolderCheckResult(numberOfNewFilesAdded, numberOfCorruptFiles, numberOfDuplicates);
        if (numberOfCorruptFiles > 0) {
            uDAO.addCorruptFilesToLog(numberOfCorruptFiles, department);
        }
        return result;
    }

}
