package org.usfirst.frc.tm744y17.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdDriveTimed;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdGearFlippersExtend;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdGearFlippersRetract;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
/**
 *
 */
public class TmACGrpCenterGearTime extends CommandGroup {

	private static final double FORWARD_SPEED = 1.0;
	private static final double REVERSE_SPEED = -1.0;
//	private static final double SPIN_SPEED = 1.0;
	
    public TmACGrpCenterGearTime() {
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
    	addSequential(new TmCCmdDriveTimed(FORWARD_SPEED, FORWARD_SPEED, CenterDriveMotorsBehaviorE.getDefault(), 0.39));
        addSequential(new TmCCmdDelay(3));
    	addSequential(new TmCCmdGearFlippersExtend());
        addSequential(new TmCCmdDelay(3));
    	addSequential(new TmCCmdDriveTimed(REVERSE_SPEED, REVERSE_SPEED, CenterDriveMotorsBehaviorE.getDefault(), 0.1)); //actually drives backward
        addSequential(new TmCCmdDelay(3));
     	addSequential(new TmCCmdGearFlippersRetract());
    }
}
