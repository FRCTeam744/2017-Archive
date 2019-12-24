package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TmCCGrpGearFlippersExtendAndBackAway extends CommandGroup {
	
	//gear placer is the front of the robot (in software), so must drive backwards to back away from gear peg
	private double driveSpeed = -0.75;
	// (back away 8in)(1ft/12in)/((max 16 ft/sec)(driveSpeed))
	private double revTime = (8/12)/(16*Math.abs(driveSpeed)); //about 0.056 seconds
	
	//oops! runs in low gear--max 6 ft/sec, this code should back up about 3 inches? (.75*6*0.05555=0.25)

    public TmCCGrpGearFlippersExtendAndBackAway() {
//        addSequential(new TmCCmdDriveForward(1.0,1.0,10.0));
        addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmCCmdDelay(0.3));
        
        //measure actual distance traveled by TmCCmdDriveTimed, then change to drive with gyro for that distance
        //addSequential(new TmACmdDriveStraightEndWithGyro(-8.0/12.0)); 
        addSequential(new TmCCmdDriveTimed(driveSpeed, driveSpeed, CenterDriveMotorsBehaviorE.getDefault(), revTime));
        
//      addSequential(new TmCCmdGearFlippersRetract());
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
