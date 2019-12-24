package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsShooter;

public class TmRoFilesAndPrefKeys {
	public class FileNames {
//		persistentFilename = "networktables.ini"		
        // NOTE: In previous years it was often necessary to DISABLE Windows FIREWALL in order to
        //       get ftp to work!! If you do it, remember to RE-ENABLE after ftp work is done.
    	
    	@SuppressWarnings("unused")
		private static final String USER_BASE_DIR = "/home/lvuser";
        
//        public static final String USB_CAMERA_IMAGES_DIR = USER_BASE_DIR + "/t744images/usb";
//        public static final String AXIS_CAMERA_IMAGES_DIR = USER_BASE_DIR + "/t744images/axis";
        
//        //note: camera image files will have something like a _nnn.png suffix
//        //appended to the filename prefixes defined below where nnn is a suitable
//        //decimal index value.
//        public static final String CAMERA_FN_PREFIX_RAW_IMAGE = AXIS_CAMERA_IMAGES_DIR + "/image";
//        public static final String CAMERA_FN_PREFIX_THRESHOLD_IMAGE = AXIS_CAMERA_IMAGES_DIR + "/thresholdImage";
//        public static final String CAMERA_FN_PREFIX_CONVEX_HULL_IMAGE = AXIS_CAMERA_IMAGES_DIR + "/convexHullImage";
//        public static final String CAMERA_FN_PREFIX_FILTERED_IMAGE = AXIS_CAMERA_IMAGES_DIR + "/filteredImage";

    	// preferences are now handled as "persistent" NetworkTables 3.0 values.  NetworkTables
    	// will save persistent values in a file on the roboRIO and will load/save them 
    	// automagically.  They can also be updated through SmartDashboard.  See the Java
    	// Programming document near pages 86-87 for details.
        @SuppressWarnings("unused")
		private static final String FRC_PREFERENCES_FN = "/home/lvuser/networktables.ini";

	}
	
//    private class PrefKeys
//    {
//        //keys used with preferences file (c.f. Tm16Misc.FileNames)
//    	
//    	//used to get data from the smartdashboard. Data is assumed to be 0xXXXX type Strings.
//    	public static final String PREFIX_KEY_DBG_TK_FLAGS = "TmDbgTkFlags"; //suffixes Enable, Disable, Default
//    	public static final String PREFIX_KEY_DBG_SD_FLAGS = "TmSdDbgSDFlags"; //suffixes Enable, Disable, Default
//    	
//    	public static final String KEY_OPTS_DBG_SMARTDASH_BITMASK = "TmSdDbgSD_bitmask";
//        public static final String KEY_OPTS_DBG_TOOLKIT_BITMASK = "TmDbgTk_bitmask";
//        
//    	public static final String KEY_OPTS_SHOOTER_IS_INSTALLED = "BootOptShooterIsInstalled";
//    	public static final String KEY_OPTS_CLIMBER_TALONS_INSTALLED = "BootOptClimberTalonsInstalled";
//    	public static final String KEY_PCM_PCM0_IS_INSTALLED = "BootOptPCM0IsInstalled";
//    	public static final String KEY_TESTING_WITH_FAKE_CAN_TALONS = "BootOptTestingWithFakeCanTalons";
//    	public static final String KEY_USB_CAMERA_0_IS_INSTALLED = "BootOptUsbCamera0Installed";
//    	public static final String KEY_USB_CAMERA_1_IS_INSTALLED = "BootOptUsbCamera1Installed";
//        
////        public static final String KEY_RUN_ON_SW_TEST_STATION = "BootOptRunningOnSwTestFixture";
////        public static final String KEY_RUN_DRIVE_WITH_GAMEPAD = "BootOptRunningDriveWithGamepad";
////        public static final String KEY_RUN_MECH_WITH_GAMEPAD = "BootOptRunningMechWithGamepad";
//        
////        public static final String KEY_DRIVE_USES_MAGNETIC_ENCODERS = "DriveUsesMagneticEncoders";
//        public static final String KEY_DRIVE_TUNING_VAL_RIGHT_FORWARD = "InitCfgDriveTuningRightForward";
//        public static final String KEY_DRIVE_TUNING_VAL_RIGHT_REVERSE = "InitCfgDriveTuningRightReverse";
//        public static final String KEY_DRIVE_TUNING_VAL_LEFT_FORWARD = "InitCfgDriveTuningLeftForward";
//        public static final String KEY_DRIVE_TUNING_VAL_LEFT_REVERSE = "InitCfgDriveTuningLeftReverse";
//        
//        public static final String KEY_SHOOTER_DEFAULT_TARGET_RPS_HIGH = "InitCfgShooterDefaultTargetRpsHigh";
//        public static final String KEY_SHOOTER_DEFAULT_TARGET_RPS_LOW = "InitCfgShooterDefaultTargetRpsLow";
//        public static final String KEY_SHOOTER_TARGET_RPS_TOLERANCE = "InitCfgShooterTargetRpsTolerance";
////        public static final String KEY_SHOOTER_TARGET_RPS = "ShooterTargetRps";
//        
//        public static final String KEY_SHOOTER_TARGET_MOTOR_SPEED_HIGH = "InitCfgShooterTargetDrumSpeedHigh";
//        public static final String KEY_SHOOTER_TARGET_MOTOR_SPEED_LOW = "InitCfgShooterTargetDrumSpeedLow";
//
//        public static final String KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED = "InitCfgShooterTriggerTargetSpeed";
//
//        public static final String KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED = "InitCfgShooterAbacusTargetSpeed";
//
//        public static final String KEY_SHOOTER_ENCODER_IS_USABLE = "InitCfgShooterEncoderUsable";
//        
//        public static final String KEY_SHOOTER_CHANGE_TALON_MODE = "InitCfgShooterAllowChangeTalonMode";
//        
//        public static final String KEY_SHOOTER_DRIVE_SPEED_LIMITER = "InitCfgShooterDriveSpeedLimit";
//
//		////        public static final String KEY_AXIS_CAMERA_INSTALLED = "AxisCameraInstalled";
//////        public static final String KEY_USB_CAMERA_INSTALLED = "UsbCameraInstalled";
////        public static final String KEY_CAMERA_NAME = "CameraNameForAutoCapture";
//      
////        //required key for saving preferences.  Must match Preferences.SAVE_FIELD.
////        public static final String KEY_SAVE_TO_FILE = "~S A V E~";
//    }

/*
//    public class PrefKeys_AsBagged
//    {
//        //keys used with preferences file (c.f. Tm16Misc.FileNames)
//    	
//    	//used to get data from the smartdashboard. Data is assumed to be 0xXXXX type Strings.
//    	public static final String PREFIX_KEY_DBG_TK_FLAGS = "TmDbgTkFlags"; //suffixes Enable, Disable, Default
//    	public static final String PREFIX_KEY_DBG_SD_FLAGS = "TmSdDbgSDFlags"; //suffixes Enable, Disable, Default
//    	
//    	public static final String KEY_PCM_PCM0_IS_INSTALLED = "BootOptPCM0IsInstalled";
//    	public static final String KEY_TESTING_WITH_FAKE_CAN_TALONS = "BootOptTestingWithFakeCanTalons";
//    	public static final String KEY_USB_CAMERA_0_IS_INSTALLED = "BootOptUsbCamera0Installed";
//    	public static final String KEY_USB_CAMERA_1_IS_INSTALLED = "BootOptUsbCamera1Installed";
//        
////        public static final String KEY_RUN_ON_SW_TEST_STATION = "BootOptRunningOnSwTestFixture";
////        public static final String KEY_RUN_DRIVE_WITH_GAMEPAD = "BootOptRunningDriveWithGamepad";
////        public static final String KEY_RUN_MECH_WITH_GAMEPAD = "BootOptRunningMechWithGamepad";
//        
////        public static final String KEY_DRIVE_USES_MAGNETIC_ENCODERS = "DriveUsesMagneticEncoders";
//        public static final String KEY_DRIVE_TUNING_VAL_RIGHT_FORWARD = "DriveTuningRightForward";
//        public static final String KEY_DRIVE_TUNING_VAL_RIGHT_REVERSE = "DriveTuningRightReverse";
//        public static final String KEY_DRIVE_TUNING_VAL_LEFT_FORWARD = "DriveTuningLeftForward";
//        public static final String KEY_DRIVE_TUNING_VAL_LEFT_REVERSE = "DriveTuningLeftReverse";
//        
//        public static final String KEY_OPTS_DBG_SMARTDASH_BITMASK = "TmSdDbgSD_bitmask";
//        public static final String KEY_OPTS_DBG_TOOLKIT_BITMASK = "TmDbgTk_bitmask";
//        
//        public static final String KEY_SHOOTER_DEFAULT_TARGET_RPS_HIGH = "ShooterDefaultTargetRpsHigh";
//        public static final String KEY_SHOOTER_DEFAULT_TARGET_RPS_LOW = "ShooterDefaultTargetRpsLow";
////        public static final String KEY_SHOOTER_TARGET_RPS = "ShooterTargetRps";
//        
//        public static final String KEY_SHOOTER_TARGET_MOTOR_SPEED_HIGH = "ShooterTargetMotorSpeedHigh";
//        public static final String KEY_SHOOTER_TARGET_MOTOR_SPEED_LOW = "ShooterTargetMotorSpeedLow";
//
//        public static final String KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED = "ShooterTriggerTargetMotorSpeed";
//        public static final String KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED = "ShooterAbacusTargetMotorSpeed";
//
//		////        public static final String KEY_AXIS_CAMERA_INSTALLED = "AxisCameraInstalled";
//////        public static final String KEY_USB_CAMERA_INSTALLED = "UsbCameraInstalled";
////        public static final String KEY_CAMERA_NAME = "CameraNameForAutoCapture";
//      
//        //required key for saving preferences.  Must match Preferences.SAVE_FIELD.
//        public static final String KEY_SAVE_TO_FILE = "~S A V E~";
//    }
*/

    public enum PrefTypeE { BOOLEAN, INT, DOUBLE, STRING; }
    public enum PrefKeysE
    {
    	//used to get data from the smartdashboard. Data is assumed to be 0xXXXX type Strings.
    	//PREFIX_KEY_DBG_TK_FLAGS("TmDbgTkFlags", default), //suffixes Enable, Disable, Default
    	//PREFIX_KEY_DBG_SD_FLAGS("TmSdDbgSDFlags", default), //suffixes Enable, Disable, Default
    	
    	//KEY_OPTS_DBG_SMARTDASH_BITMASK("TmSdDbgSD_bitmask", default),
        //KEY_OPTS_DBG_TOOLKIT_BITMASK("TmDbgTk_bitmask", default),
        
//	private static class Defaults {
////		public static final boolean DFLT_OPT_TESTING_ON_SW_TEST_FIXTURE = false;
////		public static final boolean DFLT_OPT_DRIVE_WITH_GAME_CONTROLLER = false;
////		public static final boolean DFLT_OPT_MECH_WITH_GAME_CONTROLLER = false;
//		public static final boolean DFLT_OPT_PCM0_INSTALLED = true;
//		public static final boolean DFLT_OPT_USE_FAKE_CAN_TALONS = false;
//		public static final boolean DFLT_OPT_USB_CAM_0_INSTALLED = false;
//		public static final boolean DFLT_OPT_USB_CAM_1_INSTALLED = false;
//	}
    	KEY_PCM_PCM0_IS_INSTALLED("BootOptPCM0IsInstalled", Tm17Opts.Defaults.DFLT_OPT_PCM0_INSTALLED),
    	KEY_TESTING_WITH_FAKE_CAN_TALONS("BootOptTestingWithFakeCanTalons", Tm17Opts.Defaults.DFLT_OPT_USE_FAKE_CAN_TALONS),
    	KEY_USB_CAMERA_0_IS_INSTALLED("BootOptUsbCamera0Installed", Tm17Opts.Defaults.DFLT_OPT_USB_CAM_0_INSTALLED),
    	KEY_USB_CAMERA_1_IS_INSTALLED("BootOptUsbCamera1Installed", Tm17Opts.Defaults.DFLT_OPT_USB_CAM_1_INSTALLED),
    	KEY_OPTS_SHOOTER_IS_INSTALLED("BootOptShooterIsInstalled", Tm17Opts.Defaults.DFLT_OPT_SHOOTER_IS_INSTALLED),
    	KEY_OPTS_CLIMBER_TALONS_AB_INSTALLED("BootOptClimberTalonsABInstalled", Tm17Opts.Defaults.DFLT_OPT_CLIMBER_TALONS_AB_INSTALLED),
    	KEY_OPTS_CLIMBER_TALON_40A_INSTALLED("BootOptClimberTalon40AInstalled", Tm17Opts.Defaults.DFLT_OPT_CLIMBER_TALON_40A_INSTALLED),

        KEY_SHOOTER_DRUM_USES_TALON_PID("BootCfgShooterDrumTalanPid", TmSsShooter.Cnst.DEFAULT_DRUM_USES_TALON_PID),

//        KEY_RUN_ON_SW_TEST_STATION("BootOptRunningOnSwTestFixture", Tm17Opts.Defaults.),
//        KEY_RUN_DRIVE_WITH_GAMEPAD("BootOptRunningDriveWithGamepad", Tm17Opts.Defaults.),
//        KEY_RUN_MECH_WITH_GAMEPAD("BootOptRunningMechWithGamepad", Tm17Opts.Defaults.),
        
//        KEY_DRIVE_USES_MAGNETIC_ENCODERS("DriveUsesMagneticEncoders", default),
        KEY_DRIVE_TUNING_VAL_RIGHT_FORWARD("InitCfgDriveTuningRightForward", TmSsDriveTrain.DriveTrainPreferences.DRIVE_MOTOR_TUNING_RIGHT_FORWARD),
        KEY_DRIVE_TUNING_VAL_RIGHT_REVERSE("InitCfgDriveTuningRightReverse", TmSsDriveTrain.DriveTrainPreferences.DRIVE_MOTOR_TUNING_RIGHT_REVERSE),
        KEY_DRIVE_TUNING_VAL_LEFT_FORWARD("InitCfgDriveTuningLeftForward", TmSsDriveTrain.DriveTrainPreferences.DRIVE_MOTOR_TUNING_LEFT_FORWARD),
        KEY_DRIVE_TUNING_VAL_LEFT_REVERSE("InitCfgDriveTuningLeftReverse", TmSsDriveTrain.DriveTrainPreferences.DRIVE_MOTOR_TUNING_LEFT_REVERSE),
        
        KEY_SHOOTER_DEFAULT_TARGET_RPS_HIGH("InitCfgShooterDefaultTargetRpsHigh", TmSsShooter.ShooterDrumMgmt.DrumCnst.SHOOTER_AXLE_TARGET_RPS_HIGH),
        KEY_SHOOTER_DEFAULT_TARGET_RPS_LOW("InitCfgShooterDefaultTargetRpsLow", TmSsShooter.ShooterDrumMgmt.DrumCnst.SHOOTER_AXLE_TARGET_RPS_LOW),
        KEY_SHOOTER_TARGET_RPS_TOLERANCE("InitCfgShooterTargetRpsTolerance", TmSsShooter.ShooterDrumMgmt.DrumCnst.SHOOTER_AXLE_RPS_TOLERANCE),
//        KEY_SHOOTER_TARGET_RPS("ShooterTargetRps", default),
        
        KEY_SHOOTER_TARGET_MOTOR_SPEED_HIGH("InitCfgShooterTargetDrumSpeedHigh", TmSsShooter.ShooterDrumMgmt.DrumCnst.SHOOTER_MOTOR_TARGET_SPEED_HIGH),
        KEY_SHOOTER_TARGET_MOTOR_SPEED_LOW("InitCfgShooterTargetDrumSpeedLow", TmSsShooter.ShooterDrumMgmt.DrumCnst.SHOOTER_MOTOR_TARGET_SPEED_LOW),

        KEY_SHOOTER_TRIGGER_TARGET_MOTOR_SPEED("InitCfgShooterTriggerTargetSpeed", TmSsShooter.ShooterTriggerMgmt.TrigCnst.TRIGGER_TARGET_MOTOR_SPEED),

        KEY_SHOOTER_ABACUS_TARGET_MOTOR_SPEED("InitCfgShooterAbacusTargetSpeed", TmSsShooter.ShooterAbacusMgmt.AbacusCnst.ABACUS_TARGET_MOTOR_SPEED),

        KEY_SHOOTER_ENCODER_IS_USABLE("InitCfgShooterEncoderUsable", TmSsShooter.Cnst.DEFAULT_ENCODER_IS_USABLE_STATE),
                
        KEY_SHOOTER_CHANGE_TALON_MODE("InitCfgShooterAllowChangeTalonMode", TmSsShooter.Cnst.DEFAULT_ALLOW_CHANGE_TALON_CNTL_MODE),
        
        KEY_SHOOTER_DRIVE_SPEED_LIMITER("InitCfgShooterDriveSpeedLimit", TmSsShooter.Cnst.MAX_DRIVE_SPEED_ALLOWED),
        ;
        
        
        private final String eKey;
        private final PrefTypeE eType;
        private double eValDouble;
        private int eValInt;
        private String eValString;
        private boolean eValBoolean;
        
        private PrefKeysE(String key, boolean defaultVal) {	eKey = key; eType = PrefTypeE.BOOLEAN; eValBoolean = defaultVal;}
        private PrefKeysE(String key, int defaultVal) {	eKey = key; eType = PrefTypeE.INT; eValInt = defaultVal; }
        private PrefKeysE(String key, double defaultVal) {	eKey = key; eType = PrefTypeE.DOUBLE; eValDouble = defaultVal; }
        private PrefKeysE(String key, String defaultVal) {	eKey = key; eType = PrefTypeE.STRING; eValString = defaultVal; }
        
        public String getKey() { return eKey; }
        public PrefTypeE getType() { return eType; }
        
        public boolean getDefaultBoolean() {
        	PrefTypeE expectedType = PrefTypeE.BOOLEAN;
        	if( ! eType.equals(expectedType)) { 
        		throw TmExceptions.getInstance().new InappropriatePreferenceRequestEx(this.name() + " is " + eType.name() + " not " + expectedType.name());
        	} else { return eValBoolean; }
        }
        public int getDefaultInt() {
        	PrefTypeE expectedType = PrefTypeE.INT;
        	if( ! eType.equals(expectedType)) { 
        		throw TmExceptions.getInstance().new InappropriatePreferenceRequestEx(this.name() + " is " + eType.name() + " not " + expectedType.name());
        	} else { return eValInt; }
        }
        public double getDefaultDouble() {
        	PrefTypeE expectedType = PrefTypeE.DOUBLE;
        	if( ! eType.equals(expectedType)) { 
        		throw TmExceptions.getInstance().new InappropriatePreferenceRequestEx(this.name() + " is " + eType.name() + " not " + expectedType.name());
        	} else { return eValDouble; }
        }
        public String getDefaultString() {
        	PrefTypeE expectedType = PrefTypeE.STRING;
        	if( ! eType.equals(expectedType)) { 
        		throw TmExceptions.getInstance().new InappropriatePreferenceRequestEx(this.name() + " is " + eType.name() + " not " + expectedType.name());
        	} else { return eValString; }
        }
    }
    
}
