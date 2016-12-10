/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Camera;

import Main.ImageIo;
import Main.Print;

////////////////////////////////////////////////////////////

import java.awt.TextField;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import org.opencv.core.Rect;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


/**
 *
 * @author Jessie Reyna
 */
public class CameraController implements Initializable
{
    @FXML
    private Button button;
    @FXML
    private ImageView currentFrame;
    @FXML
    private ImageView currentPicture;
    @FXML    
    private Label label;
    @FXML 
    private TextField TFFolderName = new TextField();
    @FXML 
    private Rectangle FaceRect;
        
    private String FolderName;
    private int PictureCount = 0;
    private ScheduledExecutorService timer;
    private static VideoCapture capture = new VideoCapture();
    private boolean cameraActive = false;
    private static int cameraId = 0;
    private boolean takeSnapShot = false;
    
    private BufferedImage[] Pictures = new BufferedImage[6];
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        FaceRect.setX(0);
//        FaceRect.setY(0);
//        FaceRect.setWidth(currentFrame.fitWidthProperty().intValue());
//        FaceRect.setHeight(currentFrame.fitHeightProperty().intValue());
    }
    
    @FXML 
    private void PrintSTuff()
    {
        Print.Say("\n\n\n\nHAHAHAHAHHAHA");
    
    }

    @FXML
    private void TakeShot(Mat I)
    {     
        if (PictureCount <= 6)
        {
            Mat uncropped = I;
            Rect roi = new Rect(FaceRect.xProperty().intValue(),FaceRect.yProperty().intValue(), FaceRect.widthProperty().intValue(), FaceRect.heightProperty().intValue());
            Mat cropped = new Mat(uncropped, roi);
            Print.Say("\nTAKEING SHOT\n");
            BufferedImage BI = null;           
            
            Image imageToShow02 = mat2Image(cropped);
                            
            currentPicture.setImage(imageToShow02);

            Pictures[PictureCount] = matToBufferedImage(cropped,BI );
           
            Print.Say("\nPictures:"+Pictures[PictureCount]+"\n");
            PictureCount++;
        }             
    }    
 
    @FXML
    private void SnapShot(ActionEvent event) {
       takeSnapShot = true;
    }
    
   @FXML
    private void SaveFolder(ActionEvent event)
    {
        if (Pictures.length > 0)
        {
            //Print.Say(TFFolderName.getText());
            File file = new File("JESSIE_FOLDER");
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }            
            for (int i = 0; i < Pictures.length; i++)
            {      
                 ImageIo.writeImage(Pictures[i], "jpg", "JESSIE_FOLDER/Picture_0"+i+".jpg") ;  
            }
        }
    }  

    @FXML
    protected void startCamera(ActionEvent event) throws IOException {
        capture = new VideoCapture();
        if (!this.cameraActive) {
            // start the video capture
            capture.open(01);
            

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                
                Print.Fail();
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = mat2Image(frame);
                        //updateImageView(currentFrame, imageToShow);
                        
                        if (takeSnapShot && PictureCount < 6)
                        {
                            Mat frame02 = grabFrame();
                            TakeShot(frame02);
                            takeSnapShot = false;                           
                        }              
                        currentFrame.setImage(imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.button.setText("Stop Camera");
            } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            this.button.setText("Start Camera");
            // stop the timer
            this.stopAcquisition();
        }
    }   
//    @FXML
//    private  void SnapShot(ActionEvent event)
//    {
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//        
//        try{
//            grabber.start();
//            
//            IplImage img = grabber.grab();
//            if(img != null)
//            {
//                cvSaveImage("PIC.jpg", img);
//            
//            
//            } 
//       
//        }catch(Exception e){e.printStackTrace();}; 
//    }
    
    public static BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg)
    {
        if ( matrix != null ) { 
            int cols = matrix.cols();  
            int rows = matrix.rows();  
            int elemSize = (int)matrix.elemSize();  
            byte[] data = new byte[cols * rows * elemSize];  
            int type;  
            matrix.get(0, 0, data);  
            switch (matrix.channels()) {  
            case 1:  
                type = BufferedImage.TYPE_BYTE_GRAY;  
                break;  
            case 3:  
                type = BufferedImage.TYPE_3BYTE_BGR;  
                // bgr to rgb  
                byte b;  
                for(int i=0; i<data.length; i=i+3) {  
                    b = data[i];  
                    data[i] = data[i+2];  
                    data[i+2] = b;  
                }  
                break;  
            default:  
                return null;  
            }  

            // Reuse existing BufferedImage if possible
            if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
                bimg = new BufferedImage(cols, rows, type);
            }        
            bimg.getRaster().setDataElements(0, 0, cols, rows, data);
        } else { // mat was null
            bimg = null;
        }
        return bimg;  
    }   
    
    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);
                // if the frame is not empty, process it
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }
            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;
    }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("\n=============Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }
        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    protected void setClosed() {
        this.stopAcquisition();
    }

    private Image mat2Image(Mat frame) {        
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }  
}
