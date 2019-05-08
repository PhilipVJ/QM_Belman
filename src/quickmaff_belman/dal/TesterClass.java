/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Philip
 */
public class TesterClass {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException 
    {
        DbConnection con = DbConnection.getInstance();
        OrderDAO od = new OrderDAO(con);
       
        
        System.out.println(od.getCustomerName("1000"));
        
        
    }
    
}
