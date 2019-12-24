package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *Distance from center 41.0"
 */
public class TmACGrpLeftSideGearMidfieldRed extends CommandGroup {

//	private static final double TOWARD_PEG_SPEED = 0.5;
//	private static final double SPIN_SPEED = 0.5;
//	private static final double BACK_AWAY_SPEED = -0.5;

    public TmACGrpLeftSideGearMidfieldRed() {
    	//changes made: might have to lower motor speed, reversed directions on the motors
	//    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
	//    	addSequential(new TmACmdDriveStraightEndWithGyro(+7.83, 6));
	//        addSequential(new TmCCmdDelay(0.05));
	//    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 55));  //adjust turn value
	//        addSequential(new TmCCmdDelay(0.05));
	//    	addSequential(new TmACmdDriveStraightEndWithGyro(1.2, 2.0)); //0.8, 2.0)); //TmCCmdDriveForwardDistance(TOWARD_PEG_SPEED, TOWARD_PEG_SPEED, 0.8)); //TmACmdDriveStraightEndWithGyroRev2(1.00,6));
	//        addSequential(new TmCCmdDelay(0.3));
	//        addSequential(new TmCCmdGearFlippersExtend());
	//        addSequential(new TmCCmdDelay(0.5));
    	addSequential(new TmACGrpHelperLeftSideGear());
//	    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
//	    	addSequential(new TmACmdDriveStraightEndWithGyro(7.66333+0.25+0.25, 6)); //+7.83, 6)); //+0.25
//	        addSequential(new TmCCmdDelay(0.05));
//	    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE, 54.5));  //adjust turn value, changed from 56.5
//	        addSequential(new TmCCmdDelay(0.05));
//	    	addSequential(new TmACmdDriveStraightEndWithGyro(1.2, 2.0)); //TmCCmdDriveForwardDistance(TOWARD_PEG_SPEED, TOWARD_PEG_SPEED, 0.8)); //TmACmdDriveStraightEndWithGyroRev2(1.00,6));
//	        addSequential(new TmCCmdDelay(0.3));
//	        addSequential(new TmCCmdGearFlippersExtend());
//	        addSequential(new TmCCmdDelay(0.5));
//        addSequential(new TmACmdDriveStraightEndWithGyro(-1.0));
        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0)); //TmCCmdDriveForwardDistance(BACK_AWAY_SPEED, BACK_AWAY_SPEED, -2.0));
        addSequential(new TmCCmdDelay(0.05));
    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE, 55));
    	addSequential(new TmCCmdDelay(0.05));
    	addSequential(new TmACmdDriveStraightEndWithGyro(15));
        addSequential(new TmCCmdGearFlippersRetract());
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
    }
}
