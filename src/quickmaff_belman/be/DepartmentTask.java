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
public class DepartmentTask {

    private Date startDate;
    private Date endDate;
    private boolean finishedOrder;
    private String departmentName;

    public DepartmentTask(Date startDate, Date endDate, boolean finishedOrder, String departmentName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishedOrder = finishedOrder;
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DepartmentTask{" + "startDate=" + startDate + ", endDate=" + endDate + ", finishedOrder=" + finishedOrder + ", departmentName=" + departmentName + '}';
    }
   
    

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isFinishedOrder() {
        return finishedOrder;
    }

    public void setFinishedOrder(boolean finishedOrder) {
        this.finishedOrder = finishedOrder;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    



    
    
}