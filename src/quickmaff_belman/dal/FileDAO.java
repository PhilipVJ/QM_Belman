/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.FileWrapper;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;

public class FileDAO {
    
    private Worker wor;
    private static final String PATH = "JSON";

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
                DepartmentTask taskToAdd = new DepartmentTask(startDateObj, endDateObj, finished,departmentName);
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

    public DataContainer readCSVFile(String path) throws FileNotFoundException, IOException, java.text.ParseException
    {
        System.out.println("reading csv file.");
        ArrayList<Worker> allWorkers = new ArrayList<>();
        ArrayList<ProductionOrder> allProductionOrders = new ArrayList<>();
        ArrayList<DepartmentTask> allDepartmentTasks = new ArrayList<>();
        DataContainer dCon;
        
        String line = "";
        String split = ",";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            while ((line = br.readLine()) != null)
            {
                System.out.println("inside whileloop");
                
                String[] fileInfo = line.replace("\"", "").split(split);

                if (!fileInfo[1].equals(line))
                {
                    System.out.println("salary number: " + fileInfo[3]);
                    System.out.println("initialer: " + fileInfo[1]);
                    System.out.println("Name: " + fileInfo[2]);
                    
                    Worker work = new Worker(Long.parseLong(fileInfo[3]), fileInfo[1], fileInfo[2]);
                    allWorkers.add(work);
                    System.out.println("Worker er lavet nu");
                }
                    
                if (!fileInfo[14].equals(line))
                {
                    System.out.println("deliveryTime: " + fileInfo[8]);
                    System.out.println("startDate: " + fileInfo[14]);
                    System.out.println("endDate: " + fileInfo[12]);
                    System.out.println("departmentName: " + fileInfo[11]);
                    System.out.println("finish order true/false: " + fileInfo[13]);
                    System.out.println("CostumerName :" + fileInfo[6]);
                    System.out.println("orderNumber :" + fileInfo[16]);
                    
                    Date delvTime = dateForCSV(fileInfo[8]);
                    Date startDate = dateForCSV(fileInfo[14]);
                    Date endDate = dateForCSV(fileInfo[12]);

                    System.out.println("delvTime efter string til date object: " + delvTime);
                    System.out.println("startDate efter string til date object: " + startDate);
                    System.out.println("endDate efter string til date object: " + endDate);
                    
                    DepartmentTask taskToAdd = new DepartmentTask(startDate, endDate, Boolean.parseBoolean(fileInfo[13]), fileInfo[11]);
                    allDepartmentTasks.add(taskToAdd);

                    ProductionOrder procOrder = new ProductionOrder(fileInfo[6], delvTime, fileInfo[16], allDepartmentTasks);
                    allProductionOrders.add(procOrder);
                    System.out.println("færdig med order og task");
                    System.out.println("");
                    System.out.println("");
                }
                }
                
            System.out.println("test2");
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
    
    private Date dateForCSV(String date) throws java.text.ParseException
    {
        String[] split1 = date.split(" ");
        String[] split2 = split1[0].split("/|\\-");
        Long year = Long.parseLong(split2[2]);
        Long month = Long.parseLong(split2[1]);
        Long day = Long.parseLong(split2[0]);
        Long dato = day + month + year;
//        Date test = new Date(dato);
        
        return Date.from(Instant.ofEpochSecond(dato));
    }
    
    public ArrayList<FileWrapper> getAllFolderFiles() throws IOException
    {
        File folder = new File(PATH);
        File[] allFiles = folder.listFiles();
        ArrayList<FileWrapper> allWrappedFiles = new ArrayList<>();
        if(allFiles!=null){
        for (File file : allFiles) {
            FileWrapper fWrap = new FileWrapper(file);
            allWrappedFiles.add(fWrap);         
        }
        }
        return allWrappedFiles;
    }
    
}
