package org.usfirst.frc.tm744y17.robot.cameraIdeas;

/**
 * from team 955, posted on Chief Delphi
 * found using search string: chief delphi 2017 switch USB cameras
 * copied from: https://raw.githubusercontent.com/Merfoo/MultiCamera/master/src/org/usfirst/frc/team955/robot/CameraFeeds.java
 * 
 */
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
//import com.ni.vision.NIVision.cam;

//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.CameraServer;


public class ChDCameraSwitcher1 {
//	package org.usfirst.frc.team955.robot;
//
//	import com.ni.vision.NIVision;
//	import com.ni.vision.NIVision.Image;
//	import edu.wpi.first.wpilibj.CameraServer;

//	public class CameraFeeds
//	{
		private final int camCenter;
		private final int camRight;
		private int curCam;
		private Image frame;
		private CameraServer server;
		private Controller contr;
		
		//what/where are these??
		private class Controller {
			protected boolean getButton(int btnNbr) { return false; }
		}
		private static class Config {
			protected static class CameraFeeds {
				static String camNameRight = "cam0";
				static String camNameCenter = "cam1";
				static int imgQuality = 50;
				static int btCamCenter = 0;
				static int btCamRight = 1;
			}
		}

		
//		public CameraFeeds(Controller newContr)
		public ChDCameraSwitcher1(Controller newContr)
		{
	        // Get camera ids by supplying camera name ex 'cam0', found on roborio web interface
	        camCenter = NIVision.IMAQdxOpenCamera(Config.CameraFeeds.camNameCenter, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
	        camRight = NIVision.IMAQdxOpenCamera(Config.CameraFeeds.camNameRight, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
	        curCam = camCenter;
	        // Img that will contain camera img
	        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	        // Server that we'll give the img to
	        server = CameraServer.getInstance();
	        server.setQuality(Config.CameraFeeds.imgQuality);
	        contr = newContr;
		}
		
		public void init()
		{
			changeCam(camCenter);
		}
		
		public void run()
		{
			if(contr.getButton(Config.CameraFeeds.btCamCenter))
				changeCam(camCenter);
			
			if(contr.getButton(Config.CameraFeeds.btCamRight))
				changeCam(camRight);
			
			updateCam();
		}
		
		/**
		 * Stop aka close camera stream
		 */
		public void end()
		{
			NIVision.IMAQdxStopAcquisition(curCam);
		}
		
		/**
		 * Change the camera to get imgs from to a different one
		 * @param newId for camera
		 */
		public void changeCam(int newId)
	    {
			NIVision.IMAQdxStopAcquisition(curCam);
	    	NIVision.IMAQdxConfigureGrab(newId);
	    	NIVision.IMAQdxStartAcquisition(newId);
	    	curCam = newId;
	    }
	    
		/**
		 * Get the img from current camera and give it to the server
		 */
	    public void updateCam()
	    {
	    	NIVision.IMAQdxGrab(curCam, frame, 1);
	        server.setImage(frame);
	    }
//	}

}

