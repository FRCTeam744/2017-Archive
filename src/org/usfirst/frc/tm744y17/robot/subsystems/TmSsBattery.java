/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;

//import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
////import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import first.team744.yr2014.TmDbgTk;
//import first.team744.yr2014.TmHdwr;
//import first.team744.yr2014.TmMisc;
////import first.team744.yr2014.TmOpts;
//import first.team744.yr2014.TmTools;

/**
 *
 * @author robotics
 */
public class TmSsBattery extends Subsystem implements TmStdSubsystemI {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    /**
     * provide a mechanism for other classes (like TmSsRobotDrive)
     * to get access to the Battery when needed.  We only want one instance
     * of this class in the code.
     *
     * @return - the existing instance of this TmSsBattery class (will create
     *           an instance if there isn't one yet).
     */
    public static synchronized TmSsBattery getInstance() {
        if (m_instance == null) { m_instance = new TmSsBattery(); }
        return m_instance;
    }
    private static TmSsBattery m_instance = null;
    private TmSsBattery() {}

	@Override
	public void doInstantiate() {
		m_batTmr = new Timer();
	}
	
    public void doRoboInit()
    {
//        m_batTmr = new Timer();
        m_batTmr.reset();
        doCommonPeriodic(); //assuming just logs info to SD
    }

    public void doDisabledInit()
    {
        m_batTmr.stop();
        m_batNeedStart = true;
    }
	
	@Override
	public void doAutonomousInit() {
		startTimerIfNeeded();
	}
	
	@Override
	public void doTeleopInit() {
		startTimerIfNeeded();
	}
	
	@Override
	public void doLwTestInit() {
		startTimerIfNeeded();
	}
	
	@Override
	public void doDisabledPeriodic() {
		//doCommonPeriodic();
	}
	@Override
	public void doAutonomousPeriodic() {
		doCommonPeriodic();
	}
	@Override
	public void doTeleopPeriodic() {
		doCommonPeriodic();
	}
	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub
		
	}

	public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public final static double BOGUS_LOWEST_VOLTAGE_VALUE = 99.99;

    private double initialVoltage = 0;
    private double lowestVoltage = BOGUS_LOWEST_VOLTAGE_VALUE;
    private double lowestTime    = 0;

    private Timer m_batTmr = null;
    private boolean m_batNeedStart = true;

    public double getRoboInitialBatteryVoltage() {return initialVoltage;}
    public double getRoboLowestBatteryVoltage()  {return lowestVoltage;}
    public double getRoboLowestBatteryTime()     {return lowestTime;}



    public double getRoboBatteryVoltage()
    {
        double curVolt = DriverStation.getInstance().getBatteryVoltage();

        if(initialVoltage == 0) {initialVoltage = curVolt;}

        if(DriverStation.getInstance().isEnabled())
        {
            if(curVolt < lowestVoltage)
            {
                lowestVoltage = curVolt;
                lowestTime = m_batTmr.get();
            }
        }
        return curVolt;
    }

    public void startTimerIfNeeded()
    {
        if(m_batNeedStart)
        {
            m_batTmr.start();
        }
        m_batNeedStart = false;
    }
    
    public void doCommonPeriodic()
    {
    	//also called from doRoboInit()
    	double curVolts = getRoboBatteryVoltage();
    	
        TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_BAT_INIT, getRoboInitialBatteryVoltage());
        TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_BAT_MIN, getRoboLowestBatteryVoltage());
        TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_BAT_MIN_TIME, getRoboLowestBatteryTime());
        TmPostToSd.dbgPutNumber(TmMiscSdKeys.SdKeysE.KEY_BAT_CUR, curVolts);
    }

	public static final double NOMINAL_MAX_BATTERY_VOLTAGE = 12.0;
	public static double getNominalMaxBatteryVoltage() { return NOMINAL_MAX_BATTERY_VOLTAGE; }

    public double getBatteryCompensationFactor() {
    	return getBatteryCompensationFactor(NOMINAL_MAX_BATTERY_VOLTAGE);
    }
    public double getBatteryCompensationFactor(double nominalBatteryVoltage) {
    	return nominalBatteryVoltage / getRoboBatteryVoltage();
    }
    
    public double doBatteryCompensationCalc(double val) {
    	return doBatteryCompensationCalc(val, NOMINAL_MAX_BATTERY_VOLTAGE);
    }
    
    public double doBatteryCompensationCalc(double val, double nominalBatteryVoltage) {
    	double ans = val * nominalBatteryVoltage / getRoboBatteryVoltage();
    	return ans;
    }

	@Override
	public void doRobotPeriodic() {
		// TODO Auto-generated method stub
		
	}
    
}