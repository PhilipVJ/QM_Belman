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
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.be.FolderCheckResult;
import quickmaff_belman.be.Log;
import quickmaff_belman.bll.BLLManager;

public class Model {

    private final BLLManager bMan;
    private Locale locale;
    private ResourceBundle rBundle;
    private String departmentName;
    private static final String PROP_FILE = "src/resources/config.properties";
    private final Properties properties;
    private int timeOffset;
    private Language language;

    public Model(BLLManager bMan) throws FileNotFoundException, IOException {
        this.bMan = bMan;
        properties = new Properties();
        properties.load(new FileInputStream(PROP_FILE));
        // Setting the language to Danish by default
        locale = new Locale("da", "DK");
        language = Language.DANISH;
        rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
    }

    public void setTimeOffset(String departmentName) throws IOException {
        this.timeOffset = Integer.parseInt(properties.getProperty(departmentName));
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
        setTimeOffset(departmentName);

    }



    public Language changeLanguage() {

        if (locale.getLanguage() == "da") {
            language = Language.ENGLISH;
            locale = new Locale("en", "EN");
            rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
            return Language.ENGLISH;
        } else {
            language = Language.DANISH;
            locale = new Locale("da", "DK");
            rBundle = ResourceBundle.getBundle("resources.languagepack", locale);
            return Language.DANISH;
        }
    }
    
    public Language getLanguage(){
        return language;
    }

    public ResourceBundle getResourceBundle() {
        return rBundle;
    }


    public ArrayList<BoardTask> getAllBoardTasks() throws SQLException {
        return bMan.getAllBoardTasks(departmentName, timeOffset);
    }
    
    public String getDepartmentName()
    {
        return departmentName;
    }

    public FolderCheckResult checkForUnLoadedFiles() throws IOException, SQLException, FileNotFoundException {
        return bMan.checkForUnLoadedFiles(departmentName);
    }
    public void setCompleteTask(int taskID) throws SQLException{
         bMan.setCompleteTask(taskID, departmentName);
    }
    public ArrayList<Log> getAllLogs() throws SQLException{
        return bMan.getAllLogs();
    }
    
    public boolean checkForDatabaseConnection() 
    {
        return bMan.checkForDatabaseConnection();
    }

   public void addCorruptFileToLog() throws SQLException {
        bMan.addCorruptFileToLog(departmentName);
    }

    public FolderCheckResult loadFile(File file) throws IOException, SQLException {
        return bMan.loadFile(departmentName, file);
    }
    
}
