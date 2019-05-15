/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Date;

/**
 *
 * @author Anders
 */
public class Logs{
    
    private int logID;
    private Date activityDate;
    private String activity;
    private int description;
    private String departmentName;

    public Logs(int logID, Date activityDate, String activity, int description, String departmentName) {
        this.logID = logID;
        this.activityDate = activityDate;
        this.activity = activity;
        this.description = description;
        this.departmentName = departmentName;
    }

    public int getLogID() {
        return logID;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public String getActivity() {
        return activity;
    }

    public int getDescription() {
        return description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    

    
}
