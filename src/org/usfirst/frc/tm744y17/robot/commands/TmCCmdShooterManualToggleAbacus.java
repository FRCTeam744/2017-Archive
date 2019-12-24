package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsShooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmCCmdShooterManualToggleAbacus extends Command {
	private TmSsShooter ssShooter;
    public TmCCmdShooterManualToggleAbacus() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	ssShooter = TmSsShooter.getInstance();
    	requires(ssShooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P.println(-1, Tt.extractClassName(this) + " initializing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	ssShooter.manualToggleAbacus();
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
