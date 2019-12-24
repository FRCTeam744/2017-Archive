package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *Distance from center 44.25"
 */
public class TmACGrpRightSideGearMidfieldBlue extends CommandGroup {
	
//	private static final double TOWARD_PEG_SPEED = 1.0;
//	private static final double SPIN_SPEED = 1.0;
//	private static final double BACK_AWAY_SPEED = -1.0;
    public TmACGrpRightSideGearMidfieldBlue() {
    	//changes made: might have to lower motor speed, reversed directions on the motors
    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
    	addSequential(new TmACGrpHelperRightSideGear());
//    	addSequential(new TmACmdDriveStraightEndWithGyro(7.66333+0.25, 6)); //+7.83, 6));
//        addSequential(new TmCCmdDelay(0.05));
//    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.COUNTER_CLOCKWISE, 56.5));  //adjust turn value
//        addSequential(new TmCCmdDelay(0.05));
//    	addSequential(new TmACmdDriveStraightEndWithGyro(1.2, 2.0)); //TmCCmdDriveForwardDistance(TOWARD_PEG_SPEED, TOWARD_PEG_SPEED, 0.8)); //TmACmdDriveStraightEndWithGyroRev2(1.00,6));
//        addSequential(new TmCCmdDelay(0.3));
//        addSequential(new TmCCmdGearFlippersExtend());
//        addSequential(new TmCCmdDelay(0.5));
//        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0)); //TmCCmdDriveForwardDistance(BACK_AWAY_SPEED, BACK_AWAY_SPEED, -2.0));
//        addSequential(new TmCCmdDelay(0.05));
//        addSequential(new TmCCmdGearFlippersRetract());
        addSequential(new TmACmdDriveStraightEndWithGyro(-2.0)); //TmCCmdDriveForwardDistance(BACK_AWAY_SPEED, BACK_AWAY_SPEED, -2.0));
        addSequential(new TmCCmdDelay(0.05));
    	addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 55));
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
