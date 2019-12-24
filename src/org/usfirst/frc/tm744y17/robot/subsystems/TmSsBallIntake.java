package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmCfgMotors;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys.SdKeysE;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

import edu.wpi.first.wpilibj.command.Subsystem;

public class TmSsBallIntake extends Subsystem implements TmStdSubsystemI, TmToolsI {
	/*---------------------------------------------------
	 * getInstance stuff
	 *--------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsBallIntake m_instance;

	public static synchronized TmSsBallIntake getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsBallIntake();
		}
		return m_instance;
	}

	private TmSsBallIntake() {}
	
	private class Cnst {
		public static final double DEFAULT_TARGET_SPEED = -1.0;
	}
	
	private Object m_lock = new Object();
	private TmCfgMotors.MotorConfigE gm_motorDef;
	private TmFakeable_CANTalon gm_motor;
//	private TmCfgMotors.MotorConfigE am_motorDef;
//	private TmFakeable_CANTalon am_motor;
//	private double am_motorSpeed = 0.0;
	private double gm_motorSpeed = 0.0;
//	private double am_targetSpeed = 1.0;
	private double gm_targetSpeed = Cnst.DEFAULT_TARGET_SPEED;
	@Override
	public void doInstantiate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRoboInit() {
		gm_motorDef = TmCfgMotors.MotorConfigE.BALL_INTAKE_GRABBER_MOTOR_CFG;
		gm_motor = gm_motorDef.getCanTalonMotorObj();
		setGrabberIntakeMotorSpeed(0.0);
		postToSd();
	}
	private void setGrabberIntakeMotorSpeed(double speed){
		synchronized(m_lock){
			gm_motor.set(speed);
			gm_motorSpeed = speed;
		}
	}
	
	@Override
	public void doDisabledInit() {
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(0.0);
		}
	}

	@Override
	public void doAutonomousInit() {
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(0.0);
//			setAbacusIntakeMotorSpeed(0.0);
			postToSd();
		}
	}

	@Override
	public void doTeleopInit() {
		// TODO Auto-generated method stub
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(0.0);
//			setAbacusIntakeMotorSpeed(0.0);
			postToSd();
		}
	}

	@Override
	public void doLwTestInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRobotPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDisabledPeriodic() {
		// TODO Auto-generated method stub
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(0.0);
//			setAbacusIntakeMotorSpeed(0.0);
		}
	}

	@Override
	public void doAutonomousPeriodic() {
		// TODO Auto-generated method stub
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(gm_motorSpeed);
//			setAbacusIntakeMotorSpeed(am_motorSpeed);
		}
	}

	@Override
	public void doTeleopPeriodic() {
		// TODO Auto-generated method stub
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(gm_motorSpeed);
//			setAbacusIntakeMotorSpeed(am_motorSpeed);
		}
	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized boolean isOff() {
		boolean ans = false;
		synchronized(m_lock){
			ans = (gm_motorSpeed == 0.0);
		}
		return ans;
	}
	public synchronized boolean isAbsorbing() {
		boolean ans = false;
		synchronized(m_lock){
			if(gm_targetSpeed>0) {
				ans = gm_motorSpeed > 0.0;
			} else {
				ans = gm_motorSpeed < 0.0;
			}
		}
		return ans;
	}
	public synchronized boolean isRepelling() {
		boolean ans = false;
		synchronized(m_lock){
			if(gm_targetSpeed>0) {
				ans = gm_motorSpeed < 0.0;
			} else {
				ans = gm_motorSpeed > 0.0;
			}
		}
		return ans;
	}
	public synchronized void postToSd() {
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_INTAKE_MOTOR_ABSORB, isAbsorbing());
		TmPostToSd.dbgPutBoolean(SdKeysE.KEY_INTAKE_MOTOR_REPEL, isRepelling());
	}
	public synchronized void turnMotorsOnReverse(){
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(- gm_targetSpeed);
//			setAbacusIntakeMotorSpeed(- am_targetSpeed);
			P.println("Ball intake motors On REVERSE");
			postToSd();
		}
	}
	public synchronized void turnMotorsOn(){
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(gm_targetSpeed);
//			setAbacusIntakeMotorSpeed(am_targetSpeed);
			P.println("Ball intake motors ON");
			postToSd();
		}
	}
	public synchronized void turnMotorsOff(){
		synchronized(m_lock){
			setGrabberIntakeMotorSpeed(0.0);
//			setAbacusIntakeMotorSpeed(0.0);
			P.println("Ball intake motors OFF");
			postToSd();
		}
	}

}
