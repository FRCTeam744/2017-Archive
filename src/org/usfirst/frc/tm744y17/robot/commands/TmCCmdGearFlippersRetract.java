package org.usfirst.frc.tm744y17.robot.commands;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsGearFlipper;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsGearFlipper.FlipperPositionE;

import edu.wpi.first.wpilibj.command.Command;
public class TmCCmdGearFlippersRetract extends Command{
	
	TmSsGearFlipper ssFlipper;
	
 	public TmCCmdGearFlippersRetract() {	
 		ssFlipper = TmSsGearFlipper.getInstance();
    	requires(ssFlipper);
    }

 	public void initialize() {	
    	P.println(Tt.extractClassName(this) + " initializing");
    }

 	// Called repeatedly when this Command is scheduled to run
 	public void execute() {
 		ssFlipper.setPosition(FlipperPositionE.RETRACTED);
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
