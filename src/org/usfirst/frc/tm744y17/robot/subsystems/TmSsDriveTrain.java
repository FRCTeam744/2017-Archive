package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.commands.TmACmdDoNothing;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdDriveWithJoysticks;
import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors.MotorConfigE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys;
import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsShooter.ShooterDrumMgmt.DrumCnst;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.devices.TmGyroADXRS453SPI;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon.ConnectedEncoder;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon.EncoderCountsCapabilityE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon.EncoderPolarityE;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.DriveTrainDirectionE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/*
 * The 2016 robot's gyro will be a Analog Devices EVAL-ADXRS453Z p/n 35-000058-02
 * last year's may have been ADXRS453BRGZ-ND
 * @author JudiA
 *
 */

/**
 *
 */
public class TmSsDriveTrain extends Subsystem implements TmStdSubsystemI, TmToolsI {

	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsDriveTrain m_instance;

	public static synchronized TmSsDriveTrain getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsDriveTrain();
		}
		return m_instance;
	}

	private TmSsDriveTrain() {
	}
	/*----------------end of getInstance stuff----------------*/

	/*---------------------------------------------------------
	 * fields                                     
	 *---------------------------------------------------------*/
	private TmRobotDriveSixMotors m_drive;
	private DrvEncoderMgmt m_dem;
	
	//manage preferences here so they can be updated more easily than in TmRobotDriveSixMotors
	private static DriveTrainPreferences m_dtPrefs = null;
	public static DriveTrainPreferences getDriveTrainPreferences() { 
		if(m_dtPrefs==null) { m_dtPrefs = new DriveTrainPreferences(); }
		return m_dtPrefs;
	}
	public static void showDriveTrainTuning() {
		if(m_dtPrefs==null) { m_dtPrefs = new DriveTrainPreferences(); } 
		m_dtPrefs.showTuningParms();
	}
	
	private static final int MOTOR_LIST_ARRAY_LENGTH = 6;
	private MotorConfigE m_motorList[];
	
	private Command m_defaultTeleopCommand = null;
	private Command m_defaultAutonCommand = null;
	private Command m_alternateTeleopCommand = null;
	private static final Object m_cmdLock = new Object();
	
    private boolean m_allowLiveWindowControl = true;
    

    //these are service methods for use by commands
	private double m_angle = 0; //a debug aid
	public double getAngle() {
		m_angle = m_gyro.getAngle();
		return m_angle;
	}
	
	public void resetGyro() {
		m_gyro.reset();
		m_angle = 0;
	}
	
	//service methods for use by commands
	public void stopMotors() {
		m_drive.stopMotors();
	}
	public boolean isDriveMotorsStopped() {
		return m_drive.isDriveMotorsStopped();
	}
	
	public boolean isDrivingBelow(double testSpeed) {
		return m_drive.isDrivingBelow(testSpeed);
	}
	
    private static DriveMotorEncoder m_leftEncoder;
    private static DriveMotorEncoder m_rightEncoder;
    
    private static class Cnst {
    	//4in. wheels: (4in * pi) / 12 = 1.047
    	public static final double FEET_PER_REVOLUTION = (4 * Math.PI)/12; //;1.047; //CONFIG_ME
    	
    	public static final double MAX_FEET_PER_SECOND = 16; //CONFIG_ME    	
    	public static final double MAX_FEET_PER_SECOND_LOW_GEAR = 6;
    	
    	public static final double MAX_ENCODER_AXLE_RPS = MAX_FEET_PER_SECOND/FEET_PER_REVOLUTION;
    	public static final int ENCODER_COUNTS_PER_REVOLUTION = 4096; //CONFIG_ME 	
    }
    public static double getMaxFtPerSecHighGear() { return Cnst.MAX_FEET_PER_SECOND; }
    public static double getMaxFtPerSecLowGear() { return Cnst.MAX_FEET_PER_SECOND_LOW_GEAR; }
	
	public class DriveMotorEncoder extends TmFakeable_CANTalon.ConnectedEncoder {
		private MotorConfigE encMtrCfg;
		private SdKeysE encSdKeyPosition;
				
		//magnetic encoder is connected to a CANTalon controller and accessed through it.
		//optical encoders are connected to digital I/O pins. We'll use whichever we need
		//based on the encoder type; this approach minimizes code changes when switching
		//encoder types.
		public DriveMotorEncoder(MotorConfigE mtrCfg, SdKeysE encoderSdKeyPosition) {
//			mm_encoder = mm_drumMotor.new ConnectedEncoder(mm_motorDef, null, DrumCnst.SHOOTER_AXLE_MAX_RPS, 
//					DrumCnst.SHOOTER_ENCODER_COUNTS_PER_REVOLUTION, DrumCnst.SHOOTER_ENCODER_FEET_PER_REVOLUTION);
			mtrCfg.getCanTalonMotorObj().super(mtrCfg, encoderSdKeyPosition, Cnst.MAX_ENCODER_AXLE_RPS, 
					Cnst.ENCODER_COUNTS_PER_REVOLUTION, Cnst.FEET_PER_REVOLUTION, 
					EncoderPolarityE.OPPOSITE_OF_MOTOR, EncoderCountsCapabilityE.ABSOLUTE_USED_AS_RELATIVE);
			encMtrCfg = mtrCfg;
			encSdKeyPosition = encoderSdKeyPosition;
		}
		
		/**
		 * @return counts per revolution for the installed encoder
		 */
		public int getCpr() {
			return super.getCpr();
//			final int magneticTicksPerRev = 4096;
//			return magneticTicksPerRev;
		}
		
//		public double getDistance() {
//			return super.getDistance();
////			return get() / getCpr() * FEET_PER_REVOLUTION;
//		}
		
		//try changing getLeftDistance() and getRightDistance() to use this.
		public double getDistanceAdjustedForPolarity() {
			double ans;
			double motorPolarityFactor = encMtrCfg.getNamedMotorDef().getMultiplierForDrvMtrPolarity();
			ans = motorPolarityFactor * super.getDistance();
			return ans;
		}
		
		public synchronized int get() {
			return super.get();
//			return encMtrCfg.getMotorConfigData().getEncPosition();
		}
		
		public void reset() {
			super.reset();
//			encMtrCfg.getMotorConfigData().setEncPosition(0);
		}

		public void postToSdPosition() {
			super.postToSdPosition();
//			if(encSdKeyPosition != null) {
//				TmPostToSd.dbgPutNumber(encSdKeyPosition, get());
//			}
		}
	}
	
	/**
	 * Because of the physical orientation of the motors on the drive-train, there will always be
	 * some motors running forward, and some in reverse.  Unfortunately, motors produce slightly less
	 * power when running in reverse, so the robot will have trouble driving in a straight line.  We
	 * use "tuning parms" to slow down whichever side of the robot has motors running forward.  We
	 * have hardcoded default values for these parms which can be overridden from smartDashboard 
	 * using "preferences".  This class manages all of that. 
	 * @author JudiA
	 *
	 */
	public static class DriveTrainPreferences {
	    //default values to be returned by getTuningParm() if there is no tuning
	    //parm in the preferences file 
		//veered left when driving forward
		
		//values used in orlando
	    //private static final double DRIVE_MOTOR_TUNING_LEFT_REVERSE = 0.98900; //1.00; //0.9859; //CONFIG_ME
	    //private static final double DRIVE_MOTOR_TUNING_RIGHT_REVERSE = 1.00;
	    //private static final double DRIVE_MOTOR_TUNING_LEFT_FORWARD = 1.00; //0.9859;
	    //private static final double DRIVE_MOTOR_TUNING_RIGHT_FORWARD = 0.98900; //1.00;
	    
	    //values for bot2 post-orlando using drive straight with gyro
	    public static final double DRIVE_MOTOR_TUNING_LEFT_REVERSE = 1.0; //1.00; //0.9859; //CONFIG_ME
	    public static final double DRIVE_MOTOR_TUNING_RIGHT_REVERSE = 0.9946;
	    public static final double DRIVE_MOTOR_TUNING_LEFT_FORWARD = 1.00; //0.9859;
	    public static final double DRIVE_MOTOR_TUNING_RIGHT_FORWARD = 0.9946; //1.00;
	    
	    private static double dtp_driveMotorTuningLeftReverse = DRIVE_MOTOR_TUNING_LEFT_REVERSE;
	    private static double dtp_driveMotorTuningRightReverse = DRIVE_MOTOR_TUNING_RIGHT_REVERSE;
	    private static double dtp_driveMotorTuningLeftForward = DRIVE_MOTOR_TUNING_LEFT_FORWARD;
	    private static double dtp_driveMotorTuningRightForward = DRIVE_MOTOR_TUNING_RIGHT_FORWARD;
	    
	    public static double getCurTuningParmLeftReverse() { return dtp_driveMotorTuningLeftReverse; }
	    public static double getCurTuningParmRightReverse() { return dtp_driveMotorTuningRightReverse; }
	    public static double getCurTuningParmLeftForward() { return dtp_driveMotorTuningLeftForward; }
	    public static double getCurTuningParmRightForward() { return dtp_driveMotorTuningRightForward; }
	    
	    public void checkForTuningParmUpdates() {
	    	boolean showDbgMsgs = false;
	        dtp_driveMotorTuningLeftReverse = getTuningParmLeftReverse(dtp_driveMotorTuningLeftReverse, showDbgMsgs);
	        dtp_driveMotorTuningRightReverse = getTuningParmRightReverse(dtp_driveMotorTuningRightReverse, showDbgMsgs);
	        dtp_driveMotorTuningLeftForward = getTuningParmLeftForward(dtp_driveMotorTuningLeftForward, showDbgMsgs);
	        dtp_driveMotorTuningRightForward = getTuningParmRightForward(dtp_driveMotorTuningRightForward, showDbgMsgs);    		    	
	    }
	    
	    public void showTuningParms() {
	    	P.printFrmt(-1, "Drive Tuning: L-for=%1.4f R-for=%1.4f L-rev=%1.4f R-rev=%1.4f", 
	    			dtp_driveMotorTuningLeftForward, dtp_driveMotorTuningRightForward,
	    			dtp_driveMotorTuningLeftReverse, dtp_driveMotorTuningRightReverse);
	    }
	    
		//if there's a parm in the preferences file, use it.  Else use the
		//hardcoded defaults set above.
	    public synchronized void initializeDriveMotorTuningParms(boolean showDbgMsgs) {
	        dtp_driveMotorTuningLeftReverse = getTuningParmLeftReverse(DRIVE_MOTOR_TUNING_LEFT_REVERSE, showDbgMsgs);
	        dtp_driveMotorTuningRightReverse = getTuningParmRightReverse(DRIVE_MOTOR_TUNING_RIGHT_REVERSE, showDbgMsgs);
	        dtp_driveMotorTuningLeftForward = getTuningParmLeftForward(DRIVE_MOTOR_TUNING_LEFT_FORWARD, showDbgMsgs);
	        dtp_driveMotorTuningRightForward = getTuningParmRightForward(DRIVE_MOTOR_TUNING_RIGHT_FORWARD, showDbgMsgs);    	
	    }
	    
	    private double getTuningParmLeftReverse(double defaultValue, boolean showDbgMsgs) {
	    	return getTuningParm(TmRoFilesAndPrefKeys.PrefKeysE.KEY_DRIVE_TUNING_VAL_LEFT_REVERSE, defaultValue, showDbgMsgs);
	    }
	    private double getTuningParmRightReverse(double defaultValue, boolean showDbgMsgs) {
	    	return getTuningParm(TmRoFilesAndPrefKeys.PrefKeysE.KEY_DRIVE_TUNING_VAL_RIGHT_REVERSE, defaultValue, showDbgMsgs);
	    }
	    private double getTuningParmLeftForward(double defaultValue, boolean showDbgMsgs) {
	    	return getTuningParm(TmRoFilesAndPrefKeys.PrefKeysE.KEY_DRIVE_TUNING_VAL_LEFT_FORWARD, defaultValue, showDbgMsgs);
	    }
	    private double getTuningParmRightForward(double defaultValue, boolean showDbgMsgs) {
	    	return getTuningParm(TmRoFilesAndPrefKeys.PrefKeysE.KEY_DRIVE_TUNING_VAL_RIGHT_FORWARD, defaultValue, showDbgMsgs);
	    }

	    /**
	     * read drive train tuning parms from preferences file if available.  Returns
	     * the default value provided by the caller if the key is not found in the
	     * preferences file.
	     * @param preferencesKey
	     * @param defaultVal - value to return if can't find anything in the preferences file
	     * @param showDbgMsgs
	     * @return 
	     */
	    private double getTuningParm(PrefKeysE preferencesKey, double defaultVal, 
	                                        boolean showDbgMsgs)
	    {
	    	double ans = defaultVal;
	    	double raw;
	    	raw = Tt.getPreference(preferencesKey, defaultVal,
	    			//note: x=a?b:c; means if(a){x=b;}else{x=c;}
	    			(showDbgMsgs ? -1 : 0), PrefCreateE.CREATE_AS_NEEDED);
	    	ans = Tt.clampToFrcRange(raw);
	    	if(showDbgMsgs && (ans != raw)) {
	    		P.println("preference value " + raw + " clamped to " + ans);
	    	}
	    	return ans;
	    		
	    }
    		
	}
	
    private static TmGyroADXRS453SPI m_gyro;
    
    private static final Object m_notifierLock = new Object();    
	private static final double PERIOD_FOR_CALLBACKS_FROM_NOTIFIER_IN_SECONDS = 0.005;//CONFIG_ME

	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public enum DriveSpinDirectionE {
		CLOCKWISE, COUNTER_CLOCKWISE;
		
		public double getSign() {
			if(this.equals(DriveSpinDirectionE.CLOCKWISE)) return 1;
			else return -1;
		}
	}
	
	//helper methods for use from commands
	public void setDriveDirection(DriveTrainDirectionE newDir) {
		synchronized(m_cmdLock) {
			m_drive.setDriveDirection(newDir);
		}
	}
	
	
	/**
	 * Provide traditional tank steering using the stored robot configuration. This function 
	 * lets you directly provide joystick values from any source.
	 * 
	 * Note that joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right.
	 *
	 * @param leftValue  The value of the left stick. (negative to move robot forward)
	 * @param rightValue The value of the right stick. (negative to move robot forward)
	 */
	public void doTankDrive(double leftJoyReading, double rightJoyReading, CenterDriveMotorsBehaviorE centerMotorBehavior) {
		//let m_drive code handle proper processing of sign of the values
		m_drive.tankDrive(leftJoyReading, rightJoyReading, centerMotorBehavior);
	}

	/**
	 * Joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right. So, when
	 * we drive from joysticks, negative values move the robot forward, positive ones
	 * move it in reverse.
	 * 
	 * We provide tankDriveAtSpeed for code to use when it's calculating the actual
	 * speed rather than a joystick reading.  It will do the negation required to
	 * convert 'speed' as a human being conceives of it into the values expected
	 * by the traditional tankDrive() methods.  It also bypasses the default
	 * "Square but keep sign" work normally done to make joysticks easier to use
	 * 
	 * @param leftSpeed  (positive to move robot forward)
	 * @param rightSpeed  (positive to move robot forward)
	 */
	public void doTankDriveAtSpeed(double leftSpeed, double rightSpeed, CenterDriveMotorsBehaviorE centerMotorBehavior) {
		//let m_drive code handle proper processing of sign of the values
		m_drive.tankDriveAtSpeed(leftSpeed, rightSpeed, centerMotorBehavior);
	}


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	@Override
	public void doInstantiate() {	
		//2016 code did this stuff in roboInit
		if(m_drive==null) {
			m_motorList = new MotorConfigE[6];
			m_motorList[0] = MotorConfigE.FRONT_LEFT_MOTOR_CFG;
			m_motorList[1] = MotorConfigE.REAR_LEFT_MOTOR_CFG;
			m_motorList[2] = MotorConfigE.FRONT_RIGHT_MOTOR_CFG;
			m_motorList[3] = MotorConfigE.REAR_RIGHT_MOTOR_CFG;
			m_motorList[4] = MotorConfigE.CENTER_LEFT_MOTOR_CFG;
			m_motorList[5] = MotorConfigE.CENTER_RIGHT_MOTOR_CFG;
			
			m_drive = new TmRobotDriveSixMotors(
				MotorConfigE.FRONT_LEFT_MOTOR_CFG.getMotorConfigData().getNamedIoDef(),
				MotorConfigE.REAR_LEFT_MOTOR_CFG.getMotorConfigData().getNamedIoDef(),
				MotorConfigE.FRONT_RIGHT_MOTOR_CFG.getMotorConfigData().getNamedIoDef(),
				MotorConfigE.REAR_RIGHT_MOTOR_CFG.getMotorConfigData().getNamedIoDef(),
				MotorConfigE.CENTER_LEFT_MOTOR_CFG.getMotorConfigData().getNamedIoDef(),
				MotorConfigE.CENTER_RIGHT_MOTOR_CFG.getMotorConfigData().getNamedIoDef()
				 );

			m_leftEncoder = new DriveMotorEncoder(MotorConfigE.REAR_LEFT_MOTOR_CFG,
					SdKeysE.KEY_DRIVE_ENCODER_LEFT);
			m_rightEncoder = new DriveMotorEncoder(MotorConfigE.REAR_RIGHT_MOTOR_CFG,
					SdKeysE.KEY_DRIVE_ENCODER_RIGHT);
	        m_gyro = new TmGyroADXRS453SPI(TmHdwrRoMap.RoNamedIoE.DRIVE_TRAIN_GYRO,
	        		SdKeysE.KEY_DRIVE_GYRO_ANGLE,
	        		SdKeysE.KEY_DRIVE_GYRO_RATE,
	        		SdKeysE.KEY_DRIVE_GYRO_TEMP);
			m_dem = new DrvEncoderMgmt(m_leftEncoder, m_rightEncoder);

		}
	}

	public void postToSd() {
		m_drive.driveAtCurrentSpeed();
		m_drive.postToSdCurrentDrvSpeed();
//		for(int m=0; m<MOTOR_LIST_ARRAY_LENGTH; m++) {
//			m_motorList[m].postToSdSpeed();
//			m_motorList[m].postToSdAmps();
//		}
		m_leftEncoder.postToSdPosition();
		m_rightEncoder.postToSdPosition();
		m_gyro.postToSdAngle();
	}
	
	@Override
	public void doRoboInit() {
		boolean showDbgMsgs = false;
		getDriveTrainPreferences(); //initializes m_dtPrefs
		m_dtPrefs.initializeDriveMotorTuningParms(showDbgMsgs); //initializeDriveMotorTuningParms();
		m_defaultTeleopCommand = new TmTCmdDriveWithJoysticks(TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE.getDefault());
		m_alternateTeleopCommand = new TmTCmdDriveWithJoysticks(TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE.COAST_DURING_SPINS);
		m_defaultAutonCommand = new TmACmdDoNothing();
		postToSd();
	}

	@Override
	public void doDisabledInit() {
		synchronized(m_cmdLock) {
			setDefaultCommand(null);
			m_defaultAutonCommand.cancel();
			m_defaultTeleopCommand.cancel();
			m_alternateTeleopCommand.cancel();
		}
        m_drive.stopMotors();
        m_dtPrefs.checkForTuningParmUpdates(); //updateDriveMotorTuningParms(false);
        commonInit();
	}	

	@Override
	public void doAutonomousInit() {
        m_drive.stopMotors();
        commonInit();
        synchronized(m_cmdLock) {
        	setDefaultCommand(m_defaultAutonCommand);
//        	m_defaultAutonCommand.start();
        }
	}

	@Override
	public void doTeleopInit() {
		 m_drive.stopMotors();
		 commonInit();
		 synchronized(m_cmdLock) {
			 setDefaultCommand(null);
			 m_defaultAutonCommand.cancel();
			 m_alternateTeleopCommand.cancel();
			 m_defaultTeleopCommand.cancel();
			 setDefaultCommand(m_defaultTeleopCommand);
		 }
//		 m_defaultTeleopCommand.start();
	}

	@Override
	public void doLwTestInit() {
		// TODO Auto-generated method stub		
	}
	
	public void commonInit() {
//	    boolean showDbgMsgs = false; //show debug messages
		m_drive.getSpeedLimiter().removeAllLimits();
	    m_dtPrefs.checkForTuningParmUpdates(); //updateDriveMotorTuningParms(showDbgMsgs);
	    m_dtPrefs.showTuningParms();
	    setDriveDirection(DriveTrainDirectionE.FORWARD__GEAR_PLACER_LEADS);
	    resetEncoders();
	    m_gyro.reset();
	}


	@Override
	public void doRobotPeriodic() {
		postToSd();
		m_gyro.doPeriodic();
//		m_drive.driveAtCurrentSpeed();
//		m_drive.postToSdCurrentDrvSpeed();
//		m_leftEncoder.postToSdPosition();
//		m_rightEncoder.postToSdPosition();
	}

	@Override
	public void doDisabledPeriodic() {
		m_drive.stopMotors();
	}

	@Override
	public void doAutonomousPeriodic() {
		m_drive.driveAtCurrentSpeed();
		m_drive.postToSdCurrentDrvSpeed();
		m_leftEncoder.postToSdPosition();
		m_rightEncoder.postToSdPosition();
	}

	@Override
	public void doTeleopPeriodic() {
		m_drive.driveAtCurrentSpeed();
		m_drive.postToSdCurrentDrvSpeed();
		m_leftEncoder.postToSdPosition();
		m_rightEncoder.postToSdPosition();
	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub
		
	}
	
	public enum DriveJoysticksCenterMotorOptE { DRIVE_JOYSTICKS_CENTER_MOTOR_COAST, DRIVE_JOYSTICKS_CENTER_MOTOR_MATCH_FRONT_AND_REAR; }
	public void startDriveWithJoysticksCenterMotorCoast() {
		synchronized(m_cmdLock) {
			m_alternateTeleopCommand.start(); //should stop the default teleop command automatically
		}
	}
	public void stopDriveWithJoysticksCenterMotorCoast() {
		synchronized(m_cmdLock) {
			m_alternateTeleopCommand.cancel(); //should restart the default teleop command automatically
		}
	}
	
	public class DrvEncoderMgmt {
		TmFakeable_CANTalon.ConnectedEncoder dem_leftEnc;
		TmFakeable_CANTalon.ConnectedEncoder dem_rightEnc;
		TmFakeable_CANTalon dem_leftMotorObj;
		TmFakeable_CANTalon dem_rightMotorObj;
		MotorConfigE dem_leftMtrCfg;
		MotorConfigE dem_rightMtrCfg;
		
		public double dem_leftMotorPolarityFactor;
		public double dem_rightMotorPolarityFactor;
		public int dem_leftEncoderPolarityFactor;
		public int dem_rightEncoderPolarityFactor;
		
		public double dem_leftDistanceAdjustedForPolarity;
		public double dem_rightDistanceAdjustedForPolarity;
		private double dem_leftDistance;
		private double dem_rightDistance;
		public int dem_leftPosition;
		public int dem_rightPosition;
		
		private final Object dem_lock = new Object();
		
		public DrvEncoderMgmt(TmFakeable_CANTalon.ConnectedEncoder leftEnc, TmFakeable_CANTalon.ConnectedEncoder rightEnc) {
			dem_leftEnc = leftEnc;
			dem_rightEnc = rightEnc;
			dem_leftMotorObj = leftEnc.getCanTalonMotorObj();
			dem_rightMotorObj = rightEnc.getCanTalonMotorObj();
			dem_leftMtrCfg = leftEnc.getMotorCfg();
			dem_rightMtrCfg = rightEnc.getMotorCfg();
			dem_leftMotorPolarityFactor = dem_leftMtrCfg.getNamedMotorDef().getMultiplierForDrvMtrPolarity();
			dem_rightMotorPolarityFactor = dem_rightMtrCfg.getNamedMotorDef().getMultiplierForDrvMtrPolarity();
			dem_leftEncoderPolarityFactor = leftEnc.getEncoderPolarityFactor();
			dem_rightEncoderPolarityFactor = rightEnc.getEncoderPolarityFactor();
		}
		
		public synchronized void update() {
			update(0);
		}
		public synchronized void update(int dbgMsgCnt) {
			synchronized(dem_lock) {
//			if(dbgMsgCnt>0) {
//				String msg="debug";
//			}
			//do these in quick succession to keep them in sync with each other
			//let the ConnectedEncoder get() method handle calling the motor object's getEncPosition method
			//  so that the encoder reading can be modified appropriately when necessary (see EncoderCountsCapabilityE enum)			
			dem_leftPosition = dem_leftEnc.get(); //dem_leftMotorObj.getEncPosition(dbgMsgCnt);
			dem_rightPosition = dem_rightEnc.get(); //dem_rightMotorObj.getEncPosition(dbgMsgCnt);
			
			//we can do the remaining calcs at our leisure
			dem_leftDistance = dem_leftEnc.toDistance(dem_leftPosition);
			dem_rightDistance = dem_rightEnc.toDistance(dem_rightPosition);
//			dem_leftDistanceAdjustedForPolarity = -dem_leftDistance * dem_leftMotorPolarityFactor;//The encoders give negative values when moving forward
//			dem_rightDistanceAdjustedForPolarity = -dem_rightDistance * dem_rightMotorPolarityFactor;
			//The encoders give negative values when moving forward. the encoder polarity factors handle that for us
			dem_leftDistanceAdjustedForPolarity = dem_leftDistance * dem_leftEncoderPolarityFactor * dem_leftMotorPolarityFactor;
			dem_rightDistanceAdjustedForPolarity = dem_rightDistance * dem_rightEncoderPolarityFactor * dem_rightMotorPolarityFactor;

//			if(false && dbgMsgCnt>0) {
//				P.printFrmt(-1, "drvEncUpd: l-pos=% d, r-pos=% d, l-dist=% 1.4f, r-dist=% 1.4f, l-distAdj=% 1.4f, r-distAdj=% 1.4f",
//						dem_leftPosition, dem_rightPosition, dem_leftDistance, dem_rightDistance, 
//						dem_leftDistanceAdjustedForPolarity, dem_rightDistanceAdjustedForPolarity);
//			}	
			}
		}
		
		public synchronized void reset() {
			synchronized(dem_lock) {
				//do these in quick succession to keep them in sync with each other
				dem_leftEnc.reset();
				dem_rightEnc.reset();
				this.update();			
			}
		}
//		public double getLeftDistance() { return dem_leftDistance; }
//		public double getRightDistance() { return dem_rightDistance; }
		
		public double getLeftDistanceAdjustedForPolarity() {
			return dem_leftDistanceAdjustedForPolarity; //dem_leftPolarityFactor * dem_leftDistance;
		}
		public double getRightDistanceAdjustedForPolarity() {
			return dem_rightDistanceAdjustedForPolarity; //dem_rightPolarityFactor * dem_rightDistance;
		}
		
		public double getAverageDistance() {
			double ans;
			synchronized(dem_lock) {
				ans = ((dem_leftDistance+dem_rightDistance)/2);
			}
			return ans;
		}
		
	}

//	private void resetLeftEncoder() {
//		if (m_leftEncoder != null) {
//			m_leftEncoder.reset();
//		}
//	}
//
//	private void resetRightEncoder() {
//		if (m_rightEncoder != null) {
//			m_rightEncoder.reset();
//		}
//	}

	public void resetEncoders() {
		m_dem.reset();
//		resetLeftEncoder();
//		resetRightEncoder();
	}
	
//	public double getLeftDistance() {
//		return -m_leftEncoder.getDistance();
//		//future: return m_leftEncoder.getDistanceAdjustedForPolarity();
//	}
//	
//	public double getRightDistance() {
//		return m_rightEncoder.getDistance();
//		//future: return m_rightEncoder.getDistanceAdjustedForPolarity();
//	}
//	
//	public double getAverageDistance() {
//		return ((getLeftDistance()+getRightDistance())/2);
//	}
	
	public DrvEncoderMgmt getDriveEncoderMgmtInstance() { return m_dem; }
	
	public TmRobotDriveSixMotors getChassisInstance(){ return m_drive;}
	/**
	 * if the new limit is less than the current limit, it becomes the new limit
	 * also bumps the number of speed limits in effect
	 * @param newLimit
	 * @return the current speed limit (may be less than the requested limit)
	 */
	public synchronized double restrictDriveMotorSpeed(double newLimit) {
		return m_drive.getSpeedLimiter().restrictMaxDriveSpeedAllowed(newLimit);
	}
	
	/**
	 * decrements the number of speed limits in effect. If gets to 0, restores
	 * the hardcoded default limit
	 * @return the current speed limit
	 */
	public synchronized double restoreDriveMotorSpeed() {
		return m_drive.getSpeedLimiter().restoreMaxDriveSpeedAllowed();
	}
	
	public synchronized void consMsgCurrentDriveSpeeds() {
		m_drive.consMsgCurrentDriveSpeeds();
	}
	
//	private int getLeftEncoderTicks() {
//		int value = 0;
//		if (m_leftEncoder != null) {
//			value = m_leftEncoder.get();
//		}
//		return value;
//	}
//
//	private int getRightEncoderTicks() {
//		int value = 0;
//		if (m_rightEncoder != null) {
//			value = m_rightEncoder.get();
//		}
//		return value;
//	}
	
//	private int getEncodersAvgTicks() {
//		return (getLeftEncoderTicks() + getRightEncoderTicks())/2;
//	}

}

