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
public class Log{
    
    private final int logID;
    private final Date activityDate;
    private final String activity;
    private final String description;
    private final String departmentName;

    public Log(int logID, Date activityDate, String activity, String description, String departmentName) {
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

    public String getDescription() {
        return description;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    
}
