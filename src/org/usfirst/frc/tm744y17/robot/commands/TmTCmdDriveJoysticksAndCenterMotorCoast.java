package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveJoysticksCenterMotorOptE;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmTCmdDriveJoysticksAndCenterMotorCoast extends Command {
	
	TmSsDriveTrain ssDrive;
	DriveJoysticksCenterMotorOptE m_centerMotorOpt;

    public TmTCmdDriveJoysticksAndCenterMotorCoast(DriveJoysticksCenterMotorOptE centerMotorOpt) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	ssDrive = TmSsDriveTrain.getInstance();
    	requires(ssDrive);
    	m_centerMotorOpt = centerMotorOpt;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(m_centerMotorOpt.equals(DriveJoysticksCenterMotorOptE.DRIVE_JOYSTICKS_CENTER_MOTOR_COAST)) {
    		ssDrive.startDriveWithJoysticksCenterMotorCoast();
    	} else {
    		ssDrive.stopDriveWithJoysticksCenterMotorCoast();
    	}
//    	ssDrive.getChassisInstance().s //.setDriveDirection(DriveTrainDirectionE.FORWARD_SHOOTER_LEADS);
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
