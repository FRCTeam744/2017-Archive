package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_DoubleSolenoid;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TmSsDrvGearShift extends Subsystem implements TmStdSubsystemI {

	public enum DrvGearsE { 
		HIGH(DoubleSolenoid.Value.kForward),
		LOW(DoubleSolenoid.Value.kReverse),
//		HIGH(DoubleSolenoid.Value.kReverse),
//		LOW(DoubleSolenoid.Value.kForward),
		OFF(DoubleSolenoid.Value.kOff),
		;
		private final DoubleSolenoid.Value eDirection;
		public DoubleSolenoid.Value getSolDirection() { return eDirection; }
		private DrvGearsE(DoubleSolenoid.Value dir) { eDirection = dir; }
	};
	private static DrvGearsE m_drvShifterCurrentPosition;
	private static DrvGearsE m_drvShifterPrevPosition;

	private static TmFakeable_DoubleSolenoid m_gearShifter;

	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsDrvGearShift m_instance;
	public static synchronized TmSsDrvGearShift getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsDrvGearShift();
		}
		return m_instance;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	@Override
	public void doInstantiate() {
		// stuff needed before doRobotInit is called
		
	}

	@Override
	public void doRoboInit() {
		m_gearShifter = new TmFakeable_DoubleSolenoid(
				TmHdwrRoMap.RoNamedIoE.DRV_SHIFTER_HIGH_GEAR, 
				TmHdwrRoMap.RoNamedIoE.DRV_SHIFTER_LOW_GEAR);	
		m_drvShifterPrevPosition = m_drvShifterCurrentPosition = DrvGearsE.OFF;
//		m_gearShifter.addToLiveWindow(Tm16Misc.LwSubSysName.SS_DRV_GEARSHIFTER, 
//							Tm16Misc.LwItemNames.DRV_GEARSHIFTER);
//		TmSdDbgSD.dbgPutBoolean(Tm16Misc.SdKeysE.KEY_DRIVE_GEARSHIFT, isDrvShifterInHighGear());
		postToSd(); //TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_GEARSHIFT_IS_HIGH, isDrvShifterInHighGear());
	}

	@Override
	public void doDisabledInit() {
		m_drvShifterPrevPosition = m_drvShifterCurrentPosition = DrvGearsE.LOW;
		postToSd(); //TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_GEARSHIFT_IS_HIGH, isDrvShifterInHighGear());
	}

	@Override
	public void doAutonomousInit() {
		updateDrvShifter(DrvGearsE.LOW);
		postToSd(); //
	}

	@Override
	public void doTeleopInit() {
		updateDrvShifter(DrvGearsE.HIGH);
		postToSd(); //
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

	}

	@Override
	public void doAutonomousPeriodic() {
		// TODO Auto-generated method stub
		postToSd(); //

	}

	@Override
	public void doTeleopPeriodic() {
		// TODO Auto-generated method stub
		postToSd(); //

	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub

	}

	public static boolean isDrvShifterInLowGear()
	{
		return m_gearShifter.get().equals(DrvGearsE.LOW.getSolDirection());
	}
	public static boolean isDrvShifterInHighGear()
	{
		return m_gearShifter.get().equals(DrvGearsE.HIGH.getSolDirection());
	}
	
	public static DrvGearsE getCurrentGear() {
		DrvGearsE ans;
		ans = DrvGearsE.OFF;
		DoubleSolenoid.Value dir = m_gearShifter.get();
		if( dir.equals(DrvGearsE.LOW.getSolDirection()) ) {
			ans = DrvGearsE.LOW;
		} 
		else if( dir.equals(DrvGearsE.HIGH.getSolDirection()) ) {
			ans = DrvGearsE.HIGH;
		} 
		return ans;
	}

	private static final Object m_gearShifterLock = new Object();
	public static void updateDrvShifter(DrvGearsE requestedGear)
	{
		String msgInfo = "??";
		String msgSuffix = "";
		synchronized(m_gearShifterLock) {
			if(requestedGear.equals(DrvGearsE.LOW))
			{
				m_gearShifter.set(DrvGearsE.LOW.getSolDirection());
				m_drvShifterCurrentPosition = DrvGearsE.LOW;
			}
			else if(requestedGear.equals(DrvGearsE.HIGH))
			{
				m_gearShifter.set(DrvGearsE.HIGH.getSolDirection());
				m_drvShifterCurrentPosition = DrvGearsE.HIGH;
			}
			else
			{
				//nothing
			}

			msgInfo = "already";
			//print a dbg msg whenever we've actually changed positions
			if( ! m_drvShifterCurrentPosition.equals(m_drvShifterPrevPosition))
			{
				msgInfo = "now";
				m_drvShifterPrevPosition = m_drvShifterCurrentPosition;
			} 
		}

		msgSuffix = " (expected " + m_drvShifterCurrentPosition.toString() +")";
//		Tm16DbgTk.printIt(-1, "shifter " + msgInfo + " in " + 
//				(isDrvShifterInLowGear() ? "LOW" : 
//					isDrvShifterInHighGear() ? "HIGH" : "UNKNOWN") + " gear" + msgSuffix);
		P.println(-1, "shifter " + msgInfo + " in " + 
				(isDrvShifterInLowGear() ? "LOW" : 
					isDrvShifterInHighGear() ? "HIGH" : "UNKNOWN") + " gear" + msgSuffix);
		postToSd(); //TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_GEARSHIFT_IS_HIGH, isDrvShifterInHighGear());
	}
	
	public static void postToSd() {
		//orlando real bot all hi/lo gear stuff correct except driver station boolean. HACK!!!
		TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_GEARSHIFT_IS_HIGH, isDrvShifterInHighGear());
		//TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_DRIVE_GEARSHIFT_IS_HIGH, ! isDrvShifterInHighGear());
	}
	
//	/**
//	 * this was used last year in autonomous...
//	 */
//    public synchronized void toggleShifter()
//    {
//        if(isDrvShifterInLowGear())
//        {
//        	TmSsDrvGearShift.updateDrvShifter(DrvGearsE.HIGH);
//        }
//        else
//        {
//        	TmSsDrvGearShift.updateDrvShifter(DrvGearsE.LOW);
//        }
//    }

}

