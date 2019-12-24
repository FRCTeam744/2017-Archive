package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys;
import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefKeysE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon.EncoderCountsCapabilityE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon.EncoderPolarityE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_Relay;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TmSsShooter extends Subsystem implements TmStdSubsystemI, TmToolsI {

	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsShooter m_instance;
	public static synchronized TmSsShooter getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsShooter();
		}
		return m_instance;
	}
	/*----------------end of getInstance stuff----------------*/

	private static ShooterDrumMgmt m_drumMgr;
	private static ShooterTriggerMgmt m_triggerMgr;	
	private static ShooterAbacusMgmt m_abacusMgr;
	
	private static ShooterStateMachine m_steMach;
	private static TmSsDriveTrain m_driveSs;

	private static double m_maxDriveSpeedAllowed;
	
	private static boolean m_encoderIsUsable;
	private static boolean m_drumUsesTalonPid;
	private static boolean m_allowChangeTalonCntlMode;
	
	public static class Cnst {
		public static final boolean DEFAULT_ENCODER_IS_USABLE_STATE = false;
		public static final boolean DEFAULT_ALLOW_CHANGE_TALON_CNTL_MODE = false;
		public static final boolean DEFAULT_DRUM_USES_TALON_PID = true;
		
		public static final double MAX_DRIVE_SPEED_ALLOWED = 0.25; //0.2;
		
		//this is the initial delay from time drum is turned on until trigger is turned on
		//   (ShooterTriggerMgmt will handle the trigger pulse times)
		public static final double SHOOTER_DRUM_TO_FEEDER_TRIGGER_DELAY_SECS = 0.500; //0.300; //0.250;

	}
	
	private TmSsShooter() {
		m_driveSs = TmSsDriveTrain.getInstance();
		
		m_drumMgr = new ShooterDrumMgmt();
		m_triggerMgr = new ShooterTriggerMgmt();
		m_abacusMgr = new ShooterAbacusMgmt();
		
		m_steMach = new ShooterStateMachine();
		
		m_maxDriveSpeedAllowed = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_DRIVE_SPEED_LIMITER, Cnst.MAX_DRIVE_SPEED_ALLOWED, 
				m_dbgPrtFlgs, PrefCreateE.CREATE_AS_NEEDED);
	}

	/**
	 * allows us to pick higher motor speed for more vertical shooting or lower
	 * speed for more distance
	 * @author JudiA
	 *
	 */
	public enum ShooterMotorHiLoE { HIGH_SPEED, LOW_SPEED;
		public boolean isLow() {
			return isLow(this);
		} 
		public static boolean isLow(ShooterMotorHiLoE testVal) {
			boolean ans = false;
			if(testVal.equals(LOW_SPEED)) { ans = true; }
			return ans;
		} 
	}
	

	/**
	 * service routines for use by commands
	 */
	public synchronized void setToUseHighSpeed() {
		m_drumMgr.setToUseHighSpeed();
	}
	public synchronized void setToUseLowSpeed() {
		m_drumMgr.setToUseLowSpeed();
	}
	
	public static int m_dbgPrtFlgs = 0; //0=no debug msgs
	public static void setDbgPrintFlags(int flags) { m_dbgPrtFlgs = flags; }

	public synchronized void manualToggleDrum() {
		synchronized(m_steMach.sm_lock) {
			P.println(m_dbgPrtFlgs,"shooter manualToggleMotor: m_steMach.isActiveManual()=" + m_steMach.isActiveManual());
			if(m_steMach.isActiveManual()) {
				if(m_drumMgr.isDrumOnManual()) {
					m_drumMgr.drumOffManual();
				} else {
					m_drumMgr.drumOnManual();
				}
			}
		}
	}
	public synchronized void manualToggleTrigger() {
		synchronized(m_steMach.sm_lock) {
			P.println(m_dbgPrtFlgs,"shooter manualToggleTrigger: m_steMach.isActiveManual()=" + m_steMach.isActiveManual()
							+ " m_triggerMgr.tm_trigSteMach.isActiveManual()=" + m_triggerMgr.tm_trigSteMach.isActiveManual()
							+ " m_triggerMgr.tm_trigSteMach.isMotorOnManual()=" + m_triggerMgr.tm_trigSteMach.isMotorOnManual() );
			if(m_steMach.isActiveManual()) {
				if(m_triggerMgr.tm_trigSteMach.isActiveManual()) {
					if(m_triggerMgr.tm_trigSteMach.isMotorOnManual()) {
						m_triggerMgr.tm_trigSteMach.setMotorOffManual();
					} else {
						m_triggerMgr.tm_trigSteMach.setMotorOnManual();
					}
				}
			}
		}
	}
	public synchronized void manualToggleAbacus() {
		synchronized(m_steMach.sm_lock) {
			P.println(m_dbgPrtFlgs,"shooter manualToggleAbacus: m_steMach.isActiveManual()=" + m_steMach.isActiveManual()
			+ " m_abacusMgr.am_abacusSteMach.isActiveManual()=" + m_abacusMgr.am_abacusSteMach.isActiveManual()
			+ " m_abacusMgr.am_abacusSteMach.isMotorOnManual()=" + m_abacusMgr.am_abacusSteMach.isMotorOnManual() );
			if(m_steMach.isActiveManual()) {
				if(m_abacusMgr.am_abacusSteMach.isActiveManual()) {
					if(m_abacusMgr.am_abacusSteMach.isMotorOnManual()) {
						m_abacusMgr.am_abacusSteMach.setMotorOffManual();
					} else {
						m_abacusMgr.am_abacusSteMach.setMotorOnManual();
					}
				}
			}
		}
	}
	
	public synchronized void startManual() {
		P.println(m_dbgPrtFlgs, "shooter startManual"); 
		m_steMach.startManual();
		m_drumMgr.startManualEnableDrum();
		m_triggerMgr.startManual();
		m_abacusMgr.startManual(); //startManualEnableAbacus();
	}

	public synchronized void start() {
		P.println(m_dbgPrtFlgs, "shooter start"); 
		m_steMach.start();
		m_triggerMgr.start();
	}

	public synchronized void stop() {
		P.println(m_dbgPrtFlgs, "shooter startManual"); 
		m_steMach.stop();
		m_triggerMgr.stop();
	}
	
	public synchronized boolean isShooterSsOn() {
		boolean ans;
		synchronized(m_steMach.sm_lock) {
			ans = (m_steMach.isActiveAuto() || m_steMach.isActiveManual());
		}
		return ans;
	}

	public synchronized boolean isShooterSsActiveManual() {
		boolean ans;
		synchronized(m_steMach.sm_lock) {
			ans = m_steMach.isActiveManual();
		}
		return ans;
	}

	@Override
	public void doInstantiate() {
		m_drumMgr.doInstantiate();
		m_triggerMgr.doInstantiate();
		m_abacusMgr.doInstantiate();
	}

	@Override
	public void doRoboInit() {
		m_drumMgr.doRoboInit();
		m_triggerMgr.doRoboInit();
		m_abacusMgr.doRoboInit();
	}

	@Override
	public void doDisabledInit() {
		m_steMach.stop();
		m_drumMgr.doDisabledInit();
		m_triggerMgr.doDisabledInit();
		m_abacusMgr.doDisabledInit();
	}

	@Override
	public void doAutonomousInit() {
		m_steMach.stop();
		m_drumMgr.doAutonomousInit();
		m_triggerMgr.doAutonomousInit();
		m_abacusMgr.doAutonomousInit();
	}

	@Override
	public void doTeleopInit() {
		m_steMach.stop();
		m_drumMgr.doTeleopInit();
		m_triggerMgr.doTeleopInit();
		m_abacusMgr.doTeleopInit();
	}

	@Override
	public void doLwTestInit() {
		m_drumMgr.doLwTestInit();
		m_triggerMgr.doLwTestInit();
		m_abacusMgr.doLwTestInit();
	}

	@Override
	public void doRobotPeriodic() {
		m_drumMgr.doRobotPeriodic();
		m_triggerMgr.doRobotPeriodic();
		m_abacusMgr.doRobotPeriodic();
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_SHOOTER_IS_ACTIVE, isShooterSsOn());
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_SHOOTER_IS_MANUAL_MODE, m_steMach.isActiveManual());
	}

	@Override
	public void doDisabledPeriodic() {
		m_steMach.doPeriodic();
		
		m_drumMgr.doDisabledPeriodic();
		m_triggerMgr.doDisabledPeriodic();
		m_abacusMgr.doDisabledPeriodic();
	}

	@Override
	public void doAutonomousPeriodic() {
		m_steMach.doPeriodic();
		
		m_drumMgr.doAutonomousPeriodic();
		m_triggerMgr.doAutonomousPeriodic();
		m_abacusMgr.doAutonomousPeriodic();
	}

	@Override
	public void doTeleopPeriodic() {
		m_steMach.doPeriodic();
		
		m_drumMgr.doTeleopPeriodic();
		m_triggerMgr.doTeleopPeriodic();
		m_abacusMgr.doTeleopPeriodic();
	}

	@Override
	public void doLwTestPeriodic() {
		m_drumMgr.doLwTestPeriodic();
		m_triggerMgr.doLwTestPeriodic();
		m_abacusMgr.doLwTestPeriodic();
	}
	
	private enum ShooterStateMachineE {
		INACTIVE, START, WAIT_FOR_RPS_IN_RANGE, 
		ACTIVATE_ABACUS, ACTIVATE_TRIGGER, MAINTAIN_RPS, STOP, STOP_AND_RESTART,
		START_MANUAL, MANUAL, MANUAL_TO_START, AUTO_TO_MANUAL, STOP_MANUAL  
	}
	private class ShooterStateMachine {
		private ShooterStateMachineE sm_state;
		private Object sm_lock = new Object();
		public ShooterStateMachine() {
			sm_state = ShooterStateMachineE.INACTIVE;
		}
		public boolean isActiveManual() {
			boolean ans;
			synchronized(sm_lock) {
				switch(sm_state) {
//				case AUTO_TO_MANUAL:
				case MANUAL:
					ans = true;
					break;
				default:
					ans = false;
					break;
				}
			}
			return ans;
		}
		public boolean isActiveAuto() {
			boolean ans;
			synchronized(sm_lock) {
				switch(sm_state) {
				case INACTIVE:
				case START_MANUAL:
				case MANUAL:
					ans = false;
					break;
				default:
					ans = true;
					break;
				}
			}
			return ans;
		}
		public void stop() {
			synchronized(sm_lock) {
				String stopMsg = "Shooter state machine stopping (from ste=" + sm_state.name() + ") stopping";
				P.println(m_dbgPrtFlgs, stopMsg);
				switch(sm_state) {
				case INACTIVE:
				case STOP:
					//nothing to do
					break;
				case MANUAL:
					P.println(stopMsg);
					sm_state = ShooterStateMachineE.STOP_MANUAL;
					break;
				default:
					P.println(stopMsg);
					sm_state = ShooterStateMachineE.STOP;
					break;
				}
			}
		}
		public void start() {
			synchronized(sm_lock) {
				String entrySte=sm_state.name();
				switch(sm_state) {
				case INACTIVE:
					if(m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
						P.println("Shooter state machine starting (from ste" + entrySte +")");
						sm_state = ShooterStateMachineE.START;
					} else {
						String msg = "Shooter state machine not started: robot is driving above " + m_maxDriveSpeedAllowed;
						P.println(msg);
					}
					break;
				case STOP:
					if(m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
						P.println("Shooter state machine restarting");
						sm_state = ShooterStateMachineE.STOP_AND_RESTART;
					} else {
						String msg = "Shooter state machine not started: robot is driving above " + m_maxDriveSpeedAllowed;
						P.println(msg);
					}
					break;
				case START_MANUAL:
				case MANUAL:
					if(m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
						P.println("Shooter state machine switching from manual to auto");
						sm_state = ShooterStateMachineE.MANUAL_TO_START;
					} else {
						String msg = "Shooter state machine remaining in MANUAL mode: robot is driving above " + m_maxDriveSpeedAllowed;
						P.println(msg);
					}
					break;
				default:
					//already running, do nothing
					break;
				}
			}
		}
		public void startManual() {
			synchronized(sm_lock) {
				switch(sm_state) {
				case INACTIVE:
					P.println("Shooter state machine starting MANUAL mode");
					sm_state = ShooterStateMachineE.MANUAL;
					break;
				case MANUAL:
					P.println("Shooter state machine remaining in MANUAL mode");
					break;
				default:
					P.println("Shooter state machine switching to MANUAL mode");
					sm_state = ShooterStateMachineE.AUTO_TO_MANUAL;
					break;
				}
			}
		}
		public void doPeriodic() {
			synchronized(sm_lock) {
				if(isActiveAuto() && ! m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
					String msg = "Shooter state machine (ste=" + sm_state.name() + ") stopping: robot is driving above " + m_maxDriveSpeedAllowed;
					P.println(msg);
					stop();
				}
				run();
			}
		}
		
		public void run() {
			synchronized(sm_lock) {
				ShooterStateMachineE prevState = sm_state;
				String runMsg = sm_state.name();
				if(! sm_state.equals(ShooterStateMachineE.INACTIVE)) {
//					P.println(m_dbgPrtFlgs,"shooter steMach run(): ste=" + runMsg
//							+ " drum spd=" + TmSsShooter.m_drumMgr.mm_drumMotorSpeed + " (" + TmSsShooter.m_drumMgr.mm_currentRps + " RPS)");
					P.printFrmt(m_dbgPrtFlgs,"shooter steMach run(): ste=%s drum spd=% 1.3f (% 1.3f RPS)",
							runMsg, TmSsShooter.m_drumMgr.mm_drumMotorSpeed, TmSsShooter.m_drumMgr.mm_currentRps);
				}
				switch(sm_state) {
				case INACTIVE:
					break;
				case START:
					m_drumMgr.startImmediately();
					m_driveSs.restrictDriveMotorSpeed(m_maxDriveSpeedAllowed);
					sm_state = ShooterStateMachineE.WAIT_FOR_RPS_IN_RANGE;
					break;
				case WAIT_FOR_RPS_IN_RANGE:
					if(m_drumMgr.mm_drumMotor.get() != 0.0) {
						String msg = "time to debug";
					}
					if(m_drumMgr.isRpsInTargetRange()) {
						sm_state = ShooterStateMachineE.ACTIVATE_ABACUS;
					}
					break;
				case ACTIVATE_ABACUS:
					sm_state = ShooterStateMachineE.ACTIVATE_TRIGGER;
					//fallthru //break;
				case ACTIVATE_TRIGGER:
					m_abacusMgr.start();
					m_triggerMgr.start();
					sm_state = ShooterStateMachineE.MAINTAIN_RPS;
					break;
				case MAINTAIN_RPS:
					m_drumMgr.adjustMotorSpeedMaintainRps();
					//only way out is if periodic code sees robot driving or command says 'stop'
					break;
				case AUTO_TO_MANUAL:
					m_drumMgr.stopImmediately();
					m_abacusMgr.stop();
					m_triggerMgr.stop();
					sm_state = ShooterStateMachineE.START_MANUAL;
					break;
				case START_MANUAL:
					m_drumMgr.startManualEnableDrum();
					m_abacusMgr.startManual(); //.startManualEnableAbacus();
					m_triggerMgr.startManual();
					sm_state = ShooterStateMachineE.MANUAL;
					break;
				case MANUAL:
					break;
				case MANUAL_TO_START:
//					m_drumMgr.stopImmediately();
					m_abacusMgr.stop();
					m_triggerMgr.stop();
					sm_state = ShooterStateMachineE.START;
					break;
				case STOP_AND_RESTART:
					m_drumMgr.stopImmediately();
					m_abacusMgr.stop();
					m_triggerMgr.stop();
					sm_state = ShooterStateMachineE.START;
					break;
				case STOP_MANUAL:
				case STOP:
				default:
					m_drumMgr.stopImmediately();
					m_abacusMgr.stop();
					m_triggerMgr.stop();
					m_driveSs.restoreDriveMotorSpeed();
					sm_state = ShooterStateMachineE.INACTIVE;
					break;
				}	
				if( ! prevState.equals(sm_state)) {
					P.println("Shooter state machine changed states from " + prevState.name() + " to " + sm_state.name());
				}
			}
		}
	}
	
	public static class ShooterDrumMgmt implements TmStdSubsystemI {
		
		public static class DrumCnst {
			// *-------------------Shooter drum motor
			private static final double SHOOTER_AXLE_MAX_RPM = 5200; // CONFIG_ME
			private static final double SHOOTER_AXLE_RPM_TOLERANCE = 75; //90; //30;
			public static final double SHOOTER_AXLE_MAX_RPS = SHOOTER_AXLE_MAX_RPM / 60;
			public static final double SHOOTER_AXLE_RPS_TOLERANCE = SHOOTER_AXLE_RPM_TOLERANCE / 60;
			public static final double SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE_FACTOR = 2;
//			public static final double SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE = 2 * SHOOTER_AXLE_RPS_TOLERANCE;
			public static final double SHOOTER_AXLE_LOW_SPEED_FACTOR_OF_MAX = 0.80; //TUNE_ME

			public static final double SHOOTER_TALON_PID_SETPOINT_LOW = 3000.0;
			public static final double SHOOTER_TALON_PID_SETPOINT_HIGH = SHOOTER_TALON_PID_SETPOINT_LOW;
			
			public static final int SHOOTER_ENCODER_COUNTS_PER_REVOLUTION = 4096;
			public static final double SHOOTER_ENCODER_FEET_PER_REVOLUTION = 0.0;
			
			//53.0 was measured for low speed shooting, 66.25 calculated from ((53/69.3333)*86.66666667)
			public static final double SHOOTER_AXLE_TARGET_RPS_LOW = 55.0; //SHOOTER_AXLE_MAX_RPS * SHOOTER_AXLE_LOW_SPEED_FACTOR_OF_MAX; //TUNE_ME
			public static final double SHOOTER_AXLE_TARGET_RPS_HIGH = 66.25; //SHOOTER_AXLE_MAX_RPS; //TUNE_ME

			public static final double SHOOTER_MOTOR_TARGET_SPEED_LOW = 1.0 * SHOOTER_AXLE_LOW_SPEED_FACTOR_OF_MAX; //TUNE_ME //can be overridden by preferences	
			public static final double SHOOTER_MOTOR_TARGET_SPEED_HIGH = 1.0; //TUNE_ME //can be overridden by preferences
			
//			public static  final double SHOOTER_DRUM_TO_FEEDER_TRIGGER_DELAY_SECS = 0.25;

			private static final double SHOOTER_REDUCE_RPS_FACTOR = 0.99; //TUNE_ME
			private static final double SHOOTER_INCREASE_RPS_FACTOR = 1.01; //TUNE_ME
			private static final double SHOOTER_BATTERY_COMPENSATION_ADJUSTMENT = 1.0; //CONFIG_ME

			/** see Talon SRX Software Reference Manual.pdf: Ramp Rate is the rate at which voltage is allowed
			 *  to change.  Values less than 1.173 V/sec will result in no ramping.
			 */
			private static final double SHOOTER_MOTOR_VOLTAGE_RAMP_RATE_V_PER_SEC = 24; //CONFIG_ME

		}
		
		private ShooterMotorHiLoE mm_motorHiLoSelection = ShooterMotorHiLoE.LOW_SPEED;
		private TmCfgMotors.MotorConfigE mm_motorDef;
		private TmFakeable_CANTalon mm_drumMotor;
		private TmFakeable_CANTalon.ConnectedEncoder mm_encoder;
		private boolean mm_isRunningAutoOrManual = false;
		private boolean mm_isManual = false;
		
		private Timer mm_timer = new Timer();
		private Object mm_lock = new Object();
		
		
		private double mm_targetMotorSpeedHigh = DrumCnst.SHOOTER_MOTOR_TARGET_SPEED_HIGH;
		private double mm_targetMotorSpeedLow = DrumCnst.SHOOTER_MOTOR_TARGET_SPEED_LOW;
		private double mm_targetDrumMotorSpeed = mm_targetMotorSpeedLow;
		
		/** 
		 * target motor speed adjusted slightly to increase/decrease actual RPS
		 */
		private double mm_adjustedTargetMotorSpeed = mm_targetMotorSpeedLow;
		private double mm_drumMotorSpeed = 0.0;
		
		private double mm_targetTalonPidSetpoint = DrumCnst.SHOOTER_TALON_PID_SETPOINT_LOW;
		private double mm_talonPidSetpoint = 0.0;
		private double mm_defaultTargetRpsHigh = DrumCnst.SHOOTER_AXLE_TARGET_RPS_HIGH;
		private double mm_defaultTargetRpsLow = DrumCnst.SHOOTER_AXLE_TARGET_RPS_LOW;
		private double mm_rpsTolerance = DrumCnst.SHOOTER_AXLE_RPS_TOLERANCE;
		private double mm_targetRps = mm_defaultTargetRpsLow;
		private double mm_currentRps = 0.0;
		
		public ShooterDrumMgmt() {
			mm_motorDef = TmCfgMotors.MotorConfigE.SHOOTER_MOTOR_CFG;
			mm_drumMotor = mm_motorDef.getCanTalonMotorObj();
			if(mm_drumMotor==null) {
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("TmCfgMotors..." + mm_motorDef.name() + " is not a TmFakeable_CANTalon");
			}
			mm_encoder = mm_drumMotor.new ConnectedEncoder(mm_motorDef, null, DrumCnst.SHOOTER_AXLE_MAX_RPS, 
									DrumCnst.SHOOTER_ENCODER_COUNTS_PER_REVOLUTION, DrumCnst.SHOOTER_ENCODER_FEET_PER_REVOLUTION,
									EncoderPolarityE.OPPOSITE_OF_MOTOR, 
									EncoderCountsCapabilityE.ABSOLUTE_USED_AS_RELATIVE);
		}
		
		public void doInstantiate() {}
		
		public void doRoboInit() {
			P.println("Shooter defaults set to have NO ENCODER and to skip switching drum to Voltage mode");
			m_encoderIsUsable = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_ENCODER_IS_USABLE, Cnst.DEFAULT_ENCODER_IS_USABLE_STATE, 
					m_dbgPrtFlgs, PrefCreateE.CREATE_AS_NEEDED);
			m_drumUsesTalonPid = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_DRUM_USES_TALON_PID, Cnst.DEFAULT_DRUM_USES_TALON_PID, 
					m_dbgPrtFlgs, PrefCreateE.CREATE_AS_NEEDED);
			m_allowChangeTalonCntlMode = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_CHANGE_TALON_MODE, false, 
					m_dbgPrtFlgs, PrefCreateE.CREATE_AS_NEEDED);
			if(m_allowChangeTalonCntlMode) { mm_drumMotor.changeControlMode(TalonControlMode.Voltage); }
			
			mm_timer.start();
			mm_timer.reset();
			
			mm_drumMotorSpeed = 0.0;
			mm_drumMotor.set(mm_drumMotorSpeed);			
			if(m_allowChangeTalonCntlMode) { mm_drumMotor.setVoltageCompensationRampRate(DrumCnst.SHOOTER_MOTOR_VOLTAGE_RAMP_RATE_V_PER_SEC); }

			//override hardcoded info from preferences, create preferences if they don't already exist
			mm_defaultTargetRpsHigh = Tt.getPreference(PrefKeysE.KEY_SHOOTER_DEFAULT_TARGET_RPS_HIGH, mm_defaultTargetRpsHigh, -1, PrefCreateE.CREATE_AS_NEEDED);
			mm_defaultTargetRpsLow = Tt.getPreference(PrefKeysE.KEY_SHOOTER_DEFAULT_TARGET_RPS_LOW, mm_defaultTargetRpsLow, -1, PrefCreateE.CREATE_AS_NEEDED);
			mm_targetMotorSpeedHigh = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TARGET_MOTOR_SPEED_HIGH, mm_targetMotorSpeedHigh, -1, PrefCreateE.CREATE_AS_NEEDED);
			mm_targetMotorSpeedLow = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TARGET_MOTOR_SPEED_LOW, mm_targetMotorSpeedLow, -1, PrefCreateE.CREATE_AS_NEEDED);
			mm_rpsTolerance = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TARGET_RPS_TOLERANCE, mm_rpsTolerance, -1, PrefCreateE.CREATE_AS_NEEDED);
			
			mm_targetTalonPidSetpoint = DrumCnst.SHOOTER_TALON_PID_SETPOINT_LOW;
			mm_talonPidSetpoint = 0.0;

			mm_targetRps = mm_defaultTargetRpsLow;  //mm_motorHiLoSelection = ShooterMotorHiLoE
			mm_targetDrumMotorSpeed = mm_targetMotorSpeedLow;
			mm_adjustedTargetMotorSpeed = mm_targetDrumMotorSpeed;
			
			mm_isRunningAutoOrManual = false;
			mm_isManual = false;
			
			if(m_drumUsesTalonPid) {
				mm_drumMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);	
				mm_drumMotor.reverseSensor(false);
				mm_drumMotor.configVoltageOutputs();
				mm_drumMotor.setPidGains(0.035, 0.12, 0, 2); //mm_drumMotor.setPidGains(0.035, 0.12, 0, 2);
			}

			postToSdShooterDrumMgmt();
		}
		
		public void doDisabledInit() {
			mm_drumMotorSpeed = 0.0;
			mm_drumMotor.set(mm_drumMotorSpeed);
			P.println("SHOOTER ENCODER " + (m_encoderIsUsable ? "is" : "is NOT") + "enabled");
		}
		public void doAutonomousInit() {
			commonEnabledInit();
		}
		public void doTeleopInit() {
			commonEnabledInit();
		}		
		public void commonEnabledInit() {
			stopImmediately();
			mm_encoder.reset();
			
			mm_timer.reset();
			
			synchronized(mm_lock) {
				//pick up any changes to preferences
				double dfltTargRpsHigh = mm_defaultTargetRpsHigh;
				double dfltTargRpsLow = mm_defaultTargetRpsLow;
				double targMtrSpdHigh = mm_targetMotorSpeedHigh;
				double targMtrSpdLow = mm_targetMotorSpeedLow;
				double rpsTolerance = mm_rpsTolerance;
				mm_defaultTargetRpsHigh = Tt.getPreference(PrefKeysE.KEY_SHOOTER_DEFAULT_TARGET_RPS_HIGH, mm_defaultTargetRpsHigh, -1, PrefCreateE.DO_NOT_CREATE);
				mm_defaultTargetRpsLow = Tt.getPreference(PrefKeysE.KEY_SHOOTER_DEFAULT_TARGET_RPS_LOW, mm_defaultTargetRpsLow, -1, PrefCreateE.DO_NOT_CREATE);
				mm_targetMotorSpeedHigh = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TARGET_MOTOR_SPEED_HIGH, mm_targetMotorSpeedHigh, -1, PrefCreateE.DO_NOT_CREATE);
				mm_targetMotorSpeedLow = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TARGET_MOTOR_SPEED_LOW, mm_targetMotorSpeedLow, -1, PrefCreateE.DO_NOT_CREATE);
				m_encoderIsUsable = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_ENCODER_IS_USABLE, Cnst.DEFAULT_ENCODER_IS_USABLE_STATE, 
						m_dbgPrtFlgs, PrefCreateE.DO_NOT_CREATE);
//				m_drumUsesTalonPid = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_DRUM_USES_TALON_PID, Cnst.DEFAULT_DRUM_USES_TALON_PID, 
//						m_dbgPrtFlgs, PrefCreateE.DO_NOT_CREATE);

				if( dfltTargRpsHigh != mm_defaultTargetRpsHigh ) {
					P.println("shooter default target RPS high changed from " + dfltTargRpsHigh + " to " + mm_defaultTargetRpsHigh);
				}
				if( dfltTargRpsLow != mm_defaultTargetRpsLow ) {
					P.println("shooter default target RPS low changed from " + dfltTargRpsLow + " to " + mm_defaultTargetRpsLow);
				}
				if( targMtrSpdHigh != mm_targetMotorSpeedHigh ) {
					P.println("shooter target motor speed high changed from " + targMtrSpdHigh + " to " + mm_targetMotorSpeedHigh);
				}
				if( targMtrSpdLow != mm_targetMotorSpeedLow ) {
					P.println("shooter target motor speed low changed from " + targMtrSpdLow + " to " + mm_targetMotorSpeedLow);
				}
				P.println("SHOOTER ENCODER " + (m_encoderIsUsable ? "is" : "is NOT") + "enabled");

				mm_targetRps = mm_defaultTargetRpsLow;  //mm_motorHiLoSelection == ShooterMotorHiLoE.;
				mm_targetDrumMotorSpeed = mm_targetMotorSpeedLow;
				mm_adjustedTargetMotorSpeed = mm_targetDrumMotorSpeed;
				mm_drumMotorSpeed = 0.0;
				
				mm_isRunningAutoOrManual = false;
				mm_isManual = false;
			}
		}
		
		@Override
		public void doLwTestInit() {}

		public void doRobotPeriodic() {
		}
		public void doDisabledPeriodic() {
			mm_drumMotorSpeed = 0.0;
			mm_drumMotor.set(mm_drumMotorSpeed);
		}
		public void doAutonomousPeriodic() {
			commonEnabledPeriodic();
		}
		public void doTeleopPeriodic() {
			commonEnabledPeriodic();
		}
		public void commonEnabledPeriodic() {
			synchronized(mm_lock) {
				updateDrumSpeedRps();
				if(m_drumUsesTalonPid) {
					runTalonSpeedController();
				} else {
					runMotorAtCurrentSpeed();
				}
				postToSdShooterDrumMgmt();
			}
		}
		@Override
		public void doLwTestPeriodic() {}
		
		private void postToSdShooterDrumMgmt() {
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_TALON_RPM, 
					(m_drumUsesTalonPid ? mm_drumMotor.getSpeed() : mm_drumMotor.getSpeed())); //0.0));
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_VELOCITY_CLOSED_LOOP_ERR,
					(m_drumUsesTalonPid ? mm_drumMotor.getClosedLoopError() : mm_drumMotor.getClosedLoopError())); //0.0));
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_MOTOR_SPEED, mm_drumMotor.get());
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_MOTOR_TARGET_SPEED, mm_targetDrumMotorSpeed);
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_AXLE_RPS, mm_currentRps);
			TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_AXLE_TARGET_RPS, mm_targetRps);	
			TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_SPEED_SELECT_IS_LOW, mm_motorHiLoSelection.isLow());
			TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_RPS_IN_TARGET_RANGE, isRpsInTargetRange());
		}
	
		/**
		 * service routines for use by commands - outer class has methods that call these
		 */
		public void setToUseHighSpeed() {
			synchronized(mm_lock) {
				mm_motorHiLoSelection = ShooterMotorHiLoE.HIGH_SPEED;
				mm_targetRps = mm_defaultTargetRpsHigh;
				mm_targetDrumMotorSpeed = mm_targetMotorSpeedHigh;
				mm_targetTalonPidSetpoint = DrumCnst.SHOOTER_TALON_PID_SETPOINT_HIGH;
				mm_adjustedTargetMotorSpeed = mm_targetDrumMotorSpeed;
				if( getInstance().isShooterSsOn()) {
					mm_drumMotorSpeed = mm_targetDrumMotorSpeed;
				}
				P.println("Shooter Drum set to HIGH Speed");
			}
		}
		public void setToUseLowSpeed() {
			synchronized(mm_lock) {
				mm_motorHiLoSelection = ShooterMotorHiLoE.LOW_SPEED;
				mm_targetRps = mm_defaultTargetRpsLow;
				mm_targetDrumMotorSpeed = mm_targetMotorSpeedLow;
				mm_targetTalonPidSetpoint = DrumCnst.SHOOTER_TALON_PID_SETPOINT_LOW;
				mm_adjustedTargetMotorSpeed = mm_targetDrumMotorSpeed;
				if( getInstance().isShooterSsOn()) {
					mm_drumMotorSpeed = mm_targetDrumMotorSpeed;
				}
				P.println("Shooter Drum set to LOW Speed");
			}
		}
		
		private void runMotorAtCurrentSpeed() {
			synchronized(mm_lock) {
//				if(mm_isRunning) { fix;
////					mm_motorSpeed = mm_adjustedTargetMotorSpeed;
//					mm_motor.set(mm_motorSpeed);
//				} else {
//					mm_motorSpeed = 0.0;
//					mm_motor.set(mm_motorSpeed);
//				}
				mm_drumMotor.set(mm_drumMotorSpeed);
			}
		}
		
		private void runTalonSpeedController() {
			synchronized(mm_lock) {
				mm_drumMotor.changeControlMode(TalonControlMode.Speed);
				mm_drumMotor.set(mm_talonPidSetpoint); //rps
			}
		}
		
		private void adjustMotorSpeedMaintainRps() {
			synchronized(mm_lock) {
				double adjMtr = Math.abs(mm_adjustedTargetMotorSpeed);
				double targMtr = Math.abs(mm_targetDrumMotorSpeed);
				double sign = (mm_targetDrumMotorSpeed>0) ? 1.0 : -1.0;
				if(isRpsTooHigh()) {
					//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
					if(adjMtr > targMtr) {
						adjMtr = targMtr;
					} else {
						adjMtr *= DrumCnst.SHOOTER_REDUCE_RPS_FACTOR; //0.99;
					}
					mm_adjustedTargetMotorSpeed = Tt.clampToFrcRange(sign * adjMtr);
				} 
				else if(isRpsTooLow()) {
					if(adjMtr < targMtr) {
						adjMtr = targMtr;
					} else {
						adjMtr *= DrumCnst.SHOOTER_INCREASE_RPS_FACTOR; //1.01;
					}
					mm_adjustedTargetMotorSpeed = Tt.clampToFrcRange(sign * adjMtr);
				}
			}
		}
		
		public void startImmediately() {
			synchronized(mm_lock) {
				mm_drumMotorSpeed = mm_targetDrumMotorSpeed;
				mm_talonPidSetpoint = mm_targetTalonPidSetpoint;
				if(m_drumUsesTalonPid) {
					mm_drumMotor.set(mm_talonPidSetpoint);
				} else {
					mm_drumMotor.set(mm_drumMotorSpeed);
				}
				mm_isRunningAutoOrManual = true;
				mm_isManual = false;
				P.println("Shooter Drum ON - speed=" + mm_drumMotorSpeed);
				mm_timer.reset();
			}
		}
		public void startManualEnableDrum() {
			synchronized(mm_lock) {
				mm_drumMotorSpeed = 0.0;
				mm_talonPidSetpoint = 0.0;
				if(m_drumUsesTalonPid) {
					mm_drumMotor.set(mm_talonPidSetpoint);
				} else {
					mm_drumMotor.set(mm_drumMotorSpeed);
				}
				mm_isRunningAutoOrManual = true;
				mm_isManual = true;
				P.println("Shooter Drum in Manual Mode - speed=" + mm_drumMotorSpeed);
			}
		}
		public void stopImmediately() {
			synchronized(mm_lock) {
				mm_drumMotorSpeed = 0.0;
				mm_talonPidSetpoint = 0.0;
				if(m_drumUsesTalonPid) {
					mm_drumMotor.set(mm_talonPidSetpoint);
				} else {
					mm_drumMotor.set(mm_drumMotorSpeed);
				}
				mm_isRunningAutoOrManual = false;
				mm_isManual = false;
				P.println("Shooter Drum OFF");
				mm_timer.reset();
			}
		}
		
		//service routines to be used by commands for manual control
		public boolean isDrumOnManual() {
			boolean ans = false;
			if(mm_isManual) {
				if(m_drumUsesTalonPid) {
					if( ! (mm_talonPidSetpoint == 0.0)) { //! (mm_motor.get() == 0.0)) { // ! Tt.isInRange(mm_motor.get(), -0.05, 0.05)) {
						ans = true;
					}
				} else {
					if( ! (mm_drumMotorSpeed == 0.0)) { //! (mm_motor.get() == 0.0)) { // ! Tt.isInRange(mm_motor.get(), -0.05, 0.05)) {
						ans = true;
					}
				}
			}
			return ans;
		}
		public void drumOnManual() {
			if(mm_isManual) {
				mm_drumMotorSpeed = mm_targetDrumMotorSpeed;
				mm_drumMotor.set(mm_drumMotorSpeed);
				mm_timer.reset();
				P.println("Shooter Drum ON Manual - speed=" + mm_drumMotorSpeed);
			}
		}
//		public void motorOnManual(double manualSpeed) {
//			if(mm_isManual) {
//				mm_motorSpeed = manualSpeed;
//				mm_motor.set(mm_motorSpeed);
//			}
//		}
//		public void motorAdjustSpeedManual() {
//			if(mm_isManual) {
//				adjustMotorSpeedMaintainRps();
//			}
//		}
		public void drumOffManual() {
			if(mm_isManual) {
				mm_drumMotorSpeed = 0.0;
				mm_drumMotor.set(mm_drumMotorSpeed);
				mm_timer.reset();
				P.println("Shooter Drum OFF Manual");
			}
		}
		
		/**
		 * assumes called only after the motor RPS has reached the target RPS at least once
		 * too see if we need to lower the motor voltage a bit
		 * @return
		 */
		private boolean isRpsTooHigh() {
			boolean ans = false;
			synchronized(mm_lock) {
				double curRps = Math.abs(mm_currentRps);
				double targRps = Math.abs(mm_targetRps);
				double sign = (mm_targetRps>0) ? 1.0 : -1.0;
				if(curRps > targRps + mm_rpsTolerance * DrumCnst.SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE_FACTOR) { //DrumCnst.SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE) {
					ans = true;
				}
			}
			return ans;
		}
		/**
		 * assumes called only after the motor RPS has reached the target RPS at least once
		 * to see if we need to raise the motor voltage a bit
		 * @return
		 */
		private boolean isRpsTooLow() {
			boolean ans = false;
			synchronized(mm_lock) {
				double curRps = Math.abs(mm_currentRps);
				double targRps = Math.abs(mm_targetRps);
				double sign = (mm_targetRps>0) ? 1.0 : -1.0;
				if(curRps < targRps - mm_rpsTolerance * DrumCnst.SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE_FACTOR) { //DrumCnst.SHOOTER_AXLE_RPS_TOO_HIGH_TOO_LOW_TOLERANCE) { fix;
					ans = true;
				}
			}
			return ans;
		}

		public boolean isRpsInTargetRange() {
			boolean ans = false;
			if(mm_currentRps > 0) {
				String msg = "time to debug";
			}
			if(Tt.isWithinTolerance(mm_currentRps, mm_targetRps, mm_rpsTolerance)) { //DrumCnst.SHOOTER_AXLE_RPS_TOLERANCE)) { fix;
				ans = true;
			}
			if( ! m_encoderIsUsable) {
				if(mm_timer.get()>Cnst.SHOOTER_DRUM_TO_FEEDER_TRIGGER_DELAY_SECS && mm_drumMotorSpeed==mm_targetDrumMotorSpeed) { ans = true; } //FIXME
			}
			return ans;
		}
		
		private synchronized void updateDrumSpeedRps() {
			double newRps;
			synchronized(mm_lock) {
				/**
				 * magnetic encoder is equivalent of a 1024CPR quadrature
				 * encoder. therefore: 1024*4 = 4096 ticks/revolution
				 * getSpeed returns ticks-per-period where 1 period=100ms
				 * periodsPerSec=10
				 */
				final double ticksPerRev = mm_encoder.getCpr(); //4096;
				final double periodsPerSec = 10;
				//final double maxTicksPerSec = DrumCnst.SHOOTER_AXLE_MAX_RPS * ticksPerRev;
				//final double maxTicksPer100ms = maxTicksPerSec / 10;
				// final double secsPerMinute = 60;
				final double conversionFactor = periodsPerSec / ticksPerRev;
				double rawSpeed; // will be ticks per 100ms period
				if(mm_drumMotorSpeed >= 0.8) {
					String msg = "debug";
				}
				rawSpeed = mm_drumMotor.getCountsPer100ms();

				newRps = rawSpeed * conversionFactor;

//				// we want to invert the RPS so that its sign matches the
//				// sign of the motor speed since
//				// that was the assumption when we originally tuned the pid.
//				// FUDGE!!!
//				ans = -ans;

				mm_currentRps = newRps;	
			}
		}

	}

	public static class ShooterTriggerMgmt implements TmStdSubsystemI {
		
		private Timer tm_timer = new Timer();
		private TmCfgMotors.MotorConfigE tm_motorDef;
		private TmFakeable_CANTalon tm_motor;
		private TmSsShooter.ShooterDrumMgmt tm_shooterMtrMgr;
		private Object tm_lock = new Object();
		private ShooterTriggerStateMachine tm_trigSteMach;
		private boolean tm_isManualMode = false;
		private double tm_targetMotorSpeed = TrigCnst.TRIGGER_TARGET_MOTOR_SPEED;
		private double tm_motorSpeed = 0.0;
		
		private int tm_dbgMsgCnt;
		
		public ShooterTriggerMgmt() {
			tm_motorDef = TmCfgMotors.MotorConfigE.SHOOTER_TRIGGER_MOTOR_CFG;
			tm_motor = tm_motorDef.getCanTalonMotorObj();
			tm_shooterMtrMgr = TmSsShooter.m_drumMgr;
			tm_trigSteMach = new ShooterTriggerStateMachine();
			if(tm_motor==null) {
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("TmCfgMotors..." + tm_motorDef.name() + " is not a TmFakeable_CANTalon");
			}
			tm_isManualMode = false;
			tm_motorSpeed = 0.0;
		}
		
		public class TrigCnst {
			public static final double TRIGGER_PULSE_ON_TIME_SECS = 0.050; //0.060; //0.070; //0.100;
			public static final double TRIGGER_PULSE_OFF_TIME_SECS = 0.05; //0.200;//400;
			
			//if we shoot too many balls to fast, they get jammed up.  Enforce a minimum time between balls.
			public static final double TRIGGER_PULSE_OFF_MIN_TIME_SECS = 0.0; //0.010; //325;
			
			public static final double TRIGGER_TARGET_MOTOR_SPEED = 1.0; //can be overridden by preferences
		}

		@Override
		public void doInstantiate() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void doRoboInit() {
			tm_timer.start();
			tm_timer.reset();
			tm_motorSpeed = 0.0;
			tm_motor.set(tm_motorSpeed);
			tm_isManualMode = false;
			tm_targetMotorSpeed = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED, 
											TrigCnst.TRIGGER_TARGET_MOTOR_SPEED, -1, PrefCreateE.CREATE_AS_NEEDED);
			tm_trigSteMach.postToSdShooterTriggerSteMach();
		}
		@Override
		public void doDisabledInit() {
			synchronized(tm_lock) {
				tm_motorSpeed = 0.0;
				tm_motor.set(tm_motorSpeed);
				tm_trigSteMach.stop();
				tm_isManualMode = false;
			}
		}
		@Override
		public void doAutonomousInit() {
			synchronized(tm_lock) {
				tm_motorSpeed = 0.0;
				tm_motor.set(tm_motorSpeed);
				tm_trigSteMach.stop();
				tm_isManualMode = false;
				tm_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED, tm_targetMotorSpeed, -1, PrefCreateE.DO_NOT_CREATE);
				tm_dbgMsgCnt = 20;
			}
		}
		@Override
		public void doTeleopInit() {
			synchronized(tm_lock) {
				tm_motorSpeed = 0.0;
				tm_motor.set(tm_motorSpeed);
				tm_trigSteMach.stop();
				tm_isManualMode = false;
				tm_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED, tm_targetMotorSpeed, -1, PrefCreateE.DO_NOT_CREATE);
				tm_dbgMsgCnt = 20;
			}
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
			tm_trigSteMach.stop();
		}
		@Override
		public void doAutonomousPeriodic() {
			tm_trigSteMach.doPeriodic();
		}
		@Override
		public void doTeleopPeriodic() {
			tm_trigSteMach.doPeriodic();
		}
		@Override
		public void doLwTestPeriodic() {
			// TODO Auto-generated method stub
			
		}
		
		public void start() {
			tm_trigSteMach.start();
		}
		public void startManual() {
			tm_trigSteMach.stop();
			tm_trigSteMach.startManual();
		}
		public void stop() {
			tm_trigSteMach.stop();
		}
		
//		private enum ShooterStateMachineE {
//			INACTIVE, START, WAIT_FOR_RPS_IN_RANGE, 
//			ACTIVATE_ABACUS, ACTIVATE_TRIGGER, MAINTAIN_RPS, STOP, STOP_AND_RESTART,
//			START_MANUAL, MANUAL, MANUAL_TO_START, AUTO_TO_MANUAL, STOP_MANUAL  
//		}
		private enum ShooterTriggerStateMachineE {
			INACTIVE, START, WAIT_FOR_RPS_IN_RANGE, PULSE_ON, DELAY_ON, PULSE_OFF, DELAY_OFF, STOP,
			STOP_AND_RESTART, START_MANUAL, MANUAL, MANUAL_TO_START, AUTO_TO_MANUAL, STOP_MANUAL
		}
		private class ShooterTriggerStateMachine {
			private ShooterTriggerStateMachineE tmsm_state;
			private Object tmsm_lock = new Object();
			
			public ShooterTriggerStateMachine() {
				tmsm_state = ShooterTriggerStateMachineE.INACTIVE;
			}
			public boolean isActiveManual() {
				boolean ans;
				synchronized(tm_lock) { 
				synchronized(tmsm_lock) {
					switch(tmsm_state) {
					case MANUAL:
						ans = true;
						break;
					default:
						ans = false;
						break;
					}
				}
				}
				return ans;
			}
			public boolean isActiveAuto() {
				boolean ans;
				synchronized(tm_lock) {
				synchronized(tmsm_lock) {
					switch(tmsm_state) {
					case INACTIVE:
					case START_MANUAL:
					case MANUAL:
						ans = false;
						break;
					default:
						ans = true;
						break;
					}
				}
				}
				return ans;
			}
			
			public void stop() {
				synchronized(tm_lock) {
				synchronized(tmsm_lock) {
					String stopMsg = "Shooter trigger state machine (ste=" + tmsm_state.name() + ") stopping";
					switch(tmsm_state) {
					case INACTIVE:
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						break;
					case STOP:
						P.println(stopMsg);
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.INACTIVE;
						break;
					case MANUAL:
						P.println(stopMsg);
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.STOP_MANUAL;
						break;
					default:
						P.println(stopMsg);
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.STOP;
						break;
					}
				}
				}
			}
			public void start() {
				synchronized(tm_lock) {
				synchronized(tmsm_lock) {
					switch(tmsm_state) {
					case INACTIVE:
						P.println("Shooter trigger state machine starting (from state " + tmsm_state.name() + ")");
						tmsm_state = ShooterTriggerStateMachineE.START;
						break;
					case STOP:
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						P.println("Shooter trigger state machine restarting (from state " + tmsm_state.name() + ")");
						tmsm_state = ShooterTriggerStateMachineE.STOP_AND_RESTART;
						break;
					case START_MANUAL:
					case MANUAL:
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						P.println("Shooter trigger state machine switching from manual to auto (from state " + tmsm_state.name() + ")");
						tmsm_state = ShooterTriggerStateMachineE.MANUAL_TO_START;
						break;
					default:
						//already running, do nothing
						break;
					}
				}
				}
			}
			public void startManual() {
				synchronized(tm_lock) {
				synchronized(tmsm_lock) {
					switch(tmsm_state) {
					case INACTIVE:
						P.println("Shooter trigger state machine starting MANUAL mode");
						tmsm_state = ShooterTriggerStateMachineE.START_MANUAL;
						break;
					case START_MANUAL:
					case MANUAL:
						P.println("Shooter trigger state machine remaining in MANUAL mode");
						break;
					default:
						P.println("Shooter trigger state machine switching to MANUAL mode");
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_motorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.AUTO_TO_MANUAL;
						break;
					}
				}
				}
			}

			//service methods for use by commands in manual mode
			public void setMotorOnManual() {
				synchronized(tm_lock) {
					synchronized(tmsm_lock) {
						if(isActiveManual()) {
							tm_motorSpeed = tm_targetMotorSpeed;
							P.println("Shooter Trigger ON manual");
						}
					}
				}
			}
			public boolean isMotorOnManual() {
				boolean ans = false;
				synchronized(tm_lock) {
					synchronized(tmsm_lock) {
						ans = (tm_motorSpeed != 0.0);
					}
				}
				return ans;
			}
			public void setMotorOffManual() {
				synchronized(tm_lock) {
					synchronized(tmsm_lock) {
						if(isActiveManual()) {
							tm_motorSpeed = 0.0;
							P.println("Shooter Trigger OFF manual");
						}
					}
				}
			}
			
			private void postToSdShooterTriggerSteMach() {
				synchronized(tm_lock) {
					synchronized(tmsm_lock) {
						TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_TRIGGER_MOTOR_IS_ON, (tm_motorSpeed != 0.0));
					}
				}
			}
						
			public void doPeriodic() {
				synchronized(tm_lock) {
					synchronized(tmsm_lock) {
						if( isActiveAuto() && ! m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
							String msg = "Shooter trigger state machine (ste=" + tmsm_state.name() + 
									") stopping: robot is driving above " + m_maxDriveSpeedAllowed;
							P.println(msg);
							stop();
						}
						tm_motor.set(tm_motorSpeed);
						run();
						postToSdShooterTriggerSteMach();
					}
				}
			}

			public void run() {
				synchronized(tm_lock) {
				synchronized(tmsm_lock) {
					ShooterTriggerStateMachineE prevState = tmsm_state;
					switch(tmsm_state) {
					case INACTIVE:
						break;
					case START:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_isManualMode = false;
						tmsm_state = ShooterTriggerStateMachineE.WAIT_FOR_RPS_IN_RANGE;
						//tm_motorSpeed = 0.0; //tm_motor.set(0.0);
						//fallthru //break;
					case WAIT_FOR_RPS_IN_RANGE:
						//tm_motorSpeed = 0.0; //tm_motor.set(0.0);
						if(tm_shooterMtrMgr.isRpsInTargetRange()) {
							tmsm_state = ShooterTriggerStateMachineE.PULSE_ON;
						}
						break;
					case PULSE_ON:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_motorSpeed = tm_targetMotorSpeed;
						tm_motor.set(tm_targetMotorSpeed);
						tm_timer.reset();
						tmsm_state = ShooterTriggerStateMachineE.DELAY_ON;
						break;
					case DELAY_ON:
//						tm_motor.set(tm_targetMotorSpeed);
						if(tm_timer.hasPeriodPassed(TrigCnst.TRIGGER_PULSE_ON_TIME_SECS)) {
							tmsm_state = ShooterTriggerStateMachineE.PULSE_OFF;
						}
						break;
					case PULSE_OFF:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_targetMotorSpeed);
						tm_timer.reset();
						tmsm_state = ShooterTriggerStateMachineE.DELAY_OFF;
						break;
					case DELAY_OFF:
						if(tm_timer.hasPeriodPassed(TrigCnst.TRIGGER_PULSE_OFF_TIME_SECS)) {
							tmsm_state = ShooterTriggerStateMachineE.PULSE_ON;
						}
						else if(m_encoderIsUsable && tm_timer.hasPeriodPassed(TrigCnst.TRIGGER_PULSE_OFF_MIN_TIME_SECS)) {
							if(tm_shooterMtrMgr.isRpsInTargetRange()) {
								P.println("Shooter Drum reached target RPS, time=" + tm_timer.get());
								tmsm_state = ShooterTriggerStateMachineE.PULSE_ON;
							}
						}
						break;
					case AUTO_TO_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
//						tm_motorSpeed = 0.0;
//						tm_motor.set(tm_targetMotorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.START_MANUAL;
						//fallthru //break;
					case START_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_targetMotorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.MANUAL;
						tm_isManualMode = true;
						break;
					case MANUAL:
						break;
					case MANUAL_TO_START:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_isManualMode = false;
						tmsm_state = ShooterTriggerStateMachineE.STOP_AND_RESTART;
						//fallthru //break;
					case STOP_AND_RESTART:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_targetMotorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.START;
						break;
					case STOP_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_isManualMode = false;
						tmsm_state = ShooterTriggerStateMachineE.STOP;
						//fallthru //break;
					case STOP:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						//fallthru //break;
					default:
						P.println(m_dbgPrtFlgs, "shooter trigger ste=" + tmsm_state.name());
						tm_motorSpeed = 0.0;
						tm_motor.set(tm_targetMotorSpeed);
						tmsm_state = ShooterTriggerStateMachineE.INACTIVE;
						break;
					}
					
					if( ! prevState.equals(tmsm_state)) {
						if(tm_dbgMsgCnt-- > 0) {
							P.println("Shooter Trigger state switching from " + prevState.name() + " to " + tmsm_state.name());
						}
					}
					
				}
				}
			}
		}
	}
	
	public static class ShooterAbacusMgmt implements TmStdSubsystemI {
		
		private Timer am_timer = new Timer();
		private TmCfgMotors.MotorConfigE am_motorDef;
		private TmFakeable_CANTalon am_motor;
		private TmSsShooter.ShooterDrumMgmt am_shooterMtrMgr;
		private Object am_lock = new Object();
		private ShooterAbacusStateMachine am_abacusSteMach;
		private boolean am_isManualMode = false;
		private double am_targetMotorSpeed = AbacusCnst.ABACUS_TARGET_MOTOR_SPEED;
		private double am_motorSpeed = 0.0;
		
		private int am_dbgMsgCnt;
		
		public ShooterAbacusMgmt() {
			am_motorDef = TmCfgMotors.MotorConfigE.SHOOTER_ABACUS_MOTOR_CFG;
			am_motor = am_motorDef.getCanTalonMotorObj();
			am_shooterMtrMgr = TmSsShooter.m_drumMgr;
			am_abacusSteMach = new ShooterAbacusStateMachine();
			if(am_motor==null) {
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("TmCfgMotors..." + am_motorDef.name() + " is not a TmFakeable_CANTalon");
			}
			am_isManualMode = false;
			am_motorSpeed = 0.0;
		}
		
		public class AbacusCnst {
			public static final double ABACUS_PULSE_ON_TIME_SECS = 4.00;
			public static final double ABACUS_PULSE_OFF_TIME_SECS_A = 0.050; //1.000;
			public static final double ABACUS_PULSE_REV_TIME_SECS = 0.200;
			public static final double ABACUS_PULSE_OFF_TIME_SECS_B = 0.800; //1.000;
			public static final double ABACUS_TARGET_MOTOR_SPEED = -1.0; //can be overridden by preferences
		}

		@Override
		public void doInstantiate() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void doRoboInit() {
			am_timer.start();
			am_timer.reset();
			am_motorSpeed = 0.0;
			am_motor.set(am_motorSpeed);
			am_isManualMode = false;
			am_targetMotorSpeed = Tt.getPreference(TmRoFilesAndPrefKeys.PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, 
											AbacusCnst.ABACUS_TARGET_MOTOR_SPEED, -1, PrefCreateE.CREATE_AS_NEEDED);
			am_abacusSteMach.postToSdShooterAbacusSteMach();
		}
		@Override
		public void doDisabledInit() {
			synchronized(am_lock) {
				am_motorSpeed = 0.0;
				am_motor.set(am_motorSpeed);
				am_abacusSteMach.stop();
				am_isManualMode = false;
			}
		}
		@Override
		public void doAutonomousInit() {
			synchronized(am_lock) {
				am_motorSpeed = 0.0;
				am_motor.set(am_motorSpeed);
				am_abacusSteMach.stop();
				am_isManualMode = false;
				am_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, am_targetMotorSpeed, -1, PrefCreateE.DO_NOT_CREATE);
				am_dbgMsgCnt = 4;
			}
		}
		@Override
		public void doTeleopInit() {
			synchronized(am_lock) {
				am_motorSpeed = 0.0;
				am_motor.set(am_motorSpeed);
				am_abacusSteMach.stop();
				am_isManualMode = false;
				am_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, am_targetMotorSpeed, -1, PrefCreateE.DO_NOT_CREATE);
				am_dbgMsgCnt = 4;
			}
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
			am_abacusSteMach.stop();
		}
		@Override
		public void doAutonomousPeriodic() {
			am_abacusSteMach.doPeriodic();
		}
		@Override
		public void doTeleopPeriodic() {
			am_abacusSteMach.doPeriodic();
		}
		@Override
		public void doLwTestPeriodic() {
			// TODO Auto-generated method stub
			
		}
		
		public void start() {
			am_abacusSteMach.start();
		}
		public void startManual() {
			am_abacusSteMach.stop();
			am_abacusSteMach.startManual();
		}
		public void stop() {
			am_abacusSteMach.stop();
		}
		
//		private enum ShooterStateMachineE {
//			INACTIVE, START, WAIT_FOR_RPS_IN_RANGE, 
//			ACTIVATE_ABACUS, ACTIVATE_ABACUS, MAINTAIN_RPS, STOP, STOP_AND_RESTART,
//			START_MANUAL, MANUAL, MANUAL_TO_START, AUTO_TO_MANUAL, STOP_MANUAL  
//		}
		private enum ShooterAbacusStateMachineE {
			INACTIVE, START, WAIT_FOR_RPS_IN_RANGE, PULSE_ON, DELAY_ON, PULSE_OFF_A, DELAY_OFF_A, 
			PULSE_REV, DELAY_REV, PULSE_OFF_B, DELAY_OFF_B, STOP,
			STOP_AND_RESTART, START_MANUAL, MANUAL, MANUAL_TO_START, AUTO_TO_MANUAL, STOP_MANUAL
		}
		private class ShooterAbacusStateMachine {
			private ShooterAbacusStateMachineE amsm_state;
			private Object amsm_lock = new Object();
			
			public ShooterAbacusStateMachine() {
				amsm_state = ShooterAbacusStateMachineE.INACTIVE;
			}
			public boolean isActiveManual() {
				boolean ans;
				synchronized(am_lock) { 
				synchronized(amsm_lock) {
					switch(amsm_state) {
					case MANUAL:
						ans = true;
						break;
					default:
						ans = false;
						break;
					}
				}
				}
				return ans;
			}
			public boolean isActiveAuto() {
				boolean ans;
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					switch(amsm_state) {
					case INACTIVE:
					case START_MANUAL:
					case MANUAL:
						ans = false;
						break;
					default:
						ans = true;
						break;
					}
				}
				}
				return ans;
			}
			
			public void stop() {
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					String stopMsg = "Shooter abacus state machine (ste=" + amsm_state.name() + ") stopping";
					switch(amsm_state) {
					case INACTIVE:
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						break;
					case STOP:
						P.println(stopMsg);
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						amsm_state = ShooterAbacusStateMachineE.INACTIVE;
						break;
					case MANUAL:
						P.println(stopMsg);
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						amsm_state = ShooterAbacusStateMachineE.STOP_MANUAL;
						break;
					default:
						P.println(stopMsg);
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						amsm_state = ShooterAbacusStateMachineE.STOP;
						break;
					}
				}
				}
			}
			public void start() {
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					switch(amsm_state) {
					case INACTIVE:
						P.println("Shooter abacus state machine starting (from state " + amsm_state.name() + ")");
						amsm_state = ShooterAbacusStateMachineE.START;
						break;
					case STOP:
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						P.println("Shooter abacus state machine restarting (from state " + amsm_state.name() + ")");
						amsm_state = ShooterAbacusStateMachineE.STOP_AND_RESTART;
						break;
					case START_MANUAL:
					case MANUAL:
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						P.println("Shooter Abacus state machine switching from manual to auto (from state " + amsm_state.name() + ")");
						amsm_state = ShooterAbacusStateMachineE.MANUAL_TO_START;
						break;
					default:
						//already running, do nothing
						break;
					}
				}
				}
			}
			public void startManual() {
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					switch(amsm_state) {
					case INACTIVE:
						P.println("Shooter Abacus state machine starting MANUAL mode");
						amsm_state = ShooterAbacusStateMachineE.START_MANUAL;
						break;
					case START_MANUAL:
					case MANUAL:
						P.println("Shooter Abacus state machine remaining in MANUAL mode");
						break;
					default:
						P.println("Shooter Abacus state machine switching to MANUAL mode");
						am_motorSpeed = 0.0;
						am_motor.set(am_motorSpeed);
						amsm_state = ShooterAbacusStateMachineE.AUTO_TO_MANUAL;
						break;
					}
				}
				}
			}

			//service methods for use by commands in manual mode
			public void setMotorOnManual() {
				synchronized(am_lock) {
					synchronized(amsm_lock) {
						if(isActiveManual()) {
							am_motorSpeed = am_targetMotorSpeed;
							P.println("Shooter Abacus ON manual");
						}
					}
				}
			}
			public boolean isMotorOnManual() {
				boolean ans = false;
				synchronized(am_lock) {
					synchronized(amsm_lock) {
						ans = (am_motorSpeed != 0.0);
					}
				}
				return ans;
			}
			public void setMotorOffManual() {
				synchronized(am_lock) {
					synchronized(amsm_lock) {
						if(isActiveManual()) {
							am_motorSpeed = 0.0;
							P.println("Shooter Abacus OFF manual");
						}
					}
				}
			}
			
			private void postToSdShooterAbacusSteMach() {
				synchronized(am_lock) {
					synchronized(amsm_lock) {
						TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_SHOOTER_ABACUS_IS_ON, (am_motorSpeed != 0.0));
					}
				}
			}
						
			public void doPeriodic() {
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					if( isActiveAuto() && ! m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
						String msg = "Shooter abacus state machine (ste=" + amsm_state.name() + ") stopping: robot is driving above " + m_maxDriveSpeedAllowed;
						P.println(msg);
						stop();
					}
					am_motor.set(am_motorSpeed);
					run();
					postToSdShooterAbacusSteMach();
				}
				}
			}

			public void run() {
				synchronized(am_lock) {
				synchronized(amsm_lock) {
					ShooterAbacusStateMachineE prevState = amsm_state;
					switch(amsm_state) {
					case INACTIVE:
						break;
					case START:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_isManualMode = false;
						amsm_state = ShooterAbacusStateMachineE.WAIT_FOR_RPS_IN_RANGE;
						//am_motorSpeed = 0.0; //am_motor.set(0.0);
						//fallthru //break;
					case WAIT_FOR_RPS_IN_RANGE:
						//am_motorSpeed = 0.0; //am_motor.set(0.0);
						if(am_shooterMtrMgr.isRpsInTargetRange()) {
							amsm_state = ShooterAbacusStateMachineE.PULSE_ON;
						}
						break;
					case PULSE_ON:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = am_targetMotorSpeed;
						am_motor.set(am_targetMotorSpeed);
						am_timer.reset();
						amsm_state = ShooterAbacusStateMachineE.DELAY_ON;
						break;
					case DELAY_ON:
//						am_motor.set(am_targetMotorSpeed);
						if(am_timer.hasPeriodPassed(AbacusCnst.ABACUS_PULSE_ON_TIME_SECS)) {
							amsm_state = ShooterAbacusStateMachineE.PULSE_OFF_A;
						}
						break;
					case PULSE_OFF_A:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = 0.0;
						am_motor.set(am_targetMotorSpeed);
						am_timer.reset();
						amsm_state = ShooterAbacusStateMachineE.DELAY_OFF_A;
						break;
					case DELAY_OFF_A:
						if(am_timer.hasPeriodPassed(AbacusCnst.ABACUS_PULSE_OFF_TIME_SECS_A)) {
							amsm_state = ShooterAbacusStateMachineE.PULSE_REV; //.PULSE_ON;
						}
						break;
					case PULSE_REV:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = - am_targetMotorSpeed;
						am_motor.set(am_targetMotorSpeed);
						am_timer.reset();
						amsm_state = ShooterAbacusStateMachineE.DELAY_REV;
						break;
					case DELAY_REV:
						if(am_timer.hasPeriodPassed(AbacusCnst.ABACUS_PULSE_REV_TIME_SECS)) {
							amsm_state = ShooterAbacusStateMachineE.PULSE_OFF_B;
						}
						break;
					case PULSE_OFF_B:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = 0.0;
						am_motor.set(am_targetMotorSpeed);
						am_timer.reset();
						amsm_state = ShooterAbacusStateMachineE.DELAY_OFF_B;
						break;
					case DELAY_OFF_B: 
						if(am_timer.hasPeriodPassed(AbacusCnst.ABACUS_PULSE_OFF_TIME_SECS_B)) {
							amsm_state = ShooterAbacusStateMachineE.PULSE_ON;
						}
						break;
					case AUTO_TO_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
//						am_motorSpeed = 0.0;
//						am_motor.set(am_targetMotorSpeed);
						amsm_state = ShooterAbacusStateMachineE.START_MANUAL;
						//fallthru //break;
					case START_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = 0.0;
						am_motor.set(am_targetMotorSpeed);
						amsm_state = ShooterAbacusStateMachineE.MANUAL;
						am_isManualMode = true;
						break;
					case MANUAL:
						break;
					case MANUAL_TO_START:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_isManualMode = false;
						amsm_state = ShooterAbacusStateMachineE.STOP_AND_RESTART;
						//fallthru //break;
					case STOP_AND_RESTART:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = 0.0;
						am_motor.set(am_targetMotorSpeed);
						amsm_state = ShooterAbacusStateMachineE.START;
						break;
					case STOP_MANUAL:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_isManualMode = false;
						amsm_state = ShooterAbacusStateMachineE.STOP;
						//fallthru //break;
					case STOP:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						//fallthru //break;
					default:
						P.println(m_dbgPrtFlgs, "shooter abacus ste=" + amsm_state.name());
						am_motorSpeed = 0.0;
						am_motor.set(am_targetMotorSpeed);
						amsm_state = ShooterAbacusStateMachineE.INACTIVE;
						break;
					}
					
					if( ! prevState.equals(amsm_state)) {
						if(am_dbgMsgCnt-- > 0) {
							P.println("Shooter Abacus state switching from " + prevState.name() + " to " + amsm_state.name());
						}
					}
					
				}
				}
			}
		}
	}

/*
//	protected static class ShooterAbacusMgmt_Old implements TmStdSubsystemI {
//		
//		private TmCfgMotors.MotorConfigE am_motorDef;
//		private TmFakeable_CANTalon am_motor;
//		private double am_motorSpeed;
//		private double am_targetMotorSpeed;
//		
//		private RoNamedIoE am_relayDef;
//		private TmFakeable_Relay am_relay;
//		private Relay.Value am_relaySetting;
//		
//		private boolean am_isRunning = false;
//		private boolean am_isManual = false;
//		private Object am_lock = new Object();
//		
//		private static class AbacusCnst_Old {
//			public static final double ABACUS_MOTOR_TARGET_SPEED = -1.0; //can be overridden by preferences
//		    private static final Relay.Value ABACUS_RELAY_ON = Relay.Value.kReverse;
//		    private static final Relay.Value ABACUS_RELAY_OFF = Relay.Value.kOff;
//		}
//		
//		private enum AbacusControlTypeE { SRX, SPIKE;
//			private static AbacusControlTypeE eDefault = SRX;
//			public static boolean isMotor() { return eDefault.equals(AbacusControlTypeE.SRX); }
//			public static boolean isRelay() { return eDefault.equals(AbacusControlTypeE.SPIKE); }
//		}
//		
//		public ShooterAbacusMgmt_Old() {
//			if(AbacusControlTypeE.isMotor()) {
//				am_motorDef = TmCfgMotors.MotorConfigE.SHOOTER_ABACUS_MOTOR_CFG;
//				am_motor = am_motorDef.getCanTalonMotorObj();
//				if(am_motor==null) {
//					throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("TmCfgMotors..." + am_motorDef.name() + " is not a TmFakeable_CANTalon");
//				}
//				am_targetMotorSpeed = AbacusCnst_Old.ABACUS_MOTOR_TARGET_SPEED;
//				am_motorSpeed = 0.0;
//				am_relayDef = null;
//				am_relay = null;
//			} else {
//				am_motorDef = null;
//				am_motor = null;
//				am_targetMotorSpeed = 0.0;
//				am_relayDef = RoNamedIoE.SHOOTER_ABACUS_RELAY;
//				am_relay = new TmFakeable_Relay(am_relayDef);
//				am_relaySetting = AbacusCnst_Old.ABACUS_RELAY_OFF;
//			}
//			am_isRunning = false;
//			am_isManual = false;
//		}
//		
//		
//		//hmm.... could probably do away with the auto/manual distinction for the abacus, but
//		//we'll keep it for consistency with the shooter motor and the trigger motor managers...
//		public void start() { //refactor to startImmediately
//			synchronized(am_lock) {
//				P.println(m_dbgPrtFlgs, "shooter abacus start: (" + 
//							(AbacusControlTypeE.isMotor() ? "motor) " : "relay) "));
//				if(AbacusControlTypeE.isMotor()) {
//					am_motorSpeed = am_targetMotorSpeed;
//					am_motor.set(am_motorSpeed);
//				} else {
//					am_relaySetting = AbacusCnst_Old.ABACUS_RELAY_ON;
//					am_relay.set(am_relaySetting);
//				}
//				am_isRunning = true;
//				am_isManual = false;
//			}
//		}
//		public void startManualEnableAbacus() {
//			synchronized(am_lock) {
//				P.println(m_dbgPrtFlgs, "shooter abacus startManualEnableAbacus: (" + 
//						(AbacusControlTypeE.isMotor() ? "motor) " : "relay) "));
//				if(AbacusControlTypeE.isMotor()) {
//					am_motorSpeed = 0.0; //am_targetMotorSpeed;
//					am_motor.set(am_motorSpeed);
//				} else {
//					am_relaySetting = AbacusCnst_Old.ABACUS_RELAY_OFF; //Cnst.ABACUS_RELAY_ON;
//					am_relay.set(am_relaySetting);
//				}
//				am_isRunning = false;
//				am_isManual = true;
//			}
//		}
//		public void startManualImmediate() {
//			synchronized(am_lock) {
//				P.println(m_dbgPrtFlgs, "shooter abacus startManualImmediate: (" + 
//						(AbacusControlTypeE.isMotor() ? "motor) " : "relay) "));
//				if(AbacusControlTypeE.isMotor()) {
//					am_motorSpeed = am_targetMotorSpeed;
//					am_motor.set(am_motorSpeed);
//				} else {
//					am_relaySetting = AbacusCnst_Old.ABACUS_RELAY_ON;
//					am_relay.set(am_relaySetting);
//				}
//				am_isRunning = true;
////				am_isManual = true;
//			}
//		}
//
//		public void stop() {
//			synchronized(am_lock) {
//				P.println(m_dbgPrtFlgs, "shooter abacus stop: (" + 
//						(AbacusControlTypeE.isMotor() ? "motor) " : "relay) "));
//
//				if(AbacusControlTypeE.isMotor()) {
//					am_motorSpeed = 0.0;
//					am_motor.set(am_motorSpeed);
//				} else {
//					am_relaySetting = AbacusCnst_Old.ABACUS_RELAY_OFF;
//					am_relay.set(am_relaySetting);
//				}
//				am_isRunning = false;
//				am_isManual = false;
//			}
//		}
//		
//		public boolean isMotorOnManual() {
//			boolean ans = false;
//			if(am_isManual) {
//				if( ! (am_motorSpeed==0.0)) {
//					ans = true;
//				}
//			}
//			return ans;
//		}
//		
//		public void postToSdShooterAbacus() {
//			TmPostToSd.dbgPutBoolean(SdKeysE.KEY_SHOOTER_ABACUS_IS_ON, (am_motorSpeed != 0.0));
//		}
//
//		@Override
//		public void doInstantiate() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void doRoboInit() {
//			am_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, 
//					AbacusCnst_Old.ABACUS_MOTOR_TARGET_SPEED, -1, PrefCreateE.CREATE_AS_NEEDED);
//		}
//
//		@Override
//		public void doDisabledInit() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void doAutonomousInit() {
//			am_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, AbacusCnst_Old.ABACUS_MOTOR_TARGET_SPEED, -1, PrefCreateE.DO_NOT_CREATE);
//			
//		}
//
//		@Override
//		public void doTeleopInit() {
//			am_targetMotorSpeed = Tt.getPreference(PrefKeysE.KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED, AbacusCnst_Old.ABACUS_MOTOR_TARGET_SPEED, -1, PrefCreateE.DO_NOT_CREATE);
//		}
//
//		@Override
//		public void doLwTestInit() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void doRobotPeriodic() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void doDisabledPeriodic() {
//			if(am_isRunning) { stop(); }
//		}
//
//		@Override
//		public void doAutonomousPeriodic() {
//			synchronized(am_lock) {
//				if(am_isRunning && ! m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
//					String msg = "Shooter abacus stopping: robot is driving";
//					P.println(msg);
//					stop();
//				}
//				else if(AbacusControlTypeE.isMotor()) {
//					if(am_isRunning || am_isManual) {
//						am_motor.set(am_motorSpeed);
//					} else {
//						if(am_motorSpeed != 0.0) {
//							P.println("(0)BUG!!! shooter abacus should have motor speed = 0.0 here!!");
//							am_motorSpeed = 0.0;
//						}
//						am_motor.set(am_motorSpeed);
//					}
//				}
//			}
//			postToSdShooterAbacus();
//		}
//
//		@Override
//		public void doTeleopPeriodic() {
//			synchronized(am_lock) {
//				if(am_isRunning && ! m_driveSs.isDrivingBelow(m_maxDriveSpeedAllowed)) {
//					String msg = "Shooter abacus stopping: robot is driving";
//					P.println(msg);
//					stop();
//				}
//				else if(AbacusControlTypeE.isMotor()) {
//					if(am_isRunning || am_isManual) {
//						am_motor.set(am_motorSpeed);
//					} else {
//						if(am_motorSpeed != 0.0) {
//							P.println("(1)BUG!!! shooter abacus should have motor speed = 0.0 here!!");
//							am_motorSpeed = 0.0;
//						}
//						am_motor.set(am_motorSpeed);
//					}
//				}
//			}
//			postToSdShooterAbacus();
//		}
//
//		@Override
//		public void doLwTestPeriodic() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
*/

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
}
