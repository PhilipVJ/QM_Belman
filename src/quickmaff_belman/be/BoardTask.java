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

    private final String orderNumber;
    private final Date endDate;
    private final Date startDate;
    private final int taskID;
    private final OrderOverview overview;
    private final String customerName;
    private Worker activeWorker;
    private double realProgress;

    public BoardTask(String orderNumber, Date endDate, Date startDate, OrderOverview overview, int taskID, String customerName) {
        this.orderNumber = orderNumber;
        this.endDate = endDate;
        this.startDate = startDate;
        this.overview = overview;
        this.taskID = taskID;
        this.customerName = customerName;
    }


    public int getTaskID() {
        return taskID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }


    public Date getEndDate() {
        return endDate;
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
    
    public String getShortenedCustomerName()
    {
        if(customerName.length()<=12)
        {
            return customerName;
        }
        else
        {
            String shortenedName=customerName.substring(0, 9)+"...";
            return shortenedName;
        }
    }
    public boolean passedEndDate() {
        Date today = new Date();
        if(today.after(endDate))
        {
            return true;
        }
        return false;
    }
    
    public void setRealProgress(double percentage)
    {
        this.realProgress=percentage;
    }
    public double getRealProgress()
    {
        return realProgress;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }

    @Override
    public String toString()
    {
        return orderNumber+"," + endDate + "," + startDate + "," + customerName;
    }

    public Worker getActiveWorker()
    {
        return activeWorker;
    }

    public void setActiveWorker(Worker activeWorker)
    {
        this.activeWorker = activeWorker;
    }
    
    


    
    
    
}
