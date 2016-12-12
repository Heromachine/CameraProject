/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.View;

import Main.Print;
import Main.main;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author Jessie Reyna
 */
public class MainViewController    
{
    private String ProjectName;
    
    
    @FXML
    private TextField NewProjectName;
    
    private boolean getNewProjectName()throws IOException
    {
        if (NewProjectName.getText().isEmpty())
        {               
            System.out.println("DIR NOT created");  
            return false;
        }
        else
        {
            
            ProjectName = NewProjectName.getText().toString();
            
            main.setNAME(ProjectName);

            File theDir = new File("FaceRecognitionProjects/" + NewProjectName.getText().toString());

            // if the directory does not exist, create it
            if (!theDir.exists()) {
                System.out.println("creating directory: " + NewProjectName.getText().toString());
                boolean result = false;

                try{
                    theDir.mkdir();
                    result = true;
                } 
                catch(SecurityException se){
                    //handle it
                }        
                if(result) {    
                    System.out.println("DIR created");  
                }
            }  
            
            return true;
        }            
    }  
    
    @FXML
    private void goNewProject()throws IOException
    {
        if (getNewProjectName())
            main.showCamera();
    
    }
    
    @FXML
    private void goFaceRecognition()throws IOException
    {
        main.showFaceRecognition();
    
    }
    
    
    
}
