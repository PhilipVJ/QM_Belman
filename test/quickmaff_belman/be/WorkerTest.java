/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Caspe
 */
public class WorkerTest
{
    
    public WorkerTest()
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
     * Test of getSalaryNumber method, of class Worker.
     */
    @Test
    public void testGetSalaryNumber()
    {
        Worker w1 = new Worker(1L, "KN", "Knud Nielsen");
        Worker instance = w1;
        long expResult = 1L;
        long result = instance.getSalaryNumber();
        assertEquals(expResult, result);

    }

    /**
     * Test of getIntitials method, of class Worker.
     */
    @Test
    public void testGetIntitials()
    {
        
        Worker w2 = new Worker(2L, "PT", "Peter Tang");
        Worker instance = w2;
        String expResult = "PT";
        String result = instance.getIntitials();
        assertEquals(expResult, result);

    }

    /**
     * Test of getName method, of class Worker.
     */
    @Test
    public void testGetName()
    {
        Worker w3 = new Worker(3L, "SS", "Signe Sørensen");
        Worker instance = w3;
        String expResult = "Signe Sørensen";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

}
