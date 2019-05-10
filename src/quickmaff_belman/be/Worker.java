/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

public class Worker {

    private final long salaryNumber;
    private final String intitials;
    private final String name;

    public Worker(long salaryNumber, String intitials, String name) {

        this.salaryNumber = salaryNumber;
        this.intitials = intitials;
        this.name = name;
    }

    public long getSalaryNumber() {
        return salaryNumber;
    }

    public String getIntitials() {
        return intitials;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "salaryNumber=" + salaryNumber + ", name=" + name +", intitials=" + intitials;
    }
}
