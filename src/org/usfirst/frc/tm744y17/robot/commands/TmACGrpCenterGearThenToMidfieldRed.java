package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;

import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *
 */
public class TmACGrpCenterGearThenToMidfieldRed extends CommandGroup {

//	private static final double FORWARD_SPEED = 1.0;
//	private static final double REVERSE_SPEED = -0.5;
//	private static final double SPIN_SPEED = 1.0;

	public TmACGrpCenterGearThenToMidfieldRed() {
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

		//Same auto as before
    	addSequential(new TmACGrpCenterGear());
    	addSequential(new TmCCmdDelay(.05));

    	//new stuff
    	//Reverse a smidge more
    	addSequential(new TmACmdDriveStraightEndWithGyro(-.5)); //TmCCmdDriveForwardDistance(REVERSE_SPEED, REVERSE_SPEED,-.5, 0.20));
    	addSequential(new TmCCmdDelay(.05));

//    	//Shift to High Gear - optional, but if it works, leave it
//    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.HIGH));
//    	addSequential(new TmCCmdDelay(.05));

    	//Spin clockwise 90 degree
    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE, 90));  //adjust turn value
        addSequential(new TmCCmdDelay(.05));

        //Drive Straight ~6 feet to clear airship
        addSequential(new TmACmdDriveStraightEndWithGyro(7.95));
    	addSequential(new TmCCmdDelay(.05));

    	//Spin to face loading zone
    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 90));  //adjust turn value
    	addSequential(new TmCCmdDelay(.05));

    	//Drive out to mid field
    	addSequential(new TmACmdDriveStraightEndWithGyro(10));




//    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
//    	addSequential (new TmCCmdDriveForwardDistance(FORWARD_SPEED,FORWARD_SPEED, 6.23));
//    	addSequential(new TmCCmdGearFlippersExtend());
//    	addSequential (new TmCCmdDriveForwardDistance(REVERSE_SPEED, REVERSE_SPEED, -1.0));
//    	addSequential(new TmCCmdGearFlippersRetract());
//    	addSequential(new TmCCmdDriveForwardDistance(SPIN_SPEED, -SPIN_SPEED, 1.0));  //adjust turn value
//    	addSequential(new TmCCmdDriveForwardDistance(REVERSE_SPEED, REVERSE_SPEED, 9.9));  //adjust drive value based on turn value
    	// maybe addSequential(new TmCCmdDriveForwardDistance(1.0, -1.0, 1.0));  //adjust based on values from above two programs
    }
}
