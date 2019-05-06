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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.TaskStatus;

public class OrderDAO {

    private final DbConnection con;

    public OrderDAO(DbConnection con) {
        this.con = con;
    }

    public ArrayList<BoardTask> getAllBoardTasks(String department, int offset) throws SQLServerException, SQLException {
        ArrayList<BoardTask> allTasks = new ArrayList<>();
        String sql = "SELECT startDate,endDate,orderNumber,taskID from DepartmentTask where departmentName=(?) AND finishedOrder=0 AND startDate<=(?) order by endDate asc;";

        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.add(Calendar.DAY_OF_YEAR, +offset);
            Date date = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            pst.setString(1, department);
            pst.setDate(2, sqlDate);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String orderNumber = rs.getString("orderNumber");
                Date endDate = rs.getDate("endDate");
                Date startDate = rs.getDate("startDate");
                boolean readyForWork = checkIfReadyForWork(orderNumber, department);
                int taskID = rs.getInt("taskID");
                BoardTask bTask = new BoardTask(orderNumber, endDate, startDate, readyForWork, taskID);
                allTasks.add(bTask);
            }
            return allTasks;
        }
    }

    public boolean checkIfReadyForWork(String orderNumber, String departmentName) throws SQLServerException, SQLException {
        boolean readyForWork = true;
        ArrayList<TaskStatus> allTasks = new ArrayList<>();
        String sql = "select * from departmenttask where orderNumber=(?) order by startDate asc";
        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);) {
            pst.setString(1, orderNumber);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String dName = rs.getString("departmentName");
                boolean done = rs.getBoolean("finishedOrder");
                TaskStatus tStatus = new TaskStatus(dName, done);
                allTasks.add(tStatus);
            }

        }
        int indexOfDepartment = 0; // may change
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getDepartmentName().equals(departmentName)) {
                indexOfDepartment = i;
            }
        }
        // check if all prior in the list isn't done with their task yet

        for (int i = 0; i < indexOfDepartment; i++) {
            if(allTasks.get(i).getIsFinished()==false)
            {
                readyForWork=false;
            }
        }
        return readyForWork;
    }
    
     public boolean setCompleteTask(int taskID) throws SQLServerException, SQLException
    {
        Connection connection = null;
        boolean success = false;
        try 
        {
            connection = con.getConnection();
            connection.setAutoCommit(false);
            
        
        String sql = "UPDATE DepartmentTask\n" +
                    "set finishedOrder =1\n" +
                    "where taskID = (?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        
            pst.setInt(1, taskID);
            pst.executeUpdate();
            connection.commit();
            
            
        }catch (SQLException ex)
        {
            if (connection != null)
            {                
                connection.rollback();
            }
        } finally
        {
            if (connection != null)
            {
                connection.setAutoCommit(true); 
                connection.close(); 
                success = true;
                
            }
        }

        return success;

    }

}
