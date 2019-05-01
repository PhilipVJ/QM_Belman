/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.gui.model.Model;

/**
 *
 * @author Philip
 */
public class TesterClass {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException, SQLException 
    {
        
        Model model = new Model(new BLLManager(new DatabaseFacade()));
        
        ArrayList<String> depName = model.getDepartmentNames();
        for (String object : depName) {
            System.out.println(""+ object);
            int offset = model.getTimeOffset(object);
            System.out.println(""+ offset);
            
        }
        
        
        
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int height = screenSize.height;
//        int width = screenSize.width;
//        System.out.println(width + ", " + height);

    }
    
}
