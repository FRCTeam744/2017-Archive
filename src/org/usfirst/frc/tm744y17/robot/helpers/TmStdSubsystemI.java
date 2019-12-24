package org.usfirst.frc.tm744y17.robot.helpers;

public interface TmStdSubsystemI {
	
	public void doInstantiate();
	
	public void doRoboInit();
	public void doDisabledInit();
	public void doAutonomousInit();
	public void doTeleopInit();
	public void doLwTestInit(); //for LiveWindow testing
	public void doRobotPeriodic();
	public void doDisabledPeriodic();
	public void doAutonomousPeriodic();
	public void doTeleopPeriodic();
	public void doLwTestPeriodic();
	
//	/** 
//	 * for most subsystems, LiveWindow can be used to test the various
//	 * actuators and sensors one-at-a-time.  Subsystems that can't
//	 * handle that should override this method.
//	 */
//	public default void doLwTestPeriodic() {
//		//do nothing
//	}; //for LiveWindow testing
	
}
