/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quickmaff_belman.bll.BLLManager;
import quickmaff_belman.dal.DatabaseFacade;
import quickmaff_belman.gui.controller.LoginController;
import quickmaff_belman.gui.controller.MainViewController;
import quickmaff_belman.gui.model.Model;

/**
 *
 * @author Philip
 */
public class QuickMaff_Belman extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/MainView.fxml"));
        Parent root = loader.load();
        MainViewController con = loader.getController();
        con.setModel(new Model(new BLLManager(new DatabaseFacade())));
        con.initView();
        
//        LoginController con = loader.getController();
        
        Scene scene = new Scene(root);
//        con.buttonGenerator();
    
        stage.setScene(scene);
        stage.show();
//        
//    con.initView(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
