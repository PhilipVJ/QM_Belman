/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.JUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import quickmaff_belman.be.DepartmentTask;

/**
 *
 * @author Bruger
 */
public class ProductionOrderTest
{
    
    public ProductionOrderTest()
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
     * Test of getCustomerName method, of class ProductionOrder.
     */
    @Test
    public void testGetCustomerName() throws ParseException
    {
        String dateToCheck = "31/06/2019";
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date delvTime = date.parse(dateToCheck);
        ProductionOrder instance = new ProductionOrder(delvTime, "555555", "Bilka");
        String expResult = "Bilka";
        String result = instance.getCustomerName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCustomerName method, of class ProductionOrder.
     */
    @Test
    public void testSetCustomerName() throws ParseException
    {
        String customerName = "Q8";
        String dateToCheck = "31/06/2019";
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date delvTime = date.parse(dateToCheck);
        ProductionOrder instance = new ProductionOrder(delvTime, "55555", customerName);
        instance.setCustomerName(customerName);
    }

    /**
     * Test of getDeliveryTime method, of class ProductionOrder.
     */
    @Test
    public void testGetDeliveryTime() throws ParseException
    {
        String dateToCheck = "31/06/2019";
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date delvTime = date.parse(dateToCheck);
        ProductionOrder instance = new ProductionOrder(delvTime, "55555", "ToyRus");
        Date expResult = date.parse(dateToCheck);
        Date result = instance.getDeliveryTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOrderNumber method, of class ProductionOrder.
     */
    @Test
    public void testGetOrderNumber()
    {
        System.out.println("getOrderNumber");
        ProductionOrder instance = null;
        String expResult = "";
        String result = instance.getOrderNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepartmentTasks method, of class ProductionOrder.
     */
    @Test
    public void testGetDepartmentTasks()
    {
        System.out.println("getDepartmentTasks");
        ProductionOrder instance = null;
        ArrayList<DepartmentTask> expResult = null;
        ArrayList<DepartmentTask> result = instance.getDepartmentTasks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addTask method, of class ProductionOrder.
     */
    @Test
    public void testAddTask()
    {
        System.out.println("addTask");
        DepartmentTask task = null;
        ProductionOrder instance = null;
        instance.addTask(task);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class ProductionOrder.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        ProductionOrder instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
