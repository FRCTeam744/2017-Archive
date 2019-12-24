package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_RoDigitalInput;
import org.usfirst.frc.tm744y17.robot.driverStation.TmOpIf;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TmSsClimber extends Subsystem implements TmStdSubsystemI, TmToolsI {
	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsClimber m_instance;

	public static synchronized TmSsClimber getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsClimber();
		}
		return m_instance;
	}

	private TmSsClimber() {
	}
	/*----------------end of getInstance stuff----------------*/

	private static TmCfgMotors.MotorConfigE m_climberMotorADef;
	private static TmFakeable_CANTalon m_climberMotorA;
	private static double m_climberMotorAMaxAmpsAllowed;
	private static TmCfgMotors.MotorConfigE m_climberMotorBDef;
	private static TmFakeable_CANTalon m_climberMotorB;
	private static double m_climberMotorBMaxAmpsAllowed;
	private static TmCfgMotors.MotorConfigE m_climberMotor40Def;
	private static TmFakeable_CANTalon m_climberMotor40;
	private static double m_climberMotor40MaxAmpsAllowed;
	private static TmFakeable_RoDigitalInput m_atTopLimitSwitch;
	private static double m_requestedSpeed;
	private static double m_speedSet; //the speed given to the motor set() method
	private static double m_speedWhenCurrentAboveMax;
	private static double m_maxSpeedAllowed;
	private static double m_previousRequestedSpeed;
	private static Command m_defaultTeleopCommand;
	private static boolean m_climberEnabled = false;
	private static Object m_lock = new Object();

	public static boolean isClimberEnabled() { return m_climberEnabled; }
	
	public boolean isClimberAtTop() {
		//limit switches are normally "true", "false" only when closed
		return ! m_atTopLimitSwitch.get();
	}
	
	public void stopClimber() {
		synchronized(m_lock) {
			setClimberMotorSpeed(0.0);
			m_climberEnabled = false;			
		}
	}
	
	public static void toggleClimberEnable() {
		synchronized(m_lock) {
			if(m_climberEnabled) {
				disableClimber();
			} else {
				enableClimber();
			}
		}
	}

	public static void enableClimber() {
		synchronized(m_lock) {
			m_climberEnabled = true;
			m_defaultTeleopCommand.start();
			P.println("Climber ENABLED");
			TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CLIMBER_ENABLED, m_climberEnabled);
		}
	}

	public static void disableClimber() {
		synchronized(m_lock) {
			m_climberEnabled = false;
			m_defaultTeleopCommand.cancel(); 
			P.println("Climber DISABLED");
			TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CLIMBER_ENABLED, m_climberEnabled);
		}
	}
	
	public void postToSd() {
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CLIMBER_LIMIT_SWITCH, isClimberAtTop()); //m_atToplimitSwitch.get());
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_CLIMBER_ENABLED, m_climberEnabled);
	}
	
	public class Cnst {
		public static final int MOTORS_AB_TALON_CURRENT_LIMIT = 30;
	}
	@Override
	public void doInstantiate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doRoboInit() {
		m_atTopLimitSwitch = new TmFakeable_RoDigitalInput(RoNamedIoE.CLIMBER_LIMIT_SWITCH);
		m_climberMotorADef = TmCfgMotors.MotorConfigE.CLIMBER_MOTOR_A_CFG;
		m_climberMotorA = m_climberMotorADef.getCanTalonMotorObj();
		m_climberMotorAMaxAmpsAllowed = m_climberMotorADef.getNamedMotorDef().getNamedModuleDef().getBreakerSizeDef().getMaxAmpsAllowed();
//		m_climberMotorA.setUserCurrentLimit(Cnst.MOTORS_AB_TALON_CURRENT_LIMIT); //(int)(m_climberMotorAMaxAmpsAllowed));
		m_climberMotorBDef = TmCfgMotors.MotorConfigE.CLIMBER_MOTOR_B_CFG;
		m_climberMotorB = m_climberMotorBDef.getCanTalonMotorObj();
		m_climberMotorBMaxAmpsAllowed = m_climberMotorBDef.getNamedMotorDef().getNamedModuleDef().getBreakerSizeDef().getMaxAmpsAllowed();
//		m_climberMotorB.setUserCurrentLimit(Cnst.MOTORS_AB_TALON_CURRENT_LIMIT); //(int)(m_climberMotorBMaxAmpsAllowed));
		m_climberMotor40Def = TmCfgMotors.MotorConfigE.CLIMBER_MOTOR_40_CFG;
		m_climberMotor40 = m_climberMotor40Def.getCanTalonMotorObj();
		m_climberMotor40MaxAmpsAllowed = m_climberMotor40Def.getNamedMotorDef().getNamedModuleDef().getBreakerSizeDef().getMaxAmpsAllowed();
		m_defaultTeleopCommand = new LclCmdClimbWithJoystick();
		m_speedWhenCurrentAboveMax = 1.0; //we send positive speeds to motor
		m_maxSpeedAllowed = 1.0; 
		m_previousRequestedSpeed = 0.0;
		stopClimber();
//		setClimberMotorSpeed(0.0);
//		m_climberEnabled = false;
		postToSd();
	}

	@Override
	public void doDisabledInit() {
		stopClimber();
		m_speedWhenCurrentAboveMax = 1.0; //we send positive speeds to motor
		m_maxSpeedAllowed = 1.0;
		m_previousRequestedSpeed = 0.0;
	}

	@Override
	public void doAutonomousInit() {
		stopClimber();
		m_speedWhenCurrentAboveMax = 1.0; //we send positive speeds to motor
		m_maxSpeedAllowed = 1.0;
		m_previousRequestedSpeed = 0.0;
	}

	@Override
	public void doTeleopInit() {
		stopClimber();
		m_speedWhenCurrentAboveMax = 1.0; //we send positive speeds to motor
		m_maxSpeedAllowed = 1.0;
		m_previousRequestedSpeed = 0.0;
		m_defaultTeleopCommand.start();
		postToSd();
//		TmSdDbgSD.dbgPutBoolean(SdKeysE.KEY_CLIMBER_ENABLED, m_climberEnabled);
	}

	@Override
	public void doLwTestInit() {
		// TODO Auto-generated method stub

	}
	
//	private double m_maxClimberCurrent = 0;
//	private double m_minClimberCurrent = 9999;
	@Override
	public void doRobotPeriodic() {
		refreshClimberMotorSpeed();
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

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

//	private static final double MAX_CLIMBER_MOTOR_AMPS_ALLOWED = 30.0;
	
	private synchronized void refreshClimberMotorSpeed() {
		double ampsA;
		double ampsB;
		double amps40;
//		double amps;
		double maxSpeed;
		synchronized(m_lock) {
			//OK, it looks weird....
			//the climber may have two motors on 30-Amp breakers OR one motor on a 40-Amp breaker
			//the code treats the climber as if all three motors are in use; whichever ones aren't will be "fakes
			ampsA = m_climberMotorA.getOutputCurrent();
			ampsB = m_climberMotorB.getOutputCurrent();
			amps40 = m_climberMotor40.getOutputCurrent();
//			amps = Math.max(ampsA, ampsB);
			maxSpeed = m_maxSpeedAllowed;
//			amps = Math.max(amps, amps40);
			//current reading should always be positive
//			if(ampsA > m_climberMotorAMaxAmpsAllowed) {
//				if(m_speedSet<m_speedWhenCurrentAboveMax) {
//					m_speedWhenCurrentAboveMax = m_speedSet;
//				}
//				m_speedSet *= m_climberMotorAMaxAmpsAllowed/amps;
//			}
//			else if(ampsB > m_climberMotorBMaxAmpsAllowed) {
//				if(m_speedSet<m_speedWhenCurrentAboveMax) {
//					m_speedWhenCurrentAboveMax = m_speedSet;
//				}
//				m_speedSet *= m_climberMotorBMaxAmpsAllowed/amps;
//			}
//			else if(amps40 > m_climberMotor40MaxAmpsAllowed) {
//				if(m_speedSet<m_speedWhenCurrentAboveMax) {
//					m_speedWhenCurrentAboveMax = m_speedSet;
//				}
//				m_speedSet *= m_climberMotor40MaxAmpsAllowed/amps40;
//			}
//			else {}
//			if(m_speedSet > m_speedWhenCurrentAboveMax) {
//				m_speedSet = m_speedWhenCurrentAboveMax * MAX_CLIMBER_MOTOR_AMPS_ALLOWED/amps40;
//			}
			if(ampsA > m_climberMotorAMaxAmpsAllowed) {
				if(m_speedSet<m_speedWhenCurrentAboveMax) {
					m_speedWhenCurrentAboveMax = m_speedSet;
					maxSpeed = m_climberMotorAMaxAmpsAllowed/ampsA;
				}
				if(maxSpeed<m_maxSpeedAllowed) {
					m_maxSpeedAllowed = maxSpeed;
				}
			}
			else if(ampsB > m_climberMotorBMaxAmpsAllowed) {
				if(m_speedSet<m_speedWhenCurrentAboveMax) {
					m_speedWhenCurrentAboveMax = m_speedSet;
					maxSpeed = m_climberMotorBMaxAmpsAllowed/ampsB;
				}
				if(maxSpeed<m_maxSpeedAllowed) {
					m_maxSpeedAllowed = maxSpeed;
				}
			}
			else if(amps40 > m_climberMotor40MaxAmpsAllowed) {
				if(m_speedSet<m_speedWhenCurrentAboveMax) {
					m_speedWhenCurrentAboveMax = m_speedSet;
					maxSpeed = m_climberMotor40MaxAmpsAllowed/amps40;
				}
				if(maxSpeed<m_maxSpeedAllowed) {
					m_maxSpeedAllowed = maxSpeed;
				}
			}
			else {}
			if(m_speedSet > m_maxSpeedAllowed) { //we send positive values to climber motors....
				m_speedSet = m_maxSpeedAllowed;
			}
			m_climberMotorA.set(m_speedSet);
			m_climberMotorB.set(m_speedSet);
			m_climberMotor40.set(m_speedSet);
			TmPostToSd.dbgPutNumber(SdKeysE.KEY_CLIMBER_AMPS_A, ampsA);
			TmPostToSd.dbgPutNumber(SdKeysE.KEY_CLIMBER_AMPS_B, ampsB);
			TmPostToSd.dbgPutNumber(SdKeysE.KEY_CLIMBER_AMPS_40, amps40);
			TmPostToSd.dbgPutString(SdKeysE.KEY_CLIMBER_A_MAX_MIN_AMPS, m_climberMotorA.getMaxMinOutputCurrentSummary());
			TmPostToSd.dbgPutString(SdKeysE.KEY_CLIMBER_B_MAX_MIN_AMPS, m_climberMotorB.getMaxMinOutputCurrentSummary());
			TmPostToSd.dbgPutString(SdKeysE.KEY_CLIMBER_40_MAX_MIN_AMPS, m_climberMotor40.getMaxMinOutputCurrentSummary());
			
			//this is the actual speed, <= requested speed squared <= requested speed
			TmPostToSd.dbgPutNumber(SdKeysE.KEY_CLIMBER_SPEED_WHEN_AMPS_ABOVE_MAX, m_speedWhenCurrentAboveMax);
		}
	}
	private synchronized void setClimberMotorSpeed(double speed) {
		synchronized(m_lock) {
			if(speed<0) { speed = 0; } //don't allow climber to move backwards! breaks hardware!
			m_previousRequestedSpeed = m_requestedSpeed;
			m_requestedSpeed = speed;
			m_speedSet = speed*speed;
			if(m_speedSet > m_maxSpeedAllowed) { //we send positive values to climber motors....
				m_speedSet = m_maxSpeedAllowed;
			}
			m_climberMotorA.set(m_speedSet);
			m_climberMotorB.set(m_speedSet);
			m_climberMotor40.set(m_speedSet);
		}
	}
	public synchronized double getRequestedClimberMotorSpeed() {return m_requestedSpeed; }
	public synchronized double getActualClimberMotorSpeed() { return m_climberMotorA.get(); } //both motors always set to same speed....

	/**
	 * a local command to read the value of a joystick and set climber motor's speed accordingly
	 */
	public class LclCmdClimbWithJoystick extends Command {

		TmSsClimber ssClimb;
		DriverStation m_ds;

		public LclCmdClimbWithJoystick() {
			m_ds = DriverStation.getInstance();
			TmOpIf l_opif = TmOpIf.getInstance();
			ssClimb = TmSsClimber.getInstance();
			requires(ssClimb);
		}

		// Called just before this Command runs the first time
		protected void initialize() {
			P.println(Tt.extractClassName(this) + " initializing");
		}

		// Called repeatedly when this Command is scheduled to run
		protected void execute() {
			if(m_ds.isEnabled() && m_ds.isOperatorControl() && isClimberEnabled() && ! isClimberAtTop()) {
				//joysticks return -1 when pushed all the way forward, we need positive speeds for motor....
				if(TmHdwrDsMap.DsInputSourceDefE.ROPE_CLIMBING_HALF_MOTOR_SPEED_INPUT.getAnalog() < -0.100) {
					//joystick reading is in range [-1.000, -0.100)
					//the speed we request will be squared before being sent to the motor, so we use sqrt(0.5) instead of 0.5 here
					ssClimb.setClimberMotorSpeed( -1 * TmHdwrDsMap.DsInputSourceDefE.ROPE_CLIMBING_HALF_MOTOR_SPEED_INPUT.getAnalog() * Math.sqrt(0.5));					
				} else {
					ssClimb.setClimberMotorSpeed( -1 * TmHdwrDsMap.DsInputSourceDefE.ROPE_CLIMBING_MOTOR_SPEED_INPUT.getAnalog());
				}
			} else {
				ssClimb.setClimberMotorSpeed(0.0);
			}
			postToSd();
//			TmSdDbgSD.dbgPutBoolean(SdKeysE.KEY_CLIMBER_LIMIT_SWITCH, isClimberAtTop()); //m_atToplimitSwitch.get());
		}

		// Make this return true when this Command no longer needs to run execute()
		protected boolean isFinished() {
			boolean ans;
			if(m_ds.isDisabled()) {
				ans = true;
				ssClimb.setClimberMotorSpeed(0.0);
			} else {
				ans = false;
			}
			return ans;
		}

		// Called once after isFinished returns true
		protected void end() {
			P.println(Tt.extractClassName(this) + " ending");
		}

		// Called when another command which requires one or more of the same
		// subsystems is scheduled to run
		protected void interrupted() {
			P.println(Tt.extractClassName(this) + " interrupted");

		}
	}

}
