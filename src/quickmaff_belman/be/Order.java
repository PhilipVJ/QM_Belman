/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

/**
 *
 * @author Philip
 */
public class Order
{
private String type;
private String orderNumber;

    public Order(String type, String orderNumber) {
        this.type = type;
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "Order{" + "type=" + type + ", orderNumber=" + orderNumber + '}';
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }



}
