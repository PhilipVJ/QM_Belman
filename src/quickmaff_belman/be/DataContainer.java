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
    private long containerID;

    public DataContainer(ArrayList<Worker> allWorkers, ArrayList<ProductionOrder> allProductionOrders) {
        this.allWorkers = allWorkers;
        this.allProductionOrders = allProductionOrders;
        setMetaData();
    }

    public ArrayList<Worker> getAllWorkers() {
        return allWorkers;
    }

    public ArrayList<ProductionOrder> getAllProductionOrders() {
        return allProductionOrders;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (this.containerID ^ (this.containerID >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataContainer other = (DataContainer) obj;
        return true;
    }

    public void printData() {
        allWorkers.forEach((worker) -> {
            System.out.println("" + worker.toString());
        });

        allProductionOrders.forEach((pOrder) -> {
            System.out.println("" + pOrder.getOrderNumber());
        });

    }

    private void setMetaData() {
        // Set number of characters 
        int counter = 0;
        for (Worker worker : allWorkers) {
            counter += worker.toString().length();
        }
        for (ProductionOrder order : allProductionOrders) {
            counter += order.toString().length();
        }
        containerID = counter;
    }

}
