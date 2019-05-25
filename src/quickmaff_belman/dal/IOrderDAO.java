/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.Log;
import quickmaff_belman.be.OrderOverview;

/**
 *
 * @author Philip
 */
public interface IOrderDAO {

    ArrayList<BoardTask> getAllBoardTasks(String department, int offset) throws SQLServerException, SQLException;

    ArrayList<Log> getAllLogs() throws SQLServerException, SQLException;

    String getCustomerName(String orderNumber) throws SQLServerException, SQLException;

    OrderOverview getOverview(String orderNumber, String departmentName, Date startDate) throws SQLServerException, SQLException;

    void setCompleteTask(int taskID, String departmentName) throws SQLServerException, SQLException;
    
}
