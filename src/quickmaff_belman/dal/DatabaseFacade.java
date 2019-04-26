/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.DataContainer;

/**
 *
 * @author Philip
 */
public class DatabaseFacade {

    private final FileDAO fDAO;
    private final DbDAO dDAO;

    public DatabaseFacade(FileDAO fDAO, DbDAO dDAO) {
        this.fDAO = fDAO;
        this.dDAO = dDAO;
    }

    public void loadJSONFile(String filepath) throws IOException, FileNotFoundException, ParseException {
        DataContainer con = fDAO.getDataFromJSON(filepath);
        // send videre til DbDAO
    }

}
