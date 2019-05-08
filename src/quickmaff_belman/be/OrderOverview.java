/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.ArrayList;

/**
 *
 * @author Philip
 */
public class OrderOverview {
    ArrayList<TaskStatus> allTasks;
    private boolean readyToWork;

    public OrderOverview(ArrayList<TaskStatus> allTasks, boolean readyToWork) {
        this.allTasks = allTasks;
        this.readyToWork = readyToWork;
    }

    public ArrayList<TaskStatus> getAllTaskStatus() {
        return allTasks;
    }

    public boolean isReadyToWork() {
        return readyToWork;
    }
    
    
    
}
