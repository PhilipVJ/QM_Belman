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
public class Worker {


    private long salaryNumber;
    private String intitials;
    private String name;

    public Worker(long salaryNumber, String intitials, String name) {

        this.salaryNumber = salaryNumber;
        this.intitials = intitials;
        this.name = name;
    }


    public long getSalaryNumber() {
        return salaryNumber;
    }

    public void setSalaryNumber(long salaryNumber) {
        this.salaryNumber = salaryNumber;
    }

    public String getIntitials() {
        return intitials;
    }

    public void setIntitials(String intitials) {
        this.intitials = intitials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Worker{" + "salaryNumber=" + salaryNumber + ", intitials=" + intitials + ", name=" + name + '}';
    }


    
    

}
