/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import java.util.ArrayList;
import java.util.Random;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.Worker;

/**
 * /**
 * This class works as a mockup class. All it does is provide fictional data
 * regarding the active workers on an order
 *
 * @author Philip
 */
public class BelTimer implements IBelTimer
{

    private final Worker john = new Worker(1500, "JP", "John Poulsen");
    private final Worker michael = new Worker(1501, "MJ", "Michael Jensen");
    private final Worker kirsten = new Worker(1502, "KH", "Kirsten Hansen");
    private final Worker johanne = new Worker(1503, "JM", "Johanne Michaelsen");
    private final Worker preben = new Worker(1504, "PC", "Preben Christiansen");
    private final Worker mogens = new Worker(1505, "MH", "Mogens Hansen");

    private final ArrayList<Worker> allWorkers = new ArrayList<>();

    private final Random random = new Random();

    public BelTimer()
    {
        allWorkers.add(john);
        allWorkers.add(michael);
        allWorkers.add(kirsten);
        allWorkers.add(johanne);
        allWorkers.add(preben);
        allWorkers.add(mogens);
    }

    @Override
    public ArrayList<Worker> getActiveWorkers(String orderNumber)
    {
        ArrayList<Worker> activeWorkers = new ArrayList<>();

        if (orderNumber.contains("3"))
        {
            return null;
        }
        int randomNumberOfWorkers = random.nextInt(3) + 1;

        for (int i = 0; i < randomNumberOfWorkers; i++)
        {
            boolean foundNewWorker = false;
            while (foundNewWorker == false)
            {
                Worker worker = allWorkers.get(random.nextInt(6));
                if(activeWorkers.contains(worker))
                {
                    continue;
                }
                else
                {
                   activeWorkers.add(worker);
                   foundNewWorker=true;
                }
            }

        }
        return activeWorkers;
    }


    @Override
    public double getRealProgress(BoardTask task)
    {

        if (task.getReadyForWork())
        {
            double percentage = random.nextDouble();
            return percentage;
        }
        return 0;
    }
}
