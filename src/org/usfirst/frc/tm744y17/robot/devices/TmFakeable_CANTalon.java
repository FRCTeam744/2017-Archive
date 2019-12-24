package org.usfirst.frc.tm744y17.robot.devices;

import org.usfirst.frc.tm744y17.robot.helpers.TmCANTalonFeaturesI;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsBattery;
import org.usfirst.frc.tm744y17.robot.config.Tm17Opts;
import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors.MotorConfigE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class TmFakeable_CANTalon implements TmCANTalonFeaturesI, SpeedController, MotorSafety {
	private CANTalon m_realObj = null;
	private boolean m_beingFaked = false;
	
	private ProfileLog<TmFakeable_CANTalon> m_outputCurrentLog;
	private int m_userCurrentLimit;
	
	private boolean m_usingBatteryCompensation = false;
	private double m_batteryCompensationConfigFactor = 1.0;
	private TalonControlMode m_controlMode = TalonControlMode.PercentVbus;
	final double NOMINAL_BATTERY_VOLTAGE = TmSsBattery.getNominalMaxBatteryVoltage(); //12.0;
	
	RoNamedIoE m_namedCanTalonIoDef;
	
	double m_lastRequestedSpeedRaw = 0;
	double m_lastRequestedSpeedAdjusted = 0;
	double m_lastRequestedSpeed = 0;
	

	
	private TmFakeable_CANTalon m_instance = this;

	private void setupAsFake() {
		m_realObj = null;
		m_beingFaked = true;
	}

	public boolean isFake() {
		return m_beingFaked;
	}

	public boolean isReal() {
		return !m_beingFaked;
	}

	private FakeParms m_fakeParms;

	public class FakeParms {
		
		//any parms or methods that the 'fake' object needs to
		//emulate should be coded here.
		MotorSafetyHelper f_safetyHelper;
		double f_safetyExpiration = 0;
		double f_speed = 0;
		double f_outputCurrent = 0;
		int f_userCurrentLimit = 40;
		
		int f_encoderPos = 0;
		private Timer f_encoderTimer;
		private Timer f_elapsedTimer;
		double f_maxEncoderTicksPerSec;
		int f_fakeEncoderDirection;
		double f_ticksPer100ms = 0;
		private Object f_encoderLock = new Object();
		
		public void setCurrentLimit(int amps) {
			f_userCurrentLimit = amps;
		}
		ConnectedEncoder f_fakeEncoder = null;
		public void setFakeEncoder(ConnectedEncoder encoder) { 
			if(f_fakeEncoder==null) {
				f_fakeEncoder = encoder;
				f_fakeEncoderDirection = encoder.getEncoderPolarityFactor(); //.c_encPolarityRelToMotor.eDirection;
				f_maxEncoderTicksPerSec = encoder.c_countsPerRevolution * encoder.c_maxRps;
			}
		}
		
		public double get() { return f_speed; }
		public void set(double spd) { 

//			double battCompensationSpeed = spd;
////			double fakeMotorSpeed = batCompensationSpeed;
//			
//			battCompensationSpeed = Tt.clampFrcValue(battCompensationSpeed);
//			//note: x=a?b:c; means if(a){x=b;}else{x=c;}
////			m_fakeMotorSpeed = m_isInverted ? -battCompensationSpeed : battCompensationSpeed;
//			f_speed = battCompensationSpeed;
//			f_outputCurrent = 0.75 * battCompensationSpeed;
//			
//			//2016 shooter motor stuff
//			//at 85% motor power we get 75rps. Therefore max rps is 88.2353
////			final double maxRps = 88.2353;
//			//magnetic encoder has 4096 ticks per revolution
//			//max rpm of shooter motor is 6100 rpm
//			//max speed:  (6100 rev/min)/(60 secs/min)*(4096 ticks/rev) = 416426.666667 ticks/sec
//			final double maxMotorRpm = m_maxMotorRpm;
//			final double secsPerMinute = 60;
//			final double ticksPerRev = m_encoderCodesPerRev;
//			final double maxTicksPerSec = maxMotorRpm / secsPerMinute * ticksPerRev;
//			final double maxTicksPer100ms = maxTicksPerSec / 10;
//			synchronized(encoderLock) {
//				m_fakeTicksPer100ms = maxTicksPer100ms * battCompensationSpeed;
//			}
////			final double ticksPerRev = 4096;
////			final double periodsPerSec = 10;
////			final double conversionFactor = periodsPerSec/ticksPerRev;
//			if( ! m_notifierRunning) {
//				startNotifier();
//			}
			
			f_ticksPer100ms = (f_maxEncoderTicksPerSec * spd) / 10; // (DEFAULT_MAX_ENCODER_TICKS_PER_SEC * spd) / 10;			
			f_speed = spd; 
			f_safetyHelper.feed(); 
		}
		
		//4in. wheels: (4in * pi) / 12 = 1.0471975512
		public static final double FEET_PER_REVOLUTION = 1.047;
		public static final double MAX_FEET_PER_SECOND = 16;
		public static final int DEFAULT_ENCODER_TICKS_PER_REV = 4096; //magnetic encoder
		// (ft/sec)/(ft/rev) = (rev/sec);   (rev/sec)*(ticks/rev)= (ticks/sec)
		public static final double DEFAULT_MAX_ENCODER_TICKS_PER_SEC = (MAX_FEET_PER_SECOND / FEET_PER_REVOLUTION) * DEFAULT_ENCODER_TICKS_PER_REV;
		
		public void stopMotor() { f_speed = 0.0; f_safetyHelper.feed(); }
		
		//--- misc. quick methods
		public void setExpiration(double timeout) { f_safetyExpiration = timeout; }
		public double getExpiration() { return f_safetyExpiration; }
		public boolean isAlive() { return f_safetyHelper.isAlive(); }
		public void setSafetyEnabled(boolean enabled) { f_safetyHelper.setSafetyEnabled(enabled); }
		public boolean isSafetyEnabled() { return f_safetyHelper.isSafetyEnabled(); }
		public String getDescription() { return "Fake CANTalon -- " + m_namedCanTalonIoDef.name(); }
		
		public int getEncPosition(int dbgMsgCnt) { 
			int ans;
			synchronized(f_encoderLock) {
				updateEncoderTicks(dbgMsgCnt);
				ans = f_fakeEncoderDirection * f_encoderPos;
			}
			return ans;
		}
			
		/**
		 * should only be called by something that owns f_encoderLock!!
		 */
		private void updateEncoderTicks(int dbgMsgCnt) {
//			double change = f_encoderTimer.get() * (f_maxEncoderTicksPerSec * m_lastRequestedSpeed);
			double timeRaw = f_encoderTimer.get();
			double time = (timeRaw<0.020) ? 0.020 : timeRaw;
//			double elapsedTime = f_elapsedTimer.get();
			double change = time * (f_maxEncoderTicksPerSec * f_speed);
			//round the value to nearest int, keeping sign
			int intChange = Tt.doubleToRoundedInt(change);
			f_encoderPos += intChange;
			f_encoderTimer.reset();
//			if(dbgMsgCnt>0) {
//				P.printFrmt(-1, "fakeEncUpdTicks: timeRaw=%f, time=%f, spd=% 1.2f, maxTps=%1.2f, chg=% 1.2f, iChg=% d, pos=% d", 
//						timeRaw, time, f_speed, f_maxEncoderTicksPerSec, change, intChange, f_encoderPos);
//			}
		}
		
		public void setEncPosition(int pos) { 
			synchronized(f_encoderLock) {
				f_encoderPos = pos;
				f_encoderTimer.reset();
			}
		}
		
		public double getSpeed() {
			double ans;
//			synchronized(encoderLock) {
				ans = f_ticksPer100ms;
//			}
			return ans;
		}
		public double getOutputCurrent() {
			double ans;
			//use something outrageous to remind folks that this is a FAKE CANTalon
			f_outputCurrent = f_speed * 0.090;
			ans = f_outputCurrent;
			return ans;
		}
		
		protected FakeParms(double maxEncoderTicksPerSec) {
			f_safetyHelper = new MotorSafetyHelper(m_instance);
			f_encoderTimer = new Timer();
			f_elapsedTimer = new Timer();
			f_encoderTimer.start();
			f_elapsedTimer.start();
			f_maxEncoderTicksPerSec = maxEncoderTicksPerSec;
		}
		
	}
	
	public TmFakeable_CANTalon(RoNamedIoE namedCanTalonIoDef) {
		this(namedCanTalonIoDef, FakeParms.DEFAULT_MAX_ENCODER_TICKS_PER_SEC);
	}
	private TmFakeable_CANTalon(RoNamedIoE namedCanTalonIoDef, double maxEncoderTicksPerSecond) {
		String thisClassName = Tt.extractClassName(this);
		m_namedCanTalonIoDef = namedCanTalonIoDef;
		m_fakeParms = new FakeParms(maxEncoderTicksPerSecond);
		m_outputCurrentLog = new ProfileLog<TmFakeable_CANTalon>(this);
		m_userCurrentLimit = 40;

		
		int canId = namedCanTalonIoDef.getCanId();
//works	int canId = namedCanTalonIoDef.getNamedModuleDef().getModuleIndex();
//bug	int canId = namedCanTalonIoDef.getNamedConnDef().getConnectionIndex();
		String exceptionMsgPrefix = thisClassName + " " + namedCanTalonIoDef.name() + 
				" (CAN id=" + canId + ") got exception: ";
		
		try {
			if(m_namedCanTalonIoDef.isForcedFakeRequested() || Tm17Opts.isUseFakeCanTalons()) {
				setupAsFake();
			} else {
				m_realObj = new CANTalon(canId);
				//set up whatever else is needed here...
			}
		} catch (TmExceptions.CannotSimulateCANTalonEx t) {
			TmExceptions.reportExceptionOneLine(t, exceptionMsgPrefix + t.toString());
			setupAsFake();
		} catch (Throwable t) {
			//unknown error
			TmExceptions.reportExceptionMultiLine(t, exceptionMsgPrefix + t.toString());
			setupAsFake();
		}
		
		if (isFake()) {
			System.out.println(thisClassName + " " + namedCanTalonIoDef.name() + " will be a FAKE CANTalon");
		}
	}
	
	@Override
	public String getMaxMinOutputCurrentSummary() { return m_outputCurrentLog.getMaxMinOutputCurrentSummary(); } // return m_outputCurrentLog.toString(); }
	
	public void enableBatteryCompensation() {
		m_usingBatteryCompensation = true;
	}
	
	public void disableBatteryCompensation() {
		m_usingBatteryCompensation = false;
	}
	
	public boolean isUsingBatteryCompensation() {
		return m_usingBatteryCompensation;
	}
	
	/**
	 * the ratio of nominal to current battery voltages will be multiplied by
	 * this tuning parameter before being used to adjust the actual value 
	 * sent to the motor controller
	 * @param tuningParm
	 */
	public void configBatteryCompensation(double tuningParm) {
		m_batteryCompensationConfigFactor = tuningParm;
	}
	
	//a more intuitive name for the standard getSpeed() method
	public double getCountsPer100ms() { return getSpeed(); }
	
	//used by ConnectedEncoders that detect that they're "connected" to a fake CAN Talon
	public void setFakeEncoder(ConnectedEncoder encoder) { m_fakeParms.setFakeEncoder(encoder); }

	
	//------------available on CANTalon and used in our code
	public int getEncPosition() { return getEncPosition(0); }
	public int getEncPosition(int dbgMsgCnt) {
		int ans;
		if(isFake()) {
			ans = m_fakeParms.getEncPosition(dbgMsgCnt);
		} else {
			ans = m_realObj.getEncPosition();
		}
		return ans;
	}

	public void setEncPosition(int pos) {
		if(isFake()) {
			m_fakeParms.setEncPosition(pos);
		} else {
			m_realObj.setEncPosition(pos);
		}
	}

	public double getSpeed() {
		double ans;
		if(isFake()) {
			ans = m_fakeParms.getSpeed();
		} else {
			ans = m_realObj.getSpeed();
		}
		return ans;
	}

	public double getOutputCurrent() {
		double ans;
		if(isFake()) {
			ans = m_fakeParms.getOutputCurrent();
		} else {
			ans = m_realObj.getOutputCurrent();
		}
		m_outputCurrentLog.monitorOutputCurrent(ans);
		return ans;
	}

	public void setUserCurrentLimit(int amps) {
		if(isFake()) {
			m_fakeParms.setCurrentLimit(amps);
		} else {
			m_realObj.setCurrentLimit(amps);
			m_realObj.EnableCurrentLimit(true);
		}
		m_userCurrentLimit = amps;
	}

	public void changeControlMode(TalonControlMode newMode) {
		m_controlMode = newMode;
		switch(newMode) {
		case PercentVbus:
			//the default that we were always using before
			//TmFakeable_CANTalon battery compensation code will be used by default
			break;
		case Voltage:
			//Talon's built-in battery compensation code
			m_usingBatteryCompensation = false; //disable local battery compensation
			break;
		default:
			m_usingBatteryCompensation = false; //disable local battery compensation
			break;
		}
		m_controlMode = newMode;
		if(isFake()) {
//			m_fakeParms.changeControlMode(newMode);
		} else {
			m_realObj.changeControlMode(newMode);
		}
		
	}
	
	public void setVoltageCompensationRampRate(double rampRate) {
		if(isFake()) {
//			m_fakeParms.setVoltageCompensationRampRate(rampRate);
		} else {
			m_realObj.setVoltageCompensationRampRate(rampRate);
		}
	}

	//-------------required by PIDOutput via SpeedController
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}
	
	//-------------required by SpeedController
	@Override
	public double get() {
		double ans;
		double raw;
		if(isFake()) {
			raw = m_fakeParms.get();
		} else {
			raw = m_realObj.get();
		}
		ans = 0.0;
		switch(m_controlMode) {
		case PercentVbus:
			ans = raw;
			break;
		case Voltage:
			ans = raw / NOMINAL_BATTERY_VOLTAGE;
			break;
		default:
			break;
		}
		return ans;
	}

	@Override
	public void set(double speed) {
		m_lastRequestedSpeedRaw = speed;
		switch(m_controlMode) {
		case PercentVbus:
			double battCompensationSpeed = speed;
			if(m_usingBatteryCompensation) {
				battCompensationSpeed = speed * ((NOMINAL_BATTERY_VOLTAGE / TmSsBattery.getInstance().getRoboBatteryVoltage()) * m_batteryCompensationConfigFactor);
			}
			m_lastRequestedSpeedAdjusted = battCompensationSpeed;
			m_lastRequestedSpeed = battCompensationSpeed;
			if(isFake()) {
				m_fakeParms.set(battCompensationSpeed);
			} else {
				m_realObj.set(battCompensationSpeed);
			}
			break;
		case Voltage:
			double adjSpeed = speed * NOMINAL_BATTERY_VOLTAGE;
			m_lastRequestedSpeedAdjusted = adjSpeed;
			m_lastRequestedSpeed = adjSpeed;
			if(isFake()) {
				m_fakeParms.set(adjSpeed);
			} else {
				m_realObj.set(adjSpeed);
			}
			break;
		case Speed:
//			double battCompensationSpeed = speed;
//			if(m_usingBatteryCompensation) {
//				battCompensationSpeed = speed * ((NOMINAL_BATTERY_VOLTAGE / TmSsBattery.getInstance().getRoboBatteryVoltage()) * m_batteryCompensationConfigFactor);
//			}
//			m_lastRequestedSpeedAdjusted = battCompensationSpeed;
			m_lastRequestedSpeed = speed;
			if(isFake()) {
				m_fakeParms.set(speed);
			} else {
				m_realObj.set(speed);
			}
			break;
		default:
//			super.set(newSpeed);
			break;
		}
		if(isFake()) {
			m_fakeParms.set(speed);
			m_fakeParms.f_safetyHelper.feed();
//			if (m_safetyHelper != null) {
//				m_safetyHelper.feed();
//			}
		} else {
			if(Math.abs(speed)>0.1) {
				String msg= "time to debug";
			}
			m_realObj.set(speed);
			
		}
	}
	
	public void setFeedbackDevice(FeedbackDevice device){
		if(isFake()) {
			//nothing
		}
		else {
			m_realObj.setFeedbackDevice(device);
		}
	}
	
	public void reverseSensor(boolean val) {
		if(isFake()) {
			//nothing
		}
		else{
			m_realObj.reverseSensor(val);
		}
	}
	
	public double getClosedLoopError(){
		double ans = 0.0;
		if(isFake()) {
			//nothing
		}
		else {
			ans = m_realObj.getClosedLoopError();
		}
		return ans;
	}
	
	public void configVoltageOutputs(){
		if(isFake()) {
			//nothing
		}
		else {
			m_realObj.configNominalOutputVoltage(0.0f, 0.0f);
			m_realObj.configPeakOutputVoltage(+12.0f,  0.0f);
		}
	}
	
	public void setPidGains(double F, double P, double I, double D) {
		if(isFake()) {
			//nothing
		}
		else {
			m_realObj.setProfile(0);
			m_realObj.setF(F);
			m_realObj.setP(P);
			m_realObj.setI(I);
			m_realObj.setD(D);
		}
	}

	@Override
	public void setInverted(boolean isInverted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getInverted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		if(isFake()) {
			m_fakeParms.stopMotor();
		} else {
			m_realObj.disable();
		}
	}

	@Override
	public void stopMotor() {
		if(isFake()) {
			m_fakeParms.stopMotor();
		} else {
			m_realObj.stopMotor();
		}
	}

	@Override
	public void setExpiration(double timeout) { //MotorSafety
		if(isFake()) {
			m_fakeParms.setExpiration(timeout);
		} else {
			m_realObj.setExpiration(timeout);
		}
	}

	@Override
	public double getExpiration() {
		double ans;
		if(isFake()) {
			ans = m_fakeParms.getExpiration();
		} else {
			ans = m_realObj.getExpiration();
		}
		return ans;
	}

	@Override
	public boolean isAlive() {
		boolean ans;
		if(isFake()) {
			ans = m_fakeParms.isAlive();
		} else {
			ans = m_realObj.isAlive();
		}
		return ans;
	}

	@Override
	public void setSafetyEnabled(boolean enabled) {
		if(isFake()) {
			m_fakeParms.setSafetyEnabled(enabled);
		} else {
			m_realObj.setSafetyEnabled(enabled);
		}
	}

	@Override
	public boolean isSafetyEnabled() {
		boolean ans;
		if(isFake()) {
			ans = m_fakeParms.isSafetyEnabled();
		} else {
			ans = m_realObj.isSafetyEnabled();
		}
		return ans;
	}

	@Override
	public String getDescription() {
		String ans;
		if(isFake()) {
			ans = m_fakeParms.getDescription();
		} else {
			ans = m_realObj.getDescription();
		}
		if(ans==null) {
			ans = m_namedCanTalonIoDef.name();
		}
		return ans;
	}

	//implemented so Fake CAN Talons can properly imitate the behavior of the real Talon and Encoder
	public enum EncoderPolarityE {
		MATCHES_MOTOR(1), OPPOSITE_OF_MOTOR(-1);
		private final int eDirection;
		private EncoderPolarityE(int polarityFactor) {
			eDirection = polarityFactor;
		}
	}
	public enum EncoderCountsCapabilityE {
		ABSOLUTE_USED_AS_ABSOLUTE, //absolute encoder, ignore requests to reset or set value
		ABSOLUTE_USED_AS_RELATIVE, //absolute encoder, implement resets and sets using c_countSnapshot
		RELATIVE; //relative encoder, hardware processes resets and sets appropriately
		public boolean isAbsoluteUsedAsAbsolute() { return this.equals(ABSOLUTE_USED_AS_ABSOLUTE); }
		public boolean isAbsoluteUsedAsRelative() { return this.equals(ABSOLUTE_USED_AS_RELATIVE); }
		public boolean isRelative() { return this.equals(RELATIVE); }
	}
	public class ConnectedEncoder {
		private static final int DEFAULT_COUNTS_PER_REVOLUTION = 4096;
		private static final double DEFAULT_FEET_PER_REVOLUTION = 1.0;
		private static final double DEFAULT_MAX_RPS = 5200;
		
		private MotorConfigE c_mtrCfg;
		private SdKeysE c_sdKeyPosition;
		private double c_feetPerRevolution = DEFAULT_FEET_PER_REVOLUTION;
		private int c_countsPerRevolution = DEFAULT_COUNTS_PER_REVOLUTION;
		private double c_maxRps = DEFAULT_MAX_RPS;
		private EncoderPolarityE c_encPolarityRelToMotor;
		private boolean c_isOnFakeTalon = false;
		private EncoderCountsCapabilityE c_encCountsCapability;
		private int c_countsSnapshot;
		
		//magnetic encoder is connected to a CANTalon controller and accessed through it.
//		public ConnectedEncoder(MotorConfigE mtrCfg, SdKeysE encoderSdKeyPosition, double maxRevPerSec) {
//			this(mtrCfg, encoderSdKeyPosition, maxRevPerSec, DEFAULT_COUNTS_PER_REVOLUTION, DEFAULT_FEET_PER_REVOLUTION);
//		}
		
		/**
		 * 
		 * @param mtrCfg - allows a Fake CAN Talon to properly emulate an encoder attached to the real CAN Talon
		 * @param encoderSdKeyPosition - used to post the encoder "position" on smartdashboard
		 * @param maxRevPerSec - allows a Fake CAN Talon to properly emulate an encoder attached to the real CAN Talon
		 * @param countsPerRevolution - used to calculate distances from encoder position
		 * @param feetPerRevolution - used to calculate distances from encoder position
		 * @param encPolarityRelToMotor - allows a Fake CAN Talon to properly emulate an encoder "attached" to it,
		 *                                available for use by subsystems that use the encoder values
		 * @param encCountsCapability - indicates whether this is an "absolute" encoder that can't be reset to 0                              
		 */
		public ConnectedEncoder(MotorConfigE mtrCfg, SdKeysE encoderSdKeyPosition, double maxRevPerSec,
								int countsPerRevolution, double feetPerRevolution, 
								EncoderPolarityE encPolarityRelToMotor,
								EncoderCountsCapabilityE encCountsCapability) {
			c_mtrCfg = mtrCfg;
			c_sdKeyPosition = encoderSdKeyPosition;
			c_maxRps = maxRevPerSec;
			c_feetPerRevolution = feetPerRevolution;
			c_countsPerRevolution = countsPerRevolution;
			c_encPolarityRelToMotor = encPolarityRelToMotor;
			c_encCountsCapability = encCountsCapability;
			c_countsSnapshot = 0;
			c_isOnFakeTalon = isOnFakeCANTalon();
		}
		
//		public boolean isAbsoluteUsedAsRelative() { return c_encCountsCapability.isAbsoluteUsedAsRelative(); }
		
		public int getEncoderPolarityFactor() { return c_encPolarityRelToMotor.eDirection; }
		
		public TmFakeable_CANTalon getCanTalonMotorObj() {
			return c_mtrCfg.getCanTalonMotorObj();
		}
		
		public MotorConfigE getMotorCfg() { return c_mtrCfg; }
		
		private boolean isOnFakeCANTalon() {
			boolean ans = false;
			if(c_mtrCfg.getCanTalonMotorObj() != null) {
				if(c_mtrCfg.getCanTalonMotorObj().isFake()) {
					setFakeEncoder(this);
					ans = true;
				}
			}
			return ans;
		}
		
		/**
		 * @return counts per revolution for the installed encoder
		 */
		public int getCpr() {
			return c_countsPerRevolution;
		}

		public double toDistance(int count) {
			double ans = 0;
			double ticks = count;
			ans = ticks / c_countsPerRevolution * c_feetPerRevolution;
			
			return ans;
		}

		public double getDistance() {
			int get = get();
			return toDistance(get);
		}
		
		public synchronized int get() {
			int ans;
			ans = c_mtrCfg.getMotorConfigData().getEncPosition();
			if(c_encCountsCapability.isAbsoluteUsedAsRelative()) { ans -= c_countsSnapshot; }
			return ans;
		}
		
		public void reset() {
			if(c_encCountsCapability.isRelative()) { 
				c_mtrCfg.getMotorConfigData().setEncPosition(0); 
			}
			else if(c_encCountsCapability.isAbsoluteUsedAsRelative()) { 
				c_countsSnapshot = c_mtrCfg.getMotorConfigData().getEncPosition();
			}
			else { P.println("shouldn't be trying to reset the absolute encoder connected to " + c_mtrCfg.name());}
		}

		public void postToSdPosition() {
			if(c_sdKeyPosition != null) {
				TmPostToSd.dbgPutNumber(c_sdKeyPosition, get());
			}
		}
	}
	
}
