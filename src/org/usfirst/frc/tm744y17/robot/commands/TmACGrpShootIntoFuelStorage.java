package org.usfirst.frc.tm744y17.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdDriveForwardDistance;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

/**
 *
 */
public class TmACGrpShootIntoFuelStorage extends CommandGroup {

    public TmACGrpShootIntoFuelStorage() {
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
    	addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
    	addSequential(new TmACmdDriveStraightEndWithGyro(6.77)); //TmCCmdDriveForwardDistance(1.0, 1.0, 6.77));
        addSequential(new TmCCmdDelay(0.05)); //3));
        
        //measure the degrees for the spin, then switch to TmACmdDriveSpinGyro
      //addSequential(new TmACmdDriveSpinGyro(DriveSpinDirectionE.CLOCKWISE, 45??));
    	addSequential(new TmCCmdDriveForwardDistance(1.0, -1.0, 2.0));

    	addSequential(new TmCCmdDelay(0.05)); //3));
    	addSequential(new TmACmdDriveStraightEndWithGyro(1.77)); //TmCCmdDriveForwardDistance(1.0, 1.0, 1.77));
        addSequential(new TmCCmdDelay(0.05)); //3));
    	addSequential(new TmCCmdShooterStart());
    	
    }
}
