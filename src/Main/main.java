/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Jessie Reyna
 */
public class main extends Application {
    private Stage primaryStage;
    private static BorderPane mainLayout;
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CAMERA");
        showMainView();
        showPage1();
    }
    private void showMainView() throws IOException
    {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(main.class.getResource("View/MainView.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();       
    }
    
    private void showPage1() throws IOException
    {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(main.class.getResource("View/Page1.fxml"));
        BorderPane bpPage1 = loader.load();
        mainLayout.setCenter(bpPage1);
    }    
    
    public  static void showCamera() throws IOException
    {        System.out.print("\n\nWORKED ");
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(main.class.getResource("Camera/Camera.fxml"));
        BorderPane bpCamera = loader.load();
        mainLayout.setCenter(bpCamera);    
    }


    
}
