/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.gui.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quickmaff_belman.be.BoardTask;
import quickmaff_belman.gui.model.Language;
import quickmaff_belman.gui.model.Model;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class MainViewController implements Initializable {
    
    @FXML
    private ImageView languageSwitch;
    private Model model;
    @FXML
    private Label department;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView Filter;
      
    @FXML
    private FlowPane flowPane;
    private Stage stage;
    @FXML
    private ImageView depSign;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    @FXML
    private void changeLanguage(MouseEvent event) {
        
        Language language = model.changeLanguage();
        switch (language) {
            case DANISH:
                Image daImage = new Image("/quickmaff_belman/gui/view/images/toggle.png");
                languageSwitch.setImage(daImage);
                break;
            case ENGLISH:
                Image engImage = new Image("/quickmaff_belman/gui/view/images/toggle2.png");
                languageSwitch.setImage(engImage);
                break;
            
        }
        
        setAllText();
        
    }
    
    public void initView() throws SQLException {
        setGraphics();
        setAllText();
        loadBoard();
    }
    
    public void loadBoard() throws SQLException {
       
       ArrayList<BoardTask> boardTasks = model.getAllBoardTasks();
        Image daImage = new Image("/quickmaff_belman/gui/view/images/postit.png");
       
        for (BoardTask bTask : boardTasks) {           
            StackPane sPane = new StackPane();

            ImageView view = new ImageView(daImage);
            Label orderNumber = new Label(bTask.getOrderNumber());
            orderNumber.setFont(new Font("Arial", 15));
            Label endDate = new Label("\n\n"+bTask.getEndDate());
   
            view.setPreserveRatio(true);
            view.setFitWidth(160);
            sPane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("Opening task from order:"+orderNumber );
            });
            
            sPane.getChildren().addAll(view, orderNumber, endDate);
            HBox box = new HBox(sPane);
            box.setAlignment(Pos.CENTER);
            flowPane.getChildren().add(box);
            
        }
        
    }
    
    private void setAllText() {
        department.setText(model.getResourceBundle().getString("department"));
    }
    
    @FXML
<<<<<<< HEAD
    private void filtering(MouseEvent event)
    {
//                Language language = model.changeLanguage();
//
//        switch (language)
//        {
//            case DANISH:
//                Image buttonImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap.png");
//                Filter.setImage(buttonImage);
//                break;
//            case ENGLISH:
//                Image pressImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap-tryk.png");
//                Filter.setImage(pressImage);
//                break;
//        }
=======
    private void filtering(MouseEvent event) {
        Language language = model.changeLanguage();
        
        switch (language) {
            case DANISH:
                Image buttonImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap.png");
                Filter.setImage(buttonImage);
                break;
            case ENGLISH:
                Image pressImage = new Image("/quickmaff_belman/gui/view/images/FiltrerKnap-tryk.png");
                Filter.setImage(pressImage);
                break;
        }
>>>>>>> 9e50a4fe5de256f9f378b22aa72cb9bfc91d8671
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void setGraphics() {
<<<<<<< HEAD

        flowPane.prefWidthProperty().bind(stage.widthProperty().subtract(660));
       
=======
        
        flowPane.prefWidthProperty().bind(stage.widthProperty().subtract(615));
        
>>>>>>> 9e50a4fe5de256f9f378b22aa72cb9bfc91d8671
    }
    
}
