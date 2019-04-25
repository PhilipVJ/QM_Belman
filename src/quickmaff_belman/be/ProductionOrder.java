/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Philip
 */
public class ProductionOrder {
    private String type;
    private Customer customer;
    private Delivery delivery;
    private ArrayList<DepartmentTask> dTasks;
    private Order order;

    public ProductionOrder(String type, Customer customer, Delivery delivery, ArrayList<DepartmentTask> dTasks, Order order) {
        this.type = type;
        this.customer = customer;
        this.delivery = delivery;
        this.dTasks = dTasks;
        this.order = order;
    }

    @Override
    public String toString() {
        return "ProductionOrder{" + "type=" + type + ", customer=" + customer + ", delivery=" + delivery + ", dTasks=" + dTasks + ", order=" + order + '}';
    }
    
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public ArrayList<DepartmentTask> getdTasks() {
        return dTasks;
    }

    public void setdTasks(ArrayList<DepartmentTask> dTasks) {
        this.dTasks = dTasks;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    
    
    
    
}
