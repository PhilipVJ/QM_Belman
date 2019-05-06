/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
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

    private final FlowPane fPane;
    private final Model model;
    private final Image gulPostIt = new Image("/quickmaff_belman/gui/view/images/postit_yellow.png");
    private final Image bluePostIt = new Image("/quickmaff_belman/gui/view/images/postit_blue.png");
    private final Image greenPostIt = new Image("/quickmaff_belman/gui/view/images/postit_green.png");
    private Image prevUsedImage = null;
    private final AnchorPane aPane;

    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;

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
                    Image prevImage = prevUsedImage;

                    view.setPreserveRatio(true);
                    view.setFitWidth(160);

                    sPane.getChildren().addAll(view, orderNumber, endDate);
                    // Adds a warning if the due date has passed
                    if (bTask.passedEndDate() == true) {
                        makeRedCirkel(sPane);
                    }

                    sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                        ImageView openedView = new ImageView();
                        Date today = new Date();
                        openedView.setImage(prevImage);
                        // Blurs everything which exists in the root Pane
                        ObservableList<Node> allNodes = aPane.getChildren();
                        BoxBlur blur = new BoxBlur();
                        blur.setWidth(25);
                        blur.setHeight(25);
                        for (Node child : allNodes) {
                            child.setEffect(blur);
                        }
                        // Makes a stackpane and adds it to the blurred root
                        StackPane stackPane = new StackPane(openedView);
                        stackPane.prefWidthProperty().bind(aPane.widthProperty());
                        stackPane.prefHeightProperty().bind(aPane.heightProperty());
                        Label orderLabel = new Label(model.getResourceBundle().getString("order") + ": " + bTask.getOrderNumber());
                        orderLabel.setFont(new Font("Arial", 50));
                        orderLabel.setTranslateY(-200);
                        Label endDateLabel = new Label(model.getResourceBundle().getString("endDate") + ": " + bTask.getEndDate());
                        endDateLabel.setFont(new Font("Arial", 50));
                        endDateLabel.setTranslateY(-100);
                        Button completeOrder = new Button(model.getResourceBundle().getString("completeOrder"));
                        completeOrder.setFont(new Font ("Ariel", 25));
                        completeOrder.setTranslateY(350);
                        completeOrder.setTranslateX(200);
                        
                        
                        stackPane.getChildren().addAll(orderLabel, endDateLabel, completeOrder);
                        stackPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, q ->{
                           if(q.getButton()==MouseButton.SECONDARY){
                             aPane.getChildren().remove(stackPane);
                             for (Node child : allNodes) {
                            child.setEffect(null);
                        }
                           }
                        
                        });
                        aPane.getChildren().addAll(stackPane);

                        System.out.println("Opening task from order:" + orderNumber);
                        
                    });
                    
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

    private void makeRedCirkel(StackPane sPane) {
        Circle warning = new Circle(50);
        warning.setStroke(Color.RED);
        warning.setFill(Color.TRANSPARENT);
        warning.setStrokeWidth(2);
        warning.setTranslateY(10);
        sPane.getChildren().add(warning);
    }

    private ImageView getPostItColour(BoardTask bTask) {

        Date today = new Date();
        ImageView view = new ImageView();
        // Makes the post it blue if the start day is sooner than the current day
        if (bTask.getStartDate().after(today)) {
            prevUsedImage = bluePostIt;
            view.setImage(bluePostIt);
            return view;
        } // If they are ready to start working on they will be made green                  
        else if (bTask.getReadyForWork() == true) {
            prevUsedImage = greenPostIt;
            view.setImage(greenPostIt);

        } // If the tasks start date is prior to today, but isn't ready to start work on yet
        // it will become a yellow post
        else {
            prevUsedImage = gulPostIt;
            view.setImage(gulPostIt);
        }
        return view;
    }

}
