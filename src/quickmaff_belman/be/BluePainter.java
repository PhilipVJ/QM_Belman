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
public class BluePainter implements ITaskPainter {

    private final Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");

    @Override
    public Image getColor(BoardTask task) {
        
        Date today = new Date();
        if (task.getStartDate().after(today)) {
            return bluePostIt;
        }
        return null;
    }



}
