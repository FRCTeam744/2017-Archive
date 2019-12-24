package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.T744Robot2017;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;

public class TmHdwrDsMap extends TmHdwrDsPhys {
	
	/*TODO
	 * keypad functions on the Driver Station (2015).  See "Driver Station Keys"
	 * in the "FRC Driver Station" document on-line or as a PDF.
	 * 
	 * 		F1		- forces rescan of USB ports
	 * 		[]\		- enables the robot (not sure about the [], may have been a
	 *           		funky font in the PDF file)
	 * 		Enter	- disable the robot
	 * 		Space	- emergency stop the robot	 * 
	 * 
	 */

	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmHdwrDsMap m_instance = new TmHdwrDsMap();
	public static synchronized TmHdwrDsMap getInstance() {
		if (m_instance == null) {
			m_instance = new TmHdwrDsMap();
		}
		return m_instance;
	}
	private TmHdwrDsMap() {}
	/*----------------end of getInstance stuff----------------*/

	
    public enum DsHidDefE
    {
    	//x=a?b:c; means if(a){x=b;}else{x=c;}
    	LEFT_DRIVE_JOYSTICK(UsbPortsE.USB0, DsHidDevTypesE.kJOYSTICK, Cnst.DEFAULT_JOYSTICK_DEADZONE_TOLERANCE),
    	RIGHT_DRIVE_JOYSTICK(UsbPortsE.USB1, DsHidDevTypesE.kJOYSTICK, Cnst.DEFAULT_JOYSTICK_DEADZONE_TOLERANCE),
    	MECHANISM_GAME_CONTROLLER(UsbPortsE.USB2, DsHidDevTypesE.kGAME_CNTLR, Cnst.DEFAULT_JOYSTICK_DEADZONE_TOLERANCE),
//    	MECHANISMS_JOYSTICK(UsbPortsE.USB3, DsHidDevTypesE.kJOYSTICK, Cnst.DEFAULT_JOYSTICK_DEADZONE_TOLERANCE),
//    	DRIVE_GAME_CONTROLLER(UsbPortsE.USB4, DsHidDevTypesE.kGAME_CNTLR, Cnst.DEFAULT_JOYSTICK_DEADZONE_TOLERANCE),
    	;
    	
    	private HidDevice eHidDevice;
    	UsbPortsE eUsb;
    	DsHidDevTypesE eHidType;
    	double eDeadzoneTolerance;
    	String eToStringFullData;
    	
    	private DsHidDefE(UsbPortsE uPortDef, DsHidDevTypesE devType, double deadzoneTolerance) {
    		eUsb = uPortDef;
    		eHidType = devType;
//    		eHidDevice = TmHdwrDsPhys.getInstance().new HidDevice(uPortDef, devType, this.toString());
    		eDeadzoneTolerance = deadzoneTolerance;
    		eToStringFullData = null;
    	}
    	
    	public HidDevice getHidDevice() {
    		if(eHidDevice==null) {
    			eHidDevice =  TmHdwrDsPhys.getInstance().new HidDevice(eUsb, eHidType, eDeadzoneTolerance, this.toString());
    		}
    		return eHidDevice; 
    	}
    	
    	private String setupToStringFull() {
    		// minus sign in the format string is a flag meaning "pad on the right" (i.e. left-justify the string)
    		String ans = "   HID input device: %-25s (%-4s, expecting %-11s%s)";
    		String hidDescr = getHidDevice().getName();
    		if( ! hidDescr.equals("")) {
    			hidDescr = String.format("\n%48shdwr found for this USB: %s", " ", hidDescr);
    		}
//    		ans = "   HID input device: " + this.name() + " (" + eUsb.name() + ", expecting " + eHidType.name() + hidDescr +  ")";
    		ans = String.format(ans, this.name(), eUsb.name(), eHidType.name(), hidDescr);
    		return ans;
    	}
    	
    	public String toStringFull() {
    		if(eToStringFullData == null) { eToStringFullData = setupToStringFull(); }
    		return eToStringFullData;
//    		// minus sign in the format string is a flag meaning "pad on the right" (i.e. left-justify the string)
//    		String ans = "   HID input device: %-25s (%-4s, expecting %-11s%s)";
//    		String hidDescr = getHidDevice().getName();
//    		if( ! hidDescr.equals("")) {
//    			hidDescr = String.format("\n%48shdwr found for this USB: %s", " ", hidDescr);
//    		}
////    		ans = "   HID input device: " + this.name() + " (" + eUsb.name() + ", expecting " + eHidType.name() + hidDescr +  ")";
//    		ans = String.format(ans, this.name(), eUsb.name(), eHidType.name(), hidDescr);
//    		return ans;
    	}
    	
    	public static void showAllHidDefs() {
    		for(DsHidDefE e : DsHidDefE.values()) {
    			P.println(e.toStringFull());
    		}
    	}
    	
    	//helper methods
    	public double getY() { 
    		if(this.getHidDevice().getHidType().equals(DsHidDevTypesE.kJOYSTICK)) {
    			throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("getY() called for invalid DsHidDefE." + 
    					this.name() + " - not a Joystick");
    		}
    		return this.getHidDevice().getY();
    	}
    	
    	public int getUsbPortNbr() {
    		return eUsb.ePortNbr;
    	}
    }
	
    public static class Cnst {
	    //Code supports specifying two possible physical inputs for a named input, selected by a
	    //true/false condition (a value that is known at boot time). If we want to force one of the 
	    //choices to always be used, we can use one of these two constants to keep the code readable.
		public static final boolean INPUT_SELECT_CONDITION_ALWAYS_TRUE = true; //always use the first physical input
		public static final boolean INPUT_SELECT_CONDITION_ALWAYS_FALSE = false; //always use the second physical input
		
		public static final double DEFAULT_JOYSTICK_DEADZONE_TOLERANCE = 0.05;
		
		public static final PovPopulationE POV_NBR_IGNORED = PovPopulationE.POV0;
		public static final int POV_ANGLE_IGNORED = 0;
		public static final double ANALOG_RANGE_MIN_DEFAULT = 0.5;
		public static final double ANALOG_RANGE_MAX_DEFAULT = 1.0;
		public static final double ANALOG_RANGE_MIN_IGNORED = ANALOG_RANGE_MIN_DEFAULT;
		public static final double ANALOG_RANGE_MAX_IGNORED = ANALOG_RANGE_MAX_DEFAULT;
		public static final DsHidDefE ALT_HID_DEF_IGNORED = null;
		public static final HidInputDefE ALT_HID_INPUT_DEF_IGNORED = null;
    }

    public enum DsInputSourceDefE {
        //experimentation: our joystick z-button (<joystick>.getZ() - the
        //roller at the front bottom) is -1 when all the way up, +1 when
        //all the way down. Convert the -1 to +1 range to a 0 to +1 range
        //for servos.
    	//when the input select condition is true, use the first hid/input pairing, when false, use the alt pairing
    	ROBOT_DRIVE_LEFT_SPEED_INPUT(DsHidFeatureE.kAnalog, 
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.LEFT_JOY_Y,
    									DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.Y_AXIS),
    	ROBOT_DRIVE_RIGHT_SPEED_INPUT(DsHidFeatureE.kAnalog,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.RIGHT_JOY_Y,
    									DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.Y_AXIS),

    	//last changed 2/24/17, fix "bag night" problems by swapping solenoid assignments instead of swapping joysticks here.
    	//drivers want high gear on right joystick, low gear on left joystick
    	DRIVE_IN_HIGH_GEAR_BTN(DsHidFeatureE.kButton,  
    									DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT1_TRIGGER),
    	DRIVE_IN_LOW_GEAR_BTN(DsHidFeatureE.kButton,  
    									DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT1_TRIGGER),
    	
    	DRIVE_DIRECTION_FORWARD_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.Y_BUTTON,
    									DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT3_BACK),
    	DRIVE_DIRECTION_REVERSE_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.A_BUTTON,
    									DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT2_FRONT),

    	DRIVE_START_JOYSTICKS_AND_CENTER_MOTOR_COAST_BTN(DsHidFeatureE.kButton,
    									DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT4_LEFT),
    	DRIVE_STOP_JOYSTICKS_AND_CENTER_MOTOR_COAST_BTN(DsHidFeatureE.kButton,
										DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT5_RIGHT),
    	
    	CAMERA_SELECTION_FRONT_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_NORTH_BUTTON,
    									DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT3_BACK),
    	CAMERA_SELECTION_REAR_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//    									DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_SOUTH_BUTTON,
    									DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.TOP_BUT2_FRONT),
    	
    	SHOOTER_HIGH_SPEED_BTN(DsHidFeatureE.kButton,  
										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.POV_EAST_BUTTON),
//				(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RB_BUTTON,
//										DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.BASE_BUT9_FRONT_RIGHT),
    	SHOOTER_LOW_SPEED_BTN(DsHidFeatureE.kButton, 
										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.POV_WEST_BUTTON),
//				(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LB_BUTTON,
//										DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.BASE_BUT8_FRONT_LEFT),

    	SHOOTER_START_STATE_MACHINE_BTN(DsHidFeatureE.kButton, 
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RB_BUTTON), 
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RB_BUTTON,
//										DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.BASE_BUT10_RIGHT_FRONT),
    	SHOOTER_OFF_BTN(DsHidFeatureE.kButton,  
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LB_BUTTON),
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LB_BUTTON,
//										DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.BASE_BUT11_RIGHT_BACK),
    	
    	SHOOTER_START_MANUAL_MODE_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RB_BUTTON,
										DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT9_FRONT_RIGHT),
    	SHOOTER_MANUAL_TOGGLE_DRUM_MOTOR_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LB_BUTTON,
										DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT6_LEFT_BACK),
    	
    	SHOOTER_MANUAL_TOGGLE_ABACUS_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RB_BUTTON,
										DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT8_FRONT_LEFT),
    	SHOOTER_MANUAL_TOGGLE_TRIGGER_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isRunningMechWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LB_BUTTON,
										DsHidDefE.RIGHT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT7_LEFT_FRONT),
    	
    	BALL_INTAKE_MOTORS_ON_REVERSE_BTN(DsHidFeatureE.kButton,
										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.BACK_BUTTON),
    	BALL_INTAKE_MOTORS_ON_BTN(DsHidFeatureE.kButton,
										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.Y_BUTTON),
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.Y_BUTTON,
//    									DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.TOP_BUT2_FRONT),
    	BALL_INTAKE_MOTORS_OFF_BTN(DsHidFeatureE.kButton,
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.A_BUTTON),
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.A_BUTTON, 
//    									DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.TOP_BUT3_BACK),
    	
    	GEAR_FLIPPER_EXTEND_AND_DRIVE_BACKWARDS_BTN(DsHidFeatureE.kButton,
				DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.POV_SOUTH_BUTTON),

    	GEAR_FLIPPER_EXTEND_BTN(DsHidFeatureE.kButton,
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.X_BUTTON),
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.X_BUTTON, 
//    									DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.TOP_BUT4_LEFT),
    	GEAR_FLIPPER_RETRACT_BTN(DsHidFeatureE.kButton,
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.B_BUTTON),
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.B_BUTTON, 
//    									DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.TOP_BUT5_RIGHT),
    	
    	ROPE_CLIMBING_MOTOR_SPEED_INPUT(DsHidFeatureE.kAnalog,
    									DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RIGHT_JOY_Y),
    	ROPE_CLIMBING_HALF_MOTOR_SPEED_INPUT(DsHidFeatureE.kAnalog,
				DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.LEFT_JOY_Y),
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.RIGHT_JOY_Y,
//										DsHidDefE.MECHANISMS_JOYSTICK, HidInputDefE.Y_AXIS),

    	CAMERA_LEDS_TOGGLE_ON_OFF_BTN(DsHidFeatureE.kButton,  
//    			(Tm17Opts.isDrivingWithGameController() || Tm17Opts.isRunningOnSwTestFixture()),
//										DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_EAST_BUTTON,
										DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT7_LEFT_FRONT),
    	
    	CLIMBER_TOGGLE_ENABLE_BTN(DsHidFeatureE.kButton,
    			DsHidDefE.MECHANISM_GAME_CONTROLLER, HidInputDefE.START_BUTTON),
    	
//        SHOW_ALL_BUTTON_SETTINGS_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_WEST_BUTTON),
        SHOW_ALL_BUTTON_SETTINGS_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT11_RIGHT_BACK),
//
//        SHOW_ALL_ROBOT_IO_SETTINGS_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_NORTH_BUTTON),
        SHOW_ALL_ROBOT_IO_SETTINGS_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT10_RIGHT_FRONT),
        SHOW_ALL_AUTON_ALGS_AND_PREFERENCES_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.BASE_BUT9_FRONT_RIGHT),
        
//        TEST_Z_ROLLER(DsHidFeatureE.kAnalog, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.Z_ROLLER),
        TEST_Z_ROLLER_BTN_A(DsHidFeatureE.kCannedAnalogButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.JOY_Z_DOWN_AS_BUTTON),
        TEST_Z_ROLLER_BTN_B(DsHidFeatureE.kCannedAnalogButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.JOY_Z_MID_AS_BUTTON),
        TEST_Z_ROLLER_BTN_C(DsHidFeatureE.kCannedAnalogButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.JOY_Z_UP_AS_BUTTON),
//        TEST_Z_ROLLER_BTN_D(DsHidFeatureE.kCannedAnalogButton, DsHidDefE.LEFT_DRIVE_JOYSTICK, HidInputDefE.JOY_Z_UP_AS_BUTTON),
//        
//        SHOW_ALL_MOTOR_CFG_INFO_IN_CONSOLE(DsHidFeatureE.kButton, DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.POV_SOUTH_BUTTON),
////        SHOW_ALL_MOTOR_CFG_INFO_IN_CONSOLE_JS(DsHidDefE.RIGHT_JOYSTICK, HidInputDefE.BASE_BUT10),
//        
////        SHIFT_DRIVE_TO_HIGH_GEAR_JS(DsHidDefE.RIGHT_JOYSTICK, HidInputDefE.TOP_BUT3),
////        SHIFT_DRIVE_TO_LOW_GEAR_JS(DsHidDefE.RIGHT_JOYSTICK, HidInputDefE.TOP_BUT2),
//	    SHIFT_DRIVE_TO_HIGH_GEAR(DsHidFeatureE.kButton, DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.RIGHT_TRIGGER),
//	    SHIFT_DRIVE_TO_LOW_GEAR(DsHidFeatureE.kButton, DsHidDefE.DRIVE_GAME_CONTROLLER, HidInputDefE.LEFT_TRIGGER),
//        
//        
////    	DANCING_LEDS_TEST_ALL(DsHidDefE.RIGHT_JOYSTICK, JoyInputsE.BASE_BUT8),
////    	DANCING_LEDS_TEST_ALL_JS(DsHidDefE.RIGHT_JOYSTICK, JoyInputsE.BASE_BUT8),

        ;
    	    	
        private final DsHidDefE eHidDef;
        private final HidInputDefE eHidInputDef;
 
        private final DsHidDefE eHidDefRaw;
        private final HidInputDefE eHidInputDefRaw;
        private final DsHidDefE eHidDefAltRaw;
        private final HidInputDefE eHidInputDefAltRaw;
        
        private String eToStringFullInfo = null;
        
        /**
         * a class that pairs the hid and the input source
         */
        private final HidInputSource eHidInputSource;
        
        /**
         * true iff the hid selected doesn't support the input source selected
         */
        private final boolean eHasConfigErrs;
        
        /**
         * true if the expected input feature is compatible with the feature of the input source selected
         */
        
        private final boolean eSane;
        
        private final MultiInputUsageTypeE eCombo;        
        private final boolean eCondition; //for debug
        
        private final DsHidFeatureE eHidFeatureExpected;
        
        private final  double eAnalogRdgMin;
        private final  double eAnalogRdgMax;
        private final PovPopulationE ePov;
        
        private final int ePovAngleRaw; //for debug use
        private final int ePovAngle;
        
    	boolean l_isPrimaryInputsHaveCfgErrs = false;
    	boolean l_isAltInputsHaveCfgErrs = false;
    	boolean l_isPrimaryInputsCompatible = false;
    	boolean l_isAltInputsCompatible = false;

    	private boolean l_buttonMade;        
        private boolean l_badButtonReported = false;
        private boolean l_badAnalogReported = false;
        private boolean l_badPovReported = false;
        
        public void reportBadButtonUsed() {
        	if( ! l_badButtonReported) {
        		l_badButtonReported = true;
        		System.out.println("[T744Err] " + this.toString() + " is not a valid HID button");
        	}
        }
        public void reportBadAnalogUsed(DsHidDevTypesE usingAsType) {
        	if( ! l_badAnalogReported) {
        		l_badAnalogReported = true;
        		System.out.println("[T744Err] " + this.toString() + " is not a valid HID analog input (axis) for " + usingAsType.toString());
        	}
        }
        public void reportBadPovUsed(DsHidDevTypesE usingAsType) {
        	if( ! l_badPovReported) {
        		l_badPovReported = true;
        		System.out.println("[T744Err] " + this.toString() + " is not a valid HID POV for " + usingAsType.toString());
        	}
        }
        
        private enum MultiInputUsageTypeE { NONE, CONDITIONAL_ALT } //{ NONE, CONDITIONAL_ALT, BOTH, EITHER, DYNAMIC_CONDITIONAL }
        
        private DsInputSourceDefE(DsHidFeatureE inpType, DsHidDefE hidDef, HidInputDefE hidInpDef) {
        	this(inpType, hidDef, hidInpDef, MultiInputUsageTypeE.NONE, Cnst.ALT_HID_DEF_IGNORED, Cnst.ALT_HID_INPUT_DEF_IGNORED, 
        			Cnst.INPUT_SELECT_CONDITION_ALWAYS_TRUE, 
        			Cnst.ANALOG_RANGE_MIN_IGNORED, Cnst.ANALOG_RANGE_MAX_IGNORED,
        			Cnst.POV_NBR_IGNORED, Cnst.POV_ANGLE_IGNORED);
        }
        
        private DsInputSourceDefE(DsHidFeatureE inpType, boolean condition, DsHidDefE hidDef, HidInputDefE hidInpDef, 
        				DsHidDefE hidDefAlt, HidInputDefE hidInpDefAlt) {
        	this(inpType, hidDef, hidInpDef, MultiInputUsageTypeE.CONDITIONAL_ALT, hidDefAlt, hidInpDefAlt, condition, 
        			Cnst.ANALOG_RANGE_MIN_IGNORED, Cnst.ANALOG_RANGE_MAX_IGNORED,
        			Cnst.POV_NBR_IGNORED, Cnst.POV_ANGLE_IGNORED);
        }
        
        private DsInputSourceDefE(DsHidFeatureE inpType, DsHidDefE hidDef, HidInputDefE hidInpDef, 
				  double analogRdgMin, double analogRdgMax) {
			this(inpType, hidDef, hidInpDef, MultiInputUsageTypeE.NONE, Cnst.ALT_HID_DEF_IGNORED, Cnst.ALT_HID_INPUT_DEF_IGNORED,
					Cnst.INPUT_SELECT_CONDITION_ALWAYS_TRUE,
					analogRdgMin, analogRdgMax,  Cnst.POV_NBR_IGNORED, Cnst.POV_ANGLE_IGNORED);
		}

        private DsInputSourceDefE(DsHidFeatureE inpType, DsHidDefE hidDef, HidInputDefE hidInpDef, MultiInputUsageTypeE combo, 
        						  DsHidDefE hidDefAlt, HidInputDefE hidInpDefAlt, boolean condition,
        						  double analogRdgMin, double analogRdgMax) {
        	this(inpType, hidDef, hidInpDef, MultiInputUsageTypeE.CONDITIONAL_ALT, hidDefAlt, hidInpDefAlt, condition, 
        							analogRdgMin, analogRdgMax,  Cnst.POV_NBR_IGNORED, Cnst.POV_ANGLE_IGNORED);
        }
        
        private DsInputSourceDefE(DsHidFeatureE inpType, DsHidDefE hidDef, HidInputDefE hidInpDef, PovPopulationE pov, int angle) {
			this(inpType, hidDef, hidInpDef, MultiInputUsageTypeE.NONE, 
					Cnst.ALT_HID_DEF_IGNORED, Cnst.ALT_HID_INPUT_DEF_IGNORED, Cnst.INPUT_SELECT_CONDITION_ALWAYS_TRUE, 
					Cnst.ANALOG_RANGE_MIN_IGNORED, Cnst.ANALOG_RANGE_MAX_IGNORED,  pov, angle);
		}
        
        private static int l_errCnt = 0;

        /**
         * 
         * @param expectedHidFeatureDef - the hid input type user is expecting this to be. Will verify that it's compatible with
         * 					the type in hidInpDef
         * @param hidDef - the HID to use for this input
         * @param hidInpDef - the physical button/joystick/POV/... on the HID
         * @param combo - used to properly interpret what to do if multiple hid inputs are provided
         * @param hidDefAlt - an alternate HID
         * @param hidInpDefAlt - the physical button/joystick/POV/... on the alternate HID
         * @param condition - the condition checked when combo is used to select between the first and the alternate
         * 						hid/hidInput pair
         * @param analogRdgMin - if creating a special analog button, this is the min value of the range indicating "pressed"
         * @param analogRdgMax - if creating a special analog button, this is the max value of the range indicating "pressed"
         * @param pov - if creating a special pov button or accessing a pov, this is the POV to use
         * @param angle - if creating a special pov button, this is the angle indicating "pressed"
         * 
         * Note: at present, special analog buttons and special pov buttons are not supported.  There are "canned" POV
         *       buttons defined in TmHdwrDsPhys that can be used.  There may be some "canned" analog buttons there too.
         *       Using canned buttons helps ensure that there won't be conflicts among the various driver station inputs
         */
        private DsInputSourceDefE(DsHidFeatureE expectedHidFeatureDef, 
        						  DsHidDefE hidDef, HidInputDefE hidInpDef, MultiInputUsageTypeE combo, 
        						  DsHidDefE hidDefAlt, HidInputDefE hidInpDefAlt, boolean condition,
        						  double analogRdgMin, double analogRdgMax, PovPopulationE pov, int angle) {
        	
            eHidFeatureExpected = expectedHidFeatureDef;
            
            eHidDefRaw = hidDef;
            eHidInputDefRaw = hidInpDef;
            eHidDefAltRaw = hidDefAlt;
            eHidInputDefAltRaw = hidInpDefAlt;
            
            eAnalogRdgMin = analogRdgMin;
            eAnalogRdgMax = analogRdgMax;
            ePov = pov;
            ePovAngleRaw = angle;
            ePovAngle = Math.abs(angle % 360); //ensure positive and in range
        	eCombo = combo;
        	eCondition = condition;
        	l_buttonMade = false;
        	
//        	eToStringFullInfo = setupToStringFull();
        	
        	boolean debugMe = false;
//        	//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
//        	TtDebuggingE debugging = debugMe ? TtDebuggingE.DEBUG : TtDebuggingE.NO_DEBUG;
//			if(this.name().equals("DRIVE_IN_HIGH_GEAR_BTN") ||
//					this.name().equals("DRIVE_IN_LOW_GEAR_BTN")) { 
//				String msg = "this is the button that doesn't work!!";
//				debugMe = true;
//				debugging = debugMe ? TtDebuggingE.DEBUG : TtDebuggingE.NO_DEBUG;
//			}

        	
        	switch(combo) {
        		case CONDITIONAL_ALT:
        			if(condition) {
                        eHidDef = hidDef;
                        eHidInputDef = hidInpDef;
        			} else {
                        eHidDef = hidDefAlt;
                        eHidInputDef = hidInpDefAlt;
        			}

        			break;
        		case NONE:
        		default:
                    eHidDef = hidDef;
                    eHidInputDef = hidInpDef;
        			break;
        	}
        	if(debugMe) {
        		String dbgMsg = "time to debug!!";
        	}
        	TtDebuggingE dbg = debugMe ? TtDebuggingE.DEBUG : TtDebuggingE.NO_DEBUG;
        	HidInputSource tempHidInpSrc = TmHdwrDsPhys.getInstance().new HidInputSource(hidDef, hidInpDef, dbg);
        	l_isPrimaryInputsHaveCfgErrs = ! tempHidInpSrc.isValid();
        	l_isPrimaryInputsCompatible = tempHidInpSrc.isCompatibleAs(expectedHidFeatureDef);
        	if((hidDefAlt != null) && (hidInpDefAlt != null)) {
        		HidInputSource tempHidInpSrcAlt = TmHdwrDsPhys.getInstance().new 
        				HidInputSource(hidDefAlt, hidInpDefAlt, dbg);
        		l_isAltInputsHaveCfgErrs = ! tempHidInpSrcAlt.isValid();
        		l_isAltInputsCompatible = tempHidInpSrcAlt.isCompatibleAs(expectedHidFeatureDef);
        	} else {
        		l_isAltInputsCompatible = true;
        		l_isAltInputsHaveCfgErrs = false;
        	}
        	String msgPrefix = "[T744 ERROR] " + Tt.extractClassName(this) + "." + this.name() + ": ";
        	if(l_isPrimaryInputsHaveCfgErrs) {
        		P.println(msgPrefix + "primary input def has config errors");
        	}
        	if( ! l_isPrimaryInputsCompatible) {
        		P.println(msgPrefix + "primary input def (" + eHidInputDefRaw.name() + 
        					") is incompatible with expected " + expectedHidFeatureDef.name());
        	}
        	if(l_isAltInputsHaveCfgErrs) {
        		P.println(msgPrefix + "alternate input def has config errors");
        	}
        	if( ! l_isAltInputsCompatible) {
        		P.println(msgPrefix + "alternate input def (" + eHidInputDefAltRaw.name() + 
        					") is incompatible with expected " + expectedHidFeatureDef.name());
        	}
        	
        	//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
            eHidInputSource = TmHdwrDsPhys.getInstance().new HidInputSource(eHidDef, eHidInputDef, dbg);
            eHasConfigErrs = ! eHidInputSource.isValid();
        	HidInputSourceRegistration.register(this);
        	eSane = eHidInputSource.isCompatibleAs(expectedHidFeatureDef);

//            eHidDefRaw = hidDef;
//            eHidInputDefRaw = hidInpDef;
//            eHidDefAltRaw = hidDefAlt;
//            eHidInputDefAltRaw = hidInpDefAlt;

        }
        
        private boolean hasAnyErrors() {
        	boolean ans = false;
        	if(l_isPrimaryInputsHaveCfgErrs) { ans = true; }
        	if( ! l_isPrimaryInputsCompatible) { ans = true; }
        	if(l_isAltInputsHaveCfgErrs) { ans = true; }
        	if( ! l_isAltInputsCompatible) { ans = true; }
        	return ans;
        }
        
        private DsHidDefE getHidDef() { return eHidDef; }
        private HidInputDefE getHidInputDef() { return eHidInputDef; }
        
        HidInputSource getHidInputSrc() { return eHidInputSource; }

        public boolean isButton() {
        	boolean ans = eHidInputSource.isButton();
        	ans = isValid() ? eHidInputSource.isButton() : false;
        	return ans;
        }

        public boolean isValid() {
        	boolean ans = false;
        	switch(eCombo) {
//	        	case BOTH:
//	        	case EITHER:
//	        		ans = eSane && ! ( eHasConfigErrs || eHasConfigErrsAlt);
//	        		break;
	        	case CONDITIONAL_ALT:
        		case NONE:
        		default:
        			ans = eSane && ! eHasConfigErrs;
        			break;
        	}
        	return ans;
        }
        
        public T744HidButton makeButton() {
        	if(l_buttonMade) {
        		throw TmExceptions.getInstance().new DuplicateAssignmentOfMappedIoDefEx(Tt.extractClassName(this) + "." + this.name() +
        				" has already been assigned as a button");
        	}
        	TmHdwrDsPhys phys = TmHdwrDsPhys.getInstance();
        	T744HidButton btn = phys.new T744HidButton(this);
        	l_buttonMade = true;
        	return btn;
        }
        
        public int getHidUsbPortNbr() {
        	return eHidDef.getUsbPortNbr();
        }
        
        public int getHidButtonNbr() {
        	int ans = -1;
        	if(getHidInputSrc().isButton()) {
        		ans = getHidInputSrc().getHidDevInputDef().getFrcNbr();
        	}
        	return ans;
        }
        
        public boolean getButton() {
        	HidDevice hid = this.getHidDef().getHidDevice();
        	return hid.getButton(this);
        }
        
        public double getAnalog() {
        	HidDevice hid = this.getHidDef().getHidDevice();
        	return hid.getAnalog(this);        	
        }
        
        public int getPov() {
        	HidDevice hid = this.getHidDef().getHidDevice();
        	return hid.getPov(this);        	
        }

        private String setupToStringFull() {
        	  String ans = "";
//              ans += "DS input " + this.name() + " [" + this.eHidFeatureExpected.name() + ", " + this.getHidDef().name() + ":" + 
//              			this.eHidInputDef.toStringFull() + "]";
//              ans = String.format("  DS input: %-40s : %-20s: %-26s: %s", 
        	  String inp = String.format("  DS input: %s", this.name());
        	  while(inp.length() < 50) { inp += "."; }
        	    ans = String.format("%-51s : %-20s: %-26s\n%52s: %s", 
//              		this.name(), this.eHidFeatureExpected.name(), this.getHidDef().name(), 
              		inp, this.eHidFeatureExpected.name(), this.getHidDef().name(), 
          			" ", this.eHidInputDef.toStringFull() );
        	  return ans;
        }
        
        public String toStringFull() {
        	if(eToStringFullInfo==null) { eToStringFullInfo = setupToStringFull(); }
        	return eToStringFullInfo;
//      	  String ans = "";
////            ans += "DS input " + this.name() + " [" + this.eHidFeatureExpected.name() + ", " + this.getHidDef().name() + ":" + 
////            			this.eHidInputDef.toStringFull() + "]";
////            ans = String.format("  DS input: %-40s : %-20s: %-26s: %s", 
//      	  String inp = String.format("  DS input: %s", this.name());
//      	  while(inp.length() < 45) { inp += "."; }
//      	    ans = String.format("  DS input: %-45s : %-20s: %-26s\n%53s: %s", 
////            		this.name(), this.eHidFeatureExpected.name(), this.getHidDef().name(), 
//            		inp, this.eHidFeatureExpected.name(), this.getHidDef().name(), 
//        			" ", this.eHidInputDef.toStringFull() );
//      	  return ans;
        }
	}
    
    /**
     * start scanning the enum and filling in toStringFull() data. only do one entry at 
     * a time to reduce likelihood of "output not updated often enough" errors
     * @return false if found uninitialized toStringFull() data, true if all setup
     */
    public static boolean setupToStringFullBitByBit() {
    	boolean ans = true;
    	for(DsInputSourceDefE s : DsInputSourceDefE.values()) {
    		if(s.eToStringFullInfo == null) {
    			ans = false;
    			s.eToStringFullInfo = s.setupToStringFull();
    			break;
    		}
    	}
    	return ans;
    }
    
    public static void showAllDsIo() {
    	P.println(T744Robot2017.getBuildInfoToShow());
    	for(DsHidDefE s : DsHidDefE.values()) {
    		P.println(s.toStringFull());
    	}
    	P.println("  --------------------------");
    	for(DsInputSourceDefE s : DsInputSourceDefE.values()) {
    		P.println(s.toStringFull());
    	}
    }
    
    public static void inspectDsInputSources() {
    	for(DsInputSourceDefE s : DsInputSourceDefE.values()) {
    		if(s.hasAnyErrors()) {
    			String msg = "DsInputSourceDefE " + 
    					s.name() + " has configuration errors. Check prior error messages.";
    			throw TmExceptions.getInstance().new InvalidMappedIoDefEx(msg);
    		}
    	}
    }
    
}
