/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.simple.parser.ParseException;
import quickmaff_belman.bll.BLLManager;

public class Model {

    private BLLManager bMan;
    private Locale locale;
    private ResourceBundle rBundle;

    public Model(BLLManager bMan) {
        this.bMan = bMan;

        // Setting the language to Danish by default
        locale = new Locale("da", "DK");
        rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
    }

    public void loadJSONfile(String filepath) throws IOException, FileNotFoundException, ParseException, SQLException {
        bMan.loadJSONfile(filepath);
    }

    public Language changeLanguage() {

        if (locale.getLanguage() == "da") {
            locale = new Locale("en", "EN");
            rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
            return Language.ENGLISH;
        } else {
            locale = new Locale("da", "DK");
            rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
            return Language.DANISH;
        }

    }
    
    public ResourceBundle getResourceBundle()
    {
        return rBundle;
    }

}
