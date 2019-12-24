package org.usfirst.frc.tm744y17.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
/**
 *
 */
public class TmCCmdDriveTimed extends Command {
	TmRobotDriveSixMotors sSDrive;
	TmSsDriveTrain sDrive;
	double lLeft;
	double rRight;
	double tTime;
	CenterDriveMotorsBehaviorE cCenterMotorBehavior;
	
    public TmCCmdDriveTimed(double leftSpd, double rightSpd, CenterDriveMotorsBehaviorE centerMotorBehavior, double timeSecs) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        sDrive = TmSsDriveTrain.getInstance();
        sSDrive = sDrive.getChassisInstance();
        requires(sDrive);
    	lLeft = leftSpd;
    	rRight = rightSpd;
    	tTime = timeSecs;
    	setTimeout(tTime); //scheduler will start the timeout just before calling initialize
    	cCenterMotorBehavior = centerMotorBehavior;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute () {
    	//positive values normally move robot forward
    	sSDrive.setRobotLeftRightSpeeds(lLeft, rRight, cCenterMotorBehavior);	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        return (sDrive.getLeftDistance() >= dDistance)||(sDrive.getRightDistance() >= dDistance)||isTimedOut();
    	return isTimedOut(); // (tTimer.get() >= tTime);
    }

    // Called once after isFinished returns true
    protected void end() {
    	//stopMotor() does this: sSDrive.setRobotLeftRightSpeeds(0, 0, CenterDriveMotorsBehaviorE.getDefault());
    	sSDrive.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//stopMotor() does this: sSDrive.setRobotLeftRightSpeeds(0, 0, CenterDriveMotorsBehaviorE.getDefault());
    	sSDrive.stopMotor();
    }
}
