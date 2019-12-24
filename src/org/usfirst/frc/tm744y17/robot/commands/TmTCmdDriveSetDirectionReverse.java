package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.DriveTrainDirectionE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmTCmdDriveSetDirectionReverse extends Command {
	
	TmSsDriveTrain ssDrive;

    public TmTCmdDriveSetDirectionReverse() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	ssDrive = TmSsDriveTrain.getInstance();
//    	requires(ssDrive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	ssDrive.setDriveDirection(DriveTrainDirectionE.REVERSE__SHOOTER_LEADS);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
