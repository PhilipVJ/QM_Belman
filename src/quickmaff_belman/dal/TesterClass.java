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
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.FileWrapper;

/**
 *
 * @author Philip
 */
public class TesterClass {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException, SQLException, InterruptedException 
    {
        FileWrapper fWrap = new FileWrapper(new File("JSON.txt"));
        System.out.println(""+fWrap.hashCode());

    }
    
}
