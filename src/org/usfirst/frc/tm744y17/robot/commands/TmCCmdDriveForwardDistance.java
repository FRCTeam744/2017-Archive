package org.usfirst.frc.tm744y17.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors;
import org.usfirst.frc.tm744y17.robot.devices.TmRobotDriveSixMotors.CenterDriveMotorsBehaviorE;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DrvEncoderMgmt;
/**
 *
 */
public class TmCCmdDriveForwardDistance extends Command {
	TmRobotDriveSixMotors sSDrive;
	TmSsDriveTrain sDrive;
	DrvEncoderMgmt m_dem;
	double lLeft;
	double rRight;
	double dDistance;
	
	public static final double NO_TIMEOUT = -1;
	
	public static final double MAX_FEET_PER_SEC = TmSsDriveTrain.getMaxFtPerSecHighGear(); //16;
	public static final double MAX_FEET_PER_SEC_LOW_GEAR = TmSsDriveTrain.getMaxFtPerSecLowGear(); // 6;
	
    public TmCCmdDriveForwardDistance(double left, double right, double distance) {
    	this(left, right, distance, distance/(Math.max(Math.abs(left), Math.abs(right))/2 * MAX_FEET_PER_SEC_LOW_GEAR)); 
    }
	/**
	 * in software, gear placer is at the front of the robot, shooter is at the back of the robot...
	 * @param left - speed left side of robot should move
	 * @param right - speed right side of robot should move
	 * @param distance - distance robot should travel in feet. whichever side travels that far first will stop the robot
	 * @param timeout - stop after this amount of time whether robot has traveled the distance specified or not
	 * 					must be > 0 or no timeout will occur
	 */
    public TmCCmdDriveForwardDistance(double left, double right, double distance, double timeout) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        sDrive = TmSsDriveTrain.getInstance();
        sSDrive = sDrive.getChassisInstance();
     	requires(sDrive);
       
        if(timeout > 0) {
        	setTimeout(timeout);
        }
        
        m_dem = sDrive.getDriveEncoderMgmtInstance(); 
    	lLeft = left;
    	rRight = right;
    	dDistance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_dem.reset();
    	m_dem.update();
    	P.println(-1, Tt.extractClassName(this) + ": initializing: dist=" + dDistance
    			+ " L-encDist=" + m_dem.getLeftDistanceAdjustedForPolarity()
    			+ " R-encDist=" + m_dem.getRightDistanceAdjustedForPolarity());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute () {
    	sSDrive.setRobotLeftRightSpeeds(lLeft, rRight, CenterDriveMotorsBehaviorE.getDefault());	
    }

    // Make this return true when this Command no longer needs to run execute()
    // note that isTimedOut() returns false if no setTimeout() was done in the constructor
    protected boolean isFinished() {
    	boolean ans = false;
    	boolean arrived = false;
    	m_dem.update();
    	if(lLeft >= 0 && rRight >= 0){
    		ans = (m_dem.getLeftDistanceAdjustedForPolarity() >= dDistance)||(m_dem.getRightDistanceAdjustedForPolarity() >= dDistance)||isTimedOut();
    		if(!isTimedOut()) { arrived = true; }
    	}
    	else if(lLeft >= 0 && rRight <= 0){
    		ans = (m_dem.getLeftDistanceAdjustedForPolarity() >= dDistance)||(m_dem.getRightDistanceAdjustedForPolarity() <= dDistance)||isTimedOut();
    		if(!isTimedOut()) { arrived = true; }
    	}
    	else if(lLeft <= 0 && rRight >= 0){
    		ans = (m_dem.getLeftDistanceAdjustedForPolarity() <= dDistance)||(m_dem.getRightDistanceAdjustedForPolarity() >= dDistance)||isTimedOut();
    		if(!isTimedOut()) { arrived = true; }
    	}
    	else if(lLeft <= 0 && rRight <= 0){
    		ans = (m_dem.getLeftDistanceAdjustedForPolarity() <= dDistance)||(m_dem.getRightDistanceAdjustedForPolarity() <= dDistance)||isTimedOut();
    		if(!isTimedOut()) { arrived = true; }
    	}
    	else{
    		//should never get here
    		ans = true;
    	}
    	if(ans==true) {
    		//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
    		P.println(Tt.extractClassName(this) + ": finished" + (arrived ? " - at destination" : (isTimedOut()?" - timedout":"")));
    	}
    	return ans;
    }

    // Called once after isFinished returns true
    protected void end() {
    	sSDrive.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	sSDrive.stopMotor();
    }
}
