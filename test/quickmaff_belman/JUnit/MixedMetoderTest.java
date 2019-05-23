/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.JUnit;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niklas
 */
public class MixedMetoderTest
{
    
    public MixedMetoderTest()
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
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of dateConverter method, of class MixedMetoder.
     */
    @Test
    public void testDateConverter()
    {
        Date date = new Date(999999999L);
        String expResult = "[3:1]";
        String result = MixedMetoder.dateConverter(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDigitString method, of class MixedMetoder.
     */
    @Test
    public void testGetDigitString()
    {
        int digit = 4;
        MixedMetoder instance = new MixedMetoder();
        String expResult = "0" + digit;
        String result = instance.getDigitString(digit);
        assertEquals(expResult, result);
    }

    /**
     * Insert the date for today inside the String deliveryTime as month/day/year
     * 
     * Test of csvStringToDate method, of class MixedMetoder.
     * @throws java.text.ParseException
     */
    @Test
    public void testCsvStringToDate() throws ParseException
    {
        String deliveryTime = "05/23/2019";
        Date expResult = Calendar.getInstance().getTime();
        Date result = MixedMetoder.csvStringToDate(deliveryTime);
        assertEquals(expResult, result);
    }
    
}
