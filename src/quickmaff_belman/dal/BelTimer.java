/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.util.ArrayList;
import java.util.Random;
import quickmaff_belman.be.Worker;

/**
 * /**
 * This class works as a mockup class. All it does is provide fictional data
 * regarding the active workers on an order
 *
 * @author Philip
 */
class BelTimer
{

    private final Worker john = new Worker(1500, "JP", "John Poulsen");
    private final Worker michael = new Worker(1501, "MJ", "Michael Jensen");
    private final Worker kirsten = new Worker(1502, "KH", "Kirsten Hansen");
    private final Worker johanne = new Worker(1503, "JM", "Johanne Michaelsen");
    private final Worker preben = new Worker(1504, "PC", "Preben Christiansen");
    private ArrayList<Worker> allWorkers = new ArrayList<>();

    private Random random = new Random();

    public BelTimer()
    {
        allWorkers.add(john);
        allWorkers.add(michael);
        allWorkers.add(kirsten);
        allWorkers.add(johanne);
        allWorkers.add(preben);
    }

    public Worker getActiveWorker(String taskNumber)
    {
        if (taskNumber.contains("3"))
        {
            System.out.println("No active worker");
            return null;
        }
        int randomNumber = random.nextInt(5);
        System.out.println("found person");
        return allWorkers.get(randomNumber);
    }
}
