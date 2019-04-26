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
import quickmaff_belman.gui.controller.LoginController;

/**
 *
 * @author Philip
 */
public class QuickMaff_Belman extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quickmaff_belman/gui/view/Login.fxml"));
        Parent root = loader.load();
        LoginController con = loader.getController();
        
        Scene scene = new Scene(root);
        con.buttonGenerator();
    
        stage.setScene(scene);
        stage.show();
        
    con.initView(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
