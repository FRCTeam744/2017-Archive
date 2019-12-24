package org.usfirst.frc.tm744y17.robot.commands;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsBallIntake;

import edu.wpi.first.wpilibj.command.Command;
public class TmCCmdIntakeOff extends Command{
	
	TmSsBallIntake ssIntake;
	
 	public TmCCmdIntakeOff() {	
    	ssIntake = TmSsBallIntake.getInstance();
    	requires(ssIntake);
    }

 	public void initialize() {	
    	P.println(Tt.extractClassName(this) + " initializing");
    }

 	// Called repeatedly when this Command is scheduled to run
 	public void execute() {
 		ssIntake.turnMotorsOff();
 	}

 	// Make this return true when this Command no longer needs to run execute()
 	public boolean isFinished() {
 		return true;
 	}

 	// Called once after isFinished returns true
 	public void end() {
 	}

 	// Called when another command which requires one or more of the same
 	// subsystems is scheduled to run
 	public void interrupted() {
 	}
}
