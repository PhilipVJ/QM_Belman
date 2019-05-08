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
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    private Image doneMark;
    private Image notDoneMark;
    private Image postItBorder;

    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane, ITaskPainter strategy, BooleanProperty isLoading) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;
        this.paintStrategy = strategy;
        this.isLoading = isLoading;

        doneMark = new Image("/quickmaff_belman/gui/view/images/done.png");
        notDoneMark = new Image("/quickmaff_belman/gui/view/images/notdone.png");
        postItBorder = new Image("/quickmaff_belman/gui/view/images/postItBorder.png");

    }

    @Override
    public void run() {

        while (true)
        {
            ArrayList<BoardTask> boardTasks;
            try
            {
                if (roundCounter == 0)
                {
                    isLoading.set(true);
                }

                boardTasks = model.getAllBoardTasks();
                ArrayList<HBox> boxes = new ArrayList<>();
                ImageView view = null;

                for (BoardTask bTask : boardTasks)
                {
                    view = new ImageView();
                    StackPane sPane = new StackPane();
                    Image color = paintStrategy.getColor(bTask);

                    if (color == null)
                    {
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
                    if (bTask.passedEndDate() == true)
                    {
                        makeRedCirkel(sPane);
                    }

                    sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ImageView openedView = new ImageView();
                            Date today = new Date();
                            openedView.setImage(color);
                            // Blurs everything which exists in the root Pane
                            ObservableList<Node> allNodes = aPane.getChildren();
                            BoxBlur blur = new BoxBlur();
                            blur.setWidth(25);
                            blur.setHeight(25);
                            for (Node child : allNodes)
                            {
                                child.setEffect(blur);
                            }

                            // Makes a stackpane and adds it to the blurred root
                            StackPane stackPane = new StackPane(openedView);
                            stackPane.prefWidthProperty().bind(aPane.widthProperty());
                            stackPane.prefHeightProperty().bind(aPane.heightProperty());

                            Label customerName = createCustomerLabel(bTask);
                            Label orderLabel = createOrderLabel(bTask);
                            Label endDateLabel = createEndDateLabel(bTask);

                            VBox vbox = new VBox();
                            ArrayList<HBox> allBoxes = new ArrayList<>();

                            ArrayList<TaskStatus> taskStatus = bTask.getOverview().getAllTaskStatus();
                            for (TaskStatus status : taskStatus)
                            {
                                Label statusLabel = new Label();
                                statusLabel.setFont(new Font("Ariel", 20));
                                statusLabel.setText(status.getDepartmentName());
                                ImageView view = new ImageView();
                                if (status.getIsFinished())
                                {
                                    view.setImage(doneMark);
                                }
                                else
                                {
                                    view.setImage(notDoneMark);
                                }
                                HBox box = new HBox();
                                box.getChildren().addAll(statusLabel, view);
                                allBoxes.add(box);
                            }
                            vbox.getChildren().addAll(allBoxes);

                            ImageView postItBorderView = new ImageView();
                            postItBorderView.setImage(postItBorder);
                            postItBorderView.setTranslateX(-920);
                            postItBorderView.setTranslateY(-450);

                            StackPane stPane = new StackPane();
                            stPane.setTranslateX(1150);
                            stPane.setTranslateY(200);
                            stPane.setPrefHeight(250);
                            stPane.setPrefWidth(180);

                            //Progress bar start 
                            Label lblStart = new Label();
                            lblStart.setText("Start");
                            lblStart.setFont(new Font("Arial", 16));
                            Label lblSlut = new Label();
                            lblSlut.setText("Slut dato");
                            lblSlut.setFont(new Font("Arial", 16));
                            
                            double startDato = bTask.getStartDate().getTime();
                            double slutDato = bTask.getEndDate().getTime();

                            double estimeretTid = (slutDato - startDato) / (1000 * 60 * 60 * 24);
                            double dagsDato = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
                            double dageBrugte = dagsDato - (startDato / (1000 * 60 * 60 * 24));
                            double slut = dageBrugte / estimeretTid;

                            ProgressBar pBar = new ProgressBar();
                            pBar.setProgress(slut);

                            lblStart.setTranslateX(-1458);
                            lblStart.setTranslateY(75);
                            
                            lblSlut.setTranslateX(-1257);
                            lblSlut.setTranslateY(75);
                            
                            pBar.setPrefHeight(25);
                            pBar.setPrefWidth(250);
                            pBar.setTranslateX(-1350);
                            pBar.setTranslateY(50);
                            //Progress bar slut

                            stPane.getChildren().addAll(lblStart, lblSlut, pBar, postItBorderView, vbox);

                            Button completeTask = completeTaskButton(bTask, stackPane, aPane);

                            if (bTask.getReadyForWork() == true)
                            {
                                stackPane.getChildren().add(completeTask);
                            }

                            stackPane.getChildren().addAll(orderLabel, endDateLabel, customerName, stPane);
                            stackPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, q ->
                            {
                                if (q.getButton() == MouseButton.SECONDARY)
                                {
                                    aPane.getChildren().remove(stackPane);
                                    for (Node child : allNodes)
                                    {
                                        child.setEffect(null);
                                    }
                                }

                            });

                            aPane.getChildren().addAll(stackPane);
                        }

                    });

                    HBox box = new HBox(sPane);
                    box.setAlignment(Pos.CENTER);
                    boxes.add(box);
                }

                Platform.runLater(() ->
                {
                    fPane.getChildren().clear();
                    fPane.getChildren().addAll(boxes);
                });
                if (roundCounter == 0)
                {
                    isLoading.set(false);
                }
                roundCounter++;

            }
            catch (SQLException ex)
            {
                Logger.getLogger(BoardMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
                return;
            }

        }
    }

    private Label createCustomerLabel(BoardTask bTask) {
        Label customerName = new Label(model.getResourceBundle().getString("CustomerName") + ": " + bTask.getCustomerName());
        customerName.setFont(new Font("Arial", 30));
        customerName.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        customerName.setPrefWidth(350);
        customerName.setTranslateY(-100);
        customerName.setTranslateX(-150);
        return customerName;
    }

    private Label createOrderLabel(BoardTask bTask) {
        Label orderLabel = new Label(model.getResourceBundle().getString("order") + ": " + bTask.getOrderNumber());
        orderLabel.setFont(new Font("Arial", 30));
        orderLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        orderLabel.setPrefWidth(350);
        orderLabel.setTranslateY(-300);
        orderLabel.setTranslateX(-150);
        return orderLabel;
    }

    private Label createEndDateLabel(BoardTask bTask) {
        Label endDateLabel = new Label(model.getResourceBundle().getString("endDate") + ": " + bTask.getEndDate());
        endDateLabel.setFont(new Font("Arial", 30));
        endDateLabel.setStyle("-fx-background-image: url(/quickmaff_belman/gui/view/images/postItUnderline.png);");
        endDateLabel.setPrefWidth(350);
        endDateLabel.setTranslateY(-200);
        endDateLabel.setTranslateX(-150);
        return endDateLabel;
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
        completeTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->
        {
            try
            {
                model.setCompleteTask(bTask.getTaskID());
                aPane.getChildren().remove(stackPane);
                for (Node child : allNodes)
                {
                    child.setEffect(null);
                }
                removeSmallTask(fPane, bTask.getOrderNumber());

            }
            catch (SQLException ex)
            {
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
        warning.setTranslateY(-5);
        warning.setTranslateX(-5);
        sPane.getChildren().add(warning);
    }

    private void removeSmallTask(FlowPane pane, String orderNumber) {
        ObservableList<Node> allBoxes = pane.getChildren();
        HBox toRemove = null;
        for (Node box : allBoxes)
        {
            HBox hBox = (HBox) box;
            StackPane sPane = (StackPane) hBox.getChildren().get(0);
            Label oNumber = (Label) sPane.getChildren().get(1);
            if (oNumber.getText().contains(orderNumber))
            {
                toRemove = (HBox) box;
            }
        }
        pane.getChildren().remove(toRemove);
    }

}
