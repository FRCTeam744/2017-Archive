package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefKeysE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.PrefCreateE;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

//import edu.wpi.first.wpilibj.Preferences;

public class Tm17Opts {

	//default settings
	public static class Defaults {
//		public static final boolean DFLT_OPT_TESTING_ON_SW_TEST_FIXTURE = false;
//		public static final boolean DFLT_OPT_DRIVE_WITH_GAME_CONTROLLER = false;
//		public static final boolean DFLT_OPT_MECH_WITH_GAME_CONTROLLER = false;
		public static final boolean DFLT_OPT_PCM0_INSTALLED = true;
		public static final boolean DFLT_OPT_USE_FAKE_CAN_TALONS = false;
		public static final boolean DFLT_OPT_USB_CAM_0_INSTALLED = false;
		public static final boolean DFLT_OPT_USB_CAM_1_INSTALLED = false;
		public static final boolean DFLT_OPT_CLIMBER_TALONS_AB_INSTALLED = true;
		public static final boolean DFLT_OPT_CLIMBER_TALON_40A_INSTALLED = false;
		public static final boolean DFLT_OPT_SHOOTER_IS_INSTALLED = true;
	}
	
	public static class Cnst { public static final int DEFAULT_PRINT_FLAGS_INT = -1; }
	
//	public static boolean isRunningOnSwTestFixture() { 
//		return OptionsE.OPT_RUNNING_ON_SW_TEST_STATION.isOptionActive();
//	}	
//	public static boolean isDrivingWithGameController() { 
//		return OptionsE.OPT_DRIVING_WITH_GAME_CONTROLLER.isOptionActive();
//	}
//	public static boolean isRunningMechWithGameController() { 
//		return OptionsE.OPT_MECHANISMS_WITH_GAME_CONTROLLER.isOptionActive();
//	}
	public static boolean isClimberTalonsABInstalled() { 
		return OptionsE.OPT_CLIMBER_TALONS_AB_INSTALLED.isOptionActive();
	}
	public static boolean isClimberTalon40AInstalled() { 
		return OptionsE.OPT_CLIMBER_TALON_40A_INSTALLED.isOptionActive();
	}
//	public static boolean isShooterInstalled() { 
//		return OptionsE.OPT_SHOOTER_IS_INSTALLED.isOptionActive();
//	}
	public static boolean isPcm0Installed() { 
		return OptionsE.OPT_PCM0_IS_INSTALLED.isOptionActive();
	}
	public static boolean isUseFakeCanTalons() { 
		return OptionsE.OPT_USE_FAKE_CAN_TALONS.isOptionActive();
	}
	public static boolean isUsbCam0Installed() { 
		return OptionsE.OPT_USB_CAM_0_IS_INSTALLED.isOptionActive();
	}
	public static boolean isUsbCam1Installed() { 
		return OptionsE.OPT_USB_CAM_1_IS_INSTALLED.isOptionActive();
	}

	public static void postToSdOptions() {
		for(OptionsE e : OptionsE.values()) {
			e.postToSd();
		}
	}
	
	public enum OptionsE {
//		OPT_RUNNING_ON_SW_TEST_STATION(Defaults.DFLT_OPT_TESTING_ON_SW_TEST_FIXTURE, 
//				PrefKeysE.KEY_RUN_ON_SW_TEST_STATION, 
//				SdKeysE.KEY_OPTS_RUNNING_ON_SW_TEST_FIXTURE,
//				"running on SW test station"),
//		OPT_DRIVING_WITH_GAME_CONTROLLER(Defaults.DFLT_OPT_DRIVE_WITH_GAME_CONTROLLER,  
//				PrefKeysE.KEY_RUN_DRIVE_WITH_GAMEPAD, 
//				SdKeysE.KEY_OPTS_RUNNING_DRIVE_WITH_GAMEPAD,
//				"driving with gamepad"),
//		OPT_MECHANISMS_WITH_GAME_CONTROLLER(Defaults.DFLT_OPT_MECH_WITH_GAME_CONTROLLER,  
//				PrefKeysE.KEY_RUN_MECH_WITH_GAMEPAD, 
//				SdKeysE.KEY_OPTS_RUNNING_MECHANISMS_WITH_GAMEPAD,
//				"controlling mechanisms with gamepad"),
		OPT_CLIMBER_TALONS_AB_INSTALLED(Defaults.DFLT_OPT_CLIMBER_TALONS_AB_INSTALLED,
				PrefKeysE.KEY_OPTS_CLIMBER_TALONS_AB_INSTALLED,
				SdKeysE.KEY_OPTS_CLIMBER_TALONS_AB_INSTALLED,
				"able to access climber AB talons"),
		OPT_CLIMBER_TALON_40A_INSTALLED(Defaults.DFLT_OPT_CLIMBER_TALON_40A_INSTALLED,
				PrefKeysE.KEY_OPTS_CLIMBER_TALON_40A_INSTALLED,
				SdKeysE.KEY_OPTS_CLIMBER_TALON_40A_INSTALLED,
				"able to access climber 40A talon"),
//		OPT_SHOOTER_IS_INSTALLED(Defaults.DFLT_OPT_SHOOTER_IS_INSTALLED,
//				PrefKeysE.KEY_OPTS_SHOOTER_IS_INSTALLED,
//				SdKeysE.KEY_OPTS_SHOOTER_IS_INSTALLED,
//				"able to access shooter"),
		OPT_USB_CAM_0_IS_INSTALLED(Defaults.DFLT_OPT_USB_CAM_0_INSTALLED,
				PrefKeysE.KEY_USB_CAMERA_0_IS_INSTALLED,
				SdKeysE.KEY_OPTS_USB_CAMERA_0_IS_INSTALLED,
				"able to access USB cam0"),
		OPT_USB_CAM_1_IS_INSTALLED(Defaults.DFLT_OPT_USB_CAM_1_INSTALLED,
				PrefKeysE.KEY_USB_CAMERA_1_IS_INSTALLED,
				SdKeysE.KEY_OPTS_USB_CAMERA_1_IS_INSTALLED,
				"able to access USB cam1"),
		OPT_PCM0_IS_INSTALLED(Defaults.DFLT_OPT_PCM0_INSTALLED,  
				PrefKeysE.KEY_PCM_PCM0_IS_INSTALLED, 
				SdKeysE.KEY_OPTS_PCM0_IS_INSTALLED,
				"able to access PCM0"),
		OPT_USE_FAKE_CAN_TALONS(Defaults.DFLT_OPT_USE_FAKE_CAN_TALONS,  
				PrefKeysE.KEY_TESTING_WITH_FAKE_CAN_TALONS, 
				SdKeysE.KEY_OPTS_TESTING_WITH_FAKE_CAN_TALONS,
				"testing with FAKE CAN Talons"),
		;
		private boolean eOptSetting;
		private final boolean eOptSettingDefault;
		private boolean eOptSettingRdy;
		private PrefKeysE eOptPreferencesKey;
		private SdKeysE eOptSdKey;
		private String eOptDescription;
		
		private OptionsE(boolean defaultSetting, PrefKeysE preferencesKey,
				TmMiscSdKeys.SdKeysE sdKey, String optDescription) {
			eOptSettingRdy = false;
			eOptSettingDefault = defaultSetting;
			eOptSetting = defaultSetting;
			eOptPreferencesKey = preferencesKey;
			eOptSdKey = sdKey;
			eOptDescription = optDescription;
			if(defaultSetting != preferencesKey.getDefaultBoolean()) {
				P.println("WARNING: default in OptionsE." + this.name() + " (" + 
								defaultSetting + ") ignored, does not match default in key def " + preferencesKey.name());
			}
		}
		
		public void postToSd() {
			if(eOptSdKey != null) {
				TmPostToSd.dbgPutBoolean(eOptSdKey, isOptionActive());
			}
		}
		
		public boolean isOptionActive() {
			boolean ans;
			if(eOptSettingRdy) {
				ans = eOptSetting;
			} else {
//				//first time we've been called or else we're refreshing
//				Preferences preferences = Preferences.getInstance();
//				String key = eOptPreferencesKey;
//				boolean defaultSetting = eOptSettingDefault;
//				String optDescription = eOptDescription;
//				
//				ans = defaultSetting;
//				if(key != null) {
//					boolean keyPresent = preferences.containsKey(key);
//					if(keyPresent) {
//						ans = preferences.getBoolean(key, defaultSetting);
//						P.println(-1, "per Preferences file we " + (ans ? "ARE" : "are NOT") + 
//								" " + optDescription); //" running on the software test fixture");
//					} else {
//						P.println(-1, "key " + key + " not found in preferences file.");
//					}
//				}
//				ans = Tt.getPreference(eOptPreferencesKey.getKey(), eOptSettingDefault, -1, PrefCreateE.CREATE_AS_NEEDED);
				ans = Tt.getPreference(eOptPreferencesKey, eOptPreferencesKey.getDefaultBoolean(), Cnst.DEFAULT_PRINT_FLAGS_INT, 
													PrefCreateE.CREATE_AS_NEEDED);
				eOptSetting = ans;
				P.println("Boot Opt: we " + (ans ? "ARE " : "are NOT ") + eOptDescription);
				eOptSettingRdy = true;
			}
			return ans;
		}
	
	}

}
