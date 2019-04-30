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
import org.json.simple.parser.ParseException;
import quickmaff_belman.dal.DatabaseFacade;

public class BLLManager
{
    private DatabaseFacade dFacade;

    public BLLManager(DatabaseFacade dFacade) {
        this.dFacade = dFacade;
    }
    
    public void loadJSONfile(String filepath) throws IOException, FileNotFoundException, ParseException, SQLException {
        dFacade.loadJSONFile(filepath);
    }
    
    public boolean checkForDuplicateFile(File filepath) throws IOException, SQLException 
    {
        return dFacade.checkForDuplicateFile(filepath);
    }
           
    
}
