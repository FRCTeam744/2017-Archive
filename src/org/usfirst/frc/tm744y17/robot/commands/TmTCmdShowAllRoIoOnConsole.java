package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmTCmdShowAllRoIoOnConsole extends Command {
	public TmTCmdShowAllRoIoOnConsole() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P.println(Tt.extractClassName(this) + " running");
    	TmHdwrRoMap.showAllRoIo();
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
