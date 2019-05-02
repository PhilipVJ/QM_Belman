/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Philip
 */
public class BoardTask {

    private String orderNumber;
    private Date endDate;
    private Date startDate;
    private boolean readyForWork;

    public BoardTask(String orderNumber, Date endDate, Date startDate, boolean readyForWork) {
        this.orderNumber = orderNumber;
        this.endDate = endDate;
        this.startDate = startDate;
        this.readyForWork = readyForWork;
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

}
