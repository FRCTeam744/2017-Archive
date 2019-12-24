package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap.DsHidDefE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap.DsInputSourceDefE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoModConnIoPairManager;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt.EndpointHandlingE;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

/*
 *                          +---+         button 1 is the trigger (digital)
 *                        4   3   5
 *                            2
 *                          |   |
 *                          |   |
 *                          |   |
 *               ---------- |   |-----------
 *             /            |   |           \
 *             |    6       |   |      11   |
 *             |            |   |           |
 *             |    7                  10   |
 *             |                            |
 *             |         8         9        |
 *             \___________+------+_________/
 *                         |  Z   |
 *                         |roller|
 *                         +------+
 * 
 * 
 *  experimentation: our joystick z-button (<joystick>.getZ() - the
 *  roller at the front bottom) is -1 when all the way up, +1 when
 *  all the way down. Convert the -1 to +1 range to a 0 to +1 range
 *  for the servos.
 * 
 *
 * 
 *              LT                                        RT
 *             +-+                                        +-+
 *             | |        (triggers are analog-ish)       | |
 *             | |      (combine to form 'throttle'?)     | |
 *         LB+-----+                                    +-----+RB
 *         /-+-----+-\________________________________/-+-----+-\
 *        /                                                  _   \
 *       /     __N__       +-+back      +-+start            /Y\   \
 *       |  NW/     \NE    +-+          +-+                 \_/    |
 *       | W | POV   | E     +-+mode                     _      _  |
 *       |  SW\_____/SE      +-+                        /X\    /B\ |
 *       |       S                                      \_/    \_/ |
 *       |                  _____              _____         _     |
 *       |                 / left\            /right\       /A\    |
 *       |                |  joy  |          |  joy  |      \_/    |
 *       |                 \_____/            \_____/              |
 *       |                                                         |
 *       |                                                         |
 *        \     _____________________________________________     /
 *         \---/                                             \---/
 *                   Logitech Gamepad
*/


public class TmHdwrDsPhys implements TmToolsI {
	
	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmHdwrDsPhys m_instance = new TmHdwrDsPhys();

	public static synchronized TmHdwrDsPhys getInstance() {
		if (m_instance == null) {
			//we're relying on TmHdwrDsMap to extend TmHdwrDsPhys. Shouldn't need to use getInstance to create an instance
			String msg = "TmHdwrDsPhys.getInstance() was called before TmHdwrDsPhys was instantiated!!";
			P.println(msg);
//			m_instance = new TmHdwrDsPhys();
		}
		return m_instance;
	}

	protected TmHdwrDsPhys() {
//		m_instance = this;
	}
	/*------------------- end of getInstance stuff */

//	protected static DsHidAndInputPairManager m_mgr = new DsHidAndInputPairManager();
//	public static DsHidAndInputPairManager getHidAndInputPairManager() { return m_mgr; }

	
    public enum UsbPortsE //USB ports supported by FRC driver station code
    {
    	USB0(0 + Cnst.kFIRST_USB_PORT_NUMBER),
    	USB1(1 + Cnst.kFIRST_USB_PORT_NUMBER),
    	USB2(2 + Cnst.kFIRST_USB_PORT_NUMBER),
    	USB3(3 + Cnst.kFIRST_USB_PORT_NUMBER),
    	USB4(4 + Cnst.kFIRST_USB_PORT_NUMBER),
    	USB5(5 + Cnst.kFIRST_USB_PORT_NUMBER),
    	;

    	private static class Cnst {
    		public static final int kFIRST_USB_PORT_NUMBER = 0;
    	}
    	
    	protected final int ePortNbr;
    	protected final TmToolsI.TtNamedAssignments eNamedAssignments;
    	
    	private UsbPortsE(int port) {
    		this.ePortNbr = port;
    		eNamedAssignments = new TmToolsI.TtNamedAssignments(this.name());
    	}
    	
    	public int getPortNbr() { return ePortNbr; }
    	
    	public void assign(String assignTo) { eNamedAssignments.assign(assignTo); }
    	public boolean isAssigned() { return eNamedAssignments.isAssigned(); }
    	public String showAssignedTo() { return eNamedAssignments.showAssignedTo(); }

    }
    
	public enum HidInputDefE { //implements DsInputsI {
    	//these are button numbers to feed to FRC code, paired with the applicable device type
    	TOP_BUT1_TRIGGER(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton, 0),
    	TOP_BUT2_FRONT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  1),
    	TOP_BUT3_BACK(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  2),
    	TOP_BUT4_LEFT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  3),
    	TOP_BUT5_RIGHT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  4),
    	BASE_BUT6_LEFT_BACK(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  5),
    	BASE_BUT7_LEFT_FRONT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  6),
    	BASE_BUT8_FRONT_LEFT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  7),
    	BASE_BUT9_FRONT_RIGHT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  8),
    	BASE_BUT10_RIGHT_FRONT(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  9),
    	BASE_BUT11_RIGHT_BACK(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton,  10),
    	X_AXIS(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kAnalog, 0),
    	Y_AXIS(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kAnalog, 1),
    	Z_ROLLER(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kAnalog, 2),
    	TWIST(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kAnalog, 3),
    	THROTTLE(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kAnalog, 4),

        A_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 0),
        B_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 1),
        X_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 2),
        Y_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 3),
        LB_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 4),
        RB_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 5),
        BACK_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 6),
        START_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 7),
        LEFT_JOY_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 8),
        RIGHT_JOY_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kButton, 9),
        
        /*
         * left trigger returns a variable positive value.
         * right trigger returns a variable negative value.
         * left/right joystick x/y values are from -1 (pushed forward all the way)
         *  to 1 (pulled back all the way)
         */
    	LEFT_JOY_X(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 0),
    	LEFT_JOY_Y(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 1),
    	LEFT_TRIGGER(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 2),
    	RIGHT_TRIGGER(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 3),
    	RIGHT_JOY_X(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 4), //2015 Throttle
    	RIGHT_JOY_Y(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kAnalog, 5), //2015 Twist - combo of left and right triggers
    	
    	//there is one POV on the Xbox or GamePad controller. When read, it returns
    	//an integer value indicating an angle.  The following all use
    	//the same POV, but will be true only if it matches the designated
    	//angle.
        POV_NORTH_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 0, PovPopulationE.POV0, PovAnglesE.POV_NORTH_ANGLE), // 0 degrees
        POV_SOUTH_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 1, PovPopulationE.POV0, PovAnglesE.POV_SOUTH_ANGLE), // 180 degrees
        POV_EAST_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 2, PovPopulationE.POV0, PovAnglesE.POV_EAST_ANGLE), // 90 degrees
        POV_WEST_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 3, PovPopulationE.POV0, PovAnglesE.POV_WEST_ANGLE), // 270 degrees
        
        //these are hard to hit accurately and probably shouldn't be used
        //except for lab test purposes
        POV_NE_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 4, PovPopulationE.POV0, PovAnglesE.POV_NE_ANGLE),
        POV_SE_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 5, PovPopulationE.POV0, PovAnglesE.POV_SE_ANGLE),
        POV_NW_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 6, PovPopulationE.POV0, PovAnglesE.POV_NW_ANGLE),
        POV_SW_BUTTON(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kCannedPovButton, 7, PovPopulationE.POV0, PovAnglesE.POV_SW_ANGLE),
        
//        POV0_ANGLE(DsHidDevTypesE.kGAME_CNTLR, DsHidFeatureE.kPov, 0)

        JOY_Z_UP_AS_BUTTON(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kCannedAnalogButton, AnalogInputForBtnE.JOYSTICK_Z_ROLLER, AnalogRangeForBtnE.MAX_NEG),
        JOY_Z_MID_AS_BUTTON(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kCannedAnalogButton, AnalogInputForBtnE.JOYSTICK_Z_ROLLER, AnalogRangeForBtnE.MID),
        JOY_Z_DOWN_AS_BUTTON(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kCannedAnalogButton, AnalogInputForBtnE.JOYSTICK_Z_ROLLER, AnalogRangeForBtnE.MAX_POS),
        
		;
		
		private final DsHidDevTypesE eHidType;
		private final DsHidFeatureE eHidFeature;
		private final int eTeamInputIdOffset;
		private final int eTeamInputId;
		private final int eFrcNbr;
		private final PovPopulationE ePovDef;
		private final PovAnglesE eExpectedPovAngleDef;
		private final boolean eValid;
//		private final double eAnalogButtonMinValue;
//		private final double eAnalogButtonMaxValue;
		private final AnalogInputForBtnE eAnalogButtonAnalogInputDef;
		private final AnalogRangeForBtnE eAnalogButtonAnalogRangeDef;
		
		public int getFrcNbr() { 
			return getFrcNbr(this);
		}
		public static int getFrcNbr(HidInputDefE hidInpDef) { 
			int ans = hidInpDef.eFrcNbr;

			switch(hidInpDef.eHidFeature) {
			case kButton:
			case kAnalog:
			case kPov:
				//it's something FRC code supports
				ans = hidInpDef.eFrcNbr;
				break;
			case kCannedAnalogButton:
			case kCannedPovButton:
			default:
				String errMsg = "HidInputDefE.getFrcNbr() called for a feature FRC doesn't support - " +
						hidInpDef.name() + " is a " + hidInpDef.eHidFeature.name();
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx(errMsg);
				//break;
			}
			return ans;
		}
		public DsHidDevTypesE getHidDevType() { return eHidType; }
		public DsHidFeatureE getHidInputType() { return eHidFeature; }
		public int getPovNdx() { return ePovDef.eFrcPovId; }
		public PovAnglesE getPovAngleSelection() { return eExpectedPovAngleDef; }
		
		public String toStringFull() {
			String ans = "";
			ans += this.name() + " (T744 id nbr " + eTeamInputId + ", FRC nbr " + eFrcNbr +")";
			return ans;
		}
		
		//as defaults for things that aren't analog buttons, use a min value > the max value
//		private static final double DEFAULT_ANALOG_BTN_MIN_TO_DISABLE_BTN = 1.0;
//		private static final double DEFAULT_ANALOG_BTN_MAX_TO_DISABLE_BTN = -1.0;
		private static final int USE_ANALOG_RANGE_TO_GET_ID_OFFSET = 0;
//		private static final AnalogInputForBtnE DEFAULT_ANALOG_INPUT_FOR_BTN = null; //AnalogInputForBtnE.JOYSTICK_Z_ROLLER; //null; //HidInputDefE.Z_ROLLER, 
		
		private HidInputDefE(DsHidDevTypesE hidType, DsHidFeatureE feature, int tmInpIdOffset) {
			this(hidType, feature, tmInpIdOffset, TmHdwrDsMap.Cnst.POV_NBR_IGNORED, PovAnglesE.POV_BOGUS_ANGLE_FOR_NONPOV_INPUTS,
					AnalogInputForBtnE.NONE, AnalogRangeForBtnE.NONE);
		}
		private HidInputDefE(DsHidDevTypesE hidType, DsHidFeatureE feature, int tmInpIdOffset, 
				PovPopulationE frcPovSel, PovAnglesE povAngleSel) {
			this(hidType, feature, tmInpIdOffset, frcPovSel, povAngleSel,
					AnalogInputForBtnE.NONE, AnalogRangeForBtnE.NONE);
		}
		private HidInputDefE(DsHidDevTypesE hidType, DsHidFeatureE feature, AnalogInputForBtnE analogInp, AnalogRangeForBtnE analogRange) {
			this(hidType, feature, USE_ANALOG_RANGE_TO_GET_ID_OFFSET, TmHdwrDsMap.Cnst.POV_NBR_IGNORED, PovAnglesE.POV_BOGUS_ANGLE_FOR_NONPOV_INPUTS,
					analogInp, analogRange);
		}
		private HidInputDefE(DsHidDevTypesE hidType, DsHidFeatureE feature, int tmInpIdOffset, 
								PovPopulationE frcPovSel, PovAnglesE povAngleSel,
								AnalogInputForBtnE analogInputForBtn, AnalogRangeForBtnE analogRangeForBtn) {
			eHidType = hidType;
			eHidFeature = feature;
			eTeamInputIdOffset = (feature.equals(DsHidFeatureE.kCannedAnalogButton)) ? analogRangeForBtn.ordinal() : tmInpIdOffset;
			eExpectedPovAngleDef = povAngleSel;
			ePovDef = frcPovSel;
			eAnalogButtonAnalogInputDef = analogInputForBtn;
			eAnalogButtonAnalogRangeDef = analogRangeForBtn;
						
			//bogus values used as filler for parms that don't apply to the given
			//input type (aka "feature")
			final int BOGUS_FRC_POV_BUTTON_MIN_NBR = 900; 
			final int BOGUS_FRC_ANALOG_BUTTON_MIN_NBR = 930;
			final int BOGUS_TM_MIN_INP_NBR_FOR_UNKNOWN_INPUT_TYPES = 800;
			final int BOGUS_FRC_MIN_INP_NBR_FOR_UNKNOWN_INPUT_TYPES = 880;
			final int BOGUS_TM_MIN_INP_NBR_FOR_INVALID_SPECS = 700;
			final int BOGUS_FRC_MIN_INP_NBR_FOR_INVALID_SPECS = 780;
			final int COUNT_OF_0_TO_FLAG_AS_INVALID = 0;  //must be 0!!
			
			boolean valid = false;
			
			int tmMin;
			int frcMin;
			int cnt;
			if(this.name().equals("JOY_Z_UP_AS_BUTTON")) { //"POV_EAST_BUTTON")) {
				String msg = "time to debug!!";
			}
			if(feature.equals(DsHidFeatureE.kButton)) {
				tmMin = hidType.eTmButMin;
				frcMin = hidType.eFrcButMin;
				cnt = hidType.eFrcButCnt;
			} else if(feature.equals(DsHidFeatureE.kAnalog)) {
				tmMin = hidType.eTmAnalogMin;
				frcMin = hidType.eFrcAnalogMin;
				cnt = hidType.eFrcAnalogCnt;
			} else if(feature.equals(DsHidFeatureE.kPov)) {
				tmMin = hidType.eTmPovMin;
				frcMin = hidType.eFrcPovMin;
				cnt = hidType.eFrcPovPopulationCnt;
			} else if(feature.equals(DsHidFeatureE.kCannedPovButton)) {
				//FRC doesn't use POV as buttons, it's a Team 744 thing
				tmMin = hidType.eTmPovButMin;
				frcMin = BOGUS_FRC_POV_BUTTON_MIN_NBR; //any obviously bogus value will do
				cnt = hidType.eTmPovButCnt;
			} else if(feature.equals(DsHidFeatureE.kCannedAnalogButton)) {
				//FRC doesn't use analog as buttons, it's a Team 744 thing
				tmMin = hidType.eTmAnalogButMin;
				frcMin = BOGUS_FRC_ANALOG_BUTTON_MIN_NBR;
				cnt = hidType.eTmAnalogButCnt;
			} else {
				tmMin = BOGUS_TM_MIN_INP_NBR_FOR_UNKNOWN_INPUT_TYPES;
				frcMin = BOGUS_FRC_MIN_INP_NBR_FOR_UNKNOWN_INPUT_TYPES;
				cnt = COUNT_OF_0_TO_FLAG_AS_INVALID;	
			}
			//(DsPhysDevTypesE dType, DsHidFeatureE feature, int tmInpIdOffset)
			if(cnt>0 && tmInpIdOffset>=0 && tmInpIdOffset<=cnt) {
				eTeamInputId = tmMin + tmInpIdOffset;
				eFrcNbr = frcMin + tmInpIdOffset;
				valid = true;
			} else {
				eTeamInputId = BOGUS_TM_MIN_INP_NBR_FOR_INVALID_SPECS + tmInpIdOffset;
				eFrcNbr = BOGUS_FRC_MIN_INP_NBR_FOR_INVALID_SPECS + tmInpIdOffset;
			}
			
			eValid = valid;
		}
		
    }
		

    
	public static class FrcCnst { //constants used in FRC code
		public static final int POV_ANGLE_POV_NOT_PRESSED = -1;
		public static final boolean BUTTON_READING_FOR_INVALID_BUTTONS = false;
	}
	
    /**
     * Constants used to assign a unique id number to each possible type of HID input so that the 
     * rest of the code doesn't have to know whether it's a joystick, an XBox or a GamePad.  We also
     * assign id's to things like particular angles from a POV or a particular range of values from 
     * an analog input that are used as buttons.
     * 
     * The data in this class shouldn't need to change unless FRC libraries change or we add
     * support for a new type of HID or a new use for a HID input.
     * 
     * @author JudiA
     *
     */
	private static class Cnst {  //constants
		//JS stands for joystick, GC stands for game controller (Xbox or Gamepad)
		//FRC stands for FRC code, TM stands for Team 744 code
		private static final int	JS_FRC_BUTTON_BASE_NBR	=  1;
		private static final int	JS_FRC_BUTTON_CNT = 11;
		private static final int	JS_TM_BUTTON_BASE_NBR = JS_FRC_BUTTON_BASE_NBR;
		private static final int	JS_TM_BUTTON_MAX_NBR = JS_FRC_BUTTON_BASE_NBR + JS_FRC_BUTTON_CNT - 1;
		private static final int    GC_FRC_BUTTON_BASE_NBR = 1;
		private static final int    GC_FRC_BUTTON_CNT = 10;
		private static final int 	GC_TM_BUTTON_BASE_NBR = Math.max(20, JS_TM_BUTTON_BASE_NBR + JS_FRC_BUTTON_CNT);
		private static final int 	GC_TM_BUTTON_MAX_NBR = GC_TM_BUTTON_BASE_NBR + GC_FRC_BUTTON_CNT - 1;
		
		//analog inputs are used for axis readings, etc.
		private static final int	JS_FRC_ANALOG_BASE_NBR = 0;
		private static final int 	JS_FRC_ANALOG_CNT = 5;
		private static final int    JS_TM_ANALOG_BASE_NBR = Math.max(40, GC_TM_BUTTON_BASE_NBR + GC_FRC_BUTTON_CNT);
		private static final int    JS_TM_ANALOG_MAX_NBR = JS_TM_ANALOG_BASE_NBR + JS_FRC_ANALOG_CNT - 1;
		private static final int	GC_FRC_ANALOG_BASE_NBR = 0;
		private static final int 	GC_FRC_ANALOG_CNT = 6;
		private static final int    GC_TM_ANALOG_BASE_NBR = Math.max(50, JS_TM_ANALOG_BASE_NBR + JS_FRC_ANALOG_CNT);
		private static final int    GC_TM_ANALOG_MAX_NBR = GC_TM_ANALOG_BASE_NBR + GC_FRC_ANALOG_CNT - 1;
		
		//POV "buttons" check for particular angle readings from a POV
		private static final int 	JS_TM_POV_BUTTON_CNT = 0;
		private static final int    JS_TM_POV_BUTTON_BASE_NBR = Math.max(60, GC_TM_ANALOG_BASE_NBR + GC_FRC_ANALOG_CNT);
		private static final int 	JS_TM_POV_BUTTON_MAX_NBR = JS_TM_POV_BUTTON_BASE_NBR + 0;
		private static final int 	GC_TM_POV_BUTTON_CNT = 8;
		private static final int    GC_TM_POV_BUTTON_BASE_NBR = Math.max(70, JS_TM_POV_BUTTON_BASE_NBR + JS_TM_POV_BUTTON_CNT);
		private static final int    GC_TM_POV_BUTTON_MAX_NBR = GC_TM_POV_BUTTON_BASE_NBR + GC_TM_POV_BUTTON_CNT - 1;

        //a POV returns an analog value representing the angle of the edge that's being pressed (or -1 if not pressed)
		private static final int    JS_FRC_POV_BASE_NBR = 0;
		private static final int    JS_FRC_POV_CNT = 0;
		private static final int    JS_TM_POV_BASE_NBR = Math.max(80, GC_TM_POV_BUTTON_BASE_NBR + GC_TM_POV_BUTTON_CNT);
		private static final int    JS_TM_POV_MAX_NBR = JS_TM_POV_BASE_NBR + 0;
		private static final int    GC_FRC_POV_BASE_NBR = 0;
		private static final int    GC_FRC_POV_CNT = 1;
		private static final int    GC_TM_POV_BASE_NBR = Math.max(88, JS_TM_POV_BASE_NBR + JS_FRC_POV_CNT);
		private static final int    GC_TM_POV_MAX_NBR = GC_TM_POV_BASE_NBR + GC_FRC_POV_CNT - 1;
		
		//analog "buttons" return TRUE if the analog value read is within a specified range, FALSE else.
		//they're a Team 744 thing, not something FRC implements
		private static final int	JS_TM_ANALOG_BUTTON_CNT = 3; //Use Z-axis for three different "buttons" //old: //X, Y, Z only axes only //for now, assume one button per analog, though could have more...
		private static final int	JS_TM_ANALOG_BUTTON_BASE_NBR = Math.max(90, GC_TM_POV_BASE_NBR + GC_FRC_POV_CNT);
		private static final int    JS_TM_ANALOG_BUTTON_MAX_NBR = JS_TM_ANALOG_BUTTON_BASE_NBR + JS_TM_ANALOG_BUTTON_CNT - 1;
		private static final int	GC_TM_ANALOG_BUTTON_CNT = 0; //4; //two joysticks each with X and Y axes; //for now, assume one button per analog, though could have more...
		private static final int	GC_TM_ANALOG_BUTTON_BASE_NBR = Math.max(100, JS_TM_ANALOG_BUTTON_BASE_NBR + JS_TM_ANALOG_BUTTON_CNT);
		private static final int    GC_TM_ANALOG_BUTTON_MAX_NBR = GC_TM_ANALOG_BUTTON_BASE_NBR + GC_TM_ANALOG_BUTTON_CNT - 1;

	}
	
//	private enum AnalogButtonTypesE {
////		public enum HidInputDefE { //implements DsInputsI {
////	    	//these are button numbers to feed to FRC code, paired with the applicable device type
////	    	TOP_BUT1_TRIGGER(DsHidDevTypesE.kJOYSTICK, DsHidFeatureE.kButton, 0),
//		;
//		private final HidInputDefE eHidInpDef;
//		private final double eMinAnalogRdgForPressed;
//		private final double eMaxAnalogRdgForPressed;
//		private final boolean eValid;
//		
//		private AnalogButtonTypesE(HidInputDefE hidInp, double min, double max) {
//			eHidInpDef = hidInp;
//			eMinAnalogRdgForPressed = min;
//			eMaxAnalogRdgForPressed = max;
//			
//			boolean valid = true;
//			if( ! hidInp.getHidInputType().equals(DsHidFeatureE.kAnalog)) { valid = false; }
//			if( min > max ) {valid = false;}
//			if(min < -1.0 || min > 1.0) {valid = false; }
//			if(max < -1.0 || max > 1.0) {valid = false; }			
//			eValid = valid;
//		}
//		
//		public boolean isValid() { return eValid; }
//		public HidInputDefE eHidInpDef() { return eHidInpDef;  }
//		public double getMinAnalogRdgForPressed() { return eMinAnalogRdgForPressed; }
//		public double getMaxAnalogRdgForPressed() { return eMaxAnalogRdgForPressed; }
//	}
	
	protected enum PovPopulationE {
		//so far, none of our input devices have more than one POV...
		POV0(0);
		
		private int eFrcPovId;
		private PovPopulationE(int frcPovId) {
			eFrcPovId = frcPovId;
		}
		public int getFrcPovId() { return eFrcPovId; }
	}
	
	private enum PovAnglesE {
        POV_NORTH_ANGLE(0), // 0 degrees
        POV_SOUTH_ANGLE(180), // 180 degrees
        POV_EAST_ANGLE(90), // 90 degrees
        POV_WEST_ANGLE(270), // 270 degrees
        
        //these are hard to hit accurately and probably shouldn't be used
        //except for lab test purposes
        POV_NE_ANGLE(45),
        POV_SE_ANGLE(225),
        POV_NW_ANGLE(135),
        POV_SW_ANGLE(315),
        
        POV_BOGUS_ANGLE_FOR_NONPOV_INPUTS(350); //any obviously bogus value will do....
        ;
		
		private int ePovAngle;
		
		private PovAnglesE(int angle) {
			ePovAngle = angle;			
		}
		
		public int getAngle() {return ePovAngle;}

	}
	
	public enum AnalogRangeForBtnE {
		MAX_NEG(-1.0, -0.6), MID(-0.6, 0.6), MAX_POS(0.6, 1.0), 
		NONE(1.0, -1.0), //min>max on purpose so can never be active!!!
		;
		
		private final double eMin;
		private final double eMax;
		
		private AnalogRangeForBtnE(double min, double max) {
			eMin = min;
			eMax = max;
		}
		
		public boolean isActive(double testVal) {
			boolean ans = false;
			ans = Tt.isInRange(testVal, eMin, eMax, EndpointHandlingE.EXCLUDE_ENDPOINTS);
			return ans;
		}
	}
	public enum AnalogInputForBtnE {
		JOYSTICK_Z_ROLLER(Joystick.AxisType.kZ.value),
		NONE(-1),
		;
		private final int eAxisNbr;
		private AnalogInputForBtnE(int frcAxisNbr) {
			eAxisNbr = frcAxisNbr;
		}
		public int getAxisNbr() { return eAxisNbr; }
		public HidInputDefE getStandardAxisDef() {
			HidInputDefE ans = null;
			for(HidInputDefE i: HidInputDefE.values()) {
				if(i.getHidInputType().equals(DsHidFeatureE.kAnalog)) {
					if(i.getFrcNbr() == getAxisNbr()) {
						ans = i;
						break;
					}
				}
			}
			return ans;
		}
	}

	
	public enum DsHidFeatureE { 
		kButton, kAnalog, kPov, kCannedPovButton, kCannedAnalogButton
	}
	
	
	//this class shouldn't need to change
	public enum DsHidDevTypesE {
		kJOYSTICK(Cnst.JS_FRC_BUTTON_CNT, Cnst.JS_FRC_BUTTON_BASE_NBR, 
					Cnst.JS_TM_BUTTON_BASE_NBR, Cnst.JS_TM_BUTTON_MAX_NBR,
					Cnst.JS_FRC_ANALOG_CNT, Cnst.JS_FRC_ANALOG_BASE_NBR,
					Cnst.JS_TM_ANALOG_BASE_NBR, Cnst.JS_TM_ANALOG_MAX_NBR,
					Cnst.JS_TM_POV_BUTTON_CNT, Cnst.JS_TM_POV_BUTTON_BASE_NBR, Cnst.JS_TM_POV_BUTTON_MAX_NBR,
					Cnst.JS_FRC_POV_CNT, Cnst.JS_FRC_POV_BASE_NBR,
					Cnst.JS_TM_POV_BASE_NBR, Cnst.JS_TM_POV_MAX_NBR
					,
					Cnst.JS_TM_ANALOG_BUTTON_CNT, Cnst.JS_TM_ANALOG_BUTTON_BASE_NBR, Cnst.JS_TM_ANALOG_BUTTON_MAX_NBR
					), 
		kGAME_CNTLR(Cnst.GC_FRC_BUTTON_CNT, Cnst.GC_FRC_BUTTON_BASE_NBR,
					Cnst.GC_TM_BUTTON_BASE_NBR, Cnst.GC_TM_BUTTON_MAX_NBR,
					Cnst.GC_FRC_ANALOG_CNT, Cnst.GC_FRC_ANALOG_BASE_NBR,
					Cnst.GC_TM_ANALOG_BASE_NBR, Cnst.GC_TM_ANALOG_MAX_NBR,
					Cnst.GC_TM_POV_BUTTON_CNT, Cnst.GC_TM_POV_BUTTON_BASE_NBR, Cnst.GC_TM_POV_BUTTON_MAX_NBR,
					Cnst.GC_FRC_POV_CNT, Cnst.GC_FRC_POV_BASE_NBR,
					Cnst.GC_TM_POV_BASE_NBR, Cnst.GC_TM_POV_MAX_NBR
					,
					Cnst.GC_TM_ANALOG_BUTTON_CNT, Cnst.GC_TM_ANALOG_BUTTON_BASE_NBR, Cnst.GC_TM_ANALOG_BUTTON_MAX_NBR
					), 
		;
		
		private final int eFrcButCnt;
		private final int eFrcButMin;
		private final int eTmButMin;
		private final int eTmButMax;
		private final int eFrcAnalogCnt;
		private final int eFrcAnalogMin;
		private final int eTmAnalogMin;
		private final int eTmAnalogMax;
		private final int eTmPovButCnt;
		private final int eTmPovButMin;
		private final int eTmPovButMax;
		private final int eFrcPovPopulationCnt;
		private final int eFrcPovMin;
		private final int eTmPovMin;
		private final int eTmPovMax;
		private final int eTmAnalogButCnt;
		private final int eTmAnalogButMin;
		private final int eTmAnalogButMax;
		
		public boolean hasPov(int frcPovId) {
			boolean ans = false;
			if(eFrcPovPopulationCnt > 0 && frcPovId>=eFrcPovMin && frcPovId<(eFrcPovMin + eFrcPovPopulationCnt)) {
				ans = true;
			}
			return ans;
		}
		
		private DsHidDevTypesE() {
			this(0,0,0,0,  0,0,0,0,  0,0,0,  0,0,0,0,  0,0,0);
		}
		private DsHidDevTypesE(int frcButCnt, int frcButMin, int tmButMin, int tmButMax,
								int frcAnalogCnt, int frcAnalogMin, int tmAnalogMin, int tmAnalogMax,
								int tmPovButCnt, int tmPovButMin, int tmPovButMax, 
								int frcPovCnt, int frcPovMin, int tmPovMin, int tmPovMax
								,
								int tmAnalogButCnt, int tmAnalogButMin, int tmAnalogButMax
								) {
			eFrcButCnt = frcButCnt;
			eFrcButMin = frcButMin;
			eTmButMin = tmButMin;
			eTmButMax = tmButMax;
			eFrcAnalogCnt = frcAnalogCnt;
			eFrcAnalogMin = frcAnalogMin;
			eTmAnalogMin = tmAnalogMin;
			eTmAnalogMax = tmAnalogMax;
			eTmPovButCnt = tmPovButCnt;
			eTmPovButMin = tmPovButMin;
			eTmPovButMax = tmPovButMax;
			eFrcPovPopulationCnt = frcPovCnt;
			eFrcPovMin = frcPovMin;
			eTmPovMin = tmPovMin;
			eTmPovMax = tmPovMax;
			eTmAnalogButCnt = tmAnalogButCnt;
			eTmAnalogButMin = tmAnalogButMin;
			eTmAnalogButMax = tmAnalogButMax;
		}
		
		public int getTmButMin() { return eTmButMin; }
		public int getTmAnalogMin() { return eTmAnalogMin; }
		public int getTmPovButMin() { return eTmPovButMin; }
		public int getFrcButMin() { return eFrcButMin; }
		public int getFrcAnalogMin() { return eFrcAnalogMin; }
	}
	
	public class T744HidButton extends Button {
		private DsInputSourceDefE c_dsInpSrcDef;
		private HidInputSource c_hidInpSrc;
		
		public T744HidButton(DsInputSourceDefE dsInpSrcDef) {
			c_dsInpSrcDef = dsInpSrcDef;
			c_hidInpSrc = dsInpSrcDef.getHidInputSrc();
		}
		
		@Override
		public boolean get() {
			boolean ans = FrcCnst.BUTTON_READING_FOR_INVALID_BUTTONS;
			if(c_dsInpSrcDef.isValid() && c_dsInpSrcDef.isButton()) {
				HidDevice hid = c_hidInpSrc.getHid();
				ans = hid.getButton(c_dsInpSrcDef);
			}
			return ans;
		}
		
	}
	
//	public static HidDevice getNewHidDevice(UsbPortsE usb, DsPhysDevTypesE dType, String nickName) { return new HidDevice(usb, dType, nickName); }
	public class HidDevice extends Joystick {
		
		UsbPortsE m_usb;
		DsHidDevTypesE m_type;
		String m_nickName;
		double m_deadzoneTolerance;
		
		public int getFrcUsbId() { return m_usb.getPortNbr(); }
		public DsHidDevTypesE getHidType() { return m_type; }
		
		public HidDevice(UsbPortsE usb, DsHidDevTypesE hidType, double deadzoneTolerance, String nickName) {
			super(usb.getPortNbr());
			m_usb = usb;
			m_type = hidType;
			m_deadzoneTolerance = deadzoneTolerance;
			m_nickName = nickName + " (" + hidType.toString() + ":" + usb.toString() + ")";
			usb.assign(m_nickName);
		}
		
	    protected boolean getButton(TmHdwrDsMap.DsInputSourceDefE btnInpSrcDef)
	    {
	    	boolean ans = false;
	    	HidInputDefE hidInpDef = btnInpSrcDef.getHidInputSrc().getHidDevInputDef();
	    	
	    	//Hmmm.... could use DriverStation's getJoystickType or getJoystickName or something to 
	    	//try to figure out what's connected and maybe cut down on 'check if ... connected'
	    	//message floods
	    	
	    	
	    	if(btnInpSrcDef.getHidInputSrc().isValid()) {
	    		switch(hidInpDef.getHidInputType()) {
	    		case kButton:
	    			ans = super.getRawButton(hidInpDef.getFrcNbr());
	    			break;
	    		case kCannedPovButton:
	    			int expectedAngle = hidInpDef.getPovAngleSelection().getAngle();
	    			ans = super.getPOV(hidInpDef.getPovNdx()) == expectedAngle;
	    			break;
	    		case kCannedAnalogButton:
	    			double axisRdg = super.getRawAxis(hidInpDef.eAnalogButtonAnalogInputDef.getAxisNbr());
	    			ans = hidInpDef.eAnalogButtonAnalogRangeDef.isActive(axisRdg);
	    			break;
//	    			String dbgMsg = "no canned analog buttons supported at present.  Debug!!!";
//	    			//fallthru //break;
	    		default:
	    			btnInpSrcDef.reportBadButtonUsed();
	    			ans = false;
	    			break;
	    		}
	    	}
	    	
	    	return ans;
	    }
	    
	    protected double getAnalog(TmHdwrDsMap.DsInputSourceDefE analogDef)
	    {
	    	double ans = 0.0;
	    	HidInputDefE inpDef = analogDef.getHidInputSrc().getHidDevInputDef();
	    	
	    	if(analogDef.getHidInputSrc().isValid()) {
	    		if(inpDef.getHidInputType().equals(DsHidFeatureE.kAnalog)) {
	    			DsHidDevTypesE hidType = inpDef.getHidDevType();
	    			switch(hidType) {
					case kGAME_CNTLR:
						switch(inpDef) {
						case LEFT_JOY_Y:
						case RIGHT_JOY_Y:
						case LEFT_JOY_X:
						case RIGHT_JOY_X:
						case LEFT_TRIGGER:
						case RIGHT_TRIGGER:
							ans = getRawAxis(inpDef.getFrcNbr());
							break;
						default:
							analogDef.reportBadAnalogUsed(hidType);
							break;
						}
						break;
					case kJOYSTICK:
						switch(inpDef) {
							case X_AXIS:
							case Y_AXIS:
							case Z_ROLLER:
							case TWIST:
							case THROTTLE:
								ans = getRawAxis(inpDef.getFrcNbr());
								break;
							default:
								analogDef.reportBadAnalogUsed(hidType);
								break;
						}
						break;
					default:
						analogDef.reportBadAnalogUsed(hidType);
						break;
	    			
	    			}
	    		}	    	
	    	}
	    	
	    	if(Tt.isWithinTolerance(ans, 0.0, m_deadzoneTolerance)) {
	    		ans = 0.0;
	    	}
	    	return ans;
	    }
	    
	    public int getPov(TmHdwrDsMap.DsInputSourceDefE povDef) {
	    	int ans = FrcCnst.POV_ANGLE_POV_NOT_PRESSED;
	    	HidInputDefE inpDef = povDef.getHidInputSrc().getHidDevInputDef();
	    	
	    	if(povDef.getHidInputSrc().isValid()) {
	    		if(inpDef.getHidInputType().equals(DsHidFeatureE.kPov)) {
	    			DsHidDevTypesE hidType = inpDef.getHidDevType();
	    			switch(hidType) {
					case kGAME_CNTLR:
						ans = getPOV(inpDef.getFrcNbr());
						break;
					case kJOYSTICK:
						//true joysticks have no POVs
					default:
						povDef.reportBadPovUsed(hidType);
						break;
	    			
	    			}
	    		}	    	
	    	}
	    	
	    	return ans;
	    }
	    		
	}
	
	/** This class pairs a HID device and an input on that device, verifying validity of the pairing
	 * 
	 * @author JudiA
	 *
	 */
	public class HidInputSource {
		private boolean m_isValid = false;
		private DsHidDefE m_hidDef;
		private HidInputDefE m_hidInputDef;
		private String m_configErrMsg;
		
		public HidInputSource(DsHidDefE hidDef, HidInputDefE hidInputDef) {
			this(hidDef, hidInputDef, TtDebuggingE.NO_DEBUG);
		}
		public HidInputSource(DsHidDefE hidDef, HidInputDefE hidInputDef, TtDebuggingE debugFlag ) { //boolean debugMe) {
			m_hidDef = hidDef;
			m_hidInputDef = hidInputDef;
			m_configErrMsg = "";
			DsHidDevTypesE hidType = hidDef.eHidType;
			DsHidDevTypesE inpDefType = hidInputDef.eHidType;
			
			if(debugFlag.equals(TtDebuggingE.DEBUG)) {
				String msg="time to debug!!!";
			}
			if(hidDef.eHidType.equals(hidInputDef.eHidType)) {
				m_isValid = true;
			} else {
				m_configErrMsg = "invalid device/input combo: " + hidDef.toString() + "(" + hidDef.eHidType.toString() + ") and " +
						hidInputDef.toString() + "(" + hidInputDef.eHidType.toString() + ")";
				m_isValid = false;
				System.out.println(m_configErrMsg);
			}
		}
		
        public boolean isButton() {
        	boolean ans = false;
        	switch(m_hidInputDef.eHidFeature) {
	        	case kCannedPovButton:
	        	case kCannedAnalogButton:
		        case kButton:
	        		ans = true;
	        		break;
	        	default:
	        		ans = false;
	        		break;
        	}
        	if( ! isValid()) { ans = false; }
        	return ans;
        }

		
		public boolean isValid() { return m_isValid; }
		public DsHidDefE getHidDeviceDef() { return m_hidDef; }
		public HidInputDefE getHidDevInputDef() { return m_hidInputDef; }
		public HidDevice getHid() { return m_hidDef.getHidDevice(); }
//		public int getHidInputFrcNbr() { return m_hidDef.getHidDevice(); }
		
		public boolean isCompatibleAs(DsHidFeatureE expectedHidFeatureDef) {
			boolean sane = false;
        	switch(expectedHidFeatureDef) {
			case kButton:
				switch(m_hidInputDef.eHidFeature) {
					case kButton:
					case kCannedPovButton:
					case kCannedAnalogButton:
						sane = true;
						break;
					default:
						sane = false;
						break;
				}
				break;
			case kAnalog:
			case kPov:
			case kCannedPovButton:
			case kCannedAnalogButton: 
        	    sane = m_hidInputDef.eHidFeature.equals(expectedHidFeatureDef);
				break;
//			case kUserAnalogButton:
//				sane = inpSrcHidFeat.equals(DsHidFeatureE.kAnalog);
//				eSpecialAnalogButton = new AnalogButton();
//				break;
//			case kUserPovButton:
//				sane = inpSrcHidFeat.equals(DsHidFeatureE.kPov);
//				eSpecialPovButton = new PovButton(eHidDef, ePov, ePovAngle);
//				break;
			default:
				sane = false;
				break;
        	}
        
			return sane;
		}
		
	}
	
	public static class HidInputSourceRegistration {
		
		//this only sets up a pointer to an array of pointers to something, 
		//have to fill in the individual pointers later and only then
		//create the things those pointers point to. 
		private static RegistrationInfo[][] c_thingsRegisteredArray;
		private static int c_maxHids = UsbPortsE.values().length;
		private static int c_maxHidInputs = HidInputDefE.values().length;
		private static HidInputSourceRegistration c_instance = new HidInputSourceRegistration(c_maxHids, c_maxHidInputs);
		
		private class RegistrationInfo {
			private String registrationList = "<<no one>>";
			private int registrationCnt = 0;
			
			public int getCnt() {return registrationCnt;}
			public String getRegistrationList() {return registrationList; }
			
			public int register(String who) {
				if(registrationCnt == 0) { registrationList = who; }
				else { registrationList += " " + who; }
				return ++registrationCnt;
			}
		}
		
		public HidInputSourceRegistration() {
			String msg = "a dbg brkpt";
		}
		
		public HidInputSourceRegistration(int maxHids, int maxHidInputs) {
			c_maxHids = maxHids;
			c_maxHidInputs = maxHidInputs;

			//this only reserves pointers for the actual entries. must set up the entries
			//themselves separately.
			c_thingsRegisteredArray = new RegistrationInfo[maxHids][maxHidInputs];
			for(int h=0; h<maxHids; h++)
			{
				for(int i=0; i<maxHidInputs; i++)
				{
					//this is where we set up the individual entries
					c_thingsRegisteredArray[h][i] = new RegistrationInfo();
				}
			}
			String msg = "debug brkpt here";
		}
		
		public static int register(DsInputSourceDefE dsInpSrcDef) {
			int cnt = 0;
			HidInputSource hidInpSrcPairing = dsInpSrcDef.getHidInputSrc();
			int hidNdx = hidInpSrcPairing.getHidDeviceDef().ordinal();
			int hidInpNdx = hidInpSrcPairing.getHidDevInputDef().ordinal();
			String whoIsRegistering = dsInpSrcDef.name();
			HidInputDefE hidInpDef = hidInpSrcPairing.getHidDevInputDef();
			
			if(c_maxHids > hidNdx && c_maxHidInputs > hidInpNdx) {
				cnt = c_thingsRegisteredArray[hidNdx][hidInpNdx].register(whoIsRegistering);
				if(hidInpDef.getHidInputType().equals(DsHidFeatureE.kCannedAnalogButton)) {
					int aCnt;
					String whoPrefix = "(btn)";
					HidInputDefE hidInpAsAxisDef = hidInpDef.eAnalogButtonAnalogInputDef.getStandardAxisDef();
					int hidInpAsAxisNdx = hidInpAsAxisDef.ordinal();
					aCnt = c_thingsRegisteredArray[hidNdx][hidInpAsAxisNdx].getCnt();
					if(aCnt==0) {
						aCnt = c_thingsRegisteredArray[hidNdx][hidInpAsAxisNdx].register(whoPrefix + whoIsRegistering);
					} else {
						RegistrationInfo axisRegInfo = c_thingsRegisteredArray[hidNdx][hidInpAsAxisNdx];
						String regList = axisRegInfo.getRegistrationList();
						String regListPrefix = regList.substring(0, whoPrefix.length());
						if( ! regListPrefix.equals(whoPrefix)) {
							String msg = "DsInputSourceDefE." + dsInpSrcDef.name() + ": Duplicate DS input source defs (same analog input): " + 
									axisRegInfo.getRegistrationList();
							throw TmExceptions.getInstance().new DuplicateMappedIoDefEx(msg);
						}
////						aCnt = c_thingsRegisteredArray[hidNdx][hidInpAsAxisNdx].register("(btn)" + whoIsRegistering);
//						if(aCnt > 1) {
//							RegistrationInfo axisRegInfo = c_thingsRegisteredArray[hidNdx][hidInpAsAxisNdx];
//							String regList = axisRegInfo.getRegistrationList();
//							if(aCnt>cnt) { cnt = aCnt; }
//							String msg = "DsInputSourceDefE." + dsInpSrcDef.name() + ": Duplicate DS input source defs (same analog input): " + 
//									axisRegInfo.getRegistrationList();
//							throw TmExceptions.getInstance().new DuplicateMappedIoDefEx(msg);
//						}
					}
				}
				if(cnt > 1) {
//					System.out.println("[T744 WARNING] Duplicate DS input source defs: " + 
//							c_thingsRegisteredArray[hidNdx][hidInpNdx].getRegistrationList());
					String msg = "DsInputSourceDefE." + dsInpSrcDef.name() + ": Duplicate DS input source defs: " + 
							c_thingsRegisteredArray[hidNdx][hidInpNdx].getRegistrationList();
					throw TmExceptions.getInstance().new DuplicateMappedIoDefEx(msg);
				}
			}
			return cnt;
		}
	}

}
