/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import quickmaff_belman.be.BoardTask;

/**
 *
 * @author Philip
 */
public class LabelMaker {

    public Label createCustomerLabel(BoardTask bTask, ResourceBundle bundle) {
        Label customerName = new Label(bundle.getString("CustomerName") + ": " + bTask.getCustomerName());
        customerName.setFont(new Font("Arial", 30));
        customerName.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        customerName.setPrefWidth(350);
        customerName.setTranslateY(-100);
        customerName.setTranslateX(-150);
        return customerName;
    }

    public Label createOrderLabel(BoardTask bTask, ResourceBundle bundle) {
        Label orderLabel = new Label(bundle.getString("order") + ": " + bTask.getOrderNumber());
        orderLabel.setFont(new Font("Arial", 30));
        orderLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        orderLabel.setPrefWidth(350);
        orderLabel.setTranslateY(-300);
        orderLabel.setTranslateX(-150);
        return orderLabel;
    }

    public Label createEndDateLabel(BoardTask bTask, ResourceBundle bundle) {
        Label endDateLabel = new Label(bundle.getString("endDate") + ": " + bTask.getEndDate());
        endDateLabel.setFont(new Font("Arial", 30));
        endDateLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        endDateLabel.setPrefWidth(350);
        endDateLabel.setTranslateY(-200);
        endDateLabel.setTranslateX(-150);
        return endDateLabel;
    }

    public Label makeEndLabel(ResourceBundle bundle) {
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

}