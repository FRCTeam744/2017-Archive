package org.usfirst.frc.tm744y17.robot.devices;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap.DsHidDefE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveTrainPreferences;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * This class is modeled after the RobotDrive class in wpilib, but only supports
 * a drive train that has six motors.
 * Also adds tankDriveAtSpeed methods that take positive values to move the robot
 * forward, negative ones to move it backwards (normal tankDrive methods expect
 * negative joystick readings for forward motion, positive ones for reverse)
 * @author JudiA
 *
 */
public class TmRobotDriveSixMotors implements MotorSafety {

	/**
	 * The location of a motor on the robot for the purpose of driving.
	 */
	public enum MotorType {
		kFrontLeft(0), kFrontRight(1), kRearLeft(2), kRearRight(3), kCenterLeft(4), kCenterRight(5);

		public final int value;

		private MotorType(int value) {
			this.value = value;
		}
	}

	public static final double kDefaultExpirationTime = 0.1;
	public static final double kDefaultSensitivity = 0.5;
	public static final double kDefaultMaxOutput = 1.0;
	protected static final int kMaxNumberOfMotors = 6;
	
	protected MotorSafetyHelper m_safetyHelper;
	TmSsDriveTrain.DrvEncoderMgmt m_dem;

	protected double m_sensitivity;
	protected static double m_maxOutput;
	protected static DriveSpeedLimits m_motorSpeedLimits;

	protected RoNamedIoE m_namedFrontLeftMotorDef;
	protected RoNamedIoE m_namedFrontRightMotorDef;
	protected RoNamedIoE m_namedRearLeftMotorDef;
	protected RoNamedIoE m_namedRearRightMotorDef;
	protected RoNamedIoE m_namedCenterLeftMotorDef;
	protected RoNamedIoE m_namedCenterRightMotorDef;
	protected SpeedController m_frontLeftMotor;
	protected SpeedController m_frontRightMotor;
	protected SpeedController m_rearLeftMotor;
	protected SpeedController m_rearRightMotor;
	protected SpeedController m_centerLeftMotor;
	protected SpeedController m_centerRightMotor;
	
//	private DriveTrainPreferences m_dtPrefs;
	
	private double m_lastLeftOutput = 0.0;
	private double m_lastRightOutput = 0.0;
	private CenterDriveMotorsBehaviorE m_lastCenterMotorBehavior;
	
	private double m_lastLeftOutputWithLimits = 0.0;
	private double m_lastRightOutputWithLimits = 0.0;
	
	private static Object m_motorOutputLock = new Object();

	
	protected final static int MOTOR_OBJ_LIST_LENGTH = 6;
	protected SpeedController[] m_motorObjList;
	
	protected boolean m_isNullMotors;
	protected boolean m_allocatedSpeedControllers;
	protected static boolean kArcadeRatioCurve_Reported = false;
	protected static boolean kTank_Reported = false;
	protected static boolean kArcadeStandard_Reported = false;
	protected static boolean kMecanumCartesian_Reported = false;
	protected static boolean kMecanumPolar_Reported = false;

	/**
	 * Constructor for TmSixMotorDrive with 6 motors specified as SpeedController objects. Speed controller
	 * input version of TmSixMotorDrive (see comments on RobotDrive constructors in the WPI library).
	 *
	 * @param rearLeftMotor   The back left SpeedController object used to drive the robot.
	 * @param frontLeftMotor  The front left SpeedController object used to drive the robot
	 * @param rearRightMotor  The back right SpeedController object used to drive the robot.
	 * @param frontRightMotor The front right SpeedController object used to drive the robot.
	 * @param centerLeftMotor  The center left SpeedController object used to drive the robot
	 * @param centerRightMotor  The center right SpeedController object used to drive the robot.
	 */
	public TmRobotDriveSixMotors(
			RoNamedIoE frontLeftMotor, RoNamedIoE rearLeftMotor, 
			RoNamedIoE frontRightMotor, RoNamedIoE rearRightMotor,
			RoNamedIoE centerLeftMotor, RoNamedIoE centerRightMotor) {
		m_namedFrontLeftMotorDef = requireNonNull(frontLeftMotor, "frontLeftMotorDef cannot be null");
		m_namedRearLeftMotorDef = requireNonNull(rearLeftMotor, "rearLeftMotorDef cannot be null");
		m_namedCenterLeftMotorDef = requireNonNull(centerLeftMotor, "centerLeftMotorDef cannot be null");
		m_namedFrontRightMotorDef = requireNonNull(frontRightMotor, "frontRightMotorDef cannot be null");
		m_namedRearRightMotorDef = requireNonNull(rearRightMotor, "rearRightMotorDef cannot be null");
		m_namedCenterRightMotorDef = requireNonNull(centerRightMotor, "centerRightMotorDef cannot be null");
		finishInstantiation(); 
	}
	
	public void finishInstantiation() {
		if(m_frontLeftMotor==null) {
		RoNamedIoE frontLeftMotor = m_namedFrontLeftMotorDef;
		RoNamedIoE rearLeftMotor = m_namedRearLeftMotorDef;
		RoNamedIoE centerLeftMotor = m_namedCenterLeftMotorDef;
		RoNamedIoE frontRightMotor = m_namedFrontRightMotorDef;
		RoNamedIoE rearRightMotor = m_namedRearRightMotorDef;
		RoNamedIoE centerRightMotor = m_namedCenterRightMotorDef;
		
//		SpeedController[] m_motorObjList;
		m_motorObjList = new SpeedController[6];
		m_motorObjList[0] = m_frontLeftMotor = requireNonNull(frontLeftMotor.getModConnIoPairObj().getMotorObject(), "frontLeftMotor cannot be null");
		m_motorObjList[1] = m_rearLeftMotor = requireNonNull(rearLeftMotor.getModConnIoPairObj().getMotorObject(), "rearLeftMotor cannot be null");
		m_motorObjList[2] = m_centerLeftMotor = requireNonNull(centerLeftMotor.getModConnIoPairObj().getMotorObject(), "centerLeftMotor cannot be null");
		m_motorObjList[3] = m_frontRightMotor = requireNonNull(frontRightMotor.getModConnIoPairObj().getMotorObject(), "frontRightMotor cannot be null");
		m_motorObjList[4] = m_rearRightMotor = requireNonNull(rearRightMotor.getModConnIoPairObj().getMotorObject(), "rearRightMotor cannot be null");
		m_motorObjList[5] = m_centerRightMotor = requireNonNull(centerRightMotor.getModConnIoPairObj().getMotorObject(), "centerRightMotor cannot be null");

//		checkForNullMotors();
		m_sensitivity = 0.15; //kDefaultSensitivity;
		m_maxOutput = kDefaultMaxOutput;
		m_motorSpeedLimits = new DriveSpeedLimits(kDefaultMaxOutput);
//		m_dtPrefs = TmSsDriveTrain.getDriveTrainPreferences();
		
		m_allocatedSpeedControllers = false;
		checkForNullMotors();
		setupMotorSafety();
		drive(0, 0, CenterDriveMotorsBehaviorE.getDefault());
		setDriveDirection(DriveTrainDirectionE.FORWARD__GEAR_PLACER_LEADS);
		
//		m_dem = TmSsDriveTrain.getInstance().getDriveEncoderMgmtInstance();
		}
	}
	
	/**
	 * we require all six motor controllers.
	 * @return true if any motor controller is null
	 */
	protected void checkForNullMotors() {
		boolean ans = false;
		for(int m=0; m < MOTOR_OBJ_LIST_LENGTH; m++) {
//			SpeedController[] m_motorObjList;
			if(m_motorObjList[m] == null) { ans = true; }
		}
		m_isNullMotors = ans;
	}
	public boolean isNullMotors() { return m_isNullMotors; }
	

	/**
	 * Drive the motors at "outputMagnitude" and "curve". Both outputMagnitude and curve are -1.0 to
	 * +1.0 values, where 0.0 represents stopped and not turning. {@literal curve < 0 will turn left
	 * and curve > 0} will turn right.
	 *
	 * <p>The algorithm for steering provides a constant turn radius for any normal speed range, both
	 * forward and backward. Increasing sensitivity causes sharper turns for fixed values of curve.
	 *
	 * <p>This function will most likely be used in an autonomous routine.
	 *
	 * @param outputMagnitude The speed setting for the outside wheel in a turn, forward or backwards,
	 *                        +1 to -1.
	 * @param curve           The rate of turn, constant for different forward speeds. Set {@literal
	 *                        curve < 0 for left turn or curve > 0 for right turn.} Set curve =
	 *                        e^(-r/w) to get a turn radius r for wheelbase w of your robot.
	 *                        Conversely, turn radius r = -ln(curve)*w for a given value of curve and
	 *                        wheelbase w.
	 */
	public synchronized void drive(double outputMagnitude, double curve, CenterDriveMotorsBehaviorE centerBehavior) {
		final double leftOutput;
		final double rightOutput;
		synchronized(m_motorOutputLock) {
			if (!kArcadeRatioCurve_Reported) {
				HAL.report(tResourceType.kResourceType_RobotDrive, getNumMotors(),
						tInstances.kRobotDrive_ArcadeRatioCurve);
				kArcadeRatioCurve_Reported = true;
			}
			if (curve < 0) {
				double value = Math.log(-curve);
				double ratio = (value - m_sensitivity) / (value + m_sensitivity);
				if (ratio == 0) {
					ratio = .0000000001;
				}
				leftOutput = outputMagnitude / ratio;
				rightOutput = outputMagnitude;
			} else if (curve > 0) {
				double value = Math.log(curve);
				double ratio = (value - m_sensitivity) / (value + m_sensitivity);
				if (ratio == 0) {
					ratio = .0000000001;
				}
				leftOutput = outputMagnitude;
				rightOutput = outputMagnitude / ratio;
			} else {
				leftOutput = outputMagnitude;
				rightOutput = outputMagnitude;
			}
			setRobotLeftRightSpeeds(leftOutput, rightOutput, centerBehavior);
		}
	}
	
	public synchronized void driveAtCurrentSpeed() {
		synchronized(m_motorOutputLock) {
			setRobotLeftRightSpeeds(m_lastLeftOutput, m_lastRightOutput, m_lastCenterMotorBehavior);
		}
	}
	
	public synchronized boolean isDriveMotorsStopped() {
		boolean ans = false;
		synchronized(m_motorOutputLock) {
//			if(m_lastLeftOutput==0.0 && m_lastRightOutput==0.0) {
			if(Tt.isWithinTolerance(m_lastLeftOutput, 0.0, 0.05) 
					&& Tt.isWithinTolerance(m_lastRightOutput, 0.0, 0.05)) {
				ans = true;
			}
		}
		return ans;
	}
	
	/**
	 * tests the actual motor speeds sent to the motor controllers
	 * @param testSpeed
	 * @return
	 */
	public synchronized boolean isDrivingBelow(double testSpeed) {
		boolean ans = false;
		synchronized(m_motorOutputLock) {
			if(Tt.isWithinTolerance(m_lastLeftOutputWithLimits, 0.0, testSpeed) 
					&& Tt.isWithinTolerance(m_lastRightOutputWithLimits, 0.0, testSpeed)) {
				ans = true;
			}
		}
		return ans;
	}
	
	public DriveSpeedLimits getSpeedLimiter() { return m_motorSpeedLimits; }
	
	public static class DriveSpeedLimits {
		private static int c_motorSpeedLimitsCnt;
		private static double c_defaultLimit = kDefaultMaxOutput;
		private static final Object c_lock = new Object();
		private static double c_currentLimit = c_defaultLimit;
		
		public DriveSpeedLimits(double defaultLimit) {
			c_defaultLimit = defaultLimit;
			c_currentLimit = c_defaultLimit;
		}
		
		public synchronized void removeAllLimits() {
			synchronized(c_lock) {
				c_motorSpeedLimitsCnt = 0;
				c_defaultLimit = kDefaultMaxOutput;
				c_currentLimit = c_defaultLimit;
			}
		}
		
		public synchronized double getMaxDriveSpeedAllowed() {
			double ans;
			synchronized(c_lock) {
				if(c_motorSpeedLimitsCnt == 0) {
					ans = c_defaultLimit;
				} else {
					ans = c_currentLimit;
				}
			}
			return ans;
		}

		public synchronized double restrictMaxDriveSpeedAllowed(double newLimit) {
			double ans;
			synchronized(c_lock) {
				synchronized(m_motorOutputLock) {
					ans = c_currentLimit;
					++c_motorSpeedLimitsCnt;
					if(newLimit < c_currentLimit) {
						c_currentLimit = newLimit;
					}
					ans = c_currentLimit;
					m_maxOutput = c_currentLimit;
				}
			}
			P.println("drivetrain speed restricted to " + c_currentLimit + " (requested limit is " + newLimit + 
							", current limit count is " + c_motorSpeedLimitsCnt + ")");
			return ans;
		}
		public synchronized double restoreMaxDriveSpeedAllowed() {
			double ans;
			synchronized(c_lock) {
				synchronized(m_motorOutputLock) {
					 ans = c_currentLimit;
					 if(c_motorSpeedLimitsCnt > 0) {
						--c_motorSpeedLimitsCnt;
						if(c_motorSpeedLimitsCnt == 0) {
							c_currentLimit = c_defaultLimit;
						}
					 }
					 ans = c_currentLimit;
					 m_maxOutput = c_currentLimit;
				}
			}
			P.println("drivetrain speed restore requested.  current limit is " + c_currentLimit + 
							", current limit count is " + c_motorSpeedLimitsCnt + ")");
			return ans;
		}
	}
	
	public synchronized void postToSdCurrentDrvSpeed() {
		synchronized(m_motorOutputLock) {
//			TmSdDbgSD.dbgPutNumber(TmMiscKeys.SdKeysE.KEY_DRIVE_LEFT_SPEED, (Math.abs(m_lastLeftOutput)<0.01) ? 98.76 : m_lastLeftOutput);
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_DRIVE_LEFT_SPEED, m_lastLeftOutputWithLimits);//m_lastLeftOutput);
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_DRIVE_RIGHT_SPEED, m_lastRightOutputWithLimits); //m_lastRightOutput);
			TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_CENTER_MOTOR_BEHAVIOR, m_lastCenterMotorBehavior.isCoastDuringSpins());
		}
	}

	/**
	 * The Drive Team asked for a button that would allow them to drive in reverse
	 * as if they were driving forward, i.e. push joysticks forward and robot drives in 
	 * reverse.  This also requires swapping "left" and "right".  
	 * We use this enum to indicate whether the robot should drive normally ("FORWARD") or 
	 * in this other "forward-reversed" mode ("REVERSE").
	 * The setRobotLeftRightSpeeds() method checks which mode we're in and does the right things.
	 * For 2017, the transmissions are in the back part of the robot; the gear placer leads the way when driving
	 * forward, the shooter leads the way when driving in reverse
	 * @author JudiA
	 *
	 */
	public enum DriveTrainDirectionE { 
		FORWARD__GEAR_PLACER_LEADS, REVERSE__SHOOTER_LEADS;
		
		public boolean isDriveDirectionForward() {
			return this.equals(DriveTrainDirectionE.FORWARD__GEAR_PLACER_LEADS);
		}
	}
	private DriveTrainDirectionE m_driveDirectionDef = DriveTrainDirectionE.FORWARD__GEAR_PLACER_LEADS;
	
	/**
	 * The Drive Team asked for a button that would allow them to drive in reverse
	 * as if they were driving forward, i.e. push joysticks forward and robot drives in 
	 * reverse.  This also requires swapping "left" and "right".  The button uses this 
	 * method to change the drive direction mode.  See the DriveTrainDirectionE for
	 * more info.
	 * @author JudiA
	 *
	 */
	public synchronized void setDriveDirection(DriveTrainDirectionE newDir) {
		if( ! m_driveDirectionDef.equals(newDir)) {
			P.println("Drive direction set to " + newDir.name());
		}
		m_driveDirectionDef = newDir;
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_DRIVE_DIRECTION_FORWARD, isDriveDirectionForward());
	}
	public boolean isDriveDirectionForward() {
		return m_driveDirectionDef.isDriveDirectionForward();
	}
	public double getDriveDirectionSpeedFactor() {
		//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
		return (m_driveDirectionDef.isDriveDirectionForward()) ? 1.0 : -1.0;
	}
	
	public enum CenterDriveMotorsBehaviorE {
		MATCH_FRONT_AND_REAR, COAST_DURING_SPINS;
		
		private CenterDriveMotorsBehaviorE() {}
		
		private static CenterDriveMotorsBehaviorE eDefault = MATCH_FRONT_AND_REAR;
		
		public static CenterDriveMotorsBehaviorE getDefault() { return eDefault; }
		
		public boolean isCoastDuringSpins() { return this.equals(COAST_DURING_SPINS); }
		public boolean isMatchFrontAndRear() { return this.equals(MATCH_FRONT_AND_REAR); }
	}

	public synchronized void setRobotLeftRightSpeeds(double leftOutput, double rightOutput) {
		setRobotLeftRightSpeeds(leftOutput, rightOutput, CenterDriveMotorsBehaviorE.getDefault());
	}
	/**
	 * Set the speed of the right and left motors. This method will figure out which motors need to
	 * have the speed "flipped".  It will also handle the "drive direction" mode (see 
	 * DriveDirectionE enum and related methods), behavior of center motors, and drive-train tuning
	 * parms (see TmSsDriveTrain.DriveTrainPreferences class).
	 * 
	 * So, if you want the robot to move forward full speed, set both speeds to +1.0.
	 * 
	 * @param leftOutput  The speed to send to the left side of the robot. (normally positive to move robot forward)
	 * @param rightOutput The speed to send to the right side of the robot. (normally positive to move robot forward)
	 */
	public synchronized void setRobotLeftRightSpeeds(double leftOutput, double rightOutput, CenterDriveMotorsBehaviorE centerBehavior) {
		if (isNullMotors()) {
			throw new NullPointerException("Null motor provided");
		}
		
		double validLeftOutput;
		double validRightOutput;
		boolean centerMotorCoast = false;
		
		synchronized(m_motorOutputLock) {
			//save the inputs so they can be used from doPeriodic() methods to ensure motor safety never times out
			m_lastLeftOutput = leftOutput;
			m_lastRightOutput = rightOutput;
			m_lastCenterMotorBehavior = centerBehavior;

			validLeftOutput = limit(leftOutput) * m_maxOutput;
			validRightOutput = limit(rightOutput) * m_maxOutput;
			
			m_lastLeftOutputWithLimits = validLeftOutput;
			m_lastRightOutputWithLimits = validRightOutput;

			double drvDirectionFactor = getDriveDirectionSpeedFactor();
			validLeftOutput *= drvDirectionFactor;
			validRightOutput *= drvDirectionFactor;
			if (!isDriveDirectionForward()){
				double switcH = validLeftOutput;
				validLeftOutput = validRightOutput;
				validRightOutput = switcH;
			}
			
			if(centerBehavior.isCoastDuringSpins()) {
				if((validLeftOutput > 0.0) && (validRightOutput < 0.0)) { centerMotorCoast = true; }
				else if((validLeftOutput < 0.0) && (validRightOutput > 0.0)) { centerMotorCoast = true; }
			}
			
			double leftSideTuningParm; // = 1.0;
			double rightSideTuningParm; // = 1.0;
			if((validLeftOutput==1.0) && (validRightOutput==1.0)) {
				String msg = "debug";
			}
			//at this point, pos values mean "forward", neg values mean "reverse"
//			if(m_namedFrontLeftMotorDef.isNegToMoveRobotForward()) {
//				if(validLeftOutput<0) {
//					leftSideTuningParm = DriveTrainPreferences.getCurTuningParmLeftForward();
//				} else {
//					leftSideTuningParm = DriveTrainPreferences.getCurTuningParmLeftReverse();
//				}
//			} else {
				if(validLeftOutput>=0) {
					leftSideTuningParm = DriveTrainPreferences.getCurTuningParmLeftForward();
				} else {
					leftSideTuningParm = DriveTrainPreferences.getCurTuningParmLeftReverse();
				}	
//			}
//			if(m_namedFrontRightMotorDef.isNegToMoveRobotForward()) {
//				if(validRightOutput<0) {
//					rightSideTuningParm = DriveTrainPreferences.getCurTuningParmRightForward();
//				} else {
//					rightSideTuningParm = DriveTrainPreferences.getCurTuningParmRightReverse();
//				}
//			} else {
				if(validRightOutput>=0) {
					rightSideTuningParm = DriveTrainPreferences.getCurTuningParmRightForward();
				} else {
					rightSideTuningParm = DriveTrainPreferences.getCurTuningParmRightReverse();
				}	
//			}

			//the motors don't all turn the same direction. Hopefully this code
			//will move the robot forward when the speeds are positive
			m_frontLeftMotor.set(m_namedFrontLeftMotorDef.getMultiplierForDrvMtrPolarity() * leftSideTuningParm * validLeftOutput);
			m_rearLeftMotor.set(m_namedRearLeftMotorDef.getMultiplierForDrvMtrPolarity() * leftSideTuningParm * validLeftOutput);
			if(centerMotorCoast) {
				m_centerLeftMotor.set(0.0);
			} else {
				m_centerLeftMotor.set(m_namedCenterLeftMotorDef.getMultiplierForDrvMtrPolarity() * leftSideTuningParm * validLeftOutput);
			}
			m_frontRightMotor.set(m_namedFrontRightMotorDef.getMultiplierForDrvMtrPolarity() * rightSideTuningParm * validRightOutput);
			m_rearRightMotor.set(m_namedRearRightMotorDef.getMultiplierForDrvMtrPolarity() * rightSideTuningParm * validRightOutput);
			if(centerMotorCoast) {
				m_centerRightMotor.set(0.0);
			} else {
				m_centerRightMotor.set(m_namedCenterRightMotorDef.getMultiplierForDrvMtrPolarity() * rightSideTuningParm * validRightOutput);
			}
			if (m_safetyHelper != null) {
				m_safetyHelper.feed();
			}
		}
		if(m_dbgCnt>0) {
			if(Math.abs(validRightOutput)>=0.2 || Math.abs(validLeftOutput)>=0.2) {
				TmSsDriveTrain ssDrive = TmSsDriveTrain.getInstance();
				m_dem = ssDrive.getDriveEncoderMgmtInstance();
				m_dem.update(m_dbgCnt);
				double lDist = m_dem.dem_leftDistanceAdjustedForPolarity; //dem_leftDistance; //ssDrive.getLeftDistance();
				double rDist = m_dem.dem_rightDistanceAdjustedForPolarity; //dem_rightDistance; //ssDrive.getRightDistance();
//				if(true || (lDist!=0.0) || (rDist!=0.0)) {
					P.printFrmt(-1, "setRobotLeftRightSpeeds(% 1.5f, % 1.5f) L-dist=% 1.5f, R-dist=% 1.5f, angle=%1.5f",
							validLeftOutput, validRightOutput,
							lDist, rDist, ssDrive.getAngle());
					m_dbgCnt--;
//				}
			}
		}
	}
	int m_dbgCnt = 50;

	public synchronized void consMsgCurrentDriveSpeeds() {
		double curLeft;
		double curRight;
		double curLeftLimited;
		double curRightLimited;
		String msg;
		synchronized(m_motorOutputLock) {
			curLeft = m_lastLeftOutput;
			curRight = m_lastRightOutput;
			curLeftLimited = m_lastLeftOutputWithLimits;
			curRightLimited = m_lastRightOutputWithLimits;
//			msg = String.format("left drive speed=%0.4f (limited to %0.4f), right=%0.4f (limited to %0.4f)",
//					curLeft, curLeftLimited, curRight, curRightLimited);
//			P.println(-1, msg);
		}
		msg = String.format("left drive speed=% 1.4f (limited to % 1.4f), right=% 1.4f (limited to % 1.4f), center motor %s ",
				curLeft, curLeftLimited, curRight, curRightLimited, m_lastCenterMotorBehavior.name());
		P.println(-1, msg);
	}

	/**
	 * Limit motor values to the -1.0 to +1.0 range.
	 */
	protected static double limit(double num) {
		if (num > 1.0) {
			return 1.0;
		}
		if (num < -1.0) {
			return -1.0;
		}
		return num;
	}



	/**
	 * Provide tank steering using the stored robot configuration. drive the robot using two joystick
	 * inputs. The Y-axis will be selected from each Joystick object.
	 *
	 * @param leftStick  The joystick to control the left side of the robot.
	 * @param rightStick The joystick to control the right side of the robot.
	 */
	//	  public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
	public synchronized void tankDrive(DsHidDefE leftStick, DsHidDefE rightStick, CenterDriveMotorsBehaviorE centerMotorBehavior) {
		tankDrive(leftStick, rightStick, HidInputValueHandlingE.SQUARE_BUT_KEEP_SIGN, centerMotorBehavior);
//		if (leftStick == null || rightStick == null) {
//			tankDrive(0, 0); //stop the robot
//			throw new NullPointerException("Null HID (DsHidDefE) provided");
//		}
//
//		try {
//			tankDrive(leftStick.getY(), rightStick.getY(), HidInputValueHandlingE.SQUARED);
//		} catch(TmInappropriateMappedIoDefException t) { 
//			String errMsg = "Stopping drive motors due to exception:" + 
//					t.toString() + Arrays.toString(t.getStackTrace());
//			System.out.println(errMsg);
//			t.printStackTrace();
//			tankDrive(0,0); //stop the robot
//		}
	}

	/**
	 * Provide tank steering using the stored robot configuration. drive the robot using two joystick
	 * inputs. The Y-axis will be selected from each Joystick object.
	 *
	 * Note that joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right.
	 *
	 * @param leftStick     The joystick to control the left side of the robot.
	 * @param rightStick    The joystick to control the right side of the robot.
	 * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
	 */
	public synchronized void tankDrive(DsHidDefE leftStick, DsHidDefE rightStick, HidInputValueHandlingE squaredInputs,
												CenterDriveMotorsBehaviorE centerMotorBehavior) {
		if (leftStick == null || rightStick == null) {
			String msg = (leftStick == null) ? "left=null " : "left=" + leftStick.name() + ", ";
			msg += (rightStick == null) ? "right=null" : "right=" + rightStick.name();
			throw new NullPointerException("Null HID (DsHidDefE) provided: " + msg );
		}
		try {
			tankDrive(leftStick.getY(), rightStick.getY(), squaredInputs, centerMotorBehavior);
		} catch(TmExceptions.InappropriateMappedIoDefEx t) { 
			String errMsg = "Stopping drive motors due to exception:" + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
			tankDrive(0,0, centerMotorBehavior); //stop the robot
		}
	}

	/**
	 * Provide tank steering using the stored robot configuration. This function lets you pick the
	 * axis to be used on each Joystick object for the left and right sides of the robot.
	 *
	 * Note that joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right.
	 *
	 * @param leftStick  The Joystick object to use for the left side of the robot.
	 * @param leftAxis   The axis to select on the left side Joystick object.
	 * @param rightStick The Joystick object to use for the right side of the robot.
	 * @param rightAxis  The axis to select on the right side Joystick object.
	 */
	//	  public void tankDrive(GenericHID leftStick, final int leftAxis, GenericHID rightStick,
	//	                        final int rightAxis) {
	//	    if (leftStick == null || rightStick == null) {
	//	      throw new NullPointerException("Null HID provided");
	//	    }
	//	    tankDrive(leftStick.getRawAxis(leftAxis), rightStick.getRawAxis(rightAxis), HidInputValueHandlingE.SQUARED);
	//	  }

	/**
	 * Provide tank steering using the stored robot configuration. This function lets you pick the
	 * axis to be used on each Joystick object for the left and right sides of the robot.
	 *
	 * Note that joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right.
	 *
	 * @param leftStick     The Joystick object to use for the left side of the robot.
	 * @param leftAxis      The axis to select on the left side Joystick object.
	 * @param rightStick    The Joystick object to use for the right side of the robot.
	 * @param rightAxis     The axis to select on the right side Joystick object.
	 * @param squaredInputs Setting this parameter to SQUARED decreases the sensitivity at lower speeds
	 */
	//	  public void tankDrive(GenericHID leftStick, final int leftAxis, GenericHID rightStick,
	//	                        final int rightAxis, HidInputValueHandlingE squaredInputs) {
	//	    if (leftStick == null || rightStick == null) {
	//	      throw new NullPointerException("Null HID provided");
	//	    }
	//	    tankDrive(leftStick.getRawAxis(leftAxis), rightStick.getRawAxis(rightAxis), squaredInputs);
	//	  }

	/**
	 * Provide traditional tank steering using the stored robot configuration. This function 
	 * lets you directly provide joystick values from any source.
	 *
	 * Note that joysticks return negative values when pushed away from you or to the left.
	 * They return positive values when pulled toward you or to the right.
	 *
	 * @param leftValue     The value of the left stick. (negative to move robot forward)
	 * @param rightValue    The value of the right stick. (negative to move robot forward)
	 * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
	 */
	public synchronized void tankDrive(double leftValue, double rightValue, HidInputValueHandlingE valueAdjType, 
											CenterDriveMotorsBehaviorE centerMotorBehavior) {

		if (!kTank_Reported) {
			HAL.report(tResourceType.kResourceType_RobotDrive, getNumMotors(),
					tInstances.kRobotDrive_Tank);
			kTank_Reported = true;
		}

		boolean squaredInputs = valueAdjType.equals(HidInputValueHandlingE.SQUARE_BUT_KEEP_SIGN);

		// square the inputs (while preserving the sign) to increase fine control
		// while permitting full power
		leftValue = limit(leftValue);
		rightValue = limit(rightValue);
		if (squaredInputs) {
			if (leftValue >= 0.0) {
				leftValue = leftValue * leftValue;
			} else {
				leftValue = -(leftValue * leftValue);
			}
			if (rightValue >= 0.0) {
				rightValue = rightValue * rightValue;
			} else {
				rightValue = -(rightValue * rightValue);
			}
		}
		//we were given joystick values; negate them before sending to motor outputs
		setRobotLeftRightSpeeds(-leftValue, -rightValue, centerMotorBehavior);
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
	public synchronized void tankDrive(double leftValue, double rightValue, CenterDriveMotorsBehaviorE centerBehavior) {
		tankDrive(leftValue, rightValue, HidInputValueHandlingE.SQUARE_BUT_KEEP_SIGN, centerBehavior);
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
	public synchronized void tankDriveAtSpeed(double leftSpeed, double rightSpeed, CenterDriveMotorsBehaviorE centerMotorBehavior) {
		//convert our inputs to joystick-style polarity, then use traditional tankDrive method
		tankDrive(-leftSpeed, -rightSpeed, HidInputValueHandlingE.USE_AS_IS, centerMotorBehavior);
	}

	public synchronized void tankDriveAtSpeed(double leftSpeed, double rightSpeed, HidInputValueHandlingE valueAdjAction, 
														CenterDriveMotorsBehaviorE centerMotorBehavior) {
		//convert our inputs to joystick-style polarity, then use traditional tankDrive method
		tankDrive(-leftSpeed, -rightSpeed, valueAdjAction, centerMotorBehavior);
	}

	public enum HidInputValueHandlingE { USE_AS_IS, SQUARE_BUT_KEEP_SIGN } 
	


	  /**
	   * Invert a motor direction. This is used when a motor should run in the opposite direction as the
	   * drive code would normally run it. Motors that are direct drive would be inverted, the drive
	   * code assumes that the motors are geared with one reversal.
	   *
	   * @param motor      The motor index to invert.
	   * @param isInverted True if the motor should be inverted when operated.
	   */
	  public void setInvertedMotor(MotorType motor, boolean isInverted) {
	    switch (motor) {
	      case kFrontLeft:
	        m_frontLeftMotor.setInverted(isInverted);
	        break;
	      case kFrontRight:
	        m_frontRightMotor.setInverted(isInverted);
	        break;
	      case kRearLeft:
	        m_rearLeftMotor.setInverted(isInverted);
	        break;
	      case kRearRight:
	        m_rearRightMotor.setInverted(isInverted);
	        break;
	      case kCenterLeft:
	        m_centerLeftMotor.setInverted(isInverted);
	        break;
	      case kCenterRight:
	        m_centerRightMotor.setInverted(isInverted);
	        break;
	      default:
	        throw new IllegalArgumentException("Illegal motor type: " + motor);
	    }
	  }

	private void setupMotorSafety() {
		m_safetyHelper = new MotorSafetyHelper(this);
		m_safetyHelper.setExpiration(kDefaultExpirationTime);
		m_safetyHelper.setSafetyEnabled(true);
	}

	protected int getNumMotors() {
		int motors = 0;
		if (m_frontLeftMotor != null) {
			motors++;
		}
		if (m_frontRightMotor != null) {
			motors++;
		}
		if (m_rearLeftMotor != null) {
			motors++;
		}
		if (m_rearRightMotor != null) {
			motors++;
		}
		if (m_centerLeftMotor != null) {
			motors++;
		}
		if (m_centerRightMotor != null) {
			motors++;
		}
		return motors;
	}

	//---------------overrides from MotorSafety--------------
	@Override
	public void setExpiration(double timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getExpiration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * the required method is 'stopMotor', but that gets confusing since
	 * it needs to stop all six motors. This is a more intuitively named
	 * wrapper for the required method
	 */
	public void stopMotors() {
		stopMotor();  //stops all the motors
	}

	@Override
	public void stopMotor() {
		synchronized(m_motorOutputLock) {
			setRobotLeftRightSpeeds(0, 0, CenterDriveMotorsBehaviorE.getDefault());
			for(int m=0; m < MOTOR_OBJ_LIST_LENGTH; m++) {
				m_motorObjList[m].stopMotor();
			}
			if (m_safetyHelper != null) {
				m_safetyHelper.feed();
			}
		}
	}

	@Override
	public void setSafetyEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSafetyEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		return Tt.extractClassName(this);
	}
	//---------------end overrides from MotorSafety--------------

}
