/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.Date;

/**
 *
 * @author Philip
 */
public class BoardTask {

    private String orderNumber;
    private Date endDate;
    private final Date startDate;
    private int taskID;
    private OrderOverview overview;
    private String customerName;

    public BoardTask(String orderNumber, Date endDate, Date startDate, OrderOverview overview, int taskID, String customerName) {
        this.orderNumber = orderNumber;
        this.endDate = endDate;
        this.startDate = startDate;
        this.overview = overview;
        this.taskID = taskID;
        this.customerName = customerName;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean getReadyForWork() {
        return overview.isReadyToWork();
    }

    public OrderOverview getOverview()
    {
        return overview;
    }
    public boolean passedEndDate() {
        Date today = new Date();
        if(today.after(endDate))
        {
            return true;
        }
        return false;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }

    @Override
    public String toString()
    {
        return "BoardTask{" + "orderNumber=" + orderNumber + '}';
    }
    
    
    
}
