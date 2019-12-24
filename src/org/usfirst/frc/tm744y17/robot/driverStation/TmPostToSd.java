package org.usfirst.frc.tm744y17.robot.driverStation;

import org.usfirst.frc.tm744y17.robot.helpers.TmFlagsI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

//import org.usfirst.frc.tm744y16.robot.config.Tm16Misc;
//import org.usfirst.frc.tm744y16.robot.helpers.Tm16DbgTk;
//import org.usfirst.frc.tm744y16.robot.helpers.TmFlagsI;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TmPostToSd extends SmartDashboard implements TmToolsI, TmFlagsI {
	
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmPostToSd m_instance;

	public static synchronized TmPostToSd getInstance() {
		if (m_instance == null) {
			m_instance = new TmPostToSd();
		}
		return m_instance;
	}
	
   private TmPostToSd()
   {
   }
   
//	TmFlagsI m_flagsInst = TmFlagsI.getInstance();
//   static FlagMgmt m_flagMgr = new FlagMgmt(Tm16DbgTk.extractClassName(getInstance()), 
//			(TmFlagsI.ALL_SS | TmFlagsI.M), 
//			Tm16Misc.PrefKeys.PREFIX_KEY_DBG_SD_FLAGS); //TmFlagsI will append appropriate suffixes
//   static FlagMgmt m_flagMgr = new FlagMgmt(Tm16DbgTk.extractClassName(getInstance()), 
//			F.E | G.ALL_SS, //the flag bits that should be on by default
//			Tm16Misc.PrefKeys.PREFIX_KEY_DBG_SD_FLAGS); //note: TmFlagsI will append appropriate suffixes to this key
	
	public static void doDisabledInit() { doCommonInit(); }
	public static void doAutonomousInit() { doCommonInit(); }
	public static void doTeleopInit() { doCommonInit(); }
	private static void doCommonInit() { 
//		m_flagMgr.updateDefault();
//		m_flagMgr.updateDisable();
//		m_flagMgr.updateEnable();
	}
	
//   //Java allows underscores used to separate groups of digits for readability
//   private static int m_flagConfig = 0x0000_000F;
	 
//   public static double dbgGetNumber(int flag, String key, double defaultValue) //, TmFlagsI.FlagMgmt flgMgr)
   public static double dbgGetNumber(TmMiscSdKeys.SdKeysE flagsAndKey, double defaultValue)
   {
	   double ans = defaultValue;
	   
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
       {
           ans = SmartDashboard.getNumber(flagsAndKey.getKey(), defaultValue);
       }
       return ans;
   }
   
//   // public static void dbgPutNumber(int flag, String key, double val)
//   public static void dbgPutInt(TmMiscKeys.SdKeysE flagsAndKey, int val)
//   {
//	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
//	   {
//		   SmartDashboard.putNumber(flagsAndKey.getKey(), val);
//	   }
//   }
//   //public static void dbgPutNumber(int flag, String key, double val)
//   public static void dbgPutDouble(TmMiscKeys.SdKeysE flagsAndKey, double val)
//   {
//	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
//	   {
//		   SmartDashboard.putNumber(flagsAndKey.getKey(), val);
//	   }
//   }
   //   public static void dbgPutNumber(int flag, String key, double val)
   public static void dbgPutNumber(TmMiscSdKeys.SdKeysE flagsAndKey, double val)
   {
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
	   {
		   SmartDashboard.putNumber(flagsAndKey.getKey(), val);
	   }
   }
   
//   public static boolean dbgGetBoolean(int flag, String key, boolean defaultValue)
   public static boolean dbgGetBoolean(TmMiscSdKeys.SdKeysE flagsAndKey, boolean defaultValue)
   {
	   boolean ans = defaultValue;
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
       {
           ans = SmartDashboard.getBoolean(flagsAndKey.getKey(), defaultValue);
       }
       return ans;
   }
   
//   public static void dbgPutBoolean(int flag, String key, boolean val) {
   public static void dbgPutBoolean(TmMiscSdKeys.SdKeysE flagsAndKey, boolean val) {

	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
	   {
	       SmartDashboard.putBoolean(flagsAndKey.getKey(), val);
	   }
   }
   
//   public static void dbgPutString(int flag, String key, String item) {
   public static void dbgPutString(TmMiscSdKeys.SdKeysE flagsAndKey, String item) {
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
	   {
		   SmartDashboard.putString(flagsAndKey.getKey(), item);
	   }
   }

   public static String dbgGetString(TmMiscSdKeys.SdKeysE flagsAndKey, String defaultString) {
	   String ans = defaultString;
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags()))
	   {
		   ans = SmartDashboard.getString(flagsAndKey.getKey(), defaultString);
	   }
	   return ans;
   }

   public static void dbgPutSendableByName(int flag, Sendable item) {
	   dbgPutSendableByName(flag, item, "");
   }
   public static void dbgPutSendableByName(int flag, Sendable item, String nameSuffix) {
	   if(true) //m_flagMgr.isFlagsOnBySection(flag)) //if(0 != (flag & m_flagConfig))
	   {
		   if(item != null) {
			   SmartDashboard.putData(Tt.extractClassName(item) + nameSuffix, item);
		   }
	   }
   }

//   public static void dbgPutData(int flag, String key, Sendable item) {
   public static void dbgPutData(TmMiscSdKeys.SdKeysE flagsAndKey, Sendable item) {
	   if(true) //m_flagMgr.isFlagsOnBySection(flagsAndKey.getFlags())) //if(0 != (flag & m_flagConfig))
	   {
	       SmartDashboard.putData(flagsAndKey.getKey(), item);
	   }
   }

}
