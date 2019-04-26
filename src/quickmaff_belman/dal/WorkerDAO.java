/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;
import quickmaff_belman.gui.model.ExceptionHandler;

/**
 *
 * @author Philip
 */
public class WorkerDAO {

    private DbConnection con;

    public WorkerDAO(DbConnection con) {
        this.con = con;
    }

    
    
}
