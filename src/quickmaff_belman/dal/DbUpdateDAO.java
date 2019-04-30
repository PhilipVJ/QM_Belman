/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;
import quickmaff_belman.gui.model.ExceptionHandler;

/**
 *
 * @author Philip
 */
public class DbUpdateDAO
{

    private DbConnection con;

    public DbUpdateDAO(DbConnection con)
    {
        this.con = con;
    }

    public boolean checkForDuplicateFile(FileWrapper fW) throws SQLException
    {
        String sqlGetInfo = "SELECT * FROM Log WHERE description = (?)";
                
        try(Connection connection = con.getConnection(); PreparedStatement pstGetInfo = connection.prepareStatement(sqlGetInfo);)
        {
            ResultSet rs = pstGetInfo.executeQuery();
            
            if (rs.next())
            {
                return true;
            }
        } return false;
    }

    public void updateDatabaseWithJSON(ArrayList<Worker> allWorkers, ArrayList<ProductionOrder> pOrder, FileWrapper fileW) throws SQLException
    {
        String sqlWorker = "INSERT INTO Worker VALUES (?,?,?);";
        String sqlOrder = "INSERT INTO ProductionOrder VALUES (?,?,?);";
        String sqlDep = "INSERT INTO DepartmentTask VALUES (?,?,?,?);";
        String sqlOrderTask = "INSERT INTO OrderTask VALUES (?,?);";
        String sqlLog = "INSERT INTO Log VALUES (?,?,?,?);";

        PreparedStatement pstLog;
        PreparedStatement pstWorker;
        PreparedStatement pstOrder;
        PreparedStatement pstDep;
        PreparedStatement pstOrderTask;

        Connection connection = null;

        try
        {
            connection = con.getConnection();
            
            pstWorker = connection.prepareStatement(sqlWorker);
            pstOrder = connection.prepareStatement(sqlOrder);
            pstDep = connection.prepareStatement(sqlDep, Statement.RETURN_GENERATED_KEYS);
            pstOrderTask = connection.prepareStatement(sqlOrderTask);
            pstLog = connection.prepareStatement(sqlLog);
            
            connection.setAutoCommit(false);

            for (Worker worker : allWorkers)
            {

                pstWorker.setLong(1, worker.getSalaryNumber());
                pstWorker.setString(2, worker.getName());
                pstWorker.setString(3, worker.getIntitials());
                pstWorker.addBatch();
            }

            pstWorker.executeBatch();

            // alt over virker
            for (ProductionOrder poOrder : pOrder)
            {
                java.sql.Date sqlDateDel = new java.sql.Date(poOrder.getDeliveryTime().getTime());
                pstOrder.setString(1, poOrder.getOrderNumber());
                pstOrder.setString(2, poOrder.getCustomerName());
                pstOrder.setDate(3, sqlDateDel);
                pstOrder.addBatch();

                ArrayList<DepartmentTask> dTask = poOrder.getDepartmentTasks();
                for (DepartmentTask deTask : dTask)
                {
                    java.sql.Date sqlDateStart = new java.sql.Date(deTask.getStartDate().getTime());
                    java.sql.Date sqlDateEnd = new java.sql.Date(deTask.getEndDate().getTime());

                    pstDep.setString(1, deTask.getDepartmentName());
                    pstDep.setDate(2, sqlDateStart);
                    pstDep.setDate(3, sqlDateEnd);
                    pstDep.setBoolean(4, deTask.isFinishedOrder());
                    pstDep.executeUpdate();
                    ResultSet set = pstDep.getGeneratedKeys();
                    while (set.next())
                    {
                        int genKey = set.getInt(1);
                        pstOrderTask.setString(1, poOrder.getOrderNumber());
                        pstOrderTask.setInt(2, genKey);
                        pstOrderTask.addBatch();
                    }
                }
                Date date = new Date();
                java.sql.Date sqlDateLog = new java.sql.Date(date.getTime());
                
                pstLog.setDate(1, sqlDateLog);
                pstLog.setString(2, "updateActivity");
                pstLog.setString(3, "" + fileW.hashCode());
                pstLog.setString(4, null);
                pstLog.executeUpdate();
            }
            
            pstOrder.executeBatch();
            pstOrderTask.executeBatch();
            connection.commit();

        } catch (SQLException ex)
        {
            ExceptionHandler.handleException(ex);
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

}
