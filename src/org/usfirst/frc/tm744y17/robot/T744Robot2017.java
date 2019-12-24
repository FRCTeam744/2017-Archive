package org.usfirst.frc.tm744y17.robot;


import org.usfirst.frc.tm744y17.bldVerInfo.TmVersionInfo;
import org.usfirst.frc.tm744y17.robot.config.Tm17Opts;
import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsPhys;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmDriverStation;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmOpIf;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsGearFlipper;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsShooter;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsClimber;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsAutonomous;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsBallIntake;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsBattery;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsCameraLeds;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsCameras;
import org.usfirst.frc.tm744y17.robot.helpers.TmFlagsI;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class T744Robot2017 extends IterativeRobot implements TmFlagsI, TmToolsI {
    
    /**
     * KnownSubSystems class to facilitate including all the necessary
     * subsystems in all the necessary methods.
     * @author JudiA
     *
     */
    public static class Kss { // implements TmStdSubsystemI {
    	public static TmSsBattery kssBattery;
    	public static TmSsDriveTrain kssRobotDrive;
    	public static TmSsDrvGearShift kssDrvGearShift;
    	public static TmSsCameraLeds kssCameraLeds;
    	public static TmSsCameras kssCameras;
    	public static TmSsClimber kssClimber;
    	public static TmSsShooter kssShooter;
    	public static TmSsBallIntake kssIntake;
    	public static TmSsAutonomous kssAutonomous;
    	public static TmSsGearFlipper kssGearFlipper;
//    	public static TmSsDancingLeds kssDancingLeds;
//    	public static TmSsArm kssArm;
//    	public static TmSsWhiskers kssWhiskers;
    	    	
    	public static void doGetInstance() {
        	kssBattery = TmSsBattery.getInstance();
        	kssRobotDrive = TmSsDriveTrain.getInstance();
        	kssDrvGearShift = TmSsDrvGearShift.getInstance(); 
        	kssCameraLeds = TmSsCameraLeds.getInstance();
        	kssCameras = TmSsCameras.getInstance();
        	kssClimber = TmSsClimber.getInstance();
        	kssShooter = TmSsShooter.getInstance();
        	kssIntake = TmSsBallIntake.getInstance();
        	kssGearFlipper = TmSsGearFlipper.getInstance();
        	kssAutonomous = TmSsAutonomous.getInstance();
//        	kssDancingLeds = TmSsDancingLeds.getInstance();
//        	kssArm = TmSsArm.getInstance();
//        	kssWhiskers = TmSsWhiskers.getInstance();
    	}
        
    	/**
    	 * give each subsystem a chance to do 'new' for variables and other such stuff
    	 * before any subsystem's roboInit methods are called
    	 */
    	public static void doInstantiate() {
        	kssBattery.doInstantiate();
        	kssRobotDrive.doInstantiate();
        	kssDrvGearShift.doInstantiate();   
        	kssCameraLeds.doInstantiate();
        	kssCameras.doInstantiate();
        	kssClimber.doInstantiate();
        	kssShooter.doInstantiate();
        	kssGearFlipper.doInstantiate();
        	kssAutonomous.doInstantiate();
//        	kssDancingLeds.doInstantiate();
        	kssIntake.doInstantiate();
//        	kssArm.doInstantiate();
//        	kssWhiskers.doInstantiate();
    	}

		public static void doRoboInit() {
        	kssBattery.doRoboInit();
        	kssRobotDrive.doRoboInit();
        	kssDrvGearShift.doRoboInit();   
        	kssCameraLeds.doRoboInit();
        	kssCameras.doRoboInit();
        	kssClimber.doRoboInit();
        	kssShooter.doRoboInit();
        	kssGearFlipper.doRoboInit();
        	kssAutonomous.doRoboInit();
//        	kssDancingLeds.doRoboInit();
        	kssIntake.doRoboInit();
//        	kssArm.doRoboInit();
//        	kssWhiskers.doRoboInit();
		}

		public static void doDisabledInit() {
        	kssBattery.doDisabledInit();
        	kssRobotDrive.doDisabledInit();
        	kssDrvGearShift.doDisabledInit();
        	kssCameraLeds.doDisabledInit();
        	kssCameras.doDisabledInit();
        	kssClimber.doDisabledInit();
        	kssShooter.doDisabledInit();
        	kssGearFlipper.doDisabledInit();
        	kssAutonomous.doDisabledInit();
//        	kssDancingLeds.doDisabledInit();
        	kssIntake.doDisabledInit();
//        	kssArm.doDisabledInit();
//        	kssWhiskers.doDisabledInit();
		}

		public static void doAutonomousInit() {
        	kssBattery.doAutonomousInit();
        	kssRobotDrive.doAutonomousInit();
        	kssDrvGearShift.doAutonomousInit(); 
        	kssCameraLeds.doAutonomousInit();
        	kssCameras.doAutonomousInit();
        	kssClimber.doAutonomousInit();
        	kssShooter.doAutonomousInit();
        	kssGearFlipper.doAutonomousInit();
        	kssAutonomous.doAutonomousInit();
//        	kssDancingLeds.doAutonomousInit();
        	kssIntake.doAutonomousInit();
//        	kssArm.doAutonomousInit();
//        	kssWhiskers.doAutonomousInit();
		}

		public static void doTeleopInit() {
        	kssBattery.doTeleopInit();
        	kssRobotDrive.doTeleopInit();
        	kssDrvGearShift.doTeleopInit();
        	kssCameraLeds.doTeleopInit();
        	kssCameras.doTeleopInit();
        	kssClimber.doTeleopInit();
        	kssShooter.doTeleopInit();
        	kssGearFlipper.doTeleopInit();
        	kssAutonomous.doTeleopInit();
//        	kssDancingLeds.doTeleopInit();
        	kssIntake.doTeleopInit();
//        	kssArm.doTeleopInit();
//        	kssWhiskers.doTeleopInit();
		}

		public static void doLwTestInit() {
        	kssBattery.doLwTestInit();
        	kssRobotDrive.doLwTestInit();
        	kssDrvGearShift.doLwTestInit(); 
        	kssCameraLeds.doLwTestInit();
        	kssCameras.doLwTestInit();
        	kssClimber.doLwTestInit();
        	kssShooter.doLwTestInit();
        	kssGearFlipper.doLwTestInit();
        	kssAutonomous.doLwTestInit();
//        	kssDancingLeds.doLwTestInit();
        	kssIntake.doLwTestInit();
//        	kssArm.doLwTestInit();
//        	kssWhiskers.doLwTestInit();
		}
		
		public static void doRobotPeriodic() {
        	kssBattery.doRobotPeriodic();
        	kssRobotDrive.doRobotPeriodic();
        	kssDrvGearShift.doRobotPeriodic(); 
        	kssCameraLeds.doRobotPeriodic();
        	kssCameras.doRobotPeriodic();
        	kssClimber.doRobotPeriodic();
        	kssShooter.doRobotPeriodic();
        	kssGearFlipper.doRobotPeriodic();
        	kssAutonomous.doRobotPeriodic();
//        	kssDancingLeds.doRobotPeriodic();
        	kssIntake.doRobotPeriodic();
//        	kssArm.doRobotPeriodic();
//        	kssWhiskers.doRobotPeriodic();			
		}

		public static void doDisabledPeriodic() {
        	kssBattery.doDisabledPeriodic();
        	kssRobotDrive.doDisabledPeriodic();
        	kssDrvGearShift.doDisabledPeriodic(); 
        	kssCameraLeds.doDisabledPeriodic();
        	kssCameras.doDisabledPeriodic();
        	kssClimber.doDisabledPeriodic();
        	kssShooter.doDisabledPeriodic();
        	kssAutonomous.doDisabledPeriodic();
        	kssGearFlipper.doDisabledPeriodic();
//        	kssDancingLeds.doDisabledPeriodic();
        	kssIntake.doDisabledPeriodic();
//        	kssArm.doDisabledPeriodic();
//        	kssWhiskers.doDisabledPeriodic();
		}

		public static void doAutonomousPeriodic() {
        	kssBattery.doAutonomousPeriodic();
        	kssRobotDrive.doAutonomousPeriodic();
        	kssDrvGearShift.doAutonomousPeriodic();
        	kssCameraLeds.doAutonomousPeriodic();
        	kssCameras.doAutonomousPeriodic();
        	kssClimber.doAutonomousPeriodic();
        	kssShooter.doAutonomousPeriodic();
        	kssGearFlipper.doAutonomousPeriodic();
        	kssAutonomous.doAutonomousPeriodic();
//        	kssDancingLeds.doAutonomousPeriodic();
        	kssIntake.doAutonomousPeriodic();
//        	kssArm.doAutonomousPeriodic();
//        	kssWhiskers.doAutonomousPeriodic();
		}

		public static void doTeleopPeriodic() {
        	kssBattery.doTeleopPeriodic();
        	kssRobotDrive.doTeleopPeriodic();
        	kssDrvGearShift.doTeleopPeriodic();
        	kssCameraLeds.doTeleopPeriodic();
        	kssCameras.doTeleopPeriodic();
        	kssClimber.doTeleopPeriodic();
        	kssShooter.doTeleopPeriodic();
        	kssGearFlipper.doTeleopPeriodic();
        	kssAutonomous.doTeleopPeriodic();
//        	kssDancingLeds.doTeleopPeriodic();
        	kssIntake.doTeleopPeriodic();
//        	kssArm.doTeleopPeriodic();
//        	kssWhiskers.doTeleopPeriodic();
		}

		public static void doLwTestPeriodic() {
        	kssBattery.doLwTestPeriodic();
        	kssRobotDrive.doLwTestPeriodic();
        	kssDrvGearShift.doLwTestPeriodic();
        	kssCameraLeds.doLwTestPeriodic();
        	kssCameras.doLwTestPeriodic();
        	kssClimber.doLwTestPeriodic();
        	kssShooter.doLwTestPeriodic();
        	kssGearFlipper.doLwTestPeriodic();
        	kssAutonomous.doLwTestPeriodic();
//        	kssDancingLeds.doLwTestPeriodic();
        	kssIntake.doLwTestPeriodic();
//        	kssArm.doLwTestPeriodic();
//        	kssWhiskers.doLwTestPeriodic();
		}
   }

    public static Timer m_robotTime = new Timer();
    public static double getTimeSinceBoot() { return m_robotTime.get(); }
    public static String getTimeSinceBootString() {
    	return String.format("%1.4f sec. since robotInit()", m_robotTime.get());
    }
    
    private InitVsPeriodicTimeInfo m_robotInitTimings = new InitVsPeriodicTimeInfo();
    private InitVsPeriodicTimeInfo m_disabledTimings = new InitVsPeriodicTimeInfo();
    private InitVsPeriodicTimeInfo m_autonomousTimings = new InitVsPeriodicTimeInfo();
    private InitVsPeriodicTimeInfo m_teleopTimings = new InitVsPeriodicTimeInfo();
    
//  DriverStation m_ds = DriverStation.getInstance();
//  private TmDriverStation m_tmDrvSta = new TmDriverStation();
    
    //ensure these are fully instantiated before calling other code
    private TmHdwrRoPhys m_roPhys = TmHdwrRoPhys.getInstance();
    private TmHdwrRoMap m_roMap = TmHdwrRoMap.getInstance();
    private TmHdwrDsPhys m_dsPhys = TmHdwrDsPhys.getInstance();
    private TmHdwrDsMap m_dsMap = TmHdwrDsMap.getInstance();
    
    private TmDriverStation m_tmDrvSta = TmDriverStation.getInstance();

    public static String getBuildInfoToShow() {
    	String ans = String.format("  code built: %s\n  code project: %s", 
    			TmVersionInfo.getDateTimeHostString(), TmVersionInfo.getProjectName());
    	return ans;
    }
    
    public static void showDsState() {
    	String msg = DriverStation.getInstance().isDisabled() ? "Dis" : "Ena";
		msg += DriverStation.getInstance().isAutonomous() ? "A" :
			   DriverStation.getInstance().isOperatorControl() ? "T" :
			   DriverStation.getInstance().isTest() ? "L" : "?";
    	TmPostToSd.dbgPutString(SdKeysE.KEY_DRVSTA_STATE, msg);
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	m_robotTime.start();
    	m_robotInitTimings.enterInit();
    	System.out.println("====TEAM 744 ROBOT - entered robotInit() at time " + 
    					getTimeSinceBootString() + "!");
    	System.out.println("-----code built " + TmVersionInfo.getDateTimeHostString());
    	System.out.println("-----code project " + TmVersionInfo.getProjectName());
    	System.out.flush();
    	System.out.println("Actual team number is: " + m_tmDrvSta.getTeamNumber());
    	System.out.flush();
    		
    	// Operator I/F must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.

    	//instantiate routines allow for low level initialization before
    	//various RobotInit methods are called.
    	Kss.doGetInstance();
    	Kss.doInstantiate();
    	
    	//operator interface instantiation requires all subsystems to have already
    	//been instantiated.
    	TmOpIf.doInstantiate();
    	
    	//make sure that we've configured all our motor enum values before 
    	//subsystems try to make use of them
    	TmCfgMotors.getInstance().doRoboInit();
        Kss.doRoboInit();

        //everything else needs to have been instantiated and initialized before we create commands
        TmOpIf.getInstance().createCommands();
        
    	Tm17Opts.postToSdOptions();
    	System.out.println("====Team 744 Robot - exited robotInit() at time " + 
				getTimeSinceBootString() + "!");
    	m_robotInitTimings.exitInit();
    	showDsState();
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
		System.out.println("====TEAM 744 ROBOT - entered disabledInit()!");
		m_robotInitTimings.enterPeriodic();
    	m_robotInitTimings.exitPeriodic();
    	m_disabledTimings.enterInit();
    	
//    	Tm17DbgTk.doDisabledInit();
//    	TmSdDbgSD.doDisabledInit();
    	
    	Kss.doDisabledInit();
    	Tm17Opts.postToSdOptions();
    	
    	m_disabledTimings.exitInit();
    	
    	P.println(F.ALWAYS, "robotInit timings: " + m_robotInitTimings.getMinMaxInfoString());
    	P.println(F.ALWAYS, "Disabled timings: " + m_disabledTimings.getMinMaxInfoString());
    	P.println(F.ALWAYS, "Autonomous timings: " + m_autonomousTimings.getMinMaxInfoString());
    	P.println(F.ALWAYS, "Teleop timings: " + m_teleopTimings.getMinMaxInfoString());
    	showDsState();
    }

	public void robotPeriodic() {
        Kss.doRobotPeriodic();
        showDsState();
	}

	private boolean m_dsIoToStringFullInitComplete = false;
	public void disabledPeriodic() {
		m_disabledTimings.enterPeriodic();
        Kss.doDisabledPeriodic();
        
        if( ! m_dsIoToStringFullInitComplete) { m_dsIoToStringFullInitComplete = TmHdwrDsMap.setupToStringFullBitByBit(); }

    	Scheduler.getInstance().run();
    	m_disabledTimings.exitPeriodic();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
    	m_autonomousTimings.enterInit();
    	System.out.println("====TEAM 744 ROBOT - entered autonomousInit()!");

//    	Tm17DbgTk.doAutonomousInit();
//    	TmSdDbgSD.doAutonomousInit();
    	
    	Kss.doAutonomousInit();
    	Tm17Opts.postToSdOptions();
    	m_autonomousTimings.exitInit();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	m_autonomousTimings.enterPeriodic();
        Kss.doAutonomousPeriodic();

    	Scheduler.getInstance().run();
    	m_autonomousTimings.exitPeriodic();
    }

    public void teleopInit() {
    	m_teleopTimings.enterInit();
    	System.out.println("====TEAM 744 ROBOT - entered teleopInit()!");

//    	Tm17DbgTk.doTeleopInit();
//    	TmSdDbgSD.doTeleopInit();
    	
        Kss.doTeleopInit();
    	Tm17Opts.postToSdOptions();
    	m_teleopTimings.exitInit();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	m_teleopTimings.enterPeriodic();
        Kss.doTeleopPeriodic();

    	Scheduler.getInstance().run();
    	m_teleopTimings.exitPeriodic();
    }
    
    /**
     * This function is called once when Live Window testing begins
     */
    public void testInit() {
    	System.out.println("====TEAM 744 ROBOT - entered Live Window testInit()!");
//        Kss.doLwTestInit();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
//        Kss.doLwTestPeriodic();

    	LiveWindow.run();
    }
    
    private class InitVsPeriodicTimeInfo {
    	double timeInitEntered;
    	double timeInitExited;
    	double timePeriodicEntered;
    	double timePeriodicExited;
    	double minTimeInInit;
    	double maxTimeInInit;
    	double minTimeInitToPeriodic;
    	double maxTimeInitToPeriodic;
    	double minTimeInPeriodic;
    	double maxTimeInPeriodic;
    	boolean firstTimeInPeriodicAfterInit;
    	InitVsPeriodicTimeInfo() { 
    		timeInitEntered = 0;
    		timeInitExited = 0;
    		timePeriodicEntered = 0;
    		timePeriodicExited = 0;
    		minTimeInInit = 9999;
        	maxTimeInInit = 0;
        	minTimeInitToPeriodic = 9999;
        	maxTimeInitToPeriodic = 0;
        	minTimeInPeriodic = 9999;
        	maxTimeInPeriodic = 0;
    		firstTimeInPeriodicAfterInit = true;
    	}
    	
    	public String getMinMaxInfoString() {
    		return String.format("(min/max seconds) init: [%1.5f, %1.5f] between: [%1.5f, %1.5f] " + 
    						"periodic: [%1.5f, %1.5f]",
    						minTimeInInit, maxTimeInInit, minTimeInitToPeriodic, maxTimeInitToPeriodic,
    						minTimeInPeriodic, maxTimeInPeriodic);
    	}
    	
    	public void enterInit() {
    		timeInitEntered = getTimeSinceBoot();
    		timeInitExited = 0;
    		timePeriodicEntered = 0;
    		timePeriodicExited = 0;
    		firstTimeInPeriodicAfterInit = true;
    	}
    	
    	public void exitInit() {
    		timeInitExited = getTimeSinceBoot();
    		double delta = timeInitExited - timeInitEntered;
    		if(delta < minTimeInInit) { minTimeInInit = delta; }
    		else if(delta > maxTimeInInit) { maxTimeInInit = delta; }
    		else {}
    		timePeriodicEntered = 0;
    		timePeriodicExited = 0;
    		firstTimeInPeriodicAfterInit = true;
    	}
    	
    	public void enterPeriodic() {
    		timePeriodicEntered = getTimeSinceBoot();
    		if(firstTimeInPeriodicAfterInit) {
    			timePeriodicExited = 0;
    			double delta = timePeriodicEntered - timeInitExited;
    			if(delta < minTimeInitToPeriodic) { minTimeInitToPeriodic = delta; }
    			else if(delta > maxTimeInitToPeriodic) { maxTimeInitToPeriodic = delta; }
    			else {}
    		}
			firstTimeInPeriodicAfterInit = false;
    	}

    	public void exitPeriodic() {
    		timePeriodicExited = getTimeSinceBoot();
    		double delta = timePeriodicExited - timePeriodicEntered;
    		if(delta < minTimeInPeriodic) { minTimeInPeriodic = delta; }
    		else if(delta > maxTimeInPeriodic) { maxTimeInPeriodic = delta; }
    		else {}
    	}
    }
    
}
