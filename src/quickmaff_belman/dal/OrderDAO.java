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
import java.util.Date;
import quickmaff_belman.be.BoardTask;

public class OrderDAO {

    private DbConnection con;

    public OrderDAO(DbConnection con) {
        this.con = con;
    }

    public ArrayList<BoardTask> getAllBoardTasks(String department) throws SQLServerException, SQLException {
        ArrayList<BoardTask> allTasks = new ArrayList<>();
        String sql = "select endDate,orderNumber from DepartmentTask \n"
                + "INNER JOIN OrderTask on DepartmentTask.taskID = OrderTask.taskID \n"
                + "where departmentName=(?) AND finishedOrder=0 order by endDate desc;";

        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);) {
            pst.setString(1, department);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String orderNumber = rs.getString("orderNumber");
                Date endDate = rs.getDate("endDate");

                BoardTask bTask = new BoardTask(orderNumber, endDate);
                allTasks.add(bTask);
            }
            return allTasks;
        }
    }

}
