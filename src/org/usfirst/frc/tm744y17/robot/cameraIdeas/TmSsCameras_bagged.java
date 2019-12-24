package org.usfirst.frc.tm744y17.robot.cameraIdeas;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.tm744y17.robot.config.Tm17Opts;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class TmSsCameras_bagged implements TmStdSubsystemI, TmToolsI {

	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsCameras_bagged m_instance;

	public static synchronized TmSsCameras_bagged getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsCameras_bagged();
		}
		return m_instance;
	}

	private TmSsCameras_bagged() {
		setCameraSelection(CameraSelectorE.FRONT);
	}
	/*----------------end of getInstance stuff----------------*/

	public enum CameraSelectorE { 
		FRONT, REAR;
		public boolean isFrontCameraSelected() { return this.equals(CameraSelectorE.FRONT); }
		public boolean isRearCameraSelected() { return this.equals(CameraSelectorE.REAR); }
	}
	private static CameraSelectorE m_cameraSelection;
	
	public static boolean isFrontCameraSelected() { return m_cameraSelection.isFrontCameraSelected(); }
	public static boolean isRearCameraSelected() { return m_cameraSelection.isRearCameraSelected(); }
	
	public static void setCameraSelection(CameraSelectorE camSel) {
		m_cameraSelection = camSel;
		P.println("Camera selected: " + camSel.name());
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CAMERA_FRONT_SELECTED, camSel.isFrontCameraSelected());
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CAMERA_REAR_SELECTED, camSel.isRearCameraSelected());
	}
	
	Thread visionThreadFront = null;
	Thread visionThreadRear = null;
	
	@Override
	public void doInstantiate() {
		// TODO Auto-generated method stub

	}
	
	private void optionalSimpleCameraAccess(boolean isInstalled, String camName, int camUsbNdx) {
		if(isInstalled) {
			CameraServer.getInstance().startAutomaticCapture(camName, camUsbNdx);
			P.println("CAMERA " + camName  + " connected");
		} else {
			P.println("Per Opts/Preferences, " + camName + " is NOT installed");
		}		
	}
	
	private Thread optionalCameraThread(boolean isInstalled, String camName, int camUsbPort) {
		return optionalCameraThread(isInstalled, camName, camUsbPort, VisionOutDefE.NONE);
	}
	private Thread optionalCameraThread(boolean isInstalled, String camName, int camUsbPort, VisionOutDefE vOutDef) {
		Thread optVisionThread = null;
		if(isInstalled) {
			try {
				optVisionThread = makeVisionThread(camName, camUsbPort, vOutDef);
				P.println("CAMERA thread created for " + camName);
			} catch(Throwable t) {
				TmExceptions.reportExceptionMultiLine(t, "Exception creating vision thread for Camera " + camName);
			}
		} else {
			P.println("Per Opts/Preferences, " + camName + " is NOT installed");
		}	
		return optVisionThread;
	}


	@Override
	public void doRoboInit() {
		int cameraFrontNdx = 0;
		String cameraNameFront = "cam" + cameraFrontNdx; //"FrontCamera" + cameraFrontNdx;
		int cameraRearNdx = 1;
		String cameraNameRear = "cam" + cameraRearNdx; //"RearCamera" + cameraRearNdx;
		
		if(true) {
			optionalSimpleCameraAccess(Tm17Opts.isUsbCam0Installed(), cameraNameFront, cameraFrontNdx);
			optionalSimpleCameraAccess(Tm17Opts.isUsbCam1Installed(), cameraNameRear, cameraRearNdx);
		} else {
			visionThreadFront = optionalCameraThread(Tm17Opts.isUsbCam0Installed(), cameraNameFront, cameraFrontNdx, VisionOutDefE.NONE); //VisionOutDefE.FRONT);
			visionThreadRear = optionalCameraThread(Tm17Opts.isUsbCam1Installed(), cameraNameRear, cameraRearNdx, VisionOutDefE.NONE); //VisionOutDefE.REAR);
		}
		String junk = "good debug breakpoint";
	}

	@Override
	public void doDisabledInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAutonomousInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doTeleopInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doLwTestInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doRobotPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDisabledPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAutonomousPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doTeleopPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub

	}
	
	public enum VisionOutDefE {
		FRONT(320, 240, 10, new Point(50, 50), new Point(200, 200), new Scalar(255, 255, 255), 3),
		REAR(320, 240, 10, new Point(75, 75), new Point(175, 175), new Scalar(255, 255, 255), 3),
		NONE(320, 240, 10, null, null, null, 0),
		;
//		Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
//		new Scalar(255, 255, 255), 5);
		
		int	eImgWidth;
		int eImgHeight;
		int eFPS;
		Point eTopL;
		Point eLowerR;
		Scalar eColor;
		int eLineWidth;
		private VisionOutDefE(int width, int height, int fps, Point topL, Point lowerR, Scalar color, int lineWidth) {
			eImgWidth = width;
			eImgHeight = height;
			eFPS = fps;
			eTopL = topL;
			eLowerR = lowerR;
			eColor = color;
			eLineWidth = lineWidth;			
		}
	}

	Thread makeVisionThread(String cameraName, int usbPort) {
//		Imgproc.rectangle(mat, new Point(50, 50), new Point(200, 200),
//				new Scalar(255, 255, 255), 3);
		return makeVisionThread(cameraName, usbPort, VisionOutDefE.NONE); //null, null, null, 0);
	}
	Thread makeVisionThread(String cameraName, int usbPort, VisionOutDefE visionOutputDef) { //Point topL, Point lowerR, Scalar color, int width) {
		VisionOutDefE l_vOutDef = visionOutputDef;
//		Point l_topL = topL;
//		Point l_lowerR = lowerR;
//		Scalar l_color = color;
//		int l_width = width;
		
		Thread visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(cameraName, usbPort);
			// Set the resolution
			camera.setResolution(l_vOutDef.eImgWidth, l_vOutDef.eImgHeight); //320, 240); //640, 480);
			camera.setFPS(l_vOutDef.eFPS); //10);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			String outputName = cameraName + ((l_vOutDef.eTopL==null) ? "Plain" : "Rectangle"); //l_topL==null) ? "Plain" : "Rectangle");
			CvSource outputStream = CameraServer.getInstance().putVideo(outputName, l_vOutDef.eImgWidth, l_vOutDef.eImgHeight); //320, 240); //640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
//				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
//						new Scalar(255, 255, 255), 5);
				if( ! (l_vOutDef.eTopL==null)) { //l_topL==null)) {
					Imgproc.rectangle(mat, l_vOutDef.eTopL, l_vOutDef.eLowerR, l_vOutDef.eColor, l_vOutDef.eLineWidth); //l_topL, l_lowerR, l_color, l_width);
				}
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();

		return visionThread;
	}

	Thread makeVisionThreadExample(String cameraName, int usbPort) {

		Thread visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(cameraName, usbPort);
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(255, 255, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();

		return visionThread;
	}

}
