/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.JUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philip
 */
public class MixedMethodsTest {

    public MixedMethodsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of csvStringToDate method, of class MixedMetoder.
     */
    @Test
    public void testCsvStringToDate() throws ParseException {
        String deliveryTime = "05/25/2019";

        Calendar expected = Calendar.getInstance();
        expected.set(Calendar.MONTH, 4);
        expected.set(Calendar.YEAR, 2019);
        expected.set(Calendar.DAY_OF_MONTH, 25);
        expected.set(Calendar.HOUR_OF_DAY, 0);
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.SECOND, 0);
        expected.set(Calendar.MILLISECOND, 0);
        Date exp = expected.getTime();

        Date result = MixedMethods.csvStringToDate(deliveryTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(result);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
              cal.set(Calendar.MILLISECOND, 0);
        Date formattedResult = cal.getTime();
        assertEquals(exp, formattedResult);
    }

    /**
     * Test of dateConverter method, of class MixedMetoder.
     */
    @Test
    public void testDateConverter() throws ParseException {
        SimpleDateFormat day = new SimpleDateFormat("MM/dd/yyyy");
        Date date = day.parse("05/24/2019");
        String expResult = "[21:5]";
        String result = MixedMethods.dateConverter(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDigitString method, of class MixedMetoder.
     */
    @Test
    public void testGetDigitString() {
        int digit = 4;
        String expResult = "04";
        String result = MixedMethods.getDigitString(digit);
        assertEquals(expResult, result);
    }


    /**
     * Test of getFileExtension method, of class MixedMetoder.
     */
    @Test
    public void testGetFileExtension() {
        String filePath = "DatabaseFiles/Folders/file.jpg";
        String expected = "jpg";
        String result = MixedMethods.getFileExtension(filePath);
        assertEquals(expected, result);
    }

}
