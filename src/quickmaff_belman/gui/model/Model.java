/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.bll.BLLManager;

public class Model {

    private BLLManager bMan;
    private Locale locale;
    private ResourceBundle rBundle;
    private String departmentName = "Halvfab";
    private static final String PROP_FILE = "src/resources/config.properties";
    private Properties properties;
    private int timeOffset;

    public Model(BLLManager bMan) throws FileNotFoundException, IOException {
        this.bMan = bMan;
        properties = new Properties();
        properties.load(new FileInputStream(PROP_FILE));
        // Setting the language to Danish by default
        locale = new Locale("da", "DK");
        rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
    }

    public int getTimeOffset(String departmentName) throws IOException {

        int timeOffset = Integer.parseInt(properties.getProperty(departmentName));
        return timeOffset;
    }

    public ArrayList<String> getDepartmentNames() throws FileNotFoundException, IOException {
        ArrayList<String> names = new ArrayList<>();

        Set<Object> dnames = properties.keySet();
        for (Object dname : dnames) {
            String depName = (String) dname;

            names.add(depName);

        }
        return names;
    }

    public void setDepartment(String department) throws IOException {
        this.departmentName = department;
        this.timeOffset = getTimeOffset(departmentName);

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

    public ResourceBundle getResourceBundle() {
        return rBundle;
    }

    public boolean checkForDuplicateFile(File mediafile) throws IOException, SQLException {
        return bMan.checkForDuplicateFile(mediafile);
    }

    public ArrayList<BoardTask> getAllBoardTasks() throws SQLException {
        return bMan.getAllBoardTasks(departmentName, timeOffset);
    }

}
