/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.dal;

import quickmaff_belman.be.Worker;

/**
 *
 * @author Philip
 */
public interface IBelTimer {

    Worker getActiveWorker(String orderNumber);
    
}
