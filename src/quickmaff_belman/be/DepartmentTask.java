/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.Date;

public class DepartmentTask {

    private Date startDate;
    private Date endDate;
    private boolean finishedOrder;
    private String departmentName;
    private String orderNumber;

    public DepartmentTask(Date startDate, Date endDate, boolean finishedOrder, String departmentName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishedOrder = finishedOrder;
        this.departmentName = departmentName;
    }
    
     public DepartmentTask(String orderNumber,Date startDate, Date endDate, boolean finishedOrder, String departmentName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishedOrder = finishedOrder;
        this.departmentName = departmentName;
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "DepartmentTask{" + "startDate=" + startDate + ", endDate=" + endDate + ", finishedOrder=" + finishedOrder + ", departmentName=" + departmentName + '}';
    }
   
    public String getOrderNumber()
    {
        return orderNumber;
    }
    

    public Date getStartDate() {
        return startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public boolean isFinishedOrder() {
        return finishedOrder;
    }


    public String getDepartmentName() {
        return departmentName;
    }


    
    
}
