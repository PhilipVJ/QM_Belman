/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.ArrayList;

public class DataContainer {

    private ArrayList<Worker> allWorkers;
    private ArrayList<ProductionOrder> allProductionOrders;

    public DataContainer(ArrayList<Worker> allWorkers, ArrayList<ProductionOrder> allProductionOrders) {
        this.allWorkers = allWorkers;
        this.allProductionOrders = allProductionOrders;
    }

    public ArrayList<Worker> getAllWorkers() {
        return allWorkers;
    }


    public ArrayList<ProductionOrder> getAllProductionOrders() {
        return allProductionOrders;
        
    }
    
   public void printData()
   {
       for (Worker worker : allWorkers) {
           System.out.println(""+worker.toString());
       }
       
       for (ProductionOrder pOrder : allProductionOrders) {
           System.out.println(""+pOrder.toString());
       }
   }



}
