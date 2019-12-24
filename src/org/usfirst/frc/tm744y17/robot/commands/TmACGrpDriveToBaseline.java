package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TmACGrpDriveToBaseline extends CommandGroup {

    public TmACGrpDriveToBaseline() {
    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
    	addSequential(new TmACmdDriveStraightEndWithGyro(8.0)); //TmCCmdDriveForwardDistance(1.0, 1.0, 8.0));
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
    //	addS
    }
}
