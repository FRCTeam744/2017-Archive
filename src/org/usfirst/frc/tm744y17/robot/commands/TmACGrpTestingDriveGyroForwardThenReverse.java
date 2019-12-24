package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TmACGrpTestingDriveGyroForwardThenReverse extends CommandGroup {
	
	private static final double DEFAULT_DISTANCE = 12.0;
	private static final TmSsDrvGearShift.DrvGearsE DEFAULT_GEAR = TmSsDrvGearShift.DrvGearsE.LOW;
	
    public TmACGrpTestingDriveGyroForwardThenReverse() {
    	this(DEFAULT_DISTANCE, DEFAULT_GEAR);
    }
    public TmACGrpTestingDriveGyroForwardThenReverse(double distance) {
    	this(distance, DEFAULT_GEAR);
    }
    public TmACGrpTestingDriveGyroForwardThenReverse(double distance, TmSsDrvGearShift.DrvGearsE gear) {
    	addSequential(new TmCCmdDriveShiftGear(gear));
    	addSequential(new TmACmdDriveStraightEndWithGyro(distance));
    	addSequential(new TmCCmdDelay(0.5));
    	addSequential(new TmACmdDriveStraightEndWithGyro( - distance));
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
