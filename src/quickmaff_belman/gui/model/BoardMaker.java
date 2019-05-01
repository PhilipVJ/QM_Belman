/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import quickmaff_belman.be.BoardTask;

/**
 *
 * @author Philip
 */
public class BoardMaker implements Runnable {

    private FlowPane fPane;
    private Model model;

    public BoardMaker(FlowPane fPane, Model model) {
        this.fPane = fPane;
        this.model = model;
    }

    @Override
    public void run() {

        while (true) {
            ArrayList<BoardTask> boardTasks;
            try {
                boardTasks = model.getAllBoardTasks();
                ArrayList<HBox> boxes = new ArrayList<>();

                Image daImage = new Image("/quickmaff_belman/gui/view/images/postit.png");

                for (BoardTask bTask : boardTasks) {

                    StackPane sPane = new StackPane();
                    ImageView view = new ImageView(daImage);
                    Label orderNumber = new Label(bTask.getOrderNumber());
                    orderNumber.setFont(new Font("Arial", 15));
                    Label endDate = new Label("\n\n" + bTask.getEndDate());
                    view.setPreserveRatio(true);
                    view.setFitWidth(160);

                    sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                        System.out.println("Opening task from order:" + orderNumber);
                    });

                    sPane.getChildren().addAll(view, orderNumber, endDate);
                    HBox box = new HBox(sPane);
                    box.setAlignment(Pos.CENTER);
                    boxes.add(box);
                }

                Platform.runLater(() -> {
                    fPane.getChildren().clear();
                    fPane.getChildren().addAll(boxes);
                    System.out.println("Updating tasks");
                });

            } catch (SQLException ex) {
                Logger.getLogger(BoardMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BoardMaker.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}