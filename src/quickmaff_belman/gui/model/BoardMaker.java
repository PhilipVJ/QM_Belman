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
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
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
import quickmaff_belman.be.TaskStatus;

/**
 *
 * @author Philip
 */
public class BoardMaker implements Runnable {

    private final FlowPane fPane;
    private final Model model;
    private final AnchorPane aPane;
    private final ITaskPainter paintStrategy;
    private final BooleanProperty isLoading;
    private int roundCounter = 0;
    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane, ITaskPainter strategy, BooleanProperty isLoading) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;
        this.paintStrategy = strategy;
        this.isLoading = isLoading;

    }

    @Override
    public void run() {

        while (true) {
            ArrayList<BoardTask> boardTasks;
            try {
                if(roundCounter==0){
                isLoading.set(true);
                }

                boardTasks = model.getAllBoardTasks();
                ArrayList<HBox> boxes = new ArrayList<>();
                ImageView view = null;

                for (BoardTask bTask : boardTasks) {
                    view = new ImageView();
                    StackPane sPane = new StackPane();
                    Image color = paintStrategy.getColor(bTask);

                    if (color == null) {
                        continue;
                    }

                    view.setImage(color);
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
                        openedView.setImage(color);
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

                        Label orderLabel = createOrderLabel(bTask);
                        
                        Label endDateLabel = createEndDateLabel(bTask);
                        
                        Label statusLabel = createAllStatusLabel(bTask);
                        
                        Button completeTask = completeTaskButton(bTask, stackPane, aPane);
                        
                        if(bTask.getStartDate().before(today)&& bTask.getReadyForWork() == true){
                            
                        stackPane.getChildren().add(completeTask);
                        
                        }

                        stackPane.getChildren().addAll(orderLabel, endDateLabel, statusLabel);
                        stackPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, q -> {
                            if (q.getButton() == MouseButton.SECONDARY) {
                                aPane.getChildren().remove(stackPane);
                                for (Node child : allNodes) {
                                    child.setEffect(null);
                                }
                            }

                        });

                        aPane.getChildren().addAll(stackPane);
                    });

                    HBox box = new HBox(sPane);
                    box.setAlignment(Pos.CENTER);
                    boxes.add(box);
                }

                Platform.runLater(() -> {
                    fPane.getChildren().clear();
                    fPane.getChildren().addAll(boxes);
                });
                if(roundCounter==0){
                isLoading.set(false);
                }
                roundCounter++;

            } catch (SQLException ex) {
                Logger.getLogger(BoardMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                return;
            }

        }
    }
    
//    private Label createCustomerLabel(BoardTask bTask)
//    {
//        Label customerLabel = new Label
//    }
    
    private Label createOrderLabel(BoardTask bTask) {
        Label orderLabel = new Label(model.getResourceBundle().getString("order") + ": " + bTask.getOrderNumber());
        orderLabel.setFont(new Font("Arial", 30));
        orderLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        orderLabel.setPrefWidth(300);
        orderLabel.setTranslateY(-300);
        orderLabel.setTranslateX(-150);
        return orderLabel;
    }

    private Label createEndDateLabel(BoardTask bTask) {
        Label endDateLabel = new Label(model.getResourceBundle().getString("endDate") + ": " + bTask.getEndDate());
        endDateLabel.setFont(new Font("Arial", 30));
        endDateLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        endDateLabel.setPrefWidth(300);
        endDateLabel.setTranslateY(-200);
        endDateLabel.setTranslateX(-150);
        return endDateLabel;
    }

    private Label createAllStatusLabel(BoardTask bTask) {
        // Make overview
        ArrayList<TaskStatus> allStatus = bTask.getOverview().getAllTaskStatus();
        Label statusLabel = new Label();
        String textStatus = "";
        for (TaskStatus status : allStatus) {
            textStatus+=status.getDepartmentName()+"  "+status.getIsFinished()+"\n";
        }
        statusLabel.setFont(new Font("Ariel",18));
        statusLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItBorder.png);");
        statusLabel.setPrefHeight(250);
        statusLabel.setPrefWidth(180);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setTranslateY(-250);
        statusLabel.setTranslateX(230);
        statusLabel.setText(textStatus);
        return statusLabel;
    }

    private Button completeTaskButton(BoardTask bTask, StackPane stackPane, AnchorPane aPane) {
        ObservableList<Node> allNodes = aPane.getChildren();
        Button completeTask = new Button(model.getResourceBundle().getString("completeTask"));
        completeTask.setFont(new Font("Ariel", 25));
        completeTask.setTranslateY(250);
        completeTask.setTranslateX(150);
        completeTask.setBlendMode(BlendMode.MULTIPLY);
        completeTask.setPrefHeight(60);
        completeTask.setPrefWidth(250);
        completeTask.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItButton.png);");
        completeTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    model.setCompleteTask(bTask.getTaskID());
                    aPane.getChildren().remove(stackPane);
                    for (Node child : allNodes) {
                        child.setEffect(null);
                    }
                    removeSmallTask(fPane, bTask.getOrderNumber());

                } catch (SQLException ex) {
                    ExceptionHandler.handleException(ex, model.getResourceBundle());
                }

        });
        return completeTask;
    }

    private void makeRedCirkel(StackPane sPane) {
        Circle warning = new Circle(50);
        warning.setStroke(Color.RED);
        warning.setFill(Color.TRANSPARENT);
        warning.setStrokeWidth(2);
        warning.setTranslateY(10);
        sPane.getChildren().add(warning);
    }

    private void removeSmallTask(FlowPane pane, String orderNumber) {
        ObservableList<Node> allBoxes = pane.getChildren();
        HBox toRemove = null;
        for (Node box : allBoxes) {
            HBox hBox = (HBox) box;
            StackPane sPane = (StackPane) hBox.getChildren().get(0);
            Label oNumber = (Label) sPane.getChildren().get(1);
            if (oNumber.getText().contains(orderNumber)) {
                toRemove = (HBox) box;
            }
        }
        pane.getChildren().remove(toRemove);
    }

}
