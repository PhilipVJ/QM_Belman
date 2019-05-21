/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;

public class FileDAO {

    private Worker wor;
    private static final String PATH = "DatabaseFiles";

    public DataContainer getDataFromJSON(String filepath) throws FileNotFoundException, IOException, ParseException {

        ArrayList<Worker> allWorkers = new ArrayList<>();
        ArrayList<ProductionOrder> allProductionOrders = new ArrayList<>();
        DataContainer dCon;

        Object obj = new JSONParser().parse(new FileReader(filepath));
        JSONObject jObj = (JSONObject) obj;
        // Get all AvailableWorkers
        JSONArray aWork = (JSONArray) jObj.get("AvailableWorkers");
        for (Object object : aWork) {
            JSONObject tObj = (JSONObject) object;
            String initials = (String) tObj.get("Initials");
            long salary = (long) tObj.get("SalaryNumber");
            String name = (String) tObj.get("Name");
            Worker worker = new Worker(salary, initials, name);
            allWorkers.add(worker);
        }
        // Get all ProductionOrders
        JSONArray pOrder = (JSONArray) jObj.get("ProductionOrders");

        for (Object object : pOrder) {
            JSONObject pObj = (JSONObject) object;
            JSONObject cObj = (JSONObject) pObj.get("Customer");
            String customerName = (String) cObj.get("Name");

            JSONObject dObj = (JSONObject) pObj.get("Delivery");
            String dDate = (String) dObj.get("DeliveryTime");
            Date deliveryTime = makeDateObject(dDate);

            // Get all department tasks
            JSONArray dTaskObj = (JSONArray) pObj.get("DepartmentTasks");
            ArrayList<DepartmentTask> allDepartmentTasks = new ArrayList<>();
            for (Object obj2 : dTaskObj) {
                JSONObject dTask = (JSONObject) obj2;
                JSONObject departmentObj = (JSONObject) dTask.get("Department");
                String departmentName = (String) departmentObj.get("Name");

                String endDate = (String) dTask.get("EndDate");
                Date endDateObj = makeDateObject(endDate);
                boolean finished = (boolean) dTask.get("FinishedOrder");
                String startDate = (String) dTask.get("StartDate");
                Date startDateObj = makeDateObject(startDate);
                DepartmentTask taskToAdd = new DepartmentTask(startDateObj, endDateObj, finished, departmentName);
                allDepartmentTasks.add(taskToAdd);
            }

            JSONObject order = (JSONObject) pObj.get("Order");
            String orderNumber = (String) order.get("OrderNumber");
            ProductionOrder productionOrder = new ProductionOrder(customerName, deliveryTime, orderNumber, allDepartmentTasks);
            allProductionOrders.add(productionOrder);
        }
        dCon = new DataContainer(allWorkers, allProductionOrders);
        return dCon;
    }

  

    private Date makeDateObject(String dDate) {
        int indexOfPlus = dDate.indexOf("+");
        String subString = dDate.substring(6, indexOfPlus);
        long sinceEpoch = Long.parseLong(subString);
        Date date = new Date(sinceEpoch);
        return date;
    }


    public File[] getAllFolderFiles() throws IOException {
        File folder = new File(PATH);
        File[] allFiles = folder.listFiles();
      
        return allFiles;
    }

    public DataContainer getDataFromCSV(String path) throws IOException, Exception {

        Reader reader = Files.newBufferedReader(Paths.get(path));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ArrayList<Worker> allWorkers = new ArrayList<>();
        ArrayList<ProductionOrder> allOrders = new ArrayList<>();
        ProductionOrder order = null;

        boolean skippedFirstLine = false;
        for (CSVRecord csvRecord : csvParser) {
            if (skippedFirstLine == false) {
                skippedFirstLine = true;
                continue;
            }
            // Worker is found
            if (!csvRecord.get(1).equals("")) {
                String initials = csvRecord.get(1);
                String name = csvRecord.get(2);
                long salaryNumber = Long.parseLong(csvRecord.get(3));
                Worker worker = new Worker(salaryNumber, initials, name);
                allWorkers.add(worker);
            }
            
            
            // Production order is found
            if (!csvRecord.get(04).equals("")) {
                if(order!=null)
                {
                    allOrders.add(order);
                    order=null;
                }
                String deliveryTime = csvRecord.get(8);
                String customerName = csvRecord.get(6);
                String orderNumber = csvRecord.get(16);
                Date date = csvStringToDate(deliveryTime);
                order=new ProductionOrder(date, orderNumber, customerName);
            }
            
            
            Date startDate = csvStringToDate(csvRecord.get(14));
            Date endDate = csvStringToDate(csvRecord.get(12));
            String departmentName = csvRecord.get(11);
            boolean finished = Boolean.valueOf(csvRecord.get(13));
            DepartmentTask task = new DepartmentTask(startDate, endDate, finished, departmentName);
            order.addTask(task);
        }
        allOrders.add(order);
        DataContainer con = new DataContainer(allWorkers, allOrders);
        return con;
    }

    private Date csvStringToDate(String deliveryTime) {
        int lastIndexOfSlash = deliveryTime.lastIndexOf("/");
        String subString = deliveryTime.substring(0, lastIndexOfSlash+5);
        String[] split = subString.split("/");
        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH,month-1);
        date.set(Calendar.YEAR, year);
        Date toReturn = date.getTime();
        return toReturn;
    }

}
