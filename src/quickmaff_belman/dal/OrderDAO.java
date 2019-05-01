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

public class OrderDAO {

    private DbConnection con;

    public OrderDAO(DbConnection con) {
        this.con = con;
    }

    public ArrayList<BoardTask> getAllBoardTasks(String department) throws SQLServerException, SQLException {
        ArrayList<BoardTask> allTasks = new ArrayList<>();
        String sql = "SELECT startDate,endDate,orderNumber from DepartmentTask where departmentName=(?) AND finishedOrder=0 AND startDate>=(?) order by endDate asc;";

        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);            
            Date date = cal.getTime();
            
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
           
            pst.setString(1, department);
            pst.setDate(2, sqlDate);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String orderNumber = rs.getString("orderNumber");
                Date endDate = rs.getDate("endDate");
                BoardTask bTask = new BoardTask(orderNumber, endDate);
                allTasks.add(bTask);
            }
            System.out.println("Size"+allTasks.size());
            return allTasks;
        }
    }

}
