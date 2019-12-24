package org.usfirst.frc.tm744y17.robot.helpers;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoFeatureTypesE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.SpdCntlrTypeE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;

//import org.usfirst.frc.tm744y16.robot.config.Tm16Misc;
//import org.usfirst.frc.tm744y16.robot.config.TmHdwrRoPhysHw;
//import org.usfirst.frc.tm744y16.robot.config.TmHdwrRoPhysHw.SpdCntlrTypeE;
//import org.usfirst.frc.tm744y16.robot.devices.TmCANTalon;
//import org.usfirst.frc.tm744y16.robot.devices.TmFakeCANTalon;
//import org.usfirst.frc.tm744y16.robot.devices.TmTalon;
//import org.usfirst.frc.tm744y16.robot.helpers.Tm16DbgTk;
//
//import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * an interface to aid in creating enums for motor configurations
 * for various subsystems
 * <E> should be the enum class name of the enum that implements this interface
 * @author JudiA
 *
 */
public interface TmMotorConfigEnumI<E> extends TmFlagsI {
	/* the <E> in the line above makes this an example of a "generic" class.  Using the 
	 * generic type name E allows the code in this interface to do proper type checking 
	 * without knowing the actual type ahead of time.  It also allows this code to be 
	 * reused by different enums that Java will regard as different types.  See the 
	 * isUsable() method below for an example of a "generic" method.  The main difference
	 * in syntax is that for a generic class (or interface), the type parameter is coded 
	 * as a suffix to the class name, while for a generic method, it's coded before the 
	 * return type of the method in the method declaration and as a prefix to the method
	 * name when the method is called.
	 */
//	public enum DriveMotorDesignationE { LEFT_FRONT, LEFT_REAR, RIGHT_FRONT, RIGHT_REAR, NONE }
	public class MotorConfigData {
		/**
		 * name of the MotorConfigE enum item associated with this motor config data
		 */
		String c_motorConfigDefName;
		RoNamedIoE c_namedMotorDef;
//		E c_motorConfigDef;
		SdKeysE c_sdKeySpeed;
		SdKeysE c_sdKeyAmps;
		private final TmHdwrRoPhys.SpdCntlrTypeE c_motorType;
		private final SpeedController c_motorObj;
		private final double c_speedTuningFactor;
		
		/*
//		private final String eEnumItemName;
//		private final String eMotorIdStr;
//		private final String eLwSubsysName;
//		private final SpeedController eMotorObj;
//		private final TmHdwrRoPhys.SpdCntlrTypeE eMotorType;
//		private final boolean eNeedsInversion;
//		private final int eInversionFactor;
//		private final double eSpeedTuningFactor;
//		private final String eLiveWindowItemName;
//		private final Tm16Misc.SdKeysE eSmartDashKeyCurSpd;
//		private final boolean eUseMotorSafety;
//		private final double eMotorSafetyTimeout;
//		private final DriveMotorDesignationE eDriveMotorDesignation;
				
		
//		//a MotorConfigData constructor
//		public MotorConfigData(
//				String enumItemName,
//				String motorIdStr,
//				String lwSubsysName,
//	            SpeedController motorObj,
//	            DriveMotorDesignationE driveMotorDesignation,
//	            boolean invertMotor,
//	            double speedTuningFactor, 
//	            String liveWindowItemName, 
//	            Tm16Misc.SdKeysE smartDashKeyCurSpd
////	            boolean useMotorSafety
////              double motorSafetyTimeout
//	            )
//        {
//			this(enumItemName, motorIdStr, lwSubsysName,
//					 motorObj,
//					 driveMotorDesignation,
//		             invertMotor,
//		             speedTuningFactor, 
//		             liveWindowItemName, 
//		             smartDashKeyCurSpd,
//		             Cnst.NO_MOTOR_SAFETY, //FRC code turns it on automatically if used as a drive train motor
//		             MotorSafety.DEFAULT_SAFETY_EXPIRATION
//		            );
//        }
//		
//		//a MotorConfigData constructor
//		public MotorConfigData(
//				String enumItemName,
//				String motorIdStr,
//				String lwSubsysName,
//	            SpeedController motorObj, 
//	            DriveMotorDesignationE driveMotorDesignation,
//	            boolean invertMotor,
//	            double speedTuningFactor, 
//	            String liveWindowItemName, 
//	            Tm16Misc.SdKeysE smartDashKeyCurSpd,
//	            boolean useMotorSafety
////              double motorSafetyTimeout
//	            )
//        {
//			this(enumItemName, motorIdStr, lwSubsysName,
//					 motorObj,
//					 driveMotorDesignation,
//		             invertMotor,
//		             speedTuningFactor, 
//		             liveWindowItemName, 
//		             smartDashKeyCurSpd,
//		             useMotorSafety,
//		             MotorSafety.DEFAULT_SAFETY_EXPIRATION
//		            );
//        }
//	
 * 	
 */
		
		private static final double DEFAULT_MOTOR_SPEED_TUNING_FACTOR = 1.0;
		public MotorConfigData(
				String motorConfigDefName,
//				String enumItemName,
//				String motorIdStr,
//				String lwSubsysName,
//	            SpeedController motorObj, 
//	            DriveMotorDesignationE driveMotorDesignation,
//	            boolean invertMotor,
//	            String liveWindowItemName, 
//	            Tm16Misc.SdKeysE smartDashKeyCurSpd,
//	            boolean useMotorSafety,
//	            double motorSafetyTimeout)
				RoNamedIoE namedMotorDef,
//	            double speedTuningFactor, 
				SdKeysE sdKeySpeed,
				SdKeysE sdKeyAmps)
	    {	
			this(motorConfigDefName, namedMotorDef, DEFAULT_MOTOR_SPEED_TUNING_FACTOR, sdKeySpeed, sdKeyAmps);
	    }
		//a MotorConfigData constructor
		public MotorConfigData(
				String motorConfigDefName,
//				String enumItemName,
//				String motorIdStr,
//				String lwSubsysName,
//	            SpeedController motorObj, 
//	            DriveMotorDesignationE driveMotorDesignation,
//	            boolean invertMotor,
//	            String liveWindowItemName, 
//	            Tm16Misc.SdKeysE smartDashKeyCurSpd,
//	            boolean useMotorSafety,
//	            double motorSafetyTimeout)
				RoNamedIoE namedMotorDef,
	            double speedTuningFactor, 
				SdKeysE sdKeySpeed,
				SdKeysE sdKeyAmps)
	    {	
			this.c_namedMotorDef = namedMotorDef;
			this.c_motorConfigDefName = motorConfigDefName;
//			E c_motorConfigDef;
			this.c_sdKeySpeed = sdKeySpeed;
			this.c_sdKeyAmps = sdKeyAmps;
//			this.eEnumItemName = enumItemName;
//	    	this.eMotorIdStr = motorIdStr;
//	    	this.eLwSubsysName = lwSubsysName;
//	    	this.eMotorObj = motorObj;
//            this.eDriveMotorDesignation = driveMotorDesignation;
	    	this.c_motorType = namedMotorDef.getNamedModuleDef().getSpdCntlrType();
	    	this.c_motorObj = namedMotorDef.getModConnIoPairObj().getMotorObject();
//	    	this.eNeedsInversion = invertMotor;
//	    	this.eInversionFactor = (invertMotor==Cnst.INVERT_MOTOR) ? -1 : 1;
	    	this.c_speedTuningFactor = speedTuningFactor; //not used by drive train (FRC cod manages the individual motors)
//	    	this.eLiveWindowItemName = liveWindowItemName;
//	    	this.eSmartDashKeyCurSpd = smartDashKeyCurSpd;
//	    	this.eUseMotorSafety = useMotorSafety;
//	    	this.eMotorSafetyTimeout = motorSafetyTimeout;
	    	
//	    	updateMotorSafetySettings(this);
	    	
	    }
		
		public RoNamedIoE getNamedIoDef() { return c_namedMotorDef; }
		public SdKeysE getSdKeyDefSpeed() { return c_sdKeySpeed; }
		public SdKeysE getSdKeyDefAmps() { return c_sdKeyAmps; }

		public boolean isCANTalon() {
			boolean ans = false;
			if(c_namedMotorDef.getNamedModuleDef().getModFeatureTypeDef().equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE)) {
				ans = true;
			}
			return ans;
		}
		
		/**
		 * Note: it's better to set up a ConnectedEncoder and then let its get method call this so that the encoder
		 *       reading can be adjusted properly based on the EncoderCountsCapabilityE setting rather than calling
		 *       this method directly.
		 * @return encoder position of the magnetic encoder connected to a CANTalon
		 * type of device
		 */
		public int getEncPosition() {
			int ans = 0; //default value
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				ans = ((TmFakeable_CANTalon)c_motorObj).getEncPosition();
				break;
			default:
				//use default value
			}
			return ans;
			
		}
		
		/**
		 * Note: Some encoders will simply ignore this command.  It's better to set up a ConnectedEncoder 
		 * 		 and let it manage encoder readings based on the EncoderCountsCapabilityE setting.
		 * set encoder position of the magnetic encoder connected to a CANTalon
		 * type of device
		 */
		public void setEncPosition(int newPosition) {
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				((TmFakeable_CANTalon)c_motorObj).setEncPosition(newPosition);
				break;
			default:
				//use default value
			}
		}
		
		/**
		 * return the output current of the motor if it is a CANTalon (or
		 * a TmCANTalon) type.  Otherwise, return 0
		 * @return
		 */
		public double getCANTalonMotorCurrent() {
			double ans = 0; //default value
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				ans = ((TmFakeable_CANTalon)c_motorObj).getOutputCurrent();
				break;
			default:
				//use default value
			}
			return ans;
		}
		
		public void disableBatteryCompensation() {
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				((TmFakeable_CANTalon)c_motorObj).disableBatteryCompensation();
				break;
			default:
				//use default value
			}
			
		}
		public double getSpeedTicksPer100msCANTalon() {
			double ans = 0; //default value
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				ans = ((TmFakeable_CANTalon)c_motorObj).getSpeed();
				break;
			default:
				//use default value
			}
			return ans;
		}
		
		/**
		 * if motor is TmCANTalon or TmFakeCANTalon, generate a string showing max/min output 
		 * currents detected by calls to getOuputCurrent(), else return "n/a";
		 * @return
		 */
		public String showMaxMinOutputCurrent() {
			String ans = "TBD or n/a"; //default value
			switch(c_motorType) { //SpdCntlrTypeE.
			case kTM_FAKEABLE_CAN_TALON:
				ans = ((TmFakeable_CANTalon)c_motorObj).getMaxMinOutputCurrentSummary();
				break;
			default:
				//use default value
			}
			return ans;
		}
		
//		public boolean isInversionNeeded() { return eNeedsInversion; }

	    public String toStringFull()
	    {
	    	String ans;
	    	ans = this.c_namedMotorDef.toString();
//	        ans = eEnumItemName + ": '" + eMotorIdStr + "'" +
//	    	                ", Subsystem:'" + eLwSubsysName + "'" +
//	        				", motor type " + eMotorType.toString() +
//	        				", " + (eNeedsInversion ? "Inverted" : "Not inverted")  + 
//	        				", speed tuning factor = " + eSpeedTuningFactor +
//	        				", liveWindow key = '" + eLiveWindowItemName + "'" +
//	        				", smartDashboard key = '" + eSmartDashKeyCurSpd + "'" +
//	        				", defaults to " + (eUseMotorSafety ? "Using" : "NOT using") + " motor safety" +
//	        				", default motor safety timeout = " + eMotorSafetyTimeout  + " sec." +
//	        				"";
	        return ans;
	    }

	    public String toStringShort()
	    {
	    	String ans;
	    	ans = this.c_namedMotorDef.toString();
//	        ans = eEnumItemName + ": '" + eMotorIdStr + "'" +
//	        				", motor type " + eMotorType.toString() +
////	        				", " + (eNeedsInversion ? "Inverted" : "Not inverted")  + 
////	        				", speed tuning factor = " + eSpeedTuningFactor +
////	        				", liveWindow key = '" + eLiveWindowItemName + "'" +
////	        				", smartDashboard key = '" + eSmartDashKeyCurSpd + "'" +
////	        				", defaults to " + (eUseMotorSafety ? "Using" : "NOT using") + " motor safety" +
////	        				", default motor safety timeout = " + eMotorSafetyTimeout  + " sec." +
//	        				"";
	        return ans;
	    }

	}
	
    //have to have these in a class in order to use them in calls 
	//to the enum constructor
	public static class Cnst {
        public static final boolean NO_INVERSION = false;
        public static final boolean INVERT_MOTOR = true;
        public static final boolean USE_MOTOR_SAFETY = true;
        public static final boolean NO_MOTOR_SAFETY = false;
    }
	
	
	
	/**
	 * the enum should keep its data in a MotorConfigData object. It should
	 * return a reference to that object when this method is called.
	 * @return the enum item's MotorConfigData object
	 */
	public abstract MotorConfigData getMotorConfigData();
	
//	/**
//	 * returns the id string for the motor (a nickname for the motor, e.g. "rear right mtr")
//	 * @return the motor's id string
//	 */
//	public default String getMotorIdString() { return getMotorConfigData().eMotorIdStr; }
	
//	/**
//	 * this method provides code to check the enum values for conflicting or duplicate
//	 * use of resources, etc.  It prints appropriate error messages as it goes. The 
//	 * isConflicting() method may be used to compare the MoterConfigData info.
//	 * If the enum classname in the calling code is MyMotorEnum, then this method
//	 * would be called by coding:
//	 *      boolean isItGood = TmMotorConfigEnumI.<MyMotorEnum>isUsable(MyMotorEnum.values());
//	 * @return false if problems are found, true if all is well.
//	 */
//	public static <E1 extends TmMotorConfigEnumI<E1> > boolean isUsable(E1[] values) {
//		/* the "extends TmMotorConfigEnumI<E1>" here is required to tell the Java compiler
//		 * that whatever type we pass in as E1 should have extended the TmMotorConfigEnum
//		 * interface.  Without it we wouldn't be able to call getMotorConfigData() from
//		 * within this code.  Note that this is a static method.  Methods that aren't
//		 * static can call getMotorConfigData() without having to be generic because
//		 * they are associated with the instance and therefore the type is already known.
//		 * If we goof and use a type that hasn't extended TmMotorConfigEnumI, the
//		 * compiler can catch it as an error and make us fix the bug.
//		 */
//
//		boolean ans = true;
//		String oName, iName;
//		for (E1 mtrO : values)
//		{
//			oName = mtrO.toString();
//			for (E1 mtrI : values) {
//				iName = mtrI.toString();
//				if(oName.equals(iName)) {
//					//don't need to test any more of the inner loop values
//					break;
//				} else {
//					if(TmMotorConfigEnumI.isConflicting(mtrO.getMotorConfigData(), mtrI.getMotorConfigData()))
//					{
//						ans = false;
//					}
//				}
//			}
//		}
//
//		return ans;
//	}

	/**
	 * post the motor's speed to SmartDashboard
	 */
	public default void postToSdSpeed() {
		MotorConfigData cfg = getMotorConfigData();
		double speed = cfg.getNamedIoDef().getModConnIoPairObj().getMotorObject().get();
		SdKeysE sdKey = cfg.getSdKeyDefSpeed();
		TmPostToSd.dbgPutNumber(sdKey, speed);
	}
	public default void postToSdAmps() {			
		MotorConfigData cfg = getMotorConfigData();
		double amps = cfg.getCANTalonMotorCurrent();
		SdKeysE sdKey = cfg.getSdKeyDefAmps();
		TmPostToSd.dbgPutNumber(sdKey, amps);
	}
	
	/**
	 * does a get() for the actual motor device.
	 * @return reading from the motor device
	 */
	public default double getMotorSpeed() { 
		double ans = 0;
		MotorConfigData cfg = getMotorConfigData();
		ans = cfg.getNamedIoDef().getModConnIoPairObj().getMotorObject().get();
		return ans; 
	}
	
//	/**
//	 * does a get() for the actual motor device.
//	 * @return reading from the motor device
//	 */
//	public default double getMotorSpeedWithInversion() { 
//		double ans = getMotorConfigData().eMotorObj.get();
//		return isInversionNeeded() ? -ans : ans;
//	}
	
//	/**
//	 * 
//	 * @return true if motor needs to be inverted (a setting in FRC code)
//	 */
//	public default boolean isInversionNeeded() { return getMotorConfigData().isInversionNeeded(); }
	
	
//	/**
//	 * get the key to use when writing the motor's current speed to SmartDashboard
//	 * @return
//	 */
//	public default Tm16Misc.SdKeysE getSmartDashKeyMotorSpeed() { return getMotorConfigData().eSmartDashKeyCurSpd; }
//
//	
//	public default boolean isMotorType(TmHdwrRoPhysHw.SpdCntlrTypeE type) {
//		return( getMotorConfigData().eMotorType.equals(type) );
//	}
//	
//	public default SpdCntlrTypeE getMotorType() {
//		return( getMotorConfigData().eMotorType );
//	}
//	
//	/**
//	 * if motor is TmCANTalon or TmFakeCANTalon, generate a string showing max/min output 
//	 * currents detected by calls to getOuputCurrent(), else return "n/a";
//	 * @return
//	 */
//	public default String showMaxMinOutputCurrent() { return getMotorConfigData().showMaxMinOutputCurrent(); }

	/**
	 * Update the motor safety settings for this motor. 
	 * @param cfg The MotorConfigData instance containing info for this motor
	 */
//	public static void updateMotorSafetySettings(MotorConfigData cfg)
//	{
//		if(cfg.eMotorObj == null)
//		{
//			//do nothing
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTM_TALON))
//		{
//			TmTalon mtrObj = (TmTalon)cfg.eMotorObj;
//			mtrObj.setSafetyEnabled(cfg.eUseMotorSafety);
//			if(cfg.eMotorSafetyTimeout != MotorSafety.DEFAULT_SAFETY_EXPIRATION) {
//				mtrObj.setExpiration(cfg.eMotorSafetyTimeout);
//				Tm16DbgTk.printIt(M.ALWAYS, cfg.eEnumItemName + " safety " + 
//							(mtrObj.isSafetyEnabled() ? "IS" : "is NOT") + 
//							" enabled, timeout set to " + mtrObj.getExpiration());
//			}
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTM_CAN_TALON))
//		{
//			TmCANTalon mtrObj = (TmCANTalon)cfg.eMotorObj;
//			mtrObj.setSafetyEnabled(cfg.eUseMotorSafety);
//			if(cfg.eMotorSafetyTimeout != MotorSafety.DEFAULT_SAFETY_EXPIRATION) {
//				mtrObj.setExpiration(cfg.eMotorSafetyTimeout);
//				Tm16DbgTk.printIt(M.ALWAYS, cfg.eEnumItemName + " safety " + 
//							(mtrObj.isSafetyEnabled() ? "IS" : "is NOT") + 
//							" enabled, timeout set to " + mtrObj.getExpiration());
//			}
//		}
	
//we haven't needed to implement these types....
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.TM_TALON_SRX))
//		{
//			LiveWindow.addActuator(lwSubSysName,
//                getLwItemName(), (TmTalonSRX)getMotorObj());   			
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.TM_CAN_TALON))
//		{
//			LiveWindow.addActuator(lwSubSysName,
//                getLwItemName(), (TmCANTalon)getMotorObj());   			
//		}
//		
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTALON))
//		{
//			Talon mtrObj = (Talon)cfg.eMotorObj;
//			mtrObj.setSafetyEnabled(cfg.eUseMotorSafety);
//			if(cfg.eMotorSafetyTimeout != MotorSafety.DEFAULT_SAFETY_EXPIRATION) {
//				mtrObj.setExpiration(cfg.eMotorSafetyTimeout);
//				Tm16DbgTk.printIt(M.ALWAYS, cfg.eEnumItemName + " safety " + 
//						(mtrObj.isSafetyEnabled() ? "IS" : "is NOT") + 
//						" enabled, timeout set to " + mtrObj.getExpiration());
//			}
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTALON_SRX))
//		{
//			TalonSRX mtrObj = (TalonSRX)cfg.eMotorObj;
//			mtrObj.setSafetyEnabled(cfg.eUseMotorSafety);
//			if(cfg.eMotorSafetyTimeout != MotorSafety.DEFAULT_SAFETY_EXPIRATION) {
//				mtrObj.setExpiration(cfg.eMotorSafetyTimeout);
//				Tm16DbgTk.printIt(M.ALWAYS, cfg.eEnumItemName + " safety " + 
//						(mtrObj.isSafetyEnabled() ? "IS" : "is NOT") + 
//						" enabled, timeout set to " + mtrObj.getExpiration());
//			}
//		}
//
//not supported in LiveWindow prior to 2016
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kCAN_TALON))
//		{
//			LiveWindow.addActuator(lwSubSysName,
//                getLwItemName(), (CANTalon)getMotorObj());   			
//		}
//		else
//		{
//			//do nothing
//		}
//	}
	

//	/**
//	 * Register this motor with LiveWindow so that it shows up with a useful name.
//	 * Not all motors are supported by LiveWindow; this code will ignore those 
//	 * motors. 
//	 * @param lwSubSysName The subsystem name (key) with which this motor is associated
//	 */
//	public default void addLwActuator() //String lwSubSysName)
//	{
//		MotorConfigData cfg = getMotorConfigData();
//		if((cfg.eLiveWindowItemName == null) || (cfg.eLwSubsysName == null))
//		{
//			//do nothing
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTM_TALON))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
////                getLwItemName(), (TmTalon)getMotorObj());   			
//				"TmTalon:" + getLwItemName(), (Talon)getMotorObj());   			
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTM_TALON_SRX))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
////                getLwItemName(), (TmTalonSRX)getMotorObj());   			
//				"TmTalonSRX:" + getLwItemName(), (TalonSRX)getMotorObj());   			
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTM_CAN_TALON))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
////                getLwItemName(), (TmCANTalon)getMotorObj());   			
//                "TmCANTalon:" + getLwItemName(), (CANTalon)getMotorObj());   			
//		}
//		
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTALON))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
//                "Talon:" + getLwItemName(), (Talon)getMotorObj());   			
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kTALON_SRX))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
//                "TalonSRX:" + getLwItemName(), (TalonSRX)getMotorObj());   			
//		}
//		else if(cfg.eMotorType.equals(TmHdwrRoPhysHw.SpdCntlrTypeE.kCAN_TALON))
//		{
//			LiveWindow.addActuator(cfg.eLwSubsysName,
//                "CANTalon:" + getLwItemName(), (CANTalon)getMotorObj());   			
//		}
//		else
//		{
//			//do nothing
//		}
//	}
//	
//	/**
//	 * gets the name LiveWindow should use when displaying controls for this motor
//	 * @return the name for LiveWindow to use
//	 */
//	public default String getLwSubsysName()
//	{
//		return getMotorConfigData().eLwSubsysName;
//	}
//
//	/**
//	 * gets the name LiveWindow should use when displaying controls for this motor
//	 * @return the name for LiveWindow to use
//	 */
//	public default String getLwItemName()
//	{
//		return getMotorConfigData().eLiveWindowItemName;
//	}
//	
//	/**
//	 * get a reference to the actual motor object (a Talon, TmTalon, 
//	 * CANTalon, or some similar object)
//	 * @return the reference to the actual motor object
//	 */
//	public default SpeedController getMotorObj()
//	{
//		return getMotorConfigData().eMotorObj;
//	}
//	
//	/**
//	 * set the new requested motor speed, handling inversion and speed tuning
//	 * @param speed (a value between -1 and 1)
//	 */
//	public default void setMotorSpd(double speed)
//    {
//		MotorConfigData cfg = getMotorConfigData();
//        double clampedSpd = Tm16Tools.clampFrcValue(speed);
//        cfg.eMotorObj.set(clampedSpd * cfg.eInversionFactor * cfg.eSpeedTuningFactor);
//    }
//	
//	/**
//	 * adjust the requested speed by the tuning factor, but ignore inversion info
//	 * @param speed
//	 * @return tuned speed for this motor
//	 */
//    public default double calcTunedMotorSpd(double speed)
//    {
//       double clampedSpd = Tm16Tools.clampFrcValue(speed);
//       return (clampedSpd * getMotorConfigData().eSpeedTuningFactor);
//    }
//
//	/**
//	 * adjust the requested speed by the tuning factor and invert if needed
//	 * @param speed
//	 * @return tuned and possibly inverted speed for this motor
//	 */
//    public default double calcTunedMotorSpdWithInversion(double speed)
//    {
//    	MotorConfigData cfg = getMotorConfigData();
//        double clampedSpd = Tm16Tools.clampFrcValue(speed);
//        return (clampedSpd * cfg.eInversionFactor * cfg.eSpeedTuningFactor);
//    }
//    
//    /**
//     * build a string that documents the config info used for this motor
//     * @return description string
//     */
//    public default String toStringFull()
//    {
//    	return getMotorConfigData().toStringFull();
//    }
//    
////    /**
////     * build a string that shows a minimal amount of useful info
////     */
////    public default String toStringMini()
////    {
////    	String ans = "";
////    	ans = Tm16DbgTk.extractClassName(this) + 
////    	return ans;
////    }
//    
//    
//    /**
//     * this is intended to be called from a loop that checks for conflicting
//     * or duplicate information in the enum values. It checks relevant info and
//     * prints error messages if it finds conflicts.
//     * The O and I designations refer to the "outer" and "inner" loops usually
//     * used for such tasks.
//     * @param mtrCfgO motor configuration data contained in MotorConfigData object
//     * @param mtrCfgI motor configuration data contained in MotorConfigData object
//     * @return
//     */
//    public static boolean isConflicting(MotorConfigData mtrCfgO, MotorConfigData mtrCfgI)
//    {
//    	boolean foundTrouble = false;
//		String oName;
//		String iName;
//		String oops = "----->OOPS! ";
//		oName = mtrCfgO.eEnumItemName;
//		iName = mtrCfgI.eEnumItemName;
//		if( ! oName.equals(iName)) {
//			if(mtrCfgO.eMotorIdStr.equals(mtrCfgI.eMotorIdStr)) {
//				foundTrouble = true;
//				Tm16DbgTk.printIt(M.ALWAYS, oops + "duplicate motor id strings for " + oName + " and " + iName);
//			}
//			if(mtrCfgO.eMotorObj.equals(mtrCfgI.eMotorObj)) {
//				foundTrouble = true;
//				Tm16DbgTk.printIt(M.ALWAYS, oops + "same motor used for " + oName + " and " + iName);
//			}
//			if(mtrCfgO.eLiveWindowItemName.equals(mtrCfgI.eLiveWindowItemName)) {
//				foundTrouble = true;
//				Tm16DbgTk.printIt(M.ALWAYS, oops + "same LiveWindow name used for " + oName + " and " + iName);
//			}
//			if(mtrCfgO.eSmartDashKeyCurSpd.equals(mtrCfgI.eSmartDashKeyCurSpd)) {
//				foundTrouble = true;
//				Tm16DbgTk.printIt(M.ALWAYS, oops + "same SmartDashboard key used for " + oName + " and " + iName);
//			}
//		}
//    	return foundTrouble; 
//    }

}

