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
import quickmaff_belman.be.Log;
import quickmaff_belman.be.OrderOverview;
import quickmaff_belman.be.TaskStatus;

public class OrderDAO
{

    private final DbConnection con;

    public OrderDAO(DbConnection con)
    {
        this.con = con;
    }

    public ArrayList<BoardTask> getAllBoardTasks(String department, int offset) throws SQLServerException, SQLException
    {
        ArrayList<BoardTask> allTasks = new ArrayList<>();
        String sql = "SELECT startDate,endDate,orderNumber,taskID from DepartmentTask where departmentName=(?) AND finishedOrder=0 AND startDate<=(?) order by endDate asc;";

        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);)
        {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.add(Calendar.DAY_OF_YEAR, +offset);
            Date date = cal.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            pst.setString(1, department);
            pst.setDate(2, sqlDate);
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                String orderNumber = rs.getString("orderNumber");
                Date endDate = rs.getDate("endDate");
                Date startDate = rs.getDate("startDate");
                OrderOverview overview = getOverview(orderNumber, department,startDate);
                int taskID = rs.getInt("taskID");
                String customerName = getCustomerName(orderNumber);
                BoardTask bTask = new BoardTask(orderNumber, endDate, startDate, overview, taskID, customerName);
                allTasks.add(bTask);
            }
            return allTasks;
        }
    }


    public OrderOverview getOverview(String orderNumber, String departmentName, Date startDate) throws SQLServerException, SQLException {
        boolean readyForWork = true;
        boolean foundThisDepartment=false;
        ArrayList<TaskStatus> allPriorTasks = new ArrayList<>();
        String sql = "select * from departmenttask where orderNumber=(?) order by startDate asc";
        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);)
        {
            pst.setString(1, orderNumber);
            ResultSet rs = pst.executeQuery();
            while (rs.next() && foundThisDepartment==false)
            {
                String dName = rs.getString("departmentName");
                boolean done = rs.getBoolean("finishedOrder");
                TaskStatus tStatus = new TaskStatus(dName, done);                
                allPriorTasks.add(tStatus);
                if(dName.equals(departmentName))
                {
                    foundThisDepartment=true;
                }
            }

        }
        int indexOfDepartment = 0; // may change
        for (int i = 0; i < allPriorTasks.size(); i++)
        {
            if (allPriorTasks.get(i).getDepartmentName().equals(departmentName))
            {
                indexOfDepartment = i;
            }
        }
        // check if all prior in the list isn't done with their task yet

        for (int i = 0; i < indexOfDepartment; i++)
        {
            if (allPriorTasks.get(i).getIsFinished() == false)
            {
                readyForWork = false;
            }
        }
        Date date = new Date();
        if(startDate.after(date))
        {
            readyForWork=false;
        }
        
        
        OrderOverview overview = new OrderOverview(allPriorTasks, readyForWork);
        return overview;
    }

    public void setCompleteTask(int taskID, String departmentName) throws SQLServerException, SQLException
    {
        Connection connection = null;

        try
        {
            connection = con.getConnection();
            connection.setAutoCommit(false);

            String finishOrder = "UPDATE DepartmentTask\n"
                    + "set finishedOrder =1\n"
                    + "where taskID = (?)";

            String log = "INSERT INTO Log VALUES (?,?,?,?);";

            PreparedStatement pstOrder = connection.prepareStatement(finishOrder);

            pstOrder.setInt(1, taskID);
            pstOrder.executeUpdate();
            connection.commit();

            PreparedStatement pstLog = connection.prepareStatement(log);
            
            Date date = new Date();
            java.sql.Date sqlDateLog = new java.sql.Date(date.getTime());

            pstLog.setDate(1, sqlDateLog);
            pstLog.setString(2, "Task Processed");
            pstLog.setString(3, "" + taskID);
            pstLog.setString(4, "" + departmentName);
            pstLog.executeUpdate();

            connection.commit();
        } catch (SQLException ex)
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

            }
        }
    }

    public String getCustomerName(String orderNumber) throws SQLServerException, SQLException
    {
        String sql = "Select customerName from ProductionOrder Where orderNumber = (?);";
        String name = "";
        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);)
        {
            
            pst.setString(1, orderNumber);
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                name = rs.getString("customerName");
                
            }
        }
        return name;
    }
    
    public ArrayList<Log> getAllLogs() throws SQLServerException, SQLException
    {
        ArrayList<Log> allLogs = new ArrayList<>();
        

        String sql = "SELECT * FROM Log";

        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);)
        {            
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                int logID = rs.getInt("logID");
                Date date = rs.getDate("activityDate");
                String activity = rs.getString("activity");
                String description = rs.getString("description");
                String departmentName = rs.getString("departmentName");
                Log log = new Log(logID,date,activity,description,departmentName);
                allLogs.add(log);
                
            }
        }
        return allLogs;
    }
}
