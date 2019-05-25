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
import quickmaff_belman.dal.IDatabaseFacade;

public class BLLManager implements IBLLManager
{
    private final IDatabaseFacade dFacade;

    public BLLManager(IDatabaseFacade dFacade) {
        this.dFacade = dFacade;
    }
    
    @Override
    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
        return dFacade.getAllBoardTasks(departmentName, offset);
    }

    @Override
    public FolderCheckResult checkForUnloadedFiles(String department) throws IOException, SQLException, FileNotFoundException {
        return dFacade.checkForUnloadedFiles(department);
    }
    @Override
    public void setCompleteTask(int taskID, String departmentName) throws SQLException{
         dFacade.setCompleteTask(taskID, departmentName);
    }
    @Override
    public ArrayList<Log> getAllLogs() throws SQLException{
        return dFacade.getAllLogs();
    }

    @Override
    public boolean checkForDatabaseConnection()  {
        return dFacade.checkForDatabaseConnection();
    }

    @Override
    public void addCorruptFileToLog(String department) throws SQLException {
       dFacade.addCorruptFileToLog(department);
    }

    @Override
    public FolderCheckResult loadFile(String departmentName, File file) throws IOException, SQLException {
       return dFacade.loadFile(departmentName, file);
    }
           
}
