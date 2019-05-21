/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.model;

import quickmaff_belman.be.ImageContainer;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.be.Filter;
import quickmaff_belman.be.taskpainter.ITaskPainter;
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
    private final LabelMaker labelMaker;
    private final Filter filter;
    private final ButtonMaker bMaker;
    private final Image pic = new Image("/quickmaff_belman/gui/view/images/postit_red.png");
    private final BooleanProperty connectionLost;

    private HBox toRemove = null;
    private String lastRemoved = null;

    public BoardMaker(FlowPane fPane, Model model, AnchorPane aPane, ITaskPainter strategy, BooleanProperty isLoading, Label display, Filter filter, BooleanProperty connectionLost) {
        this.fPane = fPane;
        this.model = model;
        this.aPane = aPane;
        this.paintStrategy = strategy;
        this.isLoading = isLoading;
        this.display = display;
        this.filter = filter;
        this.connectionLost = connectionLost;

        bMaker = new ButtonMaker(model);
        labelMaker = new LabelMaker(model);

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
                    if (filter.validBoardTask(bTask) == false) {
                        continue;
                    }
                    ImageContainer con = paintStrategy.getColor(bTask);
                    // If the paintStrategy returns null the board task shall not be made
                    if (con == null) {
                        continue;
                    }
                    view.setImage(con.getImage());

                    Label customerName = new Label(bTask.getShortenedCustomerName());
                    customerName.setTranslateY(-25);
                    customerName.setFont(new Font("Arial", 15));

                    Label orderNumber = new Label(bTask.getOrderNumber());
                    orderNumber.setFont(new Font("Arial", 17));
                    orderNumber.setTranslateY(-5);

                    Label endDate = new Label(Utility.dateConverter(bTask.getEndDate()));
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
                            openedView.setImage(con.getImage());
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

                            Label customerName = labelMaker.makeLabelForBigPostIt("customerName",bTask.getCustomerName(),-100);
                            Label orderLabel = labelMaker.makeLabelForBigPostIt("order",bTask.getOrderNumber(),-300);
                            Label endDateLabel = labelMaker.makeLabelForBigPostIt("endDate",Utility.dateConverter(bTask.getEndDate()),-200);

                            Label activeWorker = null;
                            if (bTask.getActiveWorker() != null) {
                                activeWorker = labelMaker.makeLabelForBigPostIt("activeWorker",bTask.getActiveWorker().toString(),0);
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

                            if (con.getColor() == PostItColor.GREEN) {
                                completeTask = bMaker.getCompleteTaskButton();
                                completeTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, k
                                        -> {
                                    StackPane popUp = popUp(bTask, bigPostIt);
                                    bigPostIt.getChildren().add(popUp);
                                });

                            }

                            bigPostIt.getChildren().addAll(orderLabel, endDateLabel, customerName, departmentArea);

                            //Insert the progressbar into the post-it greens and yellows 
                            if (con.getColor() != PostItColor.BLUE) {
                                StackPane progressPane = makeProgressBar(bTask);
                                bigPostIt.getChildren().add(progressPane);
                            }

                            // If a complete button has been made - it will be added
                            if (completeTask != null) {
                                bigPostIt.getChildren().addAll(completeTask);
                            }
                            if (activeWorker != null) {
                                bigPostIt.getChildren().add(activeWorker);
                            }
                            // Go back to main view when right click is pressed
                            bigPostIt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, q
                                    -> {
                                if (q.getButton() == MouseButton.SECONDARY) {
                                    removeNodeInJavaFXThread(aPane, bigPostIt);
                                
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
                
                checkForDeletedTask(boxes);

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
                connectionLost.set(true);
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

        Label lblStart = labelMaker.makeLabelForProgressBar("start", -1458);
        Label lblSlut = labelMaker.makeLabelForProgressBar("end",-1257);
        lblSlut.setTranslateX(-1240);

        double percantage = Utility.getPercentageTimeLeft(bTask);
        ProgressBar pBar = new ProgressBar();
        pBar.setProgress(percantage);

        pBar.setPrefHeight(25);
        pBar.setPrefWidth(250);
        pBar.setTranslateX(-1350);
        pBar.setTranslateY(50);
        progressPane.getChildren().addAll(lblStart, lblSlut, pBar);
        return progressPane;
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

    private void makeRedCirkel(StackPane sPane) {
        Circle warning = new Circle(55);
        warning.setStroke(Color.RED);
        warning.setFill(Color.TRANSPARENT);
        warning.setStrokeWidth(2);
        warning.setTranslateY(-5);

        sPane.getChildren().add(warning);
    }

    private void removeSmallTask(String orderNumber) {
        ObservableList<Node> allBoxes = fPane.getChildren();

        for (Node box : allBoxes) {
            HBox hBox = (HBox) box;
            StackPane sPane = (StackPane) hBox.getChildren().get(0);
            Label oNumber = (Label) sPane.getChildren().get(1);
            if (oNumber.getText().contains(orderNumber)) {
                toRemove = (HBox) box;
            }
        }

        removeNodeInJavaFXThread(fPane, toRemove);
        lastRemoved = orderNumber;
        toRemove=null;

    }
    /**
     * Writes on the main views display
     * @param toWrite 
     */
    private void writeOnDisplay(String toWrite) {
        Platform.runLater(() -> {
            display.setText(toWrite);

        });
    }
    /**
     * Makes the safety pop-up before completing a task
     * @param bTask
     * @param stackPane
     * @return 
     */

    private StackPane popUp(BoardTask bTask, StackPane stackPane) {
        StackPane popUp = new StackPane();
        ObservableList<Node> allNodes = aPane.getChildren();

        ImageView view = new ImageView(pic);
        view.setImage(pic);
        view.setFitHeight(570);
        view.setFitWidth(530);
        view.setRotate(10);

        Label txt = labelMaker.getWarningTxtLabel();
        Label header = labelMaker.getWarningHeaderLabel();

        Button cancelBtn = bMaker.getCancelButton();
        Button acceptBtn = bMaker.getAcceptButton();

        popUp.getChildren().addAll(view, txt, cancelBtn, acceptBtn, header);

        acceptBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e
                -> {
            try {
                model.setCompleteTask(bTask.getTaskID());
                removeNodeInJavaFXThread(aPane,stackPane);
   
                for (Node child : allNodes) {
                    child.setEffect(null);
                }
                removeSmallTask(bTask.getOrderNumber());
                writeOnDisplay(model.getResourceBundle().getString("completeTask"));
            } catch (SQLException ex) {
              connectionLost.set(true);
            }
        });

        cancelBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e
                -> {
            removeNodeInJavaFXThread(stackPane, popUp);
        });
        return popUp;
    }
   /**
    * Removes a node on the given Pane in the JavaFX thread
    * @param container
    * @param toRemove 
    */ 
    public void removeNodeInJavaFXThread(Pane container, Node toRemove)
    {
        Platform.runLater(()->
        {
            container.getChildren().remove(toRemove);
        });
    }
/**
 * This method makes sure that a task can't reappear briefly after it has been marked as done
 * @param boxes 
 */
    private void checkForDeletedTask(ArrayList<HBox> boxes) {
        HBox boxToRemove = null;
        for (HBox box : boxes) {
            StackPane pane =(StackPane) box.getChildren().get(0);
            Label label =(Label)pane.getChildren().get(1);
           String orderNumber = label.getText();
           if(orderNumber.equals(lastRemoved))
           {
            boxToRemove=box;  
           } 
        }
        if(boxToRemove!=null)
        {
            boxes.remove(boxToRemove);
        }
    }
}
