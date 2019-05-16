/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.text.Font;
import quickmaff_belman.be.BoardTask;

/**
 *
 * @author Philip
 */
public class LabelMaker {
    
    private ResourceBundle bundle;

    public LabelMaker(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    

    public Label createCustomerLabel(BoardTask bTask) {
        Label customerName = new Label(bundle.getString("CustomerName") + ": " + bTask.getCustomerName());
        customerName.setFont(new Font("Arial", 30));
        customerName.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        customerName.setPrefWidth(350);
        customerName.setTranslateY(-100);
        customerName.setTranslateX(-150);
        return customerName;
    }

    public Label createOrderLabel(BoardTask bTask) {
        Label orderLabel = new Label(bundle.getString("order") + ": " + bTask.getOrderNumber());
        orderLabel.setFont(new Font("Arial", 30));
        orderLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        orderLabel.setPrefWidth(350);
        orderLabel.setTranslateY(-300);
        orderLabel.setTranslateX(-150);
        return orderLabel;
    }

    public Label createEndDateLabel(BoardTask bTask) {
        Label endDateLabel = new Label(bundle.getString("endDate") + ": " + Utility.dateConverter(bTask.getEndDate()));
        endDateLabel.setFont(new Font("Arial", 30));
        endDateLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        endDateLabel.setPrefWidth(350);
        endDateLabel.setTranslateY(-200);
        endDateLabel.setTranslateX(-150);
        return endDateLabel;
    }

    public Label makeEndLabel() {
        Label lblSlut = new Label();
        lblSlut.setText(bundle.getString("end"));
        lblSlut.setFont(new Font("Arial", 16));
        lblSlut.setTranslateX(-1257);
        lblSlut.setTranslateY(75);
        return lblSlut;
    }

    public Label makeStartLabel() {
        Label lblStart = new Label();
        lblStart.setText("Start");
        lblStart.setFont(new Font("Arial", 16));
        lblStart.setTranslateX(-1458);
        lblStart.setTranslateY(75);
        return lblStart;
    }
    
    public Label makeWarningTxtLabel()
    {
        Label lblWarning = new Label(bundle.getString("warning"));
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
    
    public Label makeWarningHeader()
    {
        Label lblHeader = new Label(bundle.getString("warnHeader"));
        lblHeader.setFont(new Font("Arial", 50));
        lblHeader.setStyle("-fx-font-weight: bold");
        lblHeader.setTranslateX(30);
        lblHeader.setTranslateY(-160);
        lblHeader.setRotate(10);
        return lblHeader;
    }

    public Label createActiveWorkerLabel(BoardTask bTask) {
        Label activeWorker = new Label();
        activeWorker.setText(bundle.getString("activeWorker") + ": " + bTask.getActiveWorker().toString());
        activeWorker.setFont(new Font("Arial", 30));
        activeWorker.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        activeWorker.setPrefWidth(350);
        activeWorker.setTranslateY(0);
        activeWorker.setTranslateX(-150);
        return activeWorker;
    }

}
