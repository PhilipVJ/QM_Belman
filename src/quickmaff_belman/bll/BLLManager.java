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
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;
import quickmaff_belman.dal.DatabaseFacade;

public class BLLManager
{
    private final DatabaseFacade dFacade;

    public BLLManager(DatabaseFacade dFacade) {
        this.dFacade = dFacade;
    }
    

    


    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
        return dFacade.getAllBoardTasks(departmentName, offset);
    }

    public FolderCheckResult checkForUnLoadedFiles(String department) throws IOException, SQLException, FileNotFoundException {
        return dFacade.checkForUnloadedFiles(department);
    }
    public void setCompleteTask(int taskID, String departmentName) throws SQLException{
         dFacade.setCompleteTask(taskID, departmentName);
    }
    public ArrayList<Log> getAllLogs() throws SQLException{
        return dFacade.getAllLogs();
    }

    public boolean checkForDatabaseConnection()  {
        return dFacade.checkForDatabaseConnection();
    }

    public void addCorruptFileToLog(String department) throws SQLException {
       dFacade.addCorruptFileToLog(department);
    }

    public FolderCheckResult loadFile(String departmentName, File file) throws IOException, SQLException {
       return dFacade.loadFile(departmentName, file);
    }
           
}
