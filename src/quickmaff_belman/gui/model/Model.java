/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;

public class Model
{
    private BLLManager bMan;

    public Model(BLLManager bMan) {
        this.bMan = bMan;
    }
    
    public void loadJSONfile(String filepath) throws IOException, FileNotFoundException, ParseException, SQLException {
      bMan.loadJSONfile(filepath);
    }
    
    public boolean checkForDuplicateFile(File filepath) throws IOException, SQLException 
    {
        return bMan.checkForDuplicateFile(filepath);
    }
    
}
