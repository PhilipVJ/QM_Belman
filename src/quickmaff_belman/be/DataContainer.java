/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.ArrayList;

public class DataContainer {

    private final ArrayList<Worker> allWorkers;
    private final ArrayList<ProductionOrder> allProductionOrders;

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
       allWorkers.forEach((worker) -> {
           System.out.println(""+worker.toString());
        });
       
       allProductionOrders.forEach((pOrder) -> {
           System.out.println(""+pOrder.toString());
        });
   }



}
