package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;


/**
 *  measures 88.5" from the edge of the carpet
 *  (nearest bumper should be that distance from center line)
 */
public class TmACGrpShootRed extends CommandGroup {
	public TmACGrpShootRed() {
		addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
		addSequential(new TmACmdDriveStraightEndWithGyro(3.785,3));  //Drive forward to shooter point
		addSequential(new TmCCmdDelay(0.05));
		
		addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE,35));  //Turn to face boiler
		addParallel(new TmACGrpHelperStartShooterDrum(0.060)); //start shooter drum early so it has time to get up to speed
		addSequential(new WaitForChildren()); //wait for both spin and shooter drum
		
		addSequential(new TmCCmdShooterStart());
		addSequential(new TmCCmdDelay(6.5));
		addSequential(new TmCCmdShooterStop());
		addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(5.458,6)); //Drive towards the airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE, 20));  //Turn to be perpendicular with airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(1.5,1));  //Drive forward to place gear
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0));  //backing up to close flippers
    	addSequential(new TmCCmdGearFlippersRetract());
	}	
}
