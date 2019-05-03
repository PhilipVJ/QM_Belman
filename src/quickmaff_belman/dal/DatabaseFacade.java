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
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.FileWrapper;

public class DatabaseFacade {

    private final FileDAO fDAO;
    private final OrderDAO oDAO;
    private final WorkerDAO wDAO;
    private final DbUpdateDAO uDAO;
     
    public DatabaseFacade() throws IOException {
        DbConnection con = DbConnection.getInstance();

        fDAO = new FileDAO();
        oDAO = new OrderDAO(con);
        wDAO = new WorkerDAO(con);
        uDAO = new DbUpdateDAO(con);
    }

    public boolean checkForDuplicateFile(FileWrapper file) throws IOException, SQLException 
    {
 
        return uDAO.checkForDuplicateFile(file);
    }
    
    public void loadJSONFile(FileWrapper file) throws IOException, FileNotFoundException, ParseException, SQLException {
        DataContainer con = fDAO.getDataFromJSON(file.getFilePath());
        uDAO.updateDatabaseWithJSON(con.getAllWorkers(), con.getAllProductionOrders(), file);       
    }

    public ArrayList<BoardTask> getAllBoardTasks(String departmentName, int offset) throws SQLException {
       return oDAO.getAllBoardTasks(departmentName, offset);
    }
    
    

}
