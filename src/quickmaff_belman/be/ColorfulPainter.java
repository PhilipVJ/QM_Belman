/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Philip
 */
public class ColorfulPainter implements ITaskPainter {

    private final Image gulPostIt = new Image("/quickmaff_belman/gui/view/images/postit_yellow.png");
    private final Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");
    private final Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");

    @Override
    public Image getColor(BoardTask task) {

        Date today = new Date();

        if (task.getStartDate().after(today)) {
            System.out.println("returning blue");
            return bluePostIt;
        } // If they are ready to start working on they will be made green                  
        else if (task.getReadyForWork() == true) {
            System.out.println("returning green");
           return greenPostIt;

        } // If the tasks start date is prior to today, but isn't ready to start work on yet
        // it will become a yellow post
        else {
            System.out.println("returning yellow");
           return gulPostIt;
                  
        }

    }

}
