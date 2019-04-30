/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.DepartmentTask;

/**
 *
 * @author Anders
 */
public class Test extends Application{
    private DbConnection con;

    public Test(DbConnection con) {
        this.con = con;
    }
    public void main(String[] args) throws SQLException, SQLServerException, IOException  {
        departmentToFile();

    }
    public  void departmentToFile() throws SQLServerException, SQLException, IOException{
        FileWriter fileWriter = new FileWriter(new File("/QM_Belman/data/Confiq.txt"));
        PrintWriter printWriter = new PrintWriter(fileWriter);
        ArrayList<DepartmentTask> allNames = new ArrayList<>();
        String sql = "select departmentName from DepartmentTask";

        
        try (Connection connection = con.getConnection(); PreparedStatement pst = connection.prepareStatement(sql);) {
            
            ResultSet rs = pst.executeQuery();
            rs.getString("departmentName");
            while (rs.next()) {
                
                    String departmentName = rs.getString("departmentName");
                    if(!allNames.contains(departmentName)){
                        printWriter.print(departmentName.toString());
                }
                
            }
            
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    
        
    }
}
