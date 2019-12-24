package org.usfirst.frc.tm744y17.robot.config;

//import org.usfirst.frc.tm744y16.robot.config.TmHdwrRoPhysHw.SpdCntlrTypeE;
//import org.usfirst.frc.tm744y16.robot.devices.TmCANTalon;
//import org.usfirst.frc.tm744y16.robot.devices.TmFakeCANTalon;
//import org.usfirst.frc.tm744y16.robot.devices.TmTalon;
//import org.usfirst.frc.tm744y16.robot.helpers.Tm16DbgTk;
//import org.usfirst.frc.tm744y16.robot.helpers.TmFlagsI;
//import org.usfirst.frc.tm744y16.robot.helpers.TmMotorConfigEnumI;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmMotorConfigEnumI;

import edu.wpi.first.wpilibj.SpeedController;

public class TmCfgMotors {

	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmCfgMotors m_instance;
	public static synchronized TmCfgMotors getInstance() {
		if (m_instance == null) {
			m_instance = new TmCfgMotors();
		}
		return m_instance;
	}

	public void doRoboInit() {
//		//inspect the motor config enum for conflicts, etc. 
//    	if( ! TmMotorConfigEnumI.<MotorConfigE>isUsable(MotorConfigE.values()) ) {
////        	Tm16DbgTk.printIt(TmFlagsI.ALL, Tm16DbgTk.extractClassName(this) + " MotorConfigE has conflicting info!!");
//        	System.out.println(Tm16DbgTk.extractClassName(this) + "-- MotorConfigE has conflicting info!!");
//        }
    	
//    	//display motor config info in console log
//        for(MotorConfigE item : MotorConfigE.values()) {
//        	Tm16DbgTk.printIt(TmFlagsI.NONE, Tm16DbgTk.extractClassName(this) + " MotorConfigE: " + item.toStringFull());
//        }
        
//        //associate motors with the appropriate subsystem in LiveWindows
//        for (MotorConfigE mtr : MotorConfigE.values())
//        {
//        	mtr.addLwActuator(); //(mtr.getLwSubsysName());
//        }
	}
      
	/**
	 * Show motor config data in console log
	 */
	private static void showMotorConfigData() {
		System.out.println("----TmCfgMotors motor configuration info");
		for(MotorConfigE item : MotorConfigE.values()) {
	    	System.out.println("TmCfgMotor: " /*item.name() /*Tm16DbgTk.extractClassName(item)*/ + 
	    			" MotorConfigE: " + item.toStringFull());
	    }
		showMaxMinOuputCurrentInfo();
	}
    
	/**
	 * Show max/min output current for all TmCANTalon motors
	 */
	private static void showMaxMinOuputCurrentInfo() {
		System.out.println("----TmCfgMotors max/min output current info for TmCANTalon motors");
		System.out.println("(TBD");
//		for(MotorConfigE item : MotorConfigE.values()) {
//			SpdCntlrTypeE mtyp = item.getMotorType();
//			if(item.isMotorType(SpdCntlrTypeE.kTM_CAN_TALON) || item.isMotorType(SpdCntlrTypeE.kTM_FAKE_CAN_TALON) ) {
//		    	System.out.println("TmCfgMotor: " + item.toString() + ": " + item.showMaxMinOutputCurrent());
//			}
//		}
	}
    

    /** 
     * the motor speed tuning factor coded in the enum is not used by the robot drive
     * subsystem, so it doesn't matter what value we use for those motors. (The drive subsystem
     * calls FRC code that only allows a speed to be set for a side of the robot,
     * not the individual motors driving that side.)
     */
//    private static final double DEFAULT_MOTOR_SPEED_ADJ_FACTOR = 1.0;
    private static final SdKeysE NO_SD_KEY_FOR_SPEED = null;
    private static final SdKeysE NO_SD_KEY_FOR_AMPS = null; 
    
//    public enum FakeMotorUsageE {
//    	FORCE_FAKE, DETECT_FAKE; 
//    	public boolean isForcedFake() { return this.equals(FORCE_FAKE); }
//    }
    
	/** 
	 * this enum keeps all the various kinds of data associated with individual
	 * motors all in one place.
	 */
    public enum MotorConfigE implements TmMotorConfigEnumI<MotorConfigE> {
		FRONT_LEFT_MOTOR_CFG(RoNamedIoE.DRV_MTR_FRONT_LEFT, SdKeysE.KEY_DRIVE_MOTOR_FRONT_LEFT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_FRONT_LEFT_AMPS),
		CENTER_LEFT_MOTOR_CFG(RoNamedIoE.DRV_MTR_CENTER_LEFT, SdKeysE.KEY_DRIVE_MOTOR_CENTER_LEFT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_CENTER_LEFT_AMPS),
		REAR_LEFT_MOTOR_CFG(RoNamedIoE.DRV_MTR_REAR_LEFT, SdKeysE.KEY_DRIVE_MOTOR_REAR_LEFT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_REAR_LEFT_AMPS),
		FRONT_RIGHT_MOTOR_CFG(RoNamedIoE.DRV_MTR_FRONT_RIGHT, SdKeysE.KEY_DRIVE_MOTOR_FRONT_RIGHT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_FRONT_RIGHT_AMPS),
		CENTER_RIGHT_MOTOR_CFG(RoNamedIoE.DRV_MTR_CENTER_RIGHT, SdKeysE.KEY_DRIVE_MOTOR_CENTER_RIGHT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_CENTER_RIGHT_AMPS),
		REAR_RIGHT_MOTOR_CFG(RoNamedIoE.DRV_MTR_REAR_RIGHT, SdKeysE.KEY_DRIVE_MOTOR_REAR_RIGHT_SPEED, SdKeysE.KEY_DRIVE_MOTOR_REAR_RIGHT_AMPS),
		
		SHOOTER_MOTOR_CFG(RoNamedIoE.SHOOTER_DRUM_MOTOR, SdKeysE.KEY_SHOOTER_MOTOR_SPEED, NO_SD_KEY_FOR_AMPS),
		SHOOTER_TRIGGER_MOTOR_CFG(RoNamedIoE.SHOOTER_TRIGGER_MOTOR, NO_SD_KEY_FOR_SPEED, NO_SD_KEY_FOR_AMPS),
		SHOOTER_ABACUS_MOTOR_CFG(RoNamedIoE.SHOOTER_ABACUS_MOTOR, NO_SD_KEY_FOR_SPEED, NO_SD_KEY_FOR_AMPS),
		
		BALL_INTAKE_GRABBER_MOTOR_CFG(RoNamedIoE.BALL_INTAKE_MOTOR, NO_SD_KEY_FOR_SPEED, NO_SD_KEY_FOR_AMPS),
//		BALL_INTAKE_ABACUS_MOTOR_CFG(RoNamedIoE.BALL_INTAKE_ABACUS_MOTOR, NO_SD_KEY_FOR_SPEED, NO_SD_KEY_FOR_AMPS),
		
		CLIMBER_MOTOR_A_CFG(RoNamedIoE.CLIMBER_MOTOR_A, NO_SD_KEY_FOR_SPEED, SdKeysE.KEY_CLIMBER_AMPS_A),
		CLIMBER_MOTOR_B_CFG(RoNamedIoE.CLIMBER_MOTOR_B, NO_SD_KEY_FOR_SPEED, SdKeysE.KEY_CLIMBER_AMPS_B),
		CLIMBER_MOTOR_40_CFG(RoNamedIoE.CLIMBER_MOTOR_40, NO_SD_KEY_FOR_SPEED, SdKeysE.KEY_CLIMBER_AMPS_40),
    	;
    	
    	private MotorConfigData cfg;
    	
    	//FRC drive train code (RobotDrive class) does not support tuning factors, but our custom TmRobotDriveSixMotors could.
    	private static final double DEFAULT_MOTOR_SPEED_TUNING_FACTOR = 1.0;
   	    	
    	private final RoNamedIoE eMotorNamedIoDef;
    	
    	private MotorConfigE(RoNamedIoE namedIoDef, SdKeysE sdKeyDefSpeed, SdKeysE sdKeyDefAmps) {
    		this(namedIoDef, DEFAULT_MOTOR_SPEED_TUNING_FACTOR, sdKeyDefSpeed, sdKeyDefAmps);
    	}
        private MotorConfigE(RoNamedIoE namedIoDef, double motorSpeedTuningFactor, SdKeysE sdKeyDefSpeed, SdKeysE sdKeyDefAmps) {
    		cfg = new MotorConfigData("MotorConfigE." + this.name(), namedIoDef, motorSpeedTuningFactor, sdKeyDefSpeed, sdKeyDefAmps);
    		eMotorNamedIoDef = namedIoDef;
    	}
    	
    	
    	public String toStringFull() {
			// TODO Auto-generated method stub
			return null;
		}

		public RoNamedIoE getNamedMotorDef() { return eMotorNamedIoDef; }
		
		//helper methods
		public SpeedController getMotorObject() {
			SpeedController ans;
			ans = eMotorNamedIoDef.getModConnIoPairObj().getMotorObject();
			return ans;
		}
		public TmFakeable_CANTalon getCanTalonMotorObj() {
			TmFakeable_CANTalon ans = null;
			SpeedController spdCtrlr = getMotorObject();
			if(spdCtrlr instanceof TmFakeable_CANTalon) {
				ans = (TmFakeable_CANTalon)spdCtrlr;
			}
			return ans;
		}
		
    	@Override
		public MotorConfigData getMotorConfigData() {
			return cfg;
		}
    	
//    	LEFT_FRONT_MOTOR("LF", Tm16Misc.LwSubSysName.SS_ROBOT_DRIVE,
//    			(Tm16Opts.isRunningOnSwTestFixture() ? 
////    					new CANTalon(TmHdwrRoMap.CanId.DRIVE_MOTOR_LEFT_FRONT.getCanNodeId()) :
////        				new TmCANTalon(TmHdwrRoMap.CanId.DRIVE_MOTOR_LEFT_FRONT) :
//           				new TmFakeCANTalon(TmHdwr16RoMap.CanId.SHOOTER_MOTOR.getCanNodeId()) :
//    					new TmCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_LEFT_FRONT) ),
//                DriveMotorDesignationE.LEFT_FRONT,
//                Cnst.INVERT_MOTOR,
//                DEFAULT_MOTOR_SPEED_ADJ_FACTOR,  //ignored by tankDrive code
//                Tm16Misc.LwItemNames.DRIVE_MOTOR_LEFT_FRONT,
//                Tm16Misc.SdKeysE.KEY_DRIVE_MOTOR_LEFT_FRONT_SPEED,
//				TmMotorConfigEnumI.Cnst.USE_MOTOR_SAFETY, 
//				4*MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//    	LEFT_REAR_MOTOR("LR", Tm16Misc.LwSubSysName.SS_ROBOT_DRIVE,
//    			(Tm16Opts.isRunningOnSwTestFixture() ? 
////    					new TmFakeTalon(TmHdwrRoMap.Pwm.DRIVE_MOTOR_LEFT_REAR) :
//    					new TmFakeCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_LEFT_REAR.getCanNodeId()) :
//    					new TmCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_LEFT_REAR) ),
//    			DriveMotorDesignationE.LEFT_REAR,
//                Cnst.INVERT_MOTOR, 
//                DEFAULT_MOTOR_SPEED_ADJ_FACTOR,  //ignored by tankDrive code
//                Tm16Misc.LwItemNames.DRIVE_MOTOR_LEFT_REAR,
//                Tm16Misc.SdKeysE.KEY_DRIVE_MOTOR_LEFT_REAR_SPEED,                
//				TmMotorConfigEnumI.Cnst.USE_MOTOR_SAFETY, 
//				4*MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//   	    RIGHT_FRONT_MOTOR("RF", Tm16Misc.LwSubSysName.SS_ROBOT_DRIVE,
//   	    		(Tm16Opts.isRunningOnSwTestFixture() ?
//		   	    		new TmTalon(TmHdwr16RoMap.Pwm.DRIVE_MOTOR_RIGHT_FRONT) :
//		   	    		new TmCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_RIGHT_FRONT) ),
//   	    		DriveMotorDesignationE.RIGHT_FRONT,
//                Cnst.NO_INVERSION,
//                DEFAULT_MOTOR_SPEED_ADJ_FACTOR, //ignored by tankDrive code
//                Tm16Misc.LwItemNames.DRIVE_MOTOR_RIGHT_FRONT ,
//                Tm16Misc.SdKeysE.KEY_DRIVE_MOTOR_RIGHT_FRONT_SPEED,
//				TmMotorConfigEnumI.Cnst.USE_MOTOR_SAFETY, 
//				4*MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//    	RIGHT_REAR_MOTOR("RR", Tm16Misc.LwSubSysName.SS_ROBOT_DRIVE,
//    			(Tm16Opts.isRunningOnSwTestFixture() ? 
////    					new TmFakeTalon(TmHdwrRoMap.Pwm.DRIVE_MOTOR_RIGHT_REAR) :
//    					new TmFakeCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_RIGHT_REAR.getCanNodeId()) :
//    					new TmCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_RIGHT_REAR) ),
//    			DriveMotorDesignationE.RIGHT_REAR,
//                Cnst.NO_INVERSION,
//                DEFAULT_MOTOR_SPEED_ADJ_FACTOR,  //ignored by tankDrive code
//                Tm16Misc.LwItemNames.DRIVE_MOTOR_RIGHT_REAR ,
//                Tm16Misc.SdKeysE.KEY_DRIVE_MOTOR_RIGHT_REAR_SPEED,
//				TmMotorConfigEnumI.Cnst.USE_MOTOR_SAFETY, 
//				4*MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//    	
//    	SHOOTER_MOTOR("SHOOTER", Tm16Misc.LwSubSysName.SS_SHOOTER, 
//    			(Tm16Opts.isRunningOnSwTestFixture() ? 
////    					new TmFakeCANTalon(TmHdwrRoMap.CanId.SHOOTER_MOTOR.getCanNodeId()):
//    					new TmCANTalon(TmHdwr16RoMap.CanId.DRIVE_MOTOR_LEFT_FRONT) :
//    					new TmCANTalon(TmHdwr16RoMap.CanId.SHOOTER_MOTOR) ),
//    			DriveMotorDesignationE.NONE,
//                Cnst.NO_INVERSION,
//                DEFAULT_MOTOR_SPEED_ADJ_FACTOR,
//    			Tm16Misc.LwItemNames.SHOOTER_MOTOR,
//    			Tm16Misc.SdKeysE.KEY_SHOOTER_MOTOR_SPEED,
//    			TmMotorConfigEnumI.Cnst.NO_MOTOR_SAFETY,
//    			MotorSafety.DEFAULT_SAFETY_EXPIRATION
//    			),
//    	INTAKE_MOTOR("INTAKE", Tm16Misc.LwSubSysName.SS_INTAKE,
//    			(Tm16Opts.isRunningOnSwTestFixture() ?
//    					new TmFakeCANTalon(TmHdwr16RoMap.CanId.INTAKE_MOTOR.getCanNodeId()):
//    					new TmCANTalon(TmHdwr16RoMap.CanId.INTAKE_MOTOR) ),
//    			DriveMotorDesignationE.NONE,
//                Cnst.NO_INVERSION,
//    			DEFAULT_MOTOR_SPEED_ADJ_FACTOR,
//    			Tm16Misc.LwItemNames.INTAKE_MOTOR,
//    			Tm16Misc.SdKeysE.KEY_INTAKE_MOTOR_SPEED,
//    			TmMotorConfigEnumI.Cnst.NO_MOTOR_SAFETY,
//    			MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//    	
//    	INTAKE_MOTOR_AUX("INTAKE_AUX", Tm16Misc.LwSubSysName.SS_INTAKE,
//    			(Tm16Opts.isRunningOnSwTestFixture() ?
//    					new TmFakeCANTalon(TmHdwr16RoMap.CanId.INTAKE_MOTOR_AUX.getCanNodeId()):
//    					new TmCANTalon(TmHdwr16RoMap.CanId.INTAKE_MOTOR_AUX) ),
//    			DriveMotorDesignationE.NONE,
//                Cnst.NO_INVERSION,
//    			DEFAULT_MOTOR_SPEED_ADJ_FACTOR,
//    			Tm16Misc.LwItemNames.INTAKE_MOTOR_AUX,
//    			Tm16Misc.SdKeysE.KEY_INTAKE_MOTOR_SPEED_AUX,
//    			TmMotorConfigEnumI.Cnst.NO_MOTOR_SAFETY,
//    			MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//    	
//    	ARM_MOTOR("ARM", Tm16Misc.LwSubSysName.SS_ARM,
//    			(Tm16Opts.isRunningOnSwTestFixture() ?
//    					new TmFakeCANTalon(TmHdwr16RoMap.CanId.ARM_MOTOR.getCanNodeId()):
//    					new TmCANTalon(TmHdwr16RoMap.CanId.ARM_MOTOR) ),
//    			DriveMotorDesignationE.NONE,
//                Cnst.NO_INVERSION,
//    			DEFAULT_MOTOR_SPEED_ADJ_FACTOR,
//    			Tm16Misc.LwItemNames.ARM_MOTOR,
//    			Tm16Misc.SdKeysE.KEY_ARM_MOTOR_SPEED,
//    			TmMotorConfigEnumI.Cnst.NO_MOTOR_SAFETY,
//    			MotorSafety.DEFAULT_SAFETY_EXPIRATION),
//		;

//    	//the enum constructor
//    	MotorConfigE(String motorIdStr, String lwSubsysName,
//                SpeedController motorObj, 
//                DriveMotorDesignationE drvMotorDesignation,
//                boolean invertMotor,
//                double speedTuningFactor, 
//                String liveWindowItemName, 
//                Tm16Misc.SdKeysE smartDashKeyCurSpd
//	            )
//        {	this(motorIdStr, lwSubsysName,
//    					motorObj, drvMotorDesignation, invertMotor, speedTuningFactor,
//    					liveWindowItemName, smartDashKeyCurSpd,
//    					TmMotorConfigEnumI.Cnst.NO_MOTOR_SAFETY, 
//    					MotorSafety.DEFAULT_SAFETY_EXPIRATION);
//        }
//
//    	MotorConfigE(String motorIdStr,  String lwSubsysName,
//                SpeedController motorObj, 
//                DriveMotorDesignationE drvMotorDesignation,
//                boolean invertMotor,
//                double speedTuningFactor, 
//                String liveWindowItemName, 
//                Tm16Misc.SdKeysE smartDashKeyCurSpd,
//	            boolean useMotorSafety,
//	            double motorSafetyTimeout
//	            )
//        {	
//    		cfg = new MotorConfigData( this.toString(), motorIdStr, lwSubsysName,
//    					motorObj, drvMotorDesignation, invertMotor, speedTuningFactor,
//    					liveWindowItemName, smartDashKeyCurSpd,
//    					useMotorSafety, motorSafetyTimeout);
//        }
    	
//    	@Override
//		public MotorConfigData getMotorConfigData() {
//			return cfg;
//		}
//    	
    }

}
