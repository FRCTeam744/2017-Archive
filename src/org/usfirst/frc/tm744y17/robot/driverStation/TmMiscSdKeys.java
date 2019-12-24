package org.usfirst.frc.tm744y17.robot.driverStation;

//import org.usfirst.frc.tm744y17.robot.helpers.TmFlagsI;

public interface TmMiscSdKeys extends org.usfirst.frc.tm744y17.robot.helpers.TmFlagsI {
	
//	/**
//	 * --info from 2016--
//	 * as a coding convention, if setting something to M.ALWAYS as a quick and
//	 * dirty way to turn it on, OR it with the original setting so can see
//	 * that "quick and dirty" was used.  If it's REALLY a hokey quick-and-dirty
//	 * hack, OR in 1*M.ALWAYS instead.
//	 * @author robotics
//	 *
//	 */
	public class SdC { //constants for flags parms used in SdKeysE entries
		public static final F DRV_MTR_SPEED_FLAGS = F.NEVER; //F.NEVER;
		public static final F DRV_SPEED_FLAGS = F.MAYBE;
		public static final F DRV_AMPS_FLAGS = F.NEVER;
		public static final F DRV_ENCODER_FLAGS = F.MAYBE;
	}
	public enum SdKeysE {
//		KEY_OPTS_RUNNING_ON_SW_TEST_FIXTURE(F.MAYBE, "CfgRunningOnSWTestFixture"),
//		KEY_OPTS_RUNNING_DRIVE_WITH_GAMEPAD(F.MAYBE, "CfgRunningDriveWithGamepad"),
//		KEY_OPTS_RUNNING_MECHANISMS_WITH_GAMEPAD(F.MAYBE, "CfgRunningMechanismsWithGamepad"),
		KEY_OPTS_CLIMBER_TALONS_AB_INSTALLED(F.MAYBE, "CfgClimberTalonsABInstalled"),
		KEY_OPTS_CLIMBER_TALON_40A_INSTALLED(F.MAYBE, "CfgClimberTalon40AInstalled"),
		KEY_OPTS_SHOOTER_IS_INSTALLED(F.MAYBE, "CfgShooterIsInstalled"),
		KEY_OPTS_PCM0_IS_INSTALLED(F.MAYBE, "CfgPcm0IsInstalled"),
		KEY_OPTS_TESTING_WITH_FAKE_CAN_TALONS(F.MAYBE, "CfgTestingWithFakeCanTalons"),
		KEY_OPTS_USB_CAMERA_0_IS_INSTALLED(F.MAYBE, "CfgUsbCam0FrontInstalled"),
		KEY_OPTS_USB_CAMERA_1_IS_INSTALLED(F.MAYBE, "CfgUsbCam1RearInstalled"),
		
		KEY_DRVSTA_STATE(F.ALWAYS, "DrvStaState"),

		KEY_DRIVE_LEFT_SPEED(SdC.DRV_SPEED_FLAGS, "RobotSpeedLeft"),
		KEY_DRIVE_RIGHT_SPEED(SdC.DRV_SPEED_FLAGS, "RobotSpeedRight"),
		KEY_DRIVE_DIRECTION_FORWARD(SdC.DRV_SPEED_FLAGS, "DrvDirForward"),
		
		KEY_DRIVE_MOTOR_FRONT_LEFT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorFrontLeft"),
		KEY_DRIVE_MOTOR_CENTER_LEFT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorCenterLeft"),
        KEY_DRIVE_MOTOR_REAR_LEFT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorRearLeft"),
        KEY_DRIVE_MOTOR_FRONT_RIGHT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorFrontRight"),
        KEY_DRIVE_MOTOR_CENTER_RIGHT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorCenterRight"),
        KEY_DRIVE_MOTOR_REAR_RIGHT_SPEED(SdC.DRV_MTR_SPEED_FLAGS, "DrvMotorRearRight"),
        
		KEY_DRIVE_MOTOR_FRONT_LEFT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorFLAmps"),
		KEY_DRIVE_MOTOR_CENTER_LEFT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorCLAmps"),
        KEY_DRIVE_MOTOR_REAR_LEFT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorRLAmps"),
        KEY_DRIVE_MOTOR_FRONT_RIGHT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorFRAmps"),
        KEY_DRIVE_MOTOR_CENTER_RIGHT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorCRAmps"),
        KEY_DRIVE_MOTOR_REAR_RIGHT_AMPS(SdC.DRV_AMPS_FLAGS, "DrvMotorRRAmps"),
        
        KEY_DRIVE_ENCODER_LEFT(SdC.DRV_ENCODER_FLAGS, "DrvEncoderLeft"),
        KEY_DRIVE_ENCODER_RIGHT(SdC.DRV_ENCODER_FLAGS, "DrvEncoderRight"),
        
        KEY_DRIVE_GEARSHIFT_IS_HIGH(F.ALWAYS, "DriveInHighGear"),
		KEY_DRIVE_CENTER_MOTOR_BEHAVIOR(F.ALWAYS, "DrvCenterMotorCoastDuringTurns"),
        
        KEY_DRIVE_CURRENT_OK(F.MAYBE, "DrvCurrentOK"),
        
        KEY_DRIVE_GYRO_RATE(F.MAYBE, "DrvGyroRate"),
        KEY_DRIVE_GYRO_TEMP(F.MAYBE, "DrvGyroTemp"),
        KEY_DRIVE_GYRO_ANGLE(F.MAYBE, "DrvGyroAngle"),
        KEY_DRIVE_GYRO_ERROR_CNT(F.MAYBE, "DrvGyroErrors"),
        
        KEY_DRIVE_ORIENTATION_NORMAL(F.ALWAYS, "DrvOrientationNormal"),
        
        KEY_GRIP_TARGET_X_VALUE(F.MAYBE, "TargetXPos"),
        KEY_GRIP_TARGET_Y_VALUE(F.MAYBE, "TargetYPos"),

        KEY_CAMERA_LEDS(F.ALWAYS, "CameraLeds"),
        
//        KEY_CAMERA_CHOOSER(F, "CameraChooser"),
        
        KEY_SHOOTER_IS_ACTIVE(F.ALWAYS, "ShooterActive"),
        KEY_SHOOTER_IS_MANUAL_MODE(F.ALWAYS, "ShooterIsInManualMode"),
        KEY_SHOOTER_RPS_IN_TARGET_RANGE(F.ALWAYS, "ShooterRpsInTargetRange"),
        
//        //F.T | G.SS_SH may be a good set of flags to use...
//        KEY_SHOOTER_MOTOR_IS_ON(F.ALWAYS, "ShooterMotorIsOn"),
//        KEY_SHOOTER_MOTOR_DEFAULT_TARGET_SPEED(F.MAYBE, "ShooterMotorDefaultTargetSpeed"),        
        KEY_SHOOTER_MOTOR_TARGET_SPEED(F.MAYBE, "ShooterMotorTargetSpeed"), 
        KEY_SHOOTER_MOTOR_SPEED(F.MAYBE, "ShooterMotorSpeed"),
        KEY_SHOOTER_AXLE_RPS(F.MAYBE, "ShooterAxleRps"),
        KEY_SHOOTER_TALON_RPM(F.MAYBE, "ShooterTalonRpm"),
        KEY_SHOOTER_VELOCITY_CLOSED_LOOP_ERR(F.MAYBE, "ShooterVelocityClosedLoopErr"),
//        KEY_SHOOTER_CLOSED_LOOP_ERR(F.MAYBE, "ShooterClosedLoopErr"),
		KEY_SHOOTER_AXLE_TARGET_RPS(F.MAYBE, "ShooterAxleTargetRps"),
		KEY_SHOOTER_SPEED_SELECT_IS_LOW(F.ALWAYS, "ShooterUsingLowSpeed"),
		
        KEY_SHOOTER_ABACUS_IS_ON(F.MAYBE, "ShooterAbacusOn"),
        KEY_SHOOTER_TRIGGER_MOTOR_IS_ON(F.MAYBE, "ShooterTriggerOn"),
        
////        KEY_SHOOTER_MOTOR_RPM(F.MAYBE, "ShooterMotorRPM"),
//        KEY_SHOOTER_MOTOR_DEFAULT_TARGET_RPS(F.MAYBE, "ShooterMotorDefaultRPS"),
//        KEY_SHOOTER_MOTOR_RPS(F.ALWAYS, "ShooterMotorRPS"),
//        KEY_SHOOTER_MOTOR_TARGET_RPS(F.MAYBE, "ShooterMotorTargetRPS"),
//        KEY_SHOOTER_MOTOR_ENCODER_COUNT(F.MAYBE, "ShooterEncoderCount"),
//        KEY_SHOOTER_MOTOR_ENCODER_SAMPLES_TO_AVERAGE(F.MAYBE, "ShooterEncoderSamplesToAverage"),
//        KEY_SHOOTER_MOTOR_AMPS(F.MAYBE, "ShooterMotorAmps"),
//        KEY_SHOOTER_MOTOR_POWER(F.MAYBE, "ShooterMotorPwr"),
////        KEY_SHOOTER_LAUNCHER_RETRACTED(F, "LauncherRetracted"),
//        KEY_SHOOTER_BALL_PRESENT(F.MAYBE, "BallPresent"),
//        KEY_SHOOTER_X_TUNEABLES_DEFAULT(F.MAYBE, "XTuneablesDflts"),
//        KEY_SHOOTER_X_TUNEABLES_INPUTS(F.MAYBE, "XTuneablesInput"),
//        KEY_SHOOTER_Y_TUNEABLES_DEFAULT(F.MAYBE, "YTuneablesDflts"),
//        KEY_SHOOTER_Y_TUNEABLES_INPUTS(F.MAYBE, "YTuneablesInput"),
                
//        KEY_INTAKE_MOTOR_DEFAULT_TARGET_SPEED(F.MAYBE, "IntakeMotorDefaultTargetSpeed"),
//        KEY_INTAKE_MOTOR_TARGET_SPEED(F.MAYBE, "IntakeMotorTargetSpeed"),
//        KEY_INTAKE_MOTOR_SPEED(F.MAYBE, "IntakeMotorSpeed"),
//        KEY_INTAKE_MOTOR_AMPS(F.MAYBE, "IntakeMotorAmps"),
//        KEY_INTAKE_MOTOR_SPEED_AUX(F.MAYBE, "IntakeMotorAuxSpeed"),
//        KEY_INTAKE_MOTOR_AMPS_AUX(F.MAYBE, "IntakeMotorAuxAmps"),
        KEY_INTAKE_MOTOR_ABSORB(F.ALWAYS, "IntakeGrabbing"),
        KEY_INTAKE_MOTOR_REPEL(F.ALWAYS, "IntakeRepelling"),
        
        KEY_FLIPPER_EXTENDED(F.ALWAYS, "FlipperExtended"),
        
        KEY_CLIMBER_ENABLED(F.ALWAYS, "ClimberEnabled"),
        KEY_CLIMBER_LIMIT_SWITCH(F.ALWAYS, "ClimberAtTop"),
        KEY_CLIMBER_AMPS_A(F.ALWAYS, "ClimberAAmps"),
        KEY_CLIMBER_AMPS_B(F.ALWAYS, "ClimberBAmps"),
        KEY_CLIMBER_AMPS_40(F.ALWAYS, "ClimberAmps"),
        KEY_CLIMBER_A_MAX_MIN_AMPS(F.ALWAYS, "ClimberAMaxMinAmps"),
        KEY_CLIMBER_B_MAX_MIN_AMPS(F.ALWAYS, "ClimberBMaxMinAmps"),
        KEY_CLIMBER_40_MAX_MIN_AMPS(F.ALWAYS, "ClimberMaxMinAmps"),
        KEY_CLIMBER_SPEED_WHEN_AMPS_ABOVE_MAX(F.ALWAYS, "ClimberSpeedWhenMaxAmpsExceeded"),
//        KEY_CLIMBER_MIN_AMPS_A(F.ALWAYS, "ClimberMinAmps"),
//        KEY_CLIMBER_MAX_AMPS_A(F.ALWAYS, "ClimberMaxAmps"),
//        KEY_CLIMBER_MIN_AMPS_B(F.ALWAYS, "ClimberBMinAmps"),
//        KEY_CLIMBER_MAX_AMPS_B(F.ALWAYS, "ClimberBMaxAmps"),
        
        KEY_CAMERA_FRONT_SELECTED(F.ALWAYS, "CameraFront"),
        KEY_CAMERA_REAR_SELECTED(F.ALWAYS, "CameraRear"),
        
//        KEY_AUTON_CHOOSER(F.ALWAYS, "AutonChooser"),

        KEY_BAT_INIT(F.ALWAYS, "batteryInit"),
        KEY_BAT_MIN(F.ALWAYS, "batteryMin"),
        KEY_BAT_MIN_TIME(F.ALWAYS, "batteryMinTime"),
        KEY_BAT_CUR(F.ALWAYS, "batteryCur"), 
        
        KEY_AUTON_CHOOSER(F.ALWAYS, "autonSel"),
        KEY_AUTON_ALG_NBR_STRING(F.MAYBE, "autonAlgNbr"),
        ;
        
        private F eFlags; //flags to pass to TmSdDbgSD methods to control when/whether to send to SmartDashboard
        private String eKey; //key to use when sending to SmartDashboard
        
        private SdKeysE(F flags, String key) {
        	eFlags = flags;
        	eKey = key;
        }
        public F getFlags() { return eFlags; }
        public String getKey() { return eKey; }
	}
	
	
    public class LwSubSysName
    {
        public static final String SS_ROBOT_DRIVE = "RobotDrive";
        public static final String SS_DRV_GEARSHIFTER = "DrvGearShifter";
        public static final String SS_BATTERY = "Battery";
        public static final String SS_CAMERA_LEDS = "CameraLeds";
        public static final String SS_SHOOTER = "Shooter";
        public static final String SS_CAMERA = "Camera";
//        public static final String SS_ARM = "Arm";
        public static final String SS_INTAKE = "Intake";
    }
    
    public class LwItemNames
    {
        public static final String CAMERA_LEDS = "cameraLeds";

        public static final String CAMERA_ = "camera__";
        
        public static final String DRIVE_MOTOR_LEFT_FRONT = "L Fr motor";
        public static final String DRIVE_MOTOR_LEFT_REAR = "L Rr motor";
        public static final String DRIVE_MOTOR_RIGHT_FRONT = "R Fr motor";
        public static final String DRIVE_MOTOR_RIGHT_REAR = "R Rr motor";
        
        public static final String DRIVE_ENCODER_RIGHT = "Right encoder";
        public static final String DRIVE_ENCODER_LEFT = "Left encoder";
        public static final String DRIVE_ENCODER_RIGHT_INPUT_A = "Rt enc. input A";
        public static final String DRIVE_ENCODER_RIGHT_INPUT_B = "Rt enc. input B";
        public static final String DRIVE_ENCODER_LEFT_INPUT_A = "Lft enc. input A";
        public static final String DRIVE_ENCODER_LEFT_INPUT_B = "Lft enc. input B";
        
        public static final String DRV_GEARSHIFTER = "DrvGear";
        
        public static final String SHOOTER_MOTOR = "ShooterMtr";
        public static final String SHOOTER_LAUNCHER = "ShooterLnchr";
        public static final String SHOOTER_SAIL = "ShooterSail";
        public static final String SHOOTER_ENCODER = "ShooterEnc";
        public static final String SHOOTER_BALL_SENSOR = "ShooterBallSns";
        
//        public static final String ARM_MOTOR = "ArmMtr";
//        public static final String ARM_ENCODER = "ArmMagEnc";
//        public static final String ARM_VERTICAL_LIMIT_SWITCH = "ArmVertSwitch";
//        public static final String ARM_OPTICAL_ENCODER = "ArmOptEnc";
//        public static final String ARM_OPTICAL_ENCODER_INPUT_A = "ArmOptEncInpA";
//        public static final String ARM_OPTICAL_ENCODER_INPUT_B = "ArmOptEncInpB";
        
        public static final String INTAKE_MOTOR = "IntakeMtr";
        public static final String INTAKE_MOTOR_AUX = "IntakeMtrAux";
    }	

}
