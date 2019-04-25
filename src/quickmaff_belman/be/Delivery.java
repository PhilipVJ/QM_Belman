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
public class Delivery {
    private String type;
    private Date deliveryDate;

    public Delivery(String type, Date date) {
        this.type = type;
        this.deliveryDate = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return deliveryDate;
    }

    public void setDate(Date date) {
        this.deliveryDate = date;
    }

    @Override
    public String toString() {
        return "Delivery{" + "type=" + type + ", date=" + deliveryDate + '}';
    }
    
    
    
    
}
