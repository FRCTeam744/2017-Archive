package org.usfirst.frc.tm744y17.robot.subsystems;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_DoubleSolenoid;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_Relay;
import org.usfirst.frc.tm744y17.robot.driverStation.TmMiscSdKeys;
import org.usfirst.frc.tm744y17.robot.driverStation.TmPostToSd;
import org.usfirst.frc.tm744y17.robot.helpers.TmStdSubsystemI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TmSsGearFlipper extends Subsystem implements TmStdSubsystemI, TmToolsI {

	public enum FlipperPositionE {
		EXTENDED(DoubleSolenoid.Value.kForward, RELAY_FLIPPERS_EXTENDED),
		RETRACTED(DoubleSolenoid.Value.kReverse, RELAY_FLIPPERS_RETRACTED),
		;
		private final DoubleSolenoid.Value eDirection;
		private final Relay.Value eLedRelayDir;
		
		public DoubleSolenoid.Value getSolDirection() { return eDirection; }
		public Relay.Value getLedRelayDirection() { return eLedRelayDir; }
		
		private FlipperPositionE(DoubleSolenoid.Value dir, Relay.Value ledRelayDir) { eDirection = dir; eLedRelayDir = ledRelayDir; }		
	}
	
	
	
	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmSsGearFlipper m_instance;

	public static synchronized TmSsGearFlipper getInstance() {
		if (m_instance == null) {
			m_instance = new TmSsGearFlipper();
		}
		return m_instance;
	}

	private TmSsGearFlipper() {
		m_flipperSolenoid = new TmFakeable_DoubleSolenoid(RoNamedIoE.GEAR_FLIPPER_EXTENDER, RoNamedIoE.GEAR_FLIPPER_RETRACTOR);
		m_flipperLedRelay = new TmFakeable_Relay(TmHdwrRoMap.RoNamedIoE.GEAR_FLIPPER_LED_STRIP_RELAY);
	}
	/*----------------end of getInstance stuff----------------*/

    private static final Relay.Value RELAY_FLIPPERS_EXTENDED = Relay.Value.kForward;
    private static final Relay.Value RELAY_FLIPPERS_RETRACTED = Relay.Value.kReverse;
	
	private FlipperPositionE m_flipperPosition = FlipperPositionE.RETRACTED;
	private TmFakeable_DoubleSolenoid m_flipperSolenoid;
	private TmFakeable_Relay m_flipperLedRelay;
	private Object m_lock = new Object();
	
	public void postToSd() {
		TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_FLIPPER_EXTENDED, m_flipperPosition.equals(FlipperPositionE.EXTENDED));
	}
	
	public FlipperPositionE getPosition() {
		FlipperPositionE ans;
		synchronized(m_lock) {
			ans = m_flipperPosition;
			postToSd();
		}
		return ans;
	}
	
	public void setPosition(FlipperPositionE newPos) {
		synchronized(m_lock) { 
			m_flipperPosition = newPos;
			m_flipperSolenoid.set(m_flipperPosition.getSolDirection());
			m_flipperLedRelay.set(m_flipperPosition.getLedRelayDirection());
			P.println("GearFlipper " + newPos.name());
			postToSd();
//			TmPostToSd.dbgPutBoolean(TmMiscSdKeys.SdKeysE.KEY_FLIPPER_EXTENDED, newPos.equals(FlipperPositionE.EXTENDED));
		}
	}
	
	@Override
	public void doInstantiate() {
	}

	@Override
	public void doRoboInit() {
		setPosition(FlipperPositionE.RETRACTED);
		postToSd();
	}

	@Override
	public void doDisabledInit() {
		setPosition(FlipperPositionE.RETRACTED);
		postToSd();
	}

	@Override
	public void doAutonomousInit() {
		setPosition(FlipperPositionE.RETRACTED);
		postToSd();
	}

	@Override
	public void doTeleopInit() {
		setPosition(FlipperPositionE.RETRACTED);
		postToSd();
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
		
	}

	@Override
	public void doTeleopPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLwTestPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
