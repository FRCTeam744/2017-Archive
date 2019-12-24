package org.usfirst.frc.tm744y17.robot.helpers;

import edu.wpi.first.wpilibj.Preferences;

public interface TmFlagsI {
	static int m_enabledFlags = F.MAYBE.eValue;
	public enum F {
		ALWAYS(-1),
		MAYBE(0x0100),
		NEVER(0);
		
		private final int eValue;
		
		private F(int val) { eValue = val; }
		
		public boolean isActive() { 
			return (eValue != 0); 
		}
	}
	
	public class W { //class holding names of flag bits indicating "why"
		//Java allows underscores used to separate groups of hex digits for readability
		//	public static final int NEVER = 0;
		//	public static final int ALWAYS = kCATEGORY_MASK;

		public static final int T = 0x0000_0001; //"tuning" - for use while tuning, needed only if something changes
		public static final int D = 0x0000_0002; //"debug" - for debug, then probably never want it again
		public static final int M = 0x0000_0004; //"monitor" - for monitoring something interesting but not essential
		public static final int V = 0x0000_0008; //"verify" - verify correct operation of new devices 
		public static final int E = 0x0000_0010; //"essential" - should always be shown for the subsystem or other group
		//	public static final int   = 0x0000_0020; //
		//	public static final int   = 0x0000_0040; //
		//	public static final int   = 0x0000_0080; //

		public static final int TD = T|D;
		public static final int TM = T|M;
		public static final int TV = T|V;
		public static final int TDM = T|D|M;
		public static final int TDV = T|D|V;
		public static final int TMV = T|M|V;
		public static final int TDMV = T|D|M|V;
		public static final int DM = D|M;
		public static final int DV = D|V;
		public static final int DMV = D|M|V;
		public static final int MV = M|V;

	}

	public class BitFields {
	public static final int kWHY_MASK			    	= 0x0000_00FF;
	public static final int kGROUP_MASK 				= 0x3FFF_FF00;
	public static final int kRESERVED_MASK				= 0xC000_0000;
	public static final int kRESERVED_BIT_ALWAYS_FORMAT	= 0x4000_0000; //reserved for forcing formatting of msg even when not printed
	public static final int kRESERVED_BIT_ESSENTIAL_MSG	= 0x8000_0000; //should always display this message
	}
	
	public class G { //flag bits for Groups
		//	public static final int Z_		= 0x0000_0100; // 
		//	public static final int SS_		= 0x0000_0200; //
		//	public static final int SS_		= 0x0000_0400; //
		//	public static final int SS_		= 0x0000_0800; //

		public static final int AMPS	= 0x0000_1000; // motor Amps
		public static final int GRIP	= 0x0000_2000; // GRIP stuff used for vision processing
		//	public static final int SS_		= 0x0000_4000; //
		//	public static final int SS_		= 0x0000_8000; //

		public static final int SS_DRV 	= 0x0001_0000; //drivetrain
		public static final int SS_GEAR	= 0x0002_0000; //gear shift
		public static final int SS_ARM	= 0x0004_0000; //arm
		public static final int SS_IN	= 0x0008_0000; //intake

		public static final int SS_SH	= 0x0010_0000; //shooter
		public static final int SS_CAM	= 0x0020_0000; //camera
		public static final int SS_AUTO	= 0x0040_0000; //autonomous info
		public static final int SS_DLED	= 0x0080_0000; //dancing LEDs

		public static final int SS_CAML	= 0x0100_0000; //camera LEDs
		public static final int SS_BATT	= 0x0200_0000; //battery info
		public static final int PID_ARM = 0x0400_0000; //tune arm PID	
		public static final int PID_SH  = 0x0800_0000; //tune shooter PID

		public static final int PID_DRV_L = 0x1000_0000; //tune left-side PID
		public static final int PID_DRV_R = 0x2000_0000; //tune right-side PID

		public static final int ALL_SS = SS_DRV | SS_GEAR | SS_ARM | SS_IN |
				SS_SH | SS_CAM | SS_DLED | SS_CAML	|
				SS_BATT | SS_AUTO;
	}

	public class M { //master flags independent of categories and groups
		public static final int NEVER = 0;
		public static final int ALWAYS = BitFields.kWHY_MASK | BitFields.kGROUP_MASK; //all categories and groups
		public static final int ALL = ALWAYS; //alternate name that makes more sense in some contexts
		public static final int NONE = NEVER; //alternate name that makes more sense in some contexts		
	}
	//	/**
	//	 * preferences can be used to set a new default value, or to enable or
	//	 * disable specific bits.  This enum lists those possibilities.
	//	 */
	//	public static enum PreferencesUpdateTypesE { DEFAULT, ENABLES, DISABLES }

	/**
	 * this enum is used to specify when/whether to display the results of a
	 * update made to the flags.
	 */
	public static enum ShowUpdateResultsE {
		ALWAYS_SHOW_RESULT, SHOW_MODIFIED_RESULTS, NEVER;
	}
	
	/**
	 * Used to manage bit flags via a 32-bit integer
	 */
	public class FlagMgmt { //flag management
		public final Object fmLock = new Object();
		public int fmInUse;
		public int fmHardDefault;
		public int fmDefault;
		public String fmPrefFileOverrideKeyPrefix;
		public String fmInstanceName;
		
		public FlagMgmt(String instanceName, int hardcodedDefault, String prefKey) {
			fmInstanceName = instanceName;
			fmHardDefault = hardcodedDefault;
			fmDefault = fmHardDefault;
			fmInUse = hardcodedDefault;
			fmPrefFileOverrideKeyPrefix = prefKey;
			updateDefault(ShowUpdateResultsE.ALWAYS_SHOW_RESULT);
			synchronized(fmLock) { fmInUse = fmDefault; }
		}
		
		public void enableFlags(int flagBits) {
			synchronized(fmLock) { fmInUse |= flagBits; }
		}
		
		public void disableFlags(int flagBits) {
			synchronized(fmLock) { fmInUse &= ~flagBits; }
		}
		
		public void changeFlags(int flagBitsToEnable, int flagBitsToDisable) {
			synchronized(fmLock) {
				fmInUse |= flagBitsToEnable;
				fmInUse &= ~ flagBitsToDisable;
			}
		}
		
		public int getFlags() {
			synchronized(fmLock) { return fmInUse; }
		}
		
//		/** use isFlagsOnByBitField() instead */
//		@Deprecated
//		public boolean isFlagsOn(int flagsToCheck) {
//			synchronized(fmLock) { return ( 0 != (fmInUse & flagsToCheck)); }
//		}
		
		public boolean isFlagsOnByBitField(int flagsToCheck) {
			synchronized(fmLock) { return isWhyFlagsOn(flagsToCheck) && isGroupFlagsOn(flagsToCheck); }
		}
		
		public boolean isWhyFlagsOn(int flagsToCheck) {
			synchronized(fmLock) { return ( 0 != ((fmInUse & BitFields.kWHY_MASK) & flagsToCheck)); }
		}
		
		public boolean isGroupFlagsOn(int flagsToCheck) {
			synchronized(fmLock) { return ( 0 != ((fmInUse & BitFields.kGROUP_MASK) & flagsToCheck)); }
		}
		
		public boolean isReservedFlagsOn(int flagsToCheck) {
			synchronized(fmLock) { return ( 0 != ((fmInUse & BitFields.kRESERVED_MASK) & flagsToCheck)); }
		}
		
		private int getPreferencesValue(String keyPrefix, String keySuffix, int defaultVal, ShowUpdateResultsE show) {
			int ans = defaultVal;
			String strBack = String.format("0x%08X", ans);
			String strAns;
			boolean keyPresent = false;
			String key = keyPrefix + keySuffix;
			Preferences preferences = Preferences.getInstance();
			if(key != null) {
				keyPresent = preferences.containsKey(key);
				if(keyPresent) {
					strAns = preferences.getString(key, strBack);
					if((strAns != null) && (! strAns.equals("")) && (! strAns.equals(strBack)) 
							&& strAns.startsWith("0x") && (strAns.length()>2)) {
						strAns = strAns.substring(2);
						ans = Integer.parseInt(strAns, 16);
					} else {
						ans = defaultVal;
					}
					if( ! show.equals(ShowUpdateResultsE.NEVER)) {
						System.out.println("[" + fmInstanceName + ":" + key + "] preferences file flags are " + String.format("0x%08X", ans));
					}
				} else {
					if( ! show.equals(ShowUpdateResultsE.NEVER)) {
						System.out.println("[" + fmInstanceName + "] key " + key + " not found in preferences file.");
					}
				}
			}
			return ans;
		}
		
		public void updateDefault() { updateDefault(ShowUpdateResultsE.SHOW_MODIFIED_RESULTS); }
		public void updateDefault(ShowUpdateResultsE showResults) {
			int prevDefault;
			String keySuffix = "Default";
			int prefsVal = getPreferencesValue(fmPrefFileOverrideKeyPrefix, keySuffix, fmHardDefault, showResults);
			boolean showAlways = showResults.equals(ShowUpdateResultsE.ALWAYS_SHOW_RESULT);
			boolean showNever = showResults.equals(ShowUpdateResultsE.NEVER);
			synchronized(fmLock) {
				prevDefault = fmDefault;
				fmDefault = prefsVal;
			}
			if( ! showNever) {
				if( showAlways || (prefsVal != prevDefault)) { 
					System.out.println("[" + fmInstanceName + "] " + keySuffix + " flags: " + String.format("0x%08X", prefsVal));
				}
			}

		}

		public void updateEnable() { updateEnable(ShowUpdateResultsE.SHOW_MODIFIED_RESULTS); }
		public void updateEnable(ShowUpdateResultsE showResults) {
			int prevValue;
			String keySuffix = "Enable";
			int prefsVal = getPreferencesValue(fmPrefFileOverrideKeyPrefix, keySuffix, 0, showResults);
			int newVal;
			boolean showAlways = showResults.equals(ShowUpdateResultsE.ALWAYS_SHOW_RESULT);
			boolean showNever = showResults.equals(ShowUpdateResultsE.NEVER);
			synchronized(fmLock) {
				prevValue = fmInUse;
				fmInUse |= prefsVal;
				newVal = fmInUse;
			}
			if( ! showNever) {
				if( showAlways || (newVal != prevValue)) { 
					System.out.println(String.format("[%s] %s flags: 0x%08X, flags in use now: 0x%08X",
							fmInstanceName, keySuffix, prefsVal, newVal));
				}
			}

		}

		public void updateDisable() { updateDisable(ShowUpdateResultsE.SHOW_MODIFIED_RESULTS); }
		public void updateDisable(ShowUpdateResultsE showResults) {
			int prevValue;
			String keySuffix = "Disable";
			int prefsVal = getPreferencesValue(fmPrefFileOverrideKeyPrefix, keySuffix, 0, showResults);
			int newVal;
			boolean showAlways = showResults.equals(ShowUpdateResultsE.ALWAYS_SHOW_RESULT);
			boolean showNever = showResults.equals(ShowUpdateResultsE.NEVER);
			synchronized(fmLock) {
				prevValue = fmInUse;
				fmInUse &= ~prefsVal;
				newVal = fmInUse;
			}
			if( ! showNever) {
				if( showAlways || (newVal != prevValue)) { 
					System.out.println(String.format("[%s] %s flags: 0x%08X, flags in use now: 0x%08X",
							fmInstanceName, keySuffix, prefsVal, newVal));
				}
			}

		}

		public void updateEnableDisable() { updateEnableDisable(ShowUpdateResultsE.SHOW_MODIFIED_RESULTS); }
		public void updateEnableDisable(ShowUpdateResultsE showResults) {
			int prevValue;
			String keySuffixEnable = "Enable";
			String keySuffixDisable = "Disable";
			int prefsValEnable = getPreferencesValue(fmPrefFileOverrideKeyPrefix, keySuffixEnable, 0, showResults);
			int prefsValDisable = getPreferencesValue(fmPrefFileOverrideKeyPrefix, keySuffixDisable, 0, showResults);
			int newVal;
			boolean showAlways = showResults.equals(ShowUpdateResultsE.ALWAYS_SHOW_RESULT);
			boolean showNever = showResults.equals(ShowUpdateResultsE.NEVER);
			synchronized(fmLock) {
				prevValue = fmInUse;
				fmInUse |= prefsValEnable;
				fmInUse &= ~prefsValDisable;
				newVal = fmInUse;
			}
			if( ! showNever) {
				if( showAlways || (newVal != prevValue)) { 
					System.out.println(String.format("[%s] %s flags: 0x%08X, %s flags: 0x%08X, flags in use now: 0x%08X",
							fmInstanceName, keySuffixEnable, prefsValEnable, keySuffixDisable, prefsValDisable, newVal));
				}
			}

		}

	}

}
