/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.DataContainer;

/**
 *
 * @author Philip
 */
public interface IFileDAO {

    File[] getAllFolderFiles() throws IOException;

    DataContainer getDataFromCSV(String path) throws IOException;

    DataContainer getDataFromExcel(String path) throws FileNotFoundException, IOException;

    DataContainer getDataFromJSON(String filepath) throws FileNotFoundException, IOException, ParseException;
    
}
