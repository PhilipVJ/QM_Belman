/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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

    void insertWorkers(ArrayList<Worker> allWorkers) throws SQLException {
        String sql = "INSERT INTO Worker VALUES (?,?,?);";
        Connection connection = null;
        PreparedStatement pst;

        try {
            connection = con.getConnection();
            pst = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (Worker worker : allWorkers) {

                pst.setLong(1, worker.getSalaryNumber());
                pst.setString(2, worker.getName());
                pst.setString(3, worker.getIntitials());
                pst.executeUpdate();
            }
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
