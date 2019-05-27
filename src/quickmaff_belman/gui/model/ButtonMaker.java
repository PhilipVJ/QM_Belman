/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.text.Font;

/**
 *
 * @author Philip
 */
public class ButtonMaker {
    
    private Model model;

    public ButtonMaker(Model model) {
        this.model = model;
    }
    
    

    public Button getCompleteTaskButton() {
        Button completeTask = new Button(model.getResourceBundle().getString("completeTask"));
        completeTask.setFont(new Font("Ariel", 25));
        completeTask.setTranslateY(250);
        completeTask.setTranslateX(150);
        completeTask.setPrefHeight(60);
        completeTask.setPrefWidth(250);
        completeTask.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItButton.png);");

        return completeTask;
    }

    public Button getCancelButton() {
        Button cancelBtn = new Button(model.getResourceBundle().getString("no"));
        cancelBtn.setFont(new Font("Ariel", 25));
        cancelBtn.setTranslateY(158);
        cancelBtn.setTranslateX(60);
        cancelBtn.setRotate(11);
        cancelBtn.setPrefHeight(60);
        cancelBtn.setPrefWidth(150);
        cancelBtn.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItButtonCancel.png);");
        return cancelBtn;
    }

    public Button getAcceptButton() {
        Button acceptBtn = new Button(model.getResourceBundle().getString("yes"));
        acceptBtn.setFont(new Font("Ariel", 25));
        acceptBtn.setTranslateY(110);
        acceptBtn.setTranslateX(-165);
        acceptBtn.setRotate(12);
        acceptBtn.setBlendMode(BlendMode.MULTIPLY);
        acceptBtn.setPrefHeight(60);
        acceptBtn.setPrefWidth(100);
        acceptBtn.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItButtonAccept.png);");
        return acceptBtn;
    }

}
