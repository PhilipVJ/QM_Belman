/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.text.Font;

/**
 * This class is an extension of the BoardMaker. Only made to make the BoardMaker class more readable
 * 
 */
public class LabelMaker {

    private final Model model;

    public LabelMaker(Model model) {
        this.model = model;
    }

    public Label makeLabelForBigPostIt(String key, String text, int translateY) {
        Label label = new Label(model.getResourceBundle().getString(key) + ": " + text);
        label.setFont(new Font("Arial", 30));
        label.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        label.setPrefWidth(350);
        label.setTranslateY(translateY);
        label.setTranslateX(-150);
        return label;
    }

    public Label makeLabelForProgressBar(String key, int translateX) {
        Label label = new Label();
        label.setText(model.getResourceBundle().getString(key));
        label.setFont(new Font("Arial", 16));
        label.setTranslateX(translateX);
        label.setTranslateY(75);
        return label;
    }



    public Label getWarningTxtLabel() {
        Label lblWarning = new Label(model.getResourceBundle().getString("warning"));
        lblWarning.setFont(new Font("Arial", 20));
        lblWarning.setWrapText(true);
        lblWarning.setPrefHeight(100);
        lblWarning.setPrefWidth(350);
        lblWarning.setTranslateX(-5);
        lblWarning.setTranslateY(-30);
        lblWarning.setRotate(11);
        lblWarning.setBlendMode(BlendMode.MULTIPLY);
        lblWarning.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItLabel.png);");
        return lblWarning;
    }

    public Label getWarningHeaderLabel() {
        Label lblHeader = new Label(model.getResourceBundle().getString("warnHeader"));
        lblHeader.setFont(new Font("Arial", 50));
        lblHeader.setStyle("-fx-font-weight: bold");
        lblHeader.setTranslateX(30);
        lblHeader.setTranslateY(-160);
        lblHeader.setRotate(10);
        return lblHeader;
    }

}
