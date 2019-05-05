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
import java.util.ArrayList;
import java.util.Date;
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
    
    private static final String PATH = "JSON";

    public DataContainer getDataFromJSON(String filepath) throws FileNotFoundException, IOException, ParseException {
        
        ArrayList<Worker> allWorkers = new ArrayList<>();
        ArrayList<ProductionOrder> allProductionOrders = new ArrayList<>();

        Object obj=null;
        obj = new JSONParser().parse(new FileReader(filepath));
        
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
        DataContainer dCon = new DataContainer(allWorkers, allProductionOrders);
        return dCon;
    }

    private Date makeDateObject(String dDate) {
        int indexOfPlus = dDate.indexOf("+");
        String subString = dDate.substring(6, indexOfPlus);
        long sinceEpoch = Long.parseLong(subString);
        Date date = new Date(sinceEpoch);
        return date;
    }
    
    public ArrayList<FileWrapper> getAllFolderFiles() throws IOException
    {
        File folder = new File(PATH);
        File[] allFiles = folder.listFiles();
        ArrayList<FileWrapper> allWrappedFiles = new ArrayList<>();
        for (File file : allFiles) {
            FileWrapper fWrap = new FileWrapper(file);
            allWrappedFiles.add(fWrap);         
        }
        return allWrappedFiles;
    }

}
