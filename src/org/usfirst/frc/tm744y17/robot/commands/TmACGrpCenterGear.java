package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *
 */
public class TmACGrpCenterGear extends CommandGroup {

//	private static final double FORWARD_SPEED = 0.5;
//	private static final double REVERSE_SPEED = -0.5;
//	private static final double SPIN_SPEED = 0.5;
	
    public TmACGrpCenterGear() {
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
    	
    	//'forward' is shooter leading, gear placer is on the back of the robot 
    	//we're assuming the robot is positioned so the that gear placer is 
    	// facing the peg....
    	//direction change on motors - should work in the right directions
//    	addSequential (new TmCCmdDriveForwardDistance(FORWARD_SPEED, FORWARD_SPEED, 4.43));
    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
    	addSequential(new TmACmdDriveStraightEndWithGyro(5.95, 6.));
        addSequential(new TmCCmdDelay(.3));
    	addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmCCmdDelay(0.5));
    	addSequential (new TmACmdDriveStraightEndWithGyro(-2.0)); //TmCCmdDriveForwardDistance(REVERSE_SPEED, REVERSE_SPEED,-2.0, 2.5));
//        addSequential(new TmCCmdDelay(0.05));
    	addSequential(new TmCCmdGearFlippersRetract());
    	
    }
}
