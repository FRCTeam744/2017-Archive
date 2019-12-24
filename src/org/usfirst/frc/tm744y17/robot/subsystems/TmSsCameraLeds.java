package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_Relay;
import org.usfirst.frc.tm744y17.robot.driverStation.TmDriverStation;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
//import org.usfirst.frc.tm744y17.robot.config.Tm16Misc;
//import org.usfirst.frc.tm744y17.robot.config.Tm16Opts;
//import org.usfirst.frc.tm744y17.robot.config.TmHdwr16RoMap;
//import org.usfirst.frc.tm744y17.robot.devices.TmFakeRelay;
//import org.usfirst.frc.tm744y17.robot.devices.TmFakeableRelay;
//import org.usfirst.frc.tm744y17.robot.driverStation.TmDriverStation;
//import org.usfirst.frc.tm744y17.robot.helpers.Tm16DbgTk;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
//import org.usfirst.frc.tm744y17.robot.smartdash.TmSdDbgSD;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author robotics
 */
public class TmSsCameraLeds extends Subsystem implements TmStdSubsystemI
{
	TmFakeable_Relay m_lights = null;    
    TmFakeable_Relay m_flashlight = null;

    /**
     * provide a mechanism for other classes (like TmRobotDrive)
     * to get access to the Camera LEDs when needed.  We only want one instance
     * of this class in the code.
     *
     * @return - the existing instance of this class (will create
     *           an instance if there isn't one yet).
     */
    public static synchronized TmSsCameraLeds getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new TmSsCameraLeds();
        }
        return m_instance;
    }
    private static TmSsCameraLeds m_instance = null;


    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    //yes, looks weird, but they only have one wire connected, and it's
    //connected to kReverse
    private static final Relay.Value RELAY_ON = Relay.Value.kOff;//.kReverse;
    private static final Relay.Value RELAY_OFF = Relay.Value.kReverse; //.kOff;
    
//    private static final boolean USE_LVD = Tm16Opts.OPT_USE_LABVIEW_DASHBOARD; // do/don't use LabView dashboard
    
	@Override
    public void doRoboInit()
    {
		m_lights = new TmFakeable_Relay(TmHdwrRoMap.RoNamedIoE.CAMERA_LEDS_RELAY);
		m_flashlight = new TmFakeable_Relay(TmHdwrRoMap.RoNamedIoE.FLASHLIGHT_RELAY);
//   		LiveWindow.addActuator(Tm16Misc.LwSubSysName.SS_CAMERA_LEDS, 
//	                Tm16Misc.LwItemNames.CAMERA_LEDS, m_lights);
        m_lightsOn = !LIGHTS_ON;
        TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
////        if(USE_LVD) SmartDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
//        if(USE_LVD) TmLabviewDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
	}

	@Override
    public void doDisabledInit()
    {
			m_lights.set(RELAY_OFF);
			m_flashlight.set(RELAY_OFF);
        m_lightsOn = !LIGHTS_ON;
        TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
//        TmSdDbgSD.dbgPutBoolean(Tm16Misc.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
        //if(USE_LVD) SmartDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
//        if(USE_LVD) TmLabviewDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
    }

    public void enabledInit()
    {
    	if(TmDriverStation.getInstance().isEnabledLiveWindowTest()) {
    	} else {	
    		m_lights.set(RELAY_ON);
    		//only for teleop. handle there:   m_flashlight.set(RELAY_ON);
	        m_lightsOn = LIGHTS_ON;
	        TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
//	        TmSdDbgSD.dbgPutBoolean(Tm16Misc.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
//	        if(USE_LVD) SmartDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
//	        if(USE_LVD) TmLabviewDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
    	}
    }

    private static final boolean LIGHTS_ON = true;
    private boolean m_lightsOn = false;
    public synchronized void toggleLights()
    {
        m_lightsOn = ! m_lightsOn;
        if(m_lightsOn==LIGHTS_ON)
        {
        		m_lights.set(RELAY_ON);
    			m_flashlight.set(RELAY_ON);
        }
        else
        {
        		m_lights.set(RELAY_OFF);
    			m_flashlight.set(RELAY_OFF);
        }
        P.println("toggled camera LEDs: now " + (isLedRingOn()?"ON":"OFF"));
        TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
//        TmSdDbgSD.dbgPutBoolean(Tm16Misc.SdKeysE.KEY_CAMERA_LEDS, m_lightsOn);
//        if(USE_LVD) SmartDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
//        if(USE_LVD) TmLabviewDashboard.putBoolean(Tm16Misc.LvdKeys.LDBKEY_CAMERA_LEDS, m_lightsOn);
    }

    public synchronized boolean isLedRingOn()
    {
        return (m_lightsOn==LIGHTS_ON);
    }

	@Override
	public void doAutonomousInit() {
		enabledInit();
	}

	@Override
	public void doTeleopInit() {
		enabledInit();
		m_flashlight.set(RELAY_ON);
	}

	@Override
	public void doLwTestInit() {
		
	}

	@Override
	public void doRobotPeriodic() {
		
	}

	@Override
	public void doDisabledPeriodic() {
		
	}

	@Override
	public void doAutonomousPeriodic() {
		
	}

	@Override
	public void doTeleopPeriodic() {
		
	}

	@Override
	public void doInstantiate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub
		
	}

}
