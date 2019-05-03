/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

                ImageView view = null;

                for (BoardTask bTask : boardTasks) {

                    StackPane sPane = new StackPane();

                    view = getPostItColour(bTask);

                    Label orderNumber = new Label(bTask.getOrderNumber());
                    orderNumber.setFont(new Font("Arial", 15));
                    Label endDate = new Label("\n\n" + bTask.getEndDate());
   
                    view.setPreserveRatio(true);
                    view.setFitWidth(160);

                    sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                        System.out.println("Opening task from order:" + orderNumber);
                    });

                    sPane.getChildren().addAll(view, orderNumber, endDate);
                    if(bTask.passedEndDate()==true)
{
    Circle warning = new Circle(50);
    warning.setStroke(Color.RED);
    warning.setFill(Color.TRANSPARENT);
    warning.setStrokeWidth(2);
    warning.setTranslateY(10);
    sPane.getChildren().add(warning);
}
                    HBox box = new HBox(sPane);
                    box.setAlignment(Pos.CENTER);
                    boxes.add(box);
                }

                Platform.runLater(() -> {
                    fPane.getChildren().clear();
                    fPane.getChildren().addAll(boxes);
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

    private ImageView getPostItColour(BoardTask bTask) {
        Image gulPostIt = new Image("/quickmaff_belman/gui/view/images/postit.png");
        Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");
        Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");
        Image redPostIt = new Image("/quickmaff_belman/gui/view/images/postit_red.png");
    

        Date today = new Date();
        ImageView view = new ImageView();
        // Makes the post it blue if the start day is sooner than the current day
        if (bTask.getStartDate().after(today)) {
            view.setImage(bluePostIt);
            return view;
        }
        
        
        // If they are ready to start working on they will be made green                  
        if (bTask.getReadyForWork() == true) {
            view.setImage(greenPostIt);
        } // If the tasks start date is sooner than today, but isn't ready to start work on yet
        // it will become a yellow post
        else {
            view.setImage(gulPostIt);
        }

        return view;
    }
}
