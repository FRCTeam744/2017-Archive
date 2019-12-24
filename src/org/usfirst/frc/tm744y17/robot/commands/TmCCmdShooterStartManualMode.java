package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsShooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmCCmdShooterStartManualMode extends Command {
	private TmSsShooter ssShooter;
	private OptionE m_opt;
	
	public enum OptionE { TRY_ONCE, TRY_UNTIL_STARTS }
    public TmCCmdShooterStartManualMode() {
    	this(OptionE.TRY_ONCE);
    }
    public TmCCmdShooterStartManualMode(OptionE option) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	ssShooter = TmSsShooter.getInstance();
    	m_opt = option;
    	requires(ssShooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P.println(Tt.extractClassName(this) + " initializing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	ssShooter.startManual();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean ans = true;
        if(m_opt.equals(OptionE.TRY_UNTIL_STARTS)) {
        	ans = ssShooter.isShooterSsActiveManual();
        }
		return ans;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
