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
public class BoardTask {

    private final String orderNumber;
    private final Date endDate;
    private final Date startDate;
    private final int taskID;
    private final OrderOverview overview;
    private final String customerName;
    private ArrayList<Worker> activeWorkers;
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
    /**
     * If the customer name is more than 12 characters long it will be shortened
     * @return 
     */
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

    public ArrayList<Worker> getActiveWorkers()
    {
        return activeWorkers;
    }

    public void setActiveWorkers(ArrayList<Worker> activeWorkers)
    {
        this.activeWorkers = activeWorkers;
    }
    
    


    
    
    
}
