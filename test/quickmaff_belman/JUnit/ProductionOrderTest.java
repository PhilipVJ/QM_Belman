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
 * @author Niklas
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
}
