/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import quickmaff_belman.be.Customer;
import quickmaff_belman.be.DataContainer;
import quickmaff_belman.be.Delivery;
import quickmaff_belman.be.Department;
import quickmaff_belman.be.DepartmentTask;
import quickmaff_belman.be.Order;
import quickmaff_belman.be.ProductionOrder;
import quickmaff_belman.be.Worker;

/**
 *
 * @author Philip
 */
public class FileDAO {

    public DataContainer getDataFromJSON() throws FileNotFoundException, IOException, ParseException {
        ArrayList<Worker> allWorkers = new ArrayList<>();
        ArrayList<ProductionOrder> allProductionOrders = new ArrayList<>();

        Object obj = new JSONParser().parse(new FileReader("data/JSON.txt"));
        JSONObject jObj = (JSONObject) obj;
        // Get all AvailableWorkers
        JSONArray aWork = (JSONArray) jObj.get("AvailableWorkers");
        for (Object object : aWork) {
            JSONObject tObj = (JSONObject) object;
            String initials = (String) tObj.get("Initials");
            long salary = (long) tObj.get("SalaryNumber");
            String name = (String) tObj.get("Name");
            String type = (String) tObj.get("__type");
            Worker worker = new Worker(type, salary, initials, name);
            allWorkers.add(worker);
        }
        // Get all ProductionOrders
        JSONArray pOrder = (JSONArray) jObj.get("ProductionOrders");

        for (Object object : pOrder) {
            JSONObject pObj = (JSONObject) object;
            String type = (String) pObj.get("__type");
            JSONObject cObj = (JSONObject) pObj.get("Customer");
            String cName = (String) cObj.get("Name");
            String cType = (String) cObj.get("__type");
            Customer customer = new Customer(cType, cName);

            JSONObject dObj = (JSONObject) pObj.get("Delivery");
            String dType = (String) dObj.get("__type");
            String dDate = (String) dObj.get("DeliveryTime");
            Date date = makeDateObject(dDate);
            Delivery delivery = new Delivery(dType, date);
            // Get all department tasks
            JSONArray dTaskObj = (JSONArray) pObj.get("DepartmentTasks");
            ArrayList<DepartmentTask> allDepartmentTasks = new ArrayList<>();
            for (Object obj2 : dTaskObj) {
                JSONObject dTask = (JSONObject) obj2;
                String tTaskType = (String) dTask.get("__type");
                JSONObject departmentObj = (JSONObject) dTask.get("Department");
                String departmentType = (String) departmentObj.get("__type");
                String departmentName = (String) departmentObj.get("Name");
                Department department = new Department(departmentType, departmentName);
                String endDate = (String) dTask.get("EndDate");
                Date endDateObj = makeDateObject(endDate);
                boolean finished = (boolean) dTask.get("FinishedOrder");
                String startDate = (String) dTask.get("StartDate");
                Date startDateObj = makeDateObject(startDate);
                DepartmentTask taskToAdd = new DepartmentTask(tTaskType, department, startDateObj, endDateObj, finished);
                allDepartmentTasks.add(taskToAdd);
            }

            JSONObject order = (JSONObject) pObj.get("Order");
            String oType = (String) order.get("__type");
            String oNumber = (String) order.get("OrderNumber");
            Order orderObj = new Order(oType, oNumber);
            ProductionOrder productionOrder = new ProductionOrder(type, customer, delivery, allDepartmentTasks, orderObj);
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

}
