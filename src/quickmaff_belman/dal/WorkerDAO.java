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
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;

/**
 *
 * @author Philip
 */
public class WorkerDAO {

    private DbConnection con;

    public WorkerDAO(DbConnection con) {
        this.con = con;
    }

    void insertWorkers(ArrayList<Worker> allWorkers, ArrayList<ProductionOrder> pOrder) throws SQLException {
        String sqlWorker = "INSERT INTO Worker VALUES (?,?,?);";
        String sqlOrder = "INSERT INTO ProductionOrder VALUES (?,?,?);";
        String sqlOrderTask = "INSERT INTO OrderTask VALUES (?,?);";
        String sqlDep = "INSERT INTO DepartmentTask VALUES (?,?,?,?);";
        
        PreparedStatement pstWorker;
        PreparedStatement pstOrder;
        PreparedStatement pstDep;
        PreparedStatement pstOrderTask;

        Connection connection = null;
        
        try {
            connection = con.getConnection();
            
            pstWorker = connection.prepareStatement(sqlWorker);
            pstOrder = connection.prepareStatement(sqlOrder);
            pstDep = connection.prepareStatement(sqlDep);
            pstOrderTask = connection.prepareStatement(sqlOrderTask);
            
            connection.setAutoCommit(false);
            
            for (Worker worker : allWorkers) {

                pstWorker.setLong(1, worker.getSalaryNumber());
                pstWorker.setString(2, worker.getName());
                pstWorker.setString(3, worker.getIntitials());
                pstWorker.addBatch();
            }
            
            pstWorker.executeBatch();
            
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
                    
                    ResultSet rst = pstDep.executeQuery();
                    
                    while (rst.next())
                    {
                        int key = rst.getInt("taskID");
                        pstOrderTask.setString(1, poOrder.getOrderNumber());
                        pstOrderTask.setInt(2, key);
                        
                        pstOrderTask.executeQuery();
                    }
                }
            }
            
            
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


}
