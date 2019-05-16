/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be.taskpainter;

import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.ImageContainer;

/**
 *
 * @author Philip
 */
public interface ITaskPainter {
    
ImageContainer getColor(BoardTask task);
    
}
