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
    private Date startDate;

    public BoardTask(String orderNumber, Date endDate, Date startDate) {
        this.orderNumber = orderNumber;
        this.endDate = endDate;
        this.startDate = startDate;
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
    
    public Date getStartDate()
    {
        return startDate;
    }
    
    
    
}
