/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.Filter;
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
    private final Image doneMark = new Image("/quickmaff_belman/gui/view/images/done.png");
    private final Image notDoneMark = new Image("/quickmaff_belman/gui/view/images/notdone.png");
    private final Image postItBorder = new Image("/quickmaff_belman/gui/view/images/postItBorder.png");
    private final Label display;
    private final LabelMaker labelMaker = new LabelMaker();
    private Filter filter;

    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane, ITaskPainter strategy, BooleanProperty isLoading, Label display, Filter filter) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;
        this.paintStrategy = strategy;
        this.isLoading = isLoading;
        this.display = display;
        this.filter=filter;

    }

    @Override
    public void run() {

        while (true) {
            ArrayList<BoardTask> boardTasks;
            try {
                if (roundCounter == 0) {
                    isLoading.set(true);
                }
                boardTasks = model.getAllBoardTasks();
                ArrayList<HBox> boxes = new ArrayList<>();
                ImageView view = null;
                for (BoardTask bTask : boardTasks) {
                    view = new ImageView();
                    StackPane sPane = new StackPane();
                    //Filters the BoardTask on searchword and worker filter
                    if(filter.validBoardTask(bTask)==false)
                    {
                        continue;
                    }
                    
                    Image color = paintStrategy.getColor(bTask);
                    // If the paintStrategy return null the board task shall not be made
                    if (color == null) {
                        continue;
                    }
                    view.setImage(color);

                    Label customerName = new Label(bTask.getShortenedCustomerName());
                    customerName.setTranslateY(-25);
                    customerName.setFont(new Font("Arial", 15));

                    Label orderNumber = new Label(bTask.getOrderNumber());
                    orderNumber.setFont(new Font("Arial", 17));
                    orderNumber.setTranslateY(-5);

                    Label endDate = new Label(bTask.getEndDate().toString());
                    endDate.setFont(new Font("Arial", 15));
                    endDate.setTranslateY(15);

                    view.setPreserveRatio(true);
                    view.setFitWidth(160);

                    sPane.getChildren().addAll(view, orderNumber, endDate, customerName);
                    // Adds a warning if the due date has passed
                    if (bTask.passedEndDate() == true) {
                        makeRedCirkel(sPane);
                    }
                    // Makes an eventhandler which makes the post-it big and shows additional information
                    sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ImageView openedView = new ImageView();
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
                            StackPane bigPostIt = new StackPane(openedView);
                            bigPostIt.prefWidthProperty().bind(aPane.widthProperty());
                            bigPostIt.prefHeightProperty().bind(aPane.heightProperty());

                            Label customerName = labelMaker.createCustomerLabel(bTask, model.getResourceBundle());
                            Label orderLabel = labelMaker.createOrderLabel(bTask, model.getResourceBundle());
                            Label endDateLabel = labelMaker.createEndDateLabel(bTask, model.getResourceBundle());
                            
                            Label activeWorker = null;
                            if(bTask.getActiveWorker()!=null)
                            {
                               activeWorker = labelMaker.createActiveWorkerLabel(bTask, model.getResourceBundle());
                            }

                            // Makes the area where you can see the other departments process
                            VBox vbox = makeDepartmentOverview(bTask);

                            ImageView overView = new ImageView();

                            overView.setImage(postItBorder);
                            overView.setTranslateX(200);
                            overView.setTranslateY(-200);
                            StackPane departmentArea = new StackPane();
                            departmentArea.setPrefHeight(250);
                            departmentArea.setPrefWidth(180);
                            departmentArea.getChildren().addAll(overView, vbox);

                            
                            Button completeTask = null;

                            if (bTask.getReadyForWork() == true) {
                                
                                completeTask = completeTaskButton(bTask, bigPostIt, aPane);
                                
                            }

                            bigPostIt.getChildren().addAll(orderLabel, endDateLabel, customerName, departmentArea);
                           
                            //Insert the progressbar into the post-it greens and yellows 
                            Date thisDate = new Date();
                            if (!bTask.getStartDate().after(thisDate) && !bTask.getReadyForWork() || bTask.getReadyForWork() == true) 
                            {
                                StackPane progressPane = makeProgressBar(bTask);
                                bigPostIt.getChildren().add(progressPane);
                            }
                            
                            // If a complete button has been made - it will be added
                            if (completeTask != null) {
                                
                                bigPostIt.getChildren().addAll(completeTask);                               
                            }
                            if(activeWorker!=null)
                            {
                                bigPostIt.getChildren().add(activeWorker);
                            }
                            // Go back to main view when right click is pressed
                            bigPostIt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, q
                                    -> {
                                if (q.getButton() == MouseButton.SECONDARY) {
                                    aPane.getChildren().remove(bigPostIt);
                                    for (Node child : allNodes) {
                                        child.setEffect(null);
                                    }
                                }
                            });
                            aPane.getChildren().addAll(bigPostIt);
                        }
                    });

                    HBox box = new HBox(sPane);
                    box.setAlignment(Pos.CENTER);
                    boxes.add(box);
                }

                Platform.runLater(()
                        -> {
                    fPane.getChildren().clear();
                    fPane.getChildren().addAll(boxes);
                });

                if (roundCounter == 0) {
                    isLoading.set(false);
                }
                roundCounter++;

            } catch (SQLException ex) {
                ExceptionHandler.handleException(ex, model.getResourceBundle());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                return;
            }

        }
    }

    private StackPane makeProgressBar(BoardTask bTask) {
        StackPane progressPane = new StackPane();
        progressPane.setTranslateX(1150);
        progressPane.setTranslateY(200);
        progressPane.setPrefHeight(250);
        progressPane.setPrefWidth(180);

        Label lblStart = labelMaker.makeStartLabel();

        Label lblSlut = labelMaker.makeEndLabel(model.getResourceBundle());
        lblSlut.setTranslateX(-1240);
        
        double percantage = getPercentageTimeLeft(bTask);
        ProgressBar pBar = new ProgressBar();
        pBar.setProgress(percantage);

        pBar.setPrefHeight(25);
        pBar.setPrefWidth(250);
        pBar.setTranslateX(-1350);
        pBar.setTranslateY(50);
        progressPane.getChildren().addAll(lblStart, lblSlut, pBar);
        return progressPane;
    }

    private double getPercentageTimeLeft(BoardTask bTask) {
        double startTime = bTask.getStartDate().getTime();
        Date endDate = bTask.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        double endTime = calendar.getTimeInMillis();
        double totalTime = endTime - startTime;
        double currentTime = System.currentTimeMillis();
        double timePassedSinceStart = currentTime - startTime;
        double percantage = (timePassedSinceStart / totalTime);
        return percantage;
    }

    private VBox makeDepartmentOverview(BoardTask bTask) {
        VBox vbox = new VBox();
        ArrayList<HBox> allBoxes = new ArrayList<>();
        ArrayList<TaskStatus> taskStatus = bTask.getOverview().getAllTaskStatus();
        for (TaskStatus status : taskStatus) {
            Label statusLabel = new Label();
            statusLabel.setFont(new Font("Ariel", 20));
            statusLabel.setText(status.getDepartmentName());
            ImageView view = new ImageView();
            if (status.getIsFinished()) {
                view.setImage(doneMark);
            } else {
                view.setImage(notDoneMark);
            }
            HBox box = new HBox();
            box.getChildren().addAll(statusLabel, view);
            allBoxes.add(box);
        }
        vbox.getChildren().addAll(allBoxes);
        vbox.setMaxHeight(180);
        vbox.setMaxWidth(180);
        vbox.fillWidthProperty().set(false);
        vbox.setTranslateX(220);
        vbox.setTranslateY(-220);
        return vbox;
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
        completeTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e
                -> {
            try {
                model.setCompleteTask(bTask.getTaskID());
                aPane.getChildren().remove(stackPane);
                for (Node child : allNodes) {
                    child.setEffect(null);
                }
                removeSmallTask(fPane, bTask.getOrderNumber());
                writeOnDisplay(model.getResourceBundle().getString("completeTask"));

            } catch (SQLException ex) {
                ExceptionHandler.handleException(ex, model.getResourceBundle());
            }
        });
        return completeTask;
    }

    private void makeRedCirkel(StackPane sPane) {
        Circle warning = new Circle(55);
        warning.setStroke(Color.RED);
        warning.setFill(Color.TRANSPARENT);
        warning.setStrokeWidth(2);
        warning.setTranslateY(-5);

 
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

    private void writeOnDisplay(String toWrite) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                display.setText(toWrite);
            }
        });
    }

}
