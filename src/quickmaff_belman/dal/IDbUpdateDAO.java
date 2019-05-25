/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.sql.SQLException;
import quickmaff_belman.be.DataContainer;

/**
 *
 * @author Philip
 */
public interface IDbUpdateDAO {

    void addCorruptFilesToLog(int numberOfFiles, String department) throws SQLException;

    boolean checkForDuplicateFile(long hashCode) throws SQLException;

    void updateDatabaseWithFile(DataContainer container, String departmentName) throws SQLException;
    
}
