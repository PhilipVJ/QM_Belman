/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.DataContainer;

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

    public void loadJSONFile(String filepath) throws IOException, FileNotFoundException, ParseException, SQLException {
        DataContainer con = fDAO.getDataFromJSON(filepath);
        uDAO.updateDatabaseWithJSON(con.getAllWorkers(), con.getAllProductionOrders());       
    }
    
    

}
