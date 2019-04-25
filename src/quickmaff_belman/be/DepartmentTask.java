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
    private String type;
    private Department department;
    private Date startDate;
    private Date endDate;
    private boolean finishedOrder;

    public DepartmentTask(String type, Department department, Date startDate, Date endDate, boolean finishedOrder) {
        this.type = type;
        this.department = department;
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishedOrder = finishedOrder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    @Override
    public String toString() {
        return "DepartmentTask{" + "type=" + type + ", department=" + department + ", startDate=" + startDate + ", endDate=" + endDate + ", finishedOrder=" + finishedOrder + '}';
    }

    
    
}
