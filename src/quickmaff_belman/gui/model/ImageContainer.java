/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import javafx.scene.image.Image;

/**
 *
 * @author Philip
 */
public class ImageContainer {

    private final Image image;
    private final PostItColor color;

    public ImageContainer(Image image, PostItColor color) {
        this.image = image;
        this.color = color;
    }
    
    public Image getImage() {
        return image;
    }

    public PostItColor getColor() {
        return color;
    }
    

    
    
    

}
