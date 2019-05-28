/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DbConnection
{
    private static final String PROP_FILE = "data/database.info";
    private final SQLServerDataSource ds;
    private Connection connection;

    private DbConnection() throws IOException
    {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        ds = new SQLServerDataSource();
        ds.setServerName(databaseProperties.getProperty("Server"));
        ds.setDatabaseName(databaseProperties.getProperty("Database"));
        ds.setUser(databaseProperties.getProperty("User"));
        ds.setPassword(databaseProperties.getProperty("Password"));
    }
    
    public boolean makeConnection()
    {
        try {
            connection = ds.getConnection();
            return true;
        } catch (SQLServerException ex) {
            return false;
        }
    }

    public Connection getConnection() throws SQLServerException
    {    
        Connection con = null;
        if(connection!=null)
        {
            System.out.println("first");
            con=connection;
            connection=null;
            return con;
        }
        System.out.println("after");
        return ds.getConnection();
    }
    
    public static DbConnection getInstance() throws IOException
    {
        return new DbConnection();
    }
}