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
import java.util.ArrayList;
import java.util.Date;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;

/**
 *
 * @author Philip
 */
public class DbUpdateDAO {

    private final DbConnection con;

    public DbUpdateDAO(DbConnection con) {
        this.con = con;
    }

    public boolean checkForDuplicateFile(long hashCode) throws SQLException {
        String sqlGetInfo = "SELECT * FROM [Log] WHERE description = (?)";
       
        try (Connection connection = con.getConnection(); PreparedStatement pStatement = connection.prepareStatement(sqlGetInfo);) {    
            pStatement.setString(1, "FileCode: " + hashCode);
            ResultSet set = pStatement.executeQuery();     
            if (set.next()) {
                // Duplicate file found
                return true;
            }
        }
        return false;
    }

    public void updateDatabaseWithFile(DataContainer container, String departmentName) throws SQLException {
        String sqlWorker = "INSERT INTO Worker VALUES (?,?,?);";
        String sqlOrder = "INSERT INTO ProductionOrder VALUES (?,?,?);";
        String sqlDep = "INSERT INTO DepartmentTask VALUES (?,?,?,?,?);";
        String sqlLog = "INSERT INTO Log VALUES (?,?,?,?);";

        ArrayList<Worker> allWorkers = container.getAllWorkers();
        ArrayList<ProductionOrder> allOrders = container.getAllProductionOrders();

        PreparedStatement pstLog;
        PreparedStatement pstWorker;
        PreparedStatement pstOrder;
        PreparedStatement pstDep;

        Connection connection = null;

        try {
            connection = con.getConnection();

            pstWorker = connection.prepareStatement(sqlWorker);
            pstOrder = connection.prepareStatement(sqlOrder);
            pstDep = connection.prepareStatement(sqlDep);
            pstLog = connection.prepareStatement(sqlLog);

            connection.setAutoCommit(false);

            for (Worker worker : allWorkers) {

                pstWorker.setLong(1, worker.getSalaryNumber());
                pstWorker.setString(2, worker.getName());
                pstWorker.setString(3, worker.getIntitials());
                pstWorker.addBatch();
            }

            for (ProductionOrder poOrder : allOrders) {
                java.sql.Date sqlDateDel = new java.sql.Date(poOrder.getDeliveryTime().getTime());
                pstOrder.setString(1, poOrder.getOrderNumber());
                pstOrder.setString(2, poOrder.getCustomerName());
                pstOrder.setDate(3, sqlDateDel);
                pstOrder.addBatch();

                ArrayList<DepartmentTask> dTask = poOrder.getDepartmentTasks();
                for (DepartmentTask deTask : dTask) {
                    java.sql.Date sqlDateStart = new java.sql.Date(deTask.getStartDate().getTime());
                    java.sql.Date sqlDateEnd = new java.sql.Date(deTask.getEndDate().getTime());

                    pstDep.setString(1, deTask.getDepartmentName());
                    pstDep.setDate(2, sqlDateStart);
                    pstDep.setDate(3, sqlDateEnd);
                    pstDep.setBoolean(4, deTask.isFinishedOrder());
                    pstDep.setString(5, poOrder.getOrderNumber());
                    pstDep.addBatch();

                }
            }
            Date date = new Date();
            java.sql.Date sqlDateLog = new java.sql.Date(date.getTime());

            pstLog.setDate(1, sqlDateLog);
            pstLog.setString(2, "AddedNewDatabaseFile");
            pstLog.setString(3, "FileCode: " + container.hashCode());
            pstLog.setString(4, departmentName);

            pstWorker.executeBatch();
            pstLog.executeUpdate();
            pstOrder.executeBatch();
            pstDep.executeBatch();
            connection.commit();

        } catch (SQLException ex) {

            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
                
            }
        }

    }

    public void addCorruptFilesToLog(int numberOfFiles, String department) throws SQLException {
        String sqlLog = "INSERT INTO Log VALUES (?,?,?,?);";
        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sqlLog);) {
            Date date = new Date();
            java.sql.Date sqlDateLog = new java.sql.Date(date.getTime());
            pst.setDate(1, sqlDateLog);
            pst.setString(2, "CorruptFiles");
            if(numberOfFiles>1)
            {
            pst.setString(3, numberOfFiles+" corrupt files found");
            }
            else
            {
            pst.setString(3, numberOfFiles+" corrupt file found");
            }
            pst.setString(4, department);
            pst.execute();
        }
    }

}
