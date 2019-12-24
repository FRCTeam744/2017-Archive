package org.usfirst.frc.tm744y17.robot.driverStation;

import java.io.BufferedInputStream;
import java.io.IOException;

//import org.usfirst.frc.team744.yr2015.robot.subsystems.TmSsBattery;

import edu.wpi.first.wpilibj.DriverStation;

//Ugh!! if this class extends DriverStation, then creating a new instance of 
//this class calls DriverStation's constructor which causes a bunch of stuff
//to happen a second time that should never happen more than once.
public class TmDriverStation {//extends DriverStation {

	DriverStation m_ds = DriverStation.getInstance();
	
    /**
     * provide a mechanism for other classes (like subsytems)
     * to get access to this class when needed.  We only want one instance
     * of this class in the code.
     *
     * @return - the existing instance of this class (will create
     *           an instance if there isn't one yet).
     */
    public static synchronized TmDriverStation getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new TmDriverStation();
        }
        return m_instance;
    }

    private static TmDriverStation m_instance = null;
    
    private TmDriverStation() {}  //so no one can call it via new!!
      
//calls to this from doPeriodic caused messages from DS to stop,
//  causing periodic code to never get called again.
  public String getStateDescr() {
  	String ans = "";
  	ans += m_ds.isEnabled() ? "enabled" : "disabled";
  	ans += ", " + (m_ds.isAutonomous() ? "autonomous" :
  				(m_ds.isOperatorControl() ? "teleop" : "liveWindow test"));
  	return ans;
  }
    
	public boolean isLiveWindowTest() {
		return (m_ds.isTest());
	}
	
	public boolean isDisabled() {
		return (m_ds.isDisabled());
	}
	
	public boolean isTeleop() {
		return m_ds.isOperatorControl();
	}

	public boolean isAutonomous() {
		return m_ds.isAutonomous();
	}

	public boolean isEnabledLiveWindowTest() {
		return (m_ds.isEnabled() && m_ds.isTest());
	}

	public boolean isEnabledAutonomous() {
		return (m_ds.isEnabled() && m_ds.isAutonomous());
	}

	public boolean isEnabledTeleop() {
		return (m_ds.isEnabled() && m_ds.isOperatorControl());
	}
	
	public boolean isEnabledAutonomousOrTeleop() { //i.e. not live window....
		return (m_ds.isEnabled() && (m_ds.isAutonomous() || m_ds.isOperatorControl()));
	}
	
	public boolean isBlueAlliance() {
		return (m_ds.getAlliance().equals(DriverStation.Alliance.Blue));
	}
	
	public boolean isRedAlliance() {
		return (m_ds.getAlliance().equals(DriverStation.Alliance.Red));
	}
	
	public String getDsLocationAndAlliance() {
		String ans = "";
		DriverStation.Alliance color = m_ds.getAlliance();
		ans += (color==DriverStation.Alliance.Blue) ? "Blue" : "Red";
		ans += m_ds.getLocation();
		return ans;
	}
	
	private int m_teamNumber = 0;
	public int getTeamNumber() {
		int ans = 0;
		if( m_teamNumber==0 ) {
			m_teamNumber = getRobotTeamNumberFromHostname();
		}
		ans = m_teamNumber;
		return ans;
	}
	
	public boolean isTeamNbr744() { return getTeamNumber()==744; }
	public boolean isTeamNbr745() { return getTeamNumber()==745; }
	
	/**
	 * Code that will read hostname from roboRIO and return the team number. See FRC_Java_Programming document.
	 * @return team number extracted from hostname or 0 if it's not a recognized hostname
	 */
	private int getRobotTeamNumberFromHostname() {
		Runtime run = Runtime.getRuntime();
		Process proc;
		String hostname = "????";
		int teamNumber = 0;
		try {
			proc = run.exec("hostname");
			BufferedInputStream in = new BufferedInputStream(proc.getInputStream());
			byte [] b = new byte[256];
			in.read(b, 0, 256);
			hostname = new String(b).trim();
		} catch(IOException e1) {
			System.out.println("[TmDriverStation:getRobotTeamNumber] " + e1.getMessage() +
					" - cannot read roboRIO hostname");
			e1.printStackTrace();
		}
		
		switch(hostname) {
		case "roboRIO-744-FRC":
			teamNumber = 744;
			break;
		case "roboRIO-745-FRC":
			teamNumber = 745;
			break;
		default:
			teamNumber = 746;
			System.out.println("===> Unrecognized hostname from roboRIO: " + hostname);
			break;
		}
		
		return teamNumber;
	}
	
//	  /**
//	   * Report error to Driver Station. Also prints error to System.err Optionally appends Stack trace
//	   * to error message.
//	   *
//	   * @param printTrace If true, append stack trace to error string
//	   */
//	  public static void reportError(String error, boolean printTrace) {
//		  DriverStation.reportError(error, printTrace);
////	    reportErrorImpl(true, 1, error, printTrace);
//	  }
//
//	  /**
//	   * Report warning to Driver Station. Also prints error to System.err Optionally appends Stack
//	   * trace to warning message.
//	   *
//	   * @param printTrace If true, append stack trace to warning string
//	   */
//	  public static void reportWarning(String error, boolean printTrace) {
//		  DriverStation.reportWarning(error, printTrace);
////	    reportErrorImpl(false, 1, error, printTrace);
//	  }

}
