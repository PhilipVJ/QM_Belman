/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;
import quickmaff_belman.be.Worker;
import quickmaff_belman.gui.model.Utility;

public class DatabaseFacade implements IDatabaseFacade {

    private final IFileDAO fDAO;
    private final IOrderDAO oDAO;
    private final IDbUpdateDAO uDAO;
    private final IBelTimer bTimer;
    private final DbConnection con;

    public DatabaseFacade(DbConnection con, IFileDAO fDAO, IOrderDAO oDAO, IDbUpdateDAO uDAO, IBelTimer bTimer) throws IOException {
        this.con = con;
        this.fDAO = fDAO;
        this.oDAO = oDAO;
        this.uDAO = uDAO;
        this.bTimer = bTimer;
    }

    @Override
    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
        ArrayList<BoardTask> allBoardTasks = oDAO.getAllBoardTasks(departmentName, offset);
        for (BoardTask boardTask : allBoardTasks) {
            ArrayList<Worker> activeWorkers = bTimer.getActiveWorkers(boardTask.getOrderNumber());
            boardTask.setActiveWorkers(activeWorkers);
            double realProgress = bTimer.getRealProgress(boardTask);
            boardTask.setRealProgress(realProgress);
        }
        return allBoardTasks;
    }

    @Override
    public FolderCheckResult checkForUnloadedFiles(String department) throws IOException, SQLException, FileNotFoundException {

        File[] allFiles = fDAO.getAllFolderFiles();
        FolderCheckResult result = loadFile(department, allFiles);
        return result;
    }

    @Override
    public void setCompleteTask(int taskID, String departmentName) throws SQLException {
        oDAO.setCompleteTask(taskID, departmentName);
    }

    @Override
    public ArrayList<Log> getAllLogs() throws SQLException {
        return oDAO.getAllLogs();
    }

    @Override
    public boolean checkForDatabaseConnection() {

        return con.makeConnection();

    }

    @Override
    public void addCorruptFileToLog(String department) throws SQLException {
        uDAO.addCorruptFilesToLog(1, department);
    }

    @Override
    public FolderCheckResult loadFile(String department, File... files) throws SQLException, IOException {
        int numberOfNewFilesAdded = 0;
        int numberOfCorruptFiles = 0;
        int numberOfDuplicates = 0;
        int numberOfUnknownFiles = 0;

        for (File file : files) {
            String extension = Utility.getFileExtension(file.getPath());
            switch (extension) {
                case "csv": {
                    try {
                        DataContainer cFile = fDAO.getDataFromCSV(file.getPath());
                        if (!uDAO.checkForDuplicateFile(cFile.hashCode())) {
                            uDAO.updateDatabaseWithFile(cFile, department);
                            numberOfNewFilesAdded++;
                        } else {
                            numberOfDuplicates++;
                        }
                    } catch (Exception ex) {
                        if (ex instanceof SQLException) {
                            throw new SQLException();
                        }
                        if (ex instanceof IOException) {
                            throw new IOException();
                        }
                        numberOfCorruptFiles++;
                    }
                    break;
                }

                case "txt":
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
                    break;

                case "xlsx":
                    try {
                        DataContainer xFile = fDAO.getDataFromExcel(file.getPath());
                        if (!uDAO.checkForDuplicateFile(xFile.hashCode())) {
                            uDAO.updateDatabaseWithFile(xFile, department);
                            numberOfNewFilesAdded++;
                        } else {
                            numberOfDuplicates++;
                        }
                    } catch (Exception ex) {
                        if (ex instanceof SQLException) {
                            throw new SQLException();
                        }
                        if (ex instanceof IOException) {
                            throw new IOException();
                        }
                        numberOfCorruptFiles++;
                    }
                    break;

                default:
                    numberOfUnknownFiles++;
                    break;

            }
        }
        FolderCheckResult result = new FolderCheckResult(numberOfNewFilesAdded, numberOfCorruptFiles, numberOfDuplicates, numberOfUnknownFiles);
        if (numberOfCorruptFiles > 0) {
            uDAO.addCorruptFilesToLog(numberOfCorruptFiles, department);
        }
        return result;
    }

}
