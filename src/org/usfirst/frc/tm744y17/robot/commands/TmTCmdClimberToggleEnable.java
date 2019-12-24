/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.tm744y17.robot.commands;


import org.usfirst.frc.tm744y17.robot.subsystems.TmSsClimber;

import edu.wpi.first.wpilibj.command.Command;


/**
 * naming convention: TmACmd prefix indicates an autonomous command
 * 					  TmCCmd prefix indicates a common (to auton/teleop) command
 * 					  TmTCmd prefix indicates a telelop command
 */
/**
 * Enable/Disable climber subsystem
 * @author robotics
 */
public class TmTCmdClimberToggleEnable extends Command {

//	private static TmSsClimber ssClimber;
	
	public TmTCmdClimberToggleEnable()
    {
//		ssClimber = TmSsClimber.getInstance();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//        requires(ssClimber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	TmSsClimber.toggleClimberEnable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
//    	ssClimber.toggleClimberEnable();
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
