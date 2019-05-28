/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import quickmaff_belman.be.DataContainer;

/**
 *
 * @author Philip
 */
public class FileDAOTest {

    public FileDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getDataFromCSV method, of class FileDAO.
     */
    @Test
    public void testGetDataFromCSV() throws Exception {
        FileDAO fDAO = new FileDAO();
        DataContainer con = fDAO.getDataFromJSON("DatabaseFiles/database.txt");
        DataContainer con2 = fDAO.getDataFromCSV("DatabaseFiles/database.csv");
        assertEquals(con, con2);

    }

    /**
     * Test of getDataFromExcel method, of class FileDAO.
     */
    @Test
    public void testGetDataFromExcel() throws Exception {
        FileDAO fDAO = new FileDAO();
        DataContainer con = fDAO.getDataFromJSON("DatabaseFiles/database.txt");
        DataContainer con2 = fDAO.getDataFromExcel("DatabaseFiles/database.xlsx");
        assertEquals(con, con2);
    }

}
