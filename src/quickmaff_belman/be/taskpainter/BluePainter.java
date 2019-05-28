/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be.taskpainter;

import java.util.Date;
import javafx.scene.image.Image;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.ImageContainer;
import quickmaff_belman.gui.model.PostItColor;

/**
 *
 * @author Philip
 */
public class BluePainter implements ITaskPainter {

    private final Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");

    @Override
    public ImageContainer getColor(BoardTask task) {
        
        Date today = new Date();
        if (task.getStartDate().after(today)) {
            ImageContainer container = new ImageContainer(bluePostIt, PostItColor.BLUE);
            return container;
            
        }
        return null;
    }
}
