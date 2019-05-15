/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.Date;
import javafx.scene.image.Image;
import quickmaff_belman.gui.model.ImageContainer;
import quickmaff_belman.gui.model.PostItColor;

/**
 *
 * @author Philip
 */
public class ColorfulPainter implements ITaskPainter {

    private final Image yellowPostIt = new Image("/quickmaff_belman/gui/view/images/postit_yellow.png");
    private final Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");
    private final Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");

    @Override
    public ImageContainer getColor(BoardTask task) {

        Date today = new Date();

        if (task.getStartDate().after(today)) {
            ImageContainer container = new ImageContainer(bluePostIt, PostItColor.BLUE);
            return container;
        } // If they are ready to start working on they will be made green                  
        else if (task.getReadyForWork() == true) {
          ImageContainer container = new ImageContainer(greenPostIt, PostItColor.GREEN);
            return container;

        } // If the tasks start date is prior to today, but isn't ready to start work on yet
        // it will become a yellow post
        else {
            ImageContainer container = new ImageContainer(yellowPostIt, PostItColor.YELLOW);
           return container;
                  
        }

    }

}
