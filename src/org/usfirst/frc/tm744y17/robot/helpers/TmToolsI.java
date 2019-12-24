package org.usfirst.frc.tm744y17.robot.helpers;

import java.util.Collections;

import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmFlagsI.F;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;

import edu.wpi.first.wpilibj.Preferences;

//import org.usfirst.frc.tm744y17.robot.devices.TmAnalogInput;

public interface TmToolsI {
	
//    /**
//     * FRC uses values from -1.0 to 1.0 for lots of things.  We need
//     * to ensure that we don't feed in values outside of this range.
//     * @param testValue
//     * @return testValue clamped to the range -1.0 to 1.0
//     */
//    public static double ttClampFrcValue(double testValue)
//    {
//        if(testValue<-1) {return -1;}
//        else if(testValue>1) {return 1;}
//        else {return testValue;}
//    }
    
//    /**
//     * this is an example of a generic method.  We can write it once without knowing the 
//     * class or type of the input values.  The "<T>" before the return type tells the 
//     * compiler that we're using T as a placeholder for a type in what we code here.
//     * When we call the method, we tell the compiler what type to expect by placing
//     * it in brackets as a prefix to the method name.  For example:
//     * 		boolean inRange = <int>ttIsInRange(3, 2, 6);
//     * 		inRange = <double>ttIsInRange(0, -0.9, 2.5);
//     * @param testVal
//     * @param min
//     * @param max
//     * @return
//     */
//    public static <T extends Comparable<T>> boolean ttIsInRange (T testVal, T min, T max) {
//    	boolean ans = false;
//    	if((testVal>=min) && (testVal<=max)) { ans = true; }
//    	return ans;
//    }
	
	public enum PrefCreateE { CREATE_AS_NEEDED, DO_NOT_CREATE }

    public class Tt { //we can include this and then prefix these method names with Tt. to make
    	              //the code easier to read and to write.
    		
        /**
         * FRC uses values from -1.0 to 1.0 for lots of things.  We need
         * to ensure that we don't feed in values outside of this range.
         * @param testValue
         * @return testValue clamped to the range -1.0 to 1.0
         */
        public static double clampToFrcRange(double testValue)
        {
            if(testValue<-1) {return -1;}
            else if(testValue>1) {return 1;}
            else {return testValue;}
        }


        /**
         * Extract only the class name from the fully qualified class name returned
         * by {object}.getClass().getName().  For example for fully qualified class
         * name "java.lang.Integer", will return "Integer".
         * @param fqClassName - the fully qualified class name
         * @return just the class name itself
         */
        public static String extractClassName(Object obj)
        {
            return extractClassName(obj.getClass().getName());
        }
        public static String extractClassName(String fqClassName)
        {
            int dotNdx = fqClassName.lastIndexOf('.');
                    // decPtNdx = m_str.indexOf('.');
            return fqClassName.substring(dotNdx+1);
        }

    	
    	public static boolean isEven(int testVal) {
			boolean ans = false;
			if(0 == (testVal & 0x0001)) { ans = true; }
			return ans;
		}

    	public enum EndpointHandlingE { INCLUDE_ENDPOINTS, EXCLUDE_ENDPOINTS }
    	public static boolean isInRange(double testVal, double min, double max) {
//			boolean ans = false;
//			if((testVal>=min) && (testVal<=max)) { ans = true; }
//			return ans;
    		return isInRange(testVal, min, max, EndpointHandlingE.INCLUDE_ENDPOINTS);
		}
    	public static boolean isInRange(double testVal, double min, double max, EndpointHandlingE endPtHdlr) {
			boolean ans = false;
			if((testVal>=min) && (testVal<=max)) { ans = true; }
			if(endPtHdlr.equals(EndpointHandlingE.EXCLUDE_ENDPOINTS)) {
				if((testVal==min) || (testVal==max)) {
					ans = false;
				}
			}
			return ans;
		}
    
    	public static boolean isWithinTolerance(double testVal, double target, double tolerance) {
			boolean ans = false;
			double absTolerance = Math.abs(tolerance);
			if(testVal>=(target-absTolerance) && testVal<=(target+absTolerance)) { ans = true; }
			return ans;
		}
    

    	public static boolean isInRange(int testVal, int min, int max) {
			boolean ans = false;
			if((testVal>=min) && (testVal<=max)) { ans = true; }
			return ans;
		}
    
    	public static boolean isWithinCount(int testVal, int min, int cnt) {
			boolean ans = false;
			if((testVal>=min) && (testVal<=min + cnt - 1)) { ans = true; }
			return ans;
		}
    	
    	/**
    	 * round a double value to the nearest integer
    	 * 3.5 rounds to 4, -3.5 rounds to -4, etc.
    	 * @param val
    	 * @return
    	 */
    	public static int doubleToRoundedInt(double val) {
    		int ans;
    		int sign = (val >= 0) ? +1: -1;
    		double dbl;

    		dbl = Math.abs(val) + 0.5;
    		dbl -= (dbl % 1); //subtract the fractional part
    		ans = sign * (int)dbl;
    		
    		return ans;
    	}

		static Preferences preferences = Preferences.getInstance();
		
		public static boolean isPreferencePresent(String key) {
			return preferences.containsKey(key);
		}
		
		/**
		 * 
		 * @param key
		 * @param defaultSetting
		 * @param description
		 * @param printFlags - 0 never prints, -1 always prints, other settings may or may not print
		 * @param createAsNeeded - create an entry if the key isn't found
		 * @return
		 */
    	public static boolean getPreference(PrefKeysE keyDef, boolean defaultSetting, int printFlags, PrefCreateE createAsNeeded) {
			boolean ans;
			String key = keyDef.getKey();
			ans = defaultSetting;
			if(key != null) {
				boolean keyPresent = preferences.containsKey(key);
				if(keyPresent) {
					ans = preferences.getBoolean(key, defaultSetting);
					P.println(printFlags, "key '" + key + "' found in Preferences: " + ans);
				} else {
					P.println(printFlags, "key " + key + " not found in Preferences.");
					if(createAsNeeded.equals(PrefCreateE.CREATE_AS_NEEDED)) {
						preferences.putBoolean(key, defaultSetting);
						P.println(printFlags, "key " + key + " created in Preferences with value " + defaultSetting + ".");
					}
				}
			}
			return ans;
    	}
    	public static int getPreference(PrefKeysE keyDef, int defaultSetting, int printFlags, PrefCreateE createAsNeeded) {
			int ans;
			String key = keyDef.getKey();
			ans = defaultSetting;
			if(key != null) {
				boolean keyPresent = preferences.containsKey(key);
				if(keyPresent) {
					ans = preferences.getInt(key, defaultSetting);
					P.println(printFlags, "key '" + key + "' found in Preferences: " + ans);
				} else {
					P.println(printFlags, "key " + key + " not found in Preferences.");
					if(createAsNeeded.equals(PrefCreateE.CREATE_AS_NEEDED)) {
						preferences.putInt(key, defaultSetting);
						P.println(printFlags, "key " + key + " created in Preferences with value " + defaultSetting + ".");
					}
				}
			}
			return ans;
    	}
    	public static double getPreference(PrefKeysE keyDef, double defaultSetting, int printFlags, PrefCreateE createAsNeeded) {
			double ans;
			String key = keyDef.getKey();
			ans = defaultSetting;
			if(key != null) {
				boolean keyPresent = preferences.containsKey(key);
				if(keyPresent) {
					ans = preferences.getDouble(key, defaultSetting);
					P.println(printFlags, "key '" + key + "' found in Preferences: " + ans);
				} else {
					P.println(printFlags, "key " + key + " not found in Preferences.");
					if(createAsNeeded.equals(PrefCreateE.CREATE_AS_NEEDED)) {
						preferences.putDouble(key, defaultSetting);
						P.println(printFlags, "key " + key + " created in Preferences with value " + defaultSetting + ".");
					}
				}
			}
			return ans;
    	}
    	public static String getPreference(PrefKeysE keyDef, String defaultSetting, int printFlags, PrefCreateE createAsNeeded) {
    		String ans;
    		String key = keyDef.getKey();
			ans = defaultSetting;
			if(key != null) {
				boolean keyPresent = preferences.containsKey(key);
				if(keyPresent) {
					ans = preferences.getString(key, defaultSetting);
					P.println(printFlags, "key '" + key + "' found in Preferences: " + ans);
				} else {
					P.println(printFlags, "key " + key + " not found in Preferences.");
					if(createAsNeeded.equals(PrefCreateE.CREATE_AS_NEEDED)) {
						preferences.putString(key, defaultSetting);
						P.println(printFlags, "key " + key + " created in Preferences with value " + defaultSetting + ".");
					}
				}
			}
			return ans;
    	}
    
//    //for optical sensor
//    //in the lab we read approx. 4V when tracking light, 0.8V when tracking dark
//    private static final double TRACKING_REFLECTIVE_THRESHOLD = 3.0;
//    public static boolean isSeeingLight(TmAnalogInput trackingSns)
//    {
//        return (trackingSns.getVoltage() < TRACKING_REFLECTIVE_THRESHOLD);
//    }
//    public static boolean isSeeingDark(TmAnalogInput trackingSns)
//    {
//        return (trackingSns.getVoltage() >= TRACKING_REFLECTIVE_THRESHOLD);
//    }
    	
    }

    
//    //options used with System.out.println output
//    static PrOpE m_defaultPrOpEi = PrOpE.FLUSH;
//    public enum PrOpE {
//    	FLUSH(true), NOFLUSH(false);
//    	protected final boolean eFlush;
//    	private PrOpE(boolean flush) { eFlush = flush; }
//    }
    
    /**
     * wrappers for System.out.println and optional System.out.flush, etc.
     * @author JudiA
     *
     */
    public class P {
        //options used with System.out.println output
        static PrOpE m_defaultPrOpEi = PrOpE.FLUSH;
        public enum PrOpE {
        	FLUSH(true), NOFLUSH(false);
        	protected final boolean eFlush;
        	private PrOpE(boolean flush) { eFlush = flush; }
        }
        
        public static void println(int flagMask, String strToPrtAndFlush) {
        	if(flagMask != 0) {
        		println(strToPrtAndFlush);
        	}
        }
	    public static void println(String strToPrtAndFlush) {
	    	println(F.ALWAYS, PrOpE.FLUSH, strToPrtAndFlush);
	    }
	    public static void println(TmFlagsI.F flags, String strToPrtAndFlush) {
	    	println(flags, PrOpE.FLUSH, strToPrtAndFlush);
	    }
	    public static void println(PrOpE flushEi, String strToPrt) {
	    	println(F.ALWAYS, flushEi, strToPrt);
	//    	System.out.println(strToPrt);
	//    	if(flushEi.eFlush) { System.out.flush(); }
	    }
	    public static void println(TmFlagsI.F flags, PrOpE flushEi, String strToPrt) {
	    	if(flags.isActive()) {
	    		System.out.println(strToPrt);
	    		if(flushEi.eFlush) { System.out.flush(); }
	    	}
	    }
	    
		/**
		 * String gets formatted only if it's going to get displayed.  Performance should
		 * improve if the message is disabled, but that can sometimes complicate debug of
		 * timing-related problems, so we have a special flag that forces the formatting
		 * even when the message is not going to be displayed.
		 * @param flagBits
		 * @param stringToPrint
		 */
		public static void printFrmt(int flagBits, String formatStr, Object... args ) {
			if(flagBits != 0) {
				System.out.println(String.format(formatStr, args));
			}
////			boolean doFormat = m_flagMgr.isFlagsOn(reservedAlwaysFormatBit);
////			boolean doDisplay = m_flagMgr.isFlagsOn(flagBits);
//			boolean doFormat = m_flagMgr.isFlagsOnBySection(reservedAlwaysFormatBit);
//			boolean doDisplay = m_flagMgr.isFlagsOnBySection(flagBits);
//			if(doDisplay || doFormat) {
////				System.out.println(stringToPrint);
//				if(doDisplay) {
//					System.out.println(String.format(formatStr, args));
//				}
//				else if(doFormat) {
//					//do something to force the string to be formatted even though we 
//					//aren't going to display it.  Helps keep timings the same whether
//					//the message is displayed or not.  Sometimes needed to debug (or
//					//circumvent) timing-related problems.
//					if(String.format(formatStr, args).equals("")) {
//						printIt(0,"bogus msg");
//					}
//				}
//			}
		}
		

    }
    
    
    public enum TtDebuggingE { DEBUG, NO_DEBUG }
    

    /**
     * boiler-plate code for...
     * @author JudiA
     *
     */
    public class TtNamedAssignments {
    	String l_namedObjType;
    	public TtNamedAssignments(String namedObjType) { //"PCM", "USB", "USB camera", etc.
    		l_namedObjType = namedObjType;
    	}
    	String l_assignedTo = "";
    	boolean l_isAssigned = false;
    	
    	public void assign(String assignTo) { 
    		boolean alreadyAssigned = isAssigned();
    		l_assignedTo += ", " + assignTo;
    		l_isAssigned = true;
    		if(alreadyAssigned) {
    			System.out.println("Duplicate " + l_namedObjType + " assignment: " + showAssignedTo() );
    		}
    	}
    	public boolean isAssigned() { return l_isAssigned; }
    	public String showAssignedTo() { return l_assignedTo; }
    }

}
