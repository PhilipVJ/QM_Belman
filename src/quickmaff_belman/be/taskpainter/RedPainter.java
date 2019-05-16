/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be.taskpainter;

import javafx.scene.image.Image;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.ImageContainer;
import quickmaff_belman.gui.model.PostItColor;

/**
 *
 * @author Caspe
 */
public class RedPainter implements ITaskPainter {

    private final Image yellowPostIt = new Image("/quickmaff_belman/gui/view/images/postit_yellow.png");
    private final Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");

    @Override
    public ImageContainer getColor(BoardTask task) {
        // If the task hasn't passed its enddate - it shall not be made
        if (task.passedEndDate() == false) {
            return null;
        }
        ImageContainer container;
        
        if (task.getReadyForWork() == true) {
            container = new ImageContainer(greenPostIt, PostItColor.GREEN);
            return container;

        } else {
            container = new ImageContainer(yellowPostIt, PostItColor.YELLOW);
            return container;

        }
    }

}
