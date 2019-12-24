package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmCCmdDriveShiftGear extends Command {

	private TmSsDrvGearShift ssGearShift;
	private TmSsDrvGearShift.DrvGearsE m_requestedGear;
	
    public TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE requestedGear) {
    	ssGearShift = TmSsDrvGearShift.getInstance();
        requires(ssGearShift);
        m_requestedGear = requestedGear;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	TmSsDrvGearShift.updateDrvShifter(m_requestedGear);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
