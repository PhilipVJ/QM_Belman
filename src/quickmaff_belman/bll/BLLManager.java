/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.bll;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.dal.DatabaseFacade;

public class BLLManager
{
    private final DatabaseFacade dFacade;

    public BLLManager(DatabaseFacade dFacade) {
        this.dFacade = dFacade;
    }
    
    public void loadJSONfile(FileWrapper file) throws IOException, FileNotFoundException, ParseException, SQLException {
        dFacade.loadJSONFile(file);
    }
    
    public boolean checkForDuplicateFile(FileWrapper file) throws IOException, SQLException 
    {
        return dFacade.checkForDuplicateFile(file);
    }

    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
        return dFacade.getAllBoardTasks(departmentName, offset);
    }

    public int checkForUnLoadedFiles() throws IOException, SQLException, FileNotFoundException, ParseException {
        return dFacade.checkForUnloadedFiles();
    }
           
    
}
