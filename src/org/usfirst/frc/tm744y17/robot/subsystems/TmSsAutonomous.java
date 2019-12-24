package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.commands.TmACGrpCenterGear;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpCenterGearAndShootBlue;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpCenterGearAndShootRed;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpCfgEngageLowGear;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpDriveToBaseline;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpLeftSideGearAndShootBlue;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpLeftSideGearMidfieldRed;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpRightSideGearMidfieldBlue;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpRightSideGearRedAndShoot;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpShootAndPlaceGearBlue;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpShootAndPlaceGearRed;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpShootBlue;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpShootIntoFuelStorage;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpShootRed;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpTestingDriveEncodersForwardThenReverse;
import org.usfirst.frc.tm744y17.robot.commands.TmACGrpTestingDriveGyroForwardThenReverse;
import org.usfirst.frc.tm744y17.robot.commands.TmACmdDoNothing;
import org.usfirst.frc.tm744y17.robot.commands.TmACmdDriveStraightEndWithGyro;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdDriveTimed;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdDriveHalfSpeedFor30Sec;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TmSsAutonomous implements TmStdSubsystemI, TmToolsI {

	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsAutonomous m_instance;

	public static synchronized TmSsAutonomous getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsAutonomous();
		}
		return m_instance;
	}

	private TmSsAutonomous() {
	}
	/*----------------end of getInstance stuff----------------*/


//	class tempSubSysEntryPts implements TmStdSubsystemI{
	@Override
	public void doInstantiate() {
		selectedAlg = AutonomousAlgE.SELECTED_ALG_NOT_FOUND;
	}

	@Override
	public void doRoboInit() {
		//too soon: showAlgInfo();
		setUpSendable(); //wait and make sure we're talking to the dashboard first??
	}

	private static boolean algInfoDisplayed = false;
	@Override
	public void doDisabledInit() {
		setUpSendable();
		selectedAlg = getSelectedAlg();
		if( ! selectedAlg.equals(AutonomousAlgE.SELECTED_ALG_NOT_FOUND)) {
			selectedAlg.show("DisabledInit - SELECTED ALG: ");
		}
		if( ! algInfoDisplayed) { showAlgInfo(); algInfoDisplayed = true; }
		cancelCmdIfRunning();
	}

	@Override
	public void doAutonomousInit() {
		setUpSendable();
		//showAlgInfo();
		selectedAlg = getSelectedAlg();
		selectedAlg.show("AutonInit - SELECTED ALG: ");
		if(selectedAlg.getCommand() != null) {
			selectedAlg.getCommand().start();
		}
	}

	@Override
	public void doTeleopInit() {
		cancelCmdIfRunning();
		setUpSendable();
		//showAlgInfo();
		selectedAlg = getSelectedAlg(); //done to try to get SD updated
	}

	@Override
	public void doLwTestInit() {
		cancelCmdIfRunning();
	}

	@Override
	public void doRobotPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDisabledPeriodic() {
		// TODO Auto-generated method stub
		SmartDashboard.putString(TmMiscSdKeys.SdKeysE.KEY_AUTON_ALG_NBR_STRING.getKey(), getSelectedAlg().getAlgNbr()); //("" + selectedAlgNbr)); //item.getAlgNbr());
	}

	@Override
	public void doAutonomousPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doTeleopPeriodic() {
		SmartDashboard.putString(TmMiscSdKeys.SdKeysE.KEY_AUTON_ALG_NBR_STRING.getKey(), getSelectedAlg().getAlgNbr()); //("" + selectedAlgNbr)); //item.getAlgNbr());
	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub

	}
//}
	/*-------------------------------------------------------------
	 *      misc. service routine(s)
	 *-------------------------------------------------------------*/

	/**
	 * if the command selected on SD is running, cancel it
	 */
	private void cancelCmdIfRunning() {
		if(selectedAlg.getCommand() != null) {
			if(selectedAlg.getCommand().isRunning()) {
				selectedAlg.getCommand().cancel();
			}
		}
	}

	/*-------------------------------------------------------------
	 *      autonomous algorithm select SmartDashboard support 
	 *-------------------------------------------------------------*/

	//    private class AAlgSd {
	public static SendableChooser<Command> autoChooser = null;

	Command selectedCmdRaw = null; //used only for debugging 'chooser' problems
	AutonomousAlgE selectedAlg = null; //AutonomousAlgE.SELECTED_ALG_NOT_FOUND;
	
	/**
	 * represents autonomous algorithm coding status. Provides a string to be
	 * displayed on SmartDashboard to indicate the status of the item in the
	 * 'chooser'.
	 */
	public enum AutonAlgStatusE {
		kReady("OK"),
		kTest("Test"),
		kCoded("Coded"),
		kTBD("TBD"),
		kUnused("n/a"),
		kBetaTest("Beta"),
		kGood("GOOD"),
		kBad("BAD!");

		/**
		 * this is the string to be displayed on SmartDashboard for algorithms
		 * tagged with this status type 
		 */
		public String descr;
		public String descrRaw;

		private AutonAlgStatusE(String smartDashDescrString) {
			descrRaw = smartDashDescrString;
			//'-' means left-justify, 5 is the width, s is String
			descr = String.format("%-5s", descrRaw);
		}
		
		public String getSmartDashDescription() { return descr; }
	}
	
	public enum AutonAlgAvailableE {
		ALWAYS, RED, BLUE, R1, R2, R3, B1, B2, B3;
		
		public boolean isAvailable() {
			boolean ans = false;
			switch(this) {
			case ALWAYS: 
				ans = true;
				break;
			case RED:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Red)) {
					ans = true;
				}
				break;
			case BLUE:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue)) {
					ans = true;
				}
				break;
			case R1:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Red) &&
						DriverStation.getInstance().getLocation() == 1) {
					ans = true;
				}
				break;
			case R2:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Red) &&
						DriverStation.getInstance().getLocation() == 2) {
					ans = true;
				}
				break;
			case R3:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Red) &&
						DriverStation.getInstance().getLocation() == 3) {
					ans = true;
				}
				break;
			case B1:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) &&
						DriverStation.getInstance().getLocation() == 1) {
					ans = true;
				}
				break;
			case B2:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) &&
						DriverStation.getInstance().getLocation() == 2) {
					ans = true;
				}
				break;
			case B3:
				if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) &&
						DriverStation.getInstance().getLocation() == 3) {
					ans = true;
				}
				break;
			default: ans = true; break;
			}
			return ans;
		}
	}

	
	/** if we had hardware switches, this is where we'd same the individual
	 *  settings and save related info. See 2014/2015 code for example */
	public enum AutonSwitchAlgsE {
		//if switches are available, one of the possible settings
		//should be assigned to mean "use smart dashboard". This
		//enum should be associated with that setting and should
		//also be used if there are no switches on the robot
		AUTON_SWITCHES_ALG_SMARTDASHBOARD;
	}
	static AutonSwitchAlgsE autonAlg = AutonSwitchAlgsE.AUTON_SWITCHES_ALG_SMARTDASHBOARD;

	/*------------------------------------------------------------
	 * list various autonomous algorithms and related info        
	 *------------------------------------------------------------*/

	/**
	 * this enum holds information about each autonomous algorithm and is used
	 * to set up the "chooser" on the smartdashboard
	 * @author JudiA
	 *
	 */
	public enum AutonomousAlgE { //CONFIG_ME
		ALG_DO_NOTHING("Do Nothing", 
				new TmACmdDoNothing(),	AutonAlgStatusE.kGood),
		
		ALG_CFG_ENGAGE_LOW_GEAR("For Pit use: Drive just enough to engage low gear", 
				new TmACGrpCfgEngageLowGear(),	AutonAlgStatusE.kGood),
		ALG_PLACE_CENTER_GEAR("Place Center gear", 
				new TmACGrpCenterGear(), AutonAlgStatusE.kGood),
		ALG_PLACE_RIGHT_SIDE_GEAR_BLUE("Place side gear Right - Station 3 (TURN LEFT)", //"Place side gear right Red or left Blue" changed for driver clarification
				new TmACGrpRightSideGearMidfieldBlue(), AutonAlgAvailableE.BLUE, AutonAlgStatusE.kGood),
		ALG_PLACE_LEFT_SIDE_GEAR_RED("Place side gear Left - Station 1 (TURN RIGHT)", 
				new TmACGrpLeftSideGearMidfieldRed(), AutonAlgAvailableE.RED, AutonAlgStatusE.kGood),
		ALG_PLACE_LEFT_SIDE_GEAR_BLUE_AND_SHOOT("Place side gear & SHOOT Right BLUE - Station 1 (TURN RIGHT)",  //"Place side gear left Red or Right Blue" changed for driver clarification
				new TmACGrpLeftSideGearAndShootBlue(), AutonAlgAvailableE.BLUE, AutonAlgStatusE.kGood),
		ALG_PLACE_RIGHT_SIDE_GEAR_RED_AND_SHOOT("Place side gear & SHOOT Left RED - Station 3 (TURN LEFT)", 
				new TmACGrpRightSideGearRedAndShoot(), AutonAlgAvailableE.RED, AutonAlgStatusE.kGood),
		ALG_SHOOT_THEN_PLACE_GEAR_RED("Shoot then place gear red (Use when there are 1 or 3 gear autos)", new TmACGrpShootAndPlaceGearRed(),
				AutonAlgAvailableE.RED, AutonAlgStatusE.kCoded),
		ALG_SHOOT_THEN_PLACE_GEAR_BLUE("Shoot then place gear blue (Use when there are 1 or 3 gear autos)", new TmACGrpShootAndPlaceGearBlue(), 
				AutonAlgAvailableE.BLUE, AutonAlgStatusE.kCoded),
		//ALG_PLACE_LEFT_SIDE_GEAR_GET_BALLS_AND_LINE_UP_RED("Place side gear, get balls, and line up Red - Station 3", new TmACGrpLeftSideGearBucketandLineUpRed(),AutonAlgStatusE.kCoded),
		//ALG_PLACE_LEFT_SIDE_GEAR_GET_BALLS_AND_LINE_UP_BLUE("Place side gear, get balls, and line up Blue - Station 3", new TmACGrpLeftSideGearBucketandLineUpBlue(), AutonAlgStatusE.kCoded),
		//ALG_GET_BALLS_RIGHT_AND_LINE_UP_RED("Get balls on right side and line up Red",new TmACGrpBucketAndLineUpRightRed(),AutonAlgStatusE.kCoded),
		//ALG_GET_BALLS_RIGHT_AND_LINE_UP_BLUE("Get balls on right side and line up Blue",new TmACGrpBucketAndLineUpRightBlue(),AutonAlgStatusE.kCoded),
		//ALG_GET_BALLS_LEFT_AND_LINE_UP_RED("Get balls on left side and line up Red",new TmACGrpBucketAndLineUpLeftRed(),AutonAlgStatusE.kCoded),
		//ALG_GET_BALLS_LEFT_AND_LINE_UP_BLUE("Get balls on left side and line up Blue",new TmACGrpBucketAndLineUpLeftBlue(),AutonAlgStatusE.kCoded),
		ALG_PLACE_CENTER_GEAR_AND_SHOOT_RED("Place center gear and shoot Red",new TmACGrpCenterGearAndShootRed(), AutonAlgAvailableE.RED, AutonAlgStatusE.kBetaTest),
		ALG_PLACE_CENTER_GEAR_AND_SHOOT_BLUE("Place center gear and shoot Blue",new TmACGrpCenterGearAndShootBlue(), AutonAlgAvailableE.BLUE, AutonAlgStatusE.kBetaTest),
		ALG_SHOOT_BALLS_INTO_BOILER_RED("Shoot Ball into Red Boiler (Use when there are 2 gear autos",
				new TmACGrpShootRed(), AutonAlgStatusE.kCoded),
		ALG_SHOOT_BALLS_INTO_BOILER_BLUE("Shoot Ball into Blue Boiler (Use when there are 2 gear autos)",new TmACGrpShootBlue(), AutonAlgStatusE.kCoded),
		//ALG_PLACE_CENTER_GEAR_TIMED("Place Center Gear Timed",
				//new TmACGrpCenterGearTime(), AutonAlgStatusE.kCoded),
		ALG_DRIVE_STRAIGHT_WITH_GYRO("Testing: Drive Straight With Gyro",
				new TmACmdDriveStraightEndWithGyro(12), AutonAlgStatusE.kBetaTest),
		ALG_DRIVE_STRAIGHT_WITH_GYRO_REVERSE("Testing: Drive Straight With Gyro Reverse",
				new TmACmdDriveStraightEndWithGyro(-12), AutonAlgStatusE.kBetaTest),
		ALG_TEST_GYRO("Testing gyro at half speed for 30 sec",new TmTCmdDriveHalfSpeedFor30Sec(),AutonAlgStatusE.kCoded),
		
		ALG_TEST_DRIVE_GYRO_FORWARD_AND_REVERSE_LOW("Testing: Drive Straight forward then reverse (12ft, low gear)",
				new TmACGrpTestingDriveGyroForwardThenReverse(12.0, TmSsDrvGearShift.DrvGearsE.LOW), AutonAlgStatusE.kGood),
//				new TmACGrpTestingDriveEncodersForwardThenReverse(12.0, TmSsDrvGearShift.DrvGearsE.LOW), AutonAlgStatusE.kBad),
		
//		ALG_TEST_DRIVE_GYRO_FORWARD_AND_REVERSE_HIGH("Testing: Drive Straight forward then reverse (12ft, high gear)",
//				new TmACGrpTestingDriveGyroForwardThenReverse(12.0, TmSsDrvGearShift.DrvGearsE.HIGH), AutonAlgStatusE.kBad),
		ALG_TEST_DRIVE_TUNING_PARMS_FORWARD("Testing: Drive Forward Straight approx. 20ft",
				new TmCCmdDriveTimed(1.0, 1.0, CenterDriveMotorsBehaviorE.MATCH_FRONT_AND_REAR, 3.0), AutonAlgStatusE.kCoded),
		ALG_TEST_DRIVE_TUNING_PARMS_REVERSE("Testing: Drive Backwards Straight approx. 20ft",
				new TmCCmdDriveTimed(-1.0, -1.0, CenterDriveMotorsBehaviorE.MATCH_FRONT_AND_REAR, 3.0), AutonAlgStatusE.kCoded),
		//ALG_ALEX_SHOOTING_RED("Alex's shooting algorithm on Red - Station 3", new TmACGrpShootBlue(), AutonAlgStatusE.kCoded),
		//ALG_ALEX_SHOOTING_BLUE("Alex's shooting algorithm on Blue - Station 3", new TmACGrpShootRed(),AutonAlgStatusE.kCoded),
		//ALG_ALEX_CENTER_GEAR_THEN_MIDFIELD_RED("Alex's Center Gear then to Midfield - Red", new TmACGrpCenterGearThenToMidfieldRed(), AutonAlgStatusE.kCoded),
		//ALG_ALEX_CENTER_GEAR_THEN_MIDFIELD_BLUE("Alex's Center Gear then to Midfield - Blue", new TmACGrpCenterGearThenToMidfieldBlue(),AutonAlgStatusE.kCoded),
		ALG_DRIVE_TO_BASELINE("Only crosses baseline",
				new TmACGrpDriveToBaseline(),AutonAlgStatusE.kGood),
		SELECTED_ALG_NOT_FOUND("(alg not found - do nothing)", 
				new TmACmdDoNothing(), AutonAlgStatusE.kUnused)
		;
				
		private final AutonSwitchAlgsE eHwSwitchInfo;
		private final String eCmdDescription;
		private final Command eCmdToRun;
		private final AutonAlgStatusE eAlgStatus;
		private final int eNdx;
		private final String eNdxString;
		private final String eSmartDashboardString;
		private final String eShowString;
		private final AutonAlgAvailableE eWhenAvailable;
		
		private AutonomousAlgE(String cmdDescription, Command cmdToRun, 
				AutonAlgStatusE algStatus) {
			this(AutonSwitchAlgsE.AUTON_SWITCHES_ALG_SMARTDASHBOARD, cmdDescription,
					cmdToRun, AutonAlgAvailableE.ALWAYS, algStatus);
		}
		private AutonomousAlgE(String cmdDescription, Command cmdToRun, AutonAlgAvailableE available, 
				AutonAlgStatusE algStatus) {
			this(AutonSwitchAlgsE.AUTON_SWITCHES_ALG_SMARTDASHBOARD, cmdDescription,
					cmdToRun, available, algStatus);
		}
		private AutonomousAlgE(AutonSwitchAlgsE autonSwitchesAlgType, String cmdDescription, 
				/*String lcdStr,*/ Command cmdToRun, AutonAlgAvailableE available,
				AutonAlgStatusE algStatus) {
			eHwSwitchInfo = autonSwitchesAlgType;
			eCmdDescription = cmdDescription;
			eCmdToRun = cmdToRun;
			eAlgStatus = algStatus;	
			eWhenAvailable = available;
			eNdx = m_ndx++;
			eNdxString = "" + eNdx;
			eSmartDashboardString = buildSmartDashString();
			eShowString = buildShowString();
		}
		public boolean isAlgAvailable() { return eWhenAvailable.isAvailable(); }
		public AutonSwitchAlgsE getHwSwitchInfo() { return eHwSwitchInfo; }
		public String getCmdDescription() { return eCmdDescription; }
		public Command getCommand() { return eCmdToRun; }
		public AutonAlgStatusE getStatus() { return eAlgStatus; }
		public String getAlgNbr() { return eNdxString; }
		public String getSmartDashString() { return eSmartDashboardString; }
		public String getShowString() { return eShowString; }
		
		public void show() { System.out.println(eSmartDashboardString); }
		public void show(String prefix) { 
			System.out.println(prefix + eShowString);
		}
		
		private String buildShowString() {
			String ans;
			//%-25s says to left-justify a string in a field 25 chars wide
			ans = String.format("%-65s - when %s - [%s]", eSmartDashboardString, this.eWhenAvailable.name(), this.eCmdToRun.toString());
			return ans;
		}

		private String buildSmartDashString() {
			String ans;
			ans = String.format("%2d - %s - %s", eNdx, eAlgStatus.getSmartDashDescription(),
											eCmdDescription );
			return ans; 
		}
	}
	
	private static int m_ndx = 0; //indexes the AutonomousAlgE enum values 

	/**
	 * configure the SmartDashboard "chooser" used to select the autonomous
	 * algorithm to run by parsing the array of algorithm definition info.
	 */
	public void setUpSendable()
	{
		AutonomousAlgE defaultAlg = AutonomousAlgE.ALG_DO_NOTHING;
		
		if(autoChooser == null) {
			autoChooser = new SendableChooser<Command>();
	
			//begin by setting the default selection
	  		autoChooser.addDefault(defaultAlg.getSmartDashString(), 
					defaultAlg.getCommand());
	  		
	  		//add remaining algorithms from enum
	  		for(AutonomousAlgE item : AutonomousAlgE.values()) {
	  			autoChooser.addObject(item.getSmartDashString(), item.getCommand());
	  		}
	
	  		//this is used to display additional info in the chooser
	  		//(use <n/a> instead of (n/a) so will show up after all numbered items (ASCII sort order))
			autoChooser.addObject("<n/a>(does nothing)(No hardware switches available)",
					new TmACmdDoNothing() );
		} 
//		else {
//			autoChooser.
//		}

		SmartDashboard.putData(TmMiscSdKeys.SdKeysE.KEY_AUTON_CHOOSER.getKey(), autoChooser);
	}
	
	public void showAlgInfo() {
		for(AutonomousAlgE item : AutonomousAlgE.values() ) {
			System.out.println(item.getShowString()); //getSmartDashString());
		}
		getSelectedAlg().show("Selected AUTON ALG: ");
	}
	
	public AutonomousAlgE getSelectedAlg() {
		AutonomousAlgE ans = AutonomousAlgE.SELECTED_ALG_NOT_FOUND;
		boolean found = false;
		
		selectedCmdRaw = (Command) autoChooser.getSelected();
		for( AutonomousAlgE item : AutonomousAlgE.values()) {
			if( ( ! found) && (item.getCommand().equals(selectedCmdRaw)) ) {
				ans = item;
				found = true;
				SmartDashboard.putString(TmMiscSdKeys.SdKeysE.KEY_AUTON_ALG_NBR_STRING.getKey(), item.getAlgNbr());
			}
		}
		if( ! found) {
			String prefix = "";
			if(selectedCmdRaw == null) {
				prefix = "Command from SD is null.";
			} else {
				prefix = "this instance of command '" + selectedCmdRaw.toString() + "'" +
						" not found in AutonomousAlgE enum.";
			}
			System.out.println(prefix + " Using SELECTED_ALG_NOT_FOUND's command instead");
		}
		return ans;
	}

}

