/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.bll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;

/**
 *
 * @author Philip
 */
public interface IBLLManager {

    void addCorruptFileToLog(String department) throws SQLException;

    boolean checkForDatabaseConnection();

    FolderCheckResult checkForUnloadedFiles(String department) throws IOException, SQLException, FileNotFoundException;

    ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException;

    ArrayList<Log> getAllLogs() throws SQLException;

    FolderCheckResult loadFile(String departmentName, File file) throws IOException, SQLException;

    void setCompleteTask(int taskID, String departmentName) throws SQLException;
    
}
