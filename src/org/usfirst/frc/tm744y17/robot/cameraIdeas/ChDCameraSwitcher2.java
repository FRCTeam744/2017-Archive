package org.usfirst.frc.tm744y17.robot.cameraIdeas;

import java.lang.Thread;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * from team 810, posted on Chief Delphi
 * found using search string: chief delphi 2017 switch USB cameras
 * copied from: https:https://www.chiefdelphi.com/forums/showthread.php?t=154213
 * 
 */
public class ChDCameraSwitcher2 {
	public void robotInit() {
        new Thread(() -> {
            UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
            camera1.setResolution(320, 240);
            camera1.setFPS(15);
            UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
            camera2.setResolution(320, 240);
            camera2.setFPS(15);
            
            CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
            CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
            CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
            
            Mat image = new Mat();
            
            while(!Thread.interrupted()) {
            	boolean sel = true;
                if( sel /*switcher button logic*/){
                  cvSink2.setEnabled(false);
                  cvSink1.setEnabled(true);
                  cvSink1.grabFrame(image);
                } else{
                  cvSink1.setEnabled(false);
                  cvSink2.setEnabled(true);
                  cvSink2.grabFrame(image);
                }
                
                outputStream.putFrame(image);
            }
        }).start();
	}
}
