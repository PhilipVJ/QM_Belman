/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;
import quickmaff_belman.be.Worker;

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

    public boolean checkForDuplicateFile(FileWrapper file) throws IOException, SQLException {

        return uDAO.checkForDuplicateFile(file);
    }

    public void loadJSONFile(FileWrapper file, String departmentName) throws IOException, SQLException, FileNotFoundException, ParseException {
        DataContainer con = fDAO.getDataFromJSON(file.getFilePath());
        uDAO.updateDatabaseWithJSON(con, file, departmentName);
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
        
        int numberOfNewFilesAdded = 0;
        int numberOfCorruptFiles = 0;
        ArrayList<FileWrapper> allFiles = fDAO.getAllFolderFiles();
        for (FileWrapper file : allFiles) {
            if (!uDAO.checkForDuplicateFile(file)) {
                try {
                    loadJSONFile(file, department);
                    numberOfNewFilesAdded++;
                } catch (ParseException ex) {
                    numberOfCorruptFiles++; 
                    continue;
                }
              
            }
        }
        
        FolderCheckResult result = new FolderCheckResult(numberOfNewFilesAdded,numberOfCorruptFiles);
        if(numberOfCorruptFiles>0)
        {
            uDAO.addCorruptFilesToLog(numberOfCorruptFiles, department);
        }
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
}
