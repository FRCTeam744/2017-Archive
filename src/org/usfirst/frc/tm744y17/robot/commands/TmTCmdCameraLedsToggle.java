/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.tm744y17.robot.commands;


import org.usfirst.frc.tm744y17.robot.subsystems.TmSsCameraLeds;

import edu.wpi.first.wpilibj.command.Command;


/**
 * naming convention: TmACmd prefix indicates an autonomous command
 * 					  TmCCmd prefix indicates a common (to auton/teleop) command
 * 					  TmTCmd prefix indicates a telelop command
 */
/**
 * Turn the ring of LEDs on the camera off or on
 * @author robotics
 */
public class TmTCmdCameraLedsToggle extends Command {

	private static TmSsCameraLeds cb_cameraLeds;
	
	public TmTCmdCameraLedsToggle()
    {
    	cb_cameraLeds = TmSsCameraLeds.getInstance();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(cb_cameraLeds);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
        cb_cameraLeds.toggleLights();
        //toggleLights() already does this; TmDbgTk.printIt(-1, "toggling camera LEDs");
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
