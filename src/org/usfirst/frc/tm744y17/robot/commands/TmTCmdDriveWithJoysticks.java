package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.driverStation.TmOpIf;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
*
*/
public class TmTCmdDriveWithJoysticks extends Command {

	TmSsDriveTrain ssDrive;
	DriverStation m_ds;
	CenterDriveMotorsBehaviorE m_centerMotorBehavior;
	
 public TmTCmdDriveWithJoysticks(TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE centerBehavior) {
 	m_ds = DriverStation.getInstance();
 	ssDrive = TmSsDriveTrain.getInstance();
 	m_centerMotorBehavior = centerBehavior;
    requires(ssDrive);
 }

 // Called just before this Command runs the first time
 protected void initialize() {
	 P.println(Tt.extractClassName(this) + " initializing");
// 	Tm16DbgTk.printIt(-1, "m_ds.isEnabled():" + m_ds.isEnabled() + 
// 			" m_ds.isOperatorControl():" + m_ds.isOperatorControl());
 }

 // Called repeatedly when this Command is scheduled to run
 protected void execute() {
 	if(m_ds.isEnabled() && m_ds.isOperatorControl()) {
 		ssDrive.doTankDrive(TmOpIf.getLeftDriveDsInput().getAnalog(),
 				TmOpIf.getRightDriveDsInput().getAnalog(), m_centerMotorBehavior);
 	}
 }

 // Make this return true when this Command no longer needs to run execute()
 protected boolean isFinished() {
     return false;
 }

 // Called once after isFinished returns true
 protected void end() {
	 P.println(Tt.extractClassName(this) + " ending");
 }

 // Called when another command which requires one or more of the same
 // subsystems is scheduled to run
 protected void interrupted() {
	 P.println(Tt.extractClassName(this) + " interrupted");

 }
}

