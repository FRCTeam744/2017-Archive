package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 *
 */
public class TmACGrpShootAndPlaceGearBlue extends CommandGroup {

    public TmACGrpShootAndPlaceGearBlue() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	//measures 89.5" from the center line
    	//(nearest bumper should be that distance from center line)
    	
		addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
		addSequential(new TmACmdDriveStraightEndWithGyro(3.785,3));  //Drive forward to shooting point
		addSequential(new TmCCmdDelay(0.05));
		
		addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE,40));  // Turn towards boiler
		addParallel(new TmACGrpHelperStartShooterDrum(0.060)); //start shooter drum early so it has time to get up to speed
		addSequential(new WaitForChildren()); //wait for both spin and shooter drum
		
		addSequential(new TmCCmdShooterStart());
		addSequential(new TmCCmdDelay(3.0));
		addSequential(new TmCCmdShooterStop());
		addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(6.208,6)); //Drive towards airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 18));  //Turn to be perpendicular with airship
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmACmdDriveStraightEndWithGyro(1.4,1)); //Move forward to place gear
        addSequential(new TmCCmdDelay(0.05));
        addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0));  //Back away to close flippers
    	addSequential(new TmCCmdGearFlippersRetract());
    }
}
