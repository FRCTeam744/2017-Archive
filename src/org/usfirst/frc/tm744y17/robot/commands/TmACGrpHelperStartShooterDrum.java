package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveSpinDirectionE;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterStartManualMode.OptionE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;

import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *measures 89.5" from the "center line
 *(nearest bumper should be that distance from center line)
 */
public class TmACGrpHelperStartShooterDrum extends CommandGroup {
	public TmACGrpHelperStartShooterDrum(double initialDelay) {
//		addSequential(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
		addSequential(new TmCCmdDelay(initialDelay));
		addSequential(new TmCCmdShooterStartManualMode(OptionE.TRY_UNTIL_STARTS));
		addSequential(new TmCCmdShooterManualToggleMotor());
	}	
}