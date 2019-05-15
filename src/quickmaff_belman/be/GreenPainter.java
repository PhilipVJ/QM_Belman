/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import javafx.scene.image.Image;
import quickmaff_belman.gui.model.ImageContainer;
import quickmaff_belman.gui.model.PostItColor;

/**
 *
 * @author Philip
 */
public class GreenPainter implements ITaskPainter {

    private final Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");

    @Override
    public ImageContainer getColor(BoardTask task) {

        if (task.getReadyForWork() == true) {
            ImageContainer container = new ImageContainer(greenPostIt, PostItColor.GREEN);
           return container;
        }
        return null;

    }

}
