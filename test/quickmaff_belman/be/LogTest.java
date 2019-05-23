/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Caspe
 */
public class LogTest
{
    
    public LogTest()
    {
        
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Test of getLogID method, of class Log.
     */
    @Test
    public void testGetLogID() throws ParseException
    {
        String dateToCheck = "31/06/2019";
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date activityDate = date.parse(dateToCheck);
        
        Log instance = new Log(1, activityDate, "Klippe", "description", "NonProfit");
        int expResult = 1;
        int result = instance.getLogID();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getActivityDate method, of class Log.
     */
    @Test
    public void testGetActivityDate() throws ParseException
    {
        String dateToCheck = "11/08/2019";
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date activityDate = date.parse(dateToCheck);
        Log instance = new Log(1, activityDate, "Klippe", "description", "NonProfit");
        
        Date expResult = activityDate;
        Date result = instance.getActivityDate();
        assertEquals(expResult, result);

    }


}
