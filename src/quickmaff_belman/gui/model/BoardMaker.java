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
import quickmaff_belman.be.ITaskPainter;

/**
 *
 * @author Philip
 */
public class BoardMaker implements Runnable {

    private final FlowPane fPane;
    private final Model model;
    private Image tempImage = null;
    private final AnchorPane aPane;
    private ITaskPainter paintStrategy;

    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane, ITaskPainter strategy) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;
        this.paintStrategy = strategy;

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
                    view = new ImageView();
                    StackPane sPane = new StackPane();
                    tempImage = paintStrategy.getColor(bTask);
                    if(tempImage==null)
                    {
                        continue;
                    }
                    view.setImage(tempImage);
                    Label orderNumber = new Label(bTask.getOrderNumber());
                    orderNumber.setFont(new Font("Arial", 15));
                    Label endDate = new Label("\n\n" + bTask.getEndDate());

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
                        openedView.setImage(tempImage);
                        // Blurs everything which exists in the root Pane
                        ObservableList<Node> allNodes = aPane.getChildren();
                        BoxBlur blur = new BoxBlur();
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
                        
                        stackPane.getChildren().add(orderLabel);
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

}
