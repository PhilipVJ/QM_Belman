/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.JUnit;

import quickmaff_belman.be.*;
import java.util.ArrayList;
import java.util.Date;

public class ProductionOrder {
    private String customerName;
    private final Date deliveryTime;
    private final String orderNumber;
    private ArrayList<DepartmentTask> dTasks;

    public ProductionOrder(String customerName, Date deliveryTime, String orderNumber, ArrayList<DepartmentTask> dTasks) {
        this.customerName = customerName;
        this.deliveryTime = deliveryTime;
        this.orderNumber = orderNumber;
        this.dTasks = dTasks;
    }

    public ProductionOrder(Date deliveryTime, String orderNumber, String customerName) {
        this.deliveryTime = deliveryTime;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        dTasks = new ArrayList<>();
    }
    
    

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    
    



    



    
    
    
    
    
}
