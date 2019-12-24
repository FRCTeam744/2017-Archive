package org.usfirst.frc.tm744y17.robot.helpers;

import org.usfirst.frc.tm744y17.robot.T744Robot2017;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys;
//import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.SpdCntlrTypeE;

//import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public interface TmCANTalonFeaturesI {

	public class ProfileLog<M extends SpeedController> {	
		public double maxOutputCurrent;
		public double maxTimestamp;
		public double minOutputCurrent;
		public double minTimestamp;
		private Timer timestampTimer; //by default, shadows T744Robot2017.m_robotTime
		private M motorObj;
		private String m_maxMinOutputCurrentSummary;
		
		public ProfileLog(SpeedController genericMotorObj) {
			maxOutputCurrent = 0.0;
			maxTimestamp = 0.0;
			minOutputCurrent = 0.0;
			minTimestamp = 0.0;
			timestampTimer = T744Robot2017.m_robotTime; //try getTimeSinceBoot() instead
			m_maxMinOutputCurrentSummary = "<pending>";
			
			TmHdwrRoPhys.SpdCntlrTypeE motorType = TmHdwrRoPhys.getSpdCntlrType(genericMotorObj);
			
			motorObj = null;
			if(motorType != null) {
				switch(motorType) {
				case kTM_FAKEABLE_CAN_TALON:
//				case kTM_FAKEABLE_PWM_TALON:
					motorObj = (M)genericMotorObj;
					break;
				default:
					motorObj = null;
					break;
				}
			}
		}
		
		public void monitorOutputCurrent(double outputCurrent) {
			if(outputCurrent > maxOutputCurrent) {
				maxOutputCurrent = outputCurrent;
				maxTimestamp = timestampTimer.get();
				m_maxMinOutputCurrentSummary = toString();
			}
			else if(outputCurrent < minOutputCurrent) {
				minOutputCurrent = outputCurrent;
				minTimestamp = timestampTimer.get();
				m_maxMinOutputCurrentSummary = toString();
			}
		}
		
		public String toString() {
			String ans = String.format("max: %1.3fA at %1.6fsec, min: %1.3fA at %1.6fsec", 
					maxOutputCurrent, maxTimestamp, minOutputCurrent, minTimestamp);
			return ans; 			
		}
		
		public String getMaxMinOutputCurrentSummary() { return m_maxMinOutputCurrentSummary; }

	}
	
//	private ProfileLog m_outputCurrentLog = new ProfileLog();
//
//	Timer m_timestampTimer; //by default, shadows T744Robot2016.m_robotTime
	
	/** 
	 * return the String returned by ProfileLog's toString() method
	 * @return
	 */
//	public String showMaxMinOutputCurrent() { return m_outputCurrentLog.toString(); }
	public abstract String getMaxMinOutputCurrentSummary(); // { return m_outputCurrentLog.toString(); }
//	public default String getMaxMinOutputCurrentSummary() { return maxMinOutputCurrentSummary; }
}
