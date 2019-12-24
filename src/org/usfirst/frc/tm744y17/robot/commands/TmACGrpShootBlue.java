package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;


/**
 *measures 89.5" from the "center line
 *(nearest bumper should be that distance from center line)
 */
public class TmACGrpShootBlue extends CommandGroup {
	public TmACGrpShootBlue() {
		addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
		addSequential(new TmACmdDriveStraightEndWithGyro(3.785,3));  //Drive forward to shooter point
		addSequential(new TmCCmdDelay(0.05));
		
		addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE,40));  //turn to face boiler
		addParallel(new TmACGrpHelperStartShooterDrum(0.060)); //start shooter drum early so it has time to get up to speed
		addSequential(new WaitForChildren()); //wait for both spin and shooter drum
		
		addSequential(new TmCCmdShooterStart());
		addSequential(new TmCCmdDelay(7.5));
		addSequential(new TmCCmdShooterStop());
		addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(6.208,6)); //Drive toward the airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 18));  //turn to be perpendicular with airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(1.4,1));  //Drive to place gear
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0));  //Backing up to close flippers
    	addSequential(new TmCCmdGearFlippersRetract());
	}	
}