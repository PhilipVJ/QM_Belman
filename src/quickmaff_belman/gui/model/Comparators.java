/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.Comparator;
import quickmaff_belman.be.BoardTask;

/**
 *
 * @author Caspe
 */
public class Comparators
{
    public Comparator<BoardTask> sortStartDate()
    {
        return (BoardTask bt1, BoardTask bt2) -> bt1.getStartDate().compareTo(bt2.getStartDate());  
    }
    
    public Comparator<BoardTask> sortEndDate()
    {
        return (BoardTask bt1, BoardTask bt2) -> bt1.getEndDate().compareTo(bt2.getEndDate());  
    }
}
