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
    private final boolean readyForWork;
    private int taskID;

    public BoardTask(String orderNumber, Date endDate, Date startDate, boolean readyForWork, int taskID) {
        this.orderNumber = orderNumber;
        this.endDate = endDate;
        this.startDate = startDate;
        this.readyForWork = readyForWork;
        this.taskID = taskID;
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
        return readyForWork;
    }

    public boolean passedEndDate() {
        Date today = new Date();
        if(today.after(endDate))
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "BoardTask{" + "orderNumber=" + orderNumber + '}';
    }
    
    
    
}
