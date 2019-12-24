package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.T744Robot2017;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoFeatureTypesE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoModConnIoPair;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoNamedConnectionsE;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoNamedModulesE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

import edu.wpi.first.wpilibj.Relay;

public class TmHdwrRoMap { //extends TmHdwrRoPhys {
	
	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmHdwrRoMap m_instance = new TmHdwrRoMap();

	public static synchronized TmHdwrRoMap getInstance() {
		if (m_instance == null) {
			System.out.println("TmHdwrRoMap.getInstance() called before TmHdwrRoMap was instantiated.");
			m_instance = new TmHdwrRoMap();
		}
		TmHdwrRoPhys.getModConnIoPairManager().populate();
		return m_instance;
	}

	private TmHdwrRoMap() {}
	/*----------------end of getInstance stuff----------------*/

	public static class RoMapCnst {
		public static final Relay.Direction NOT_A_RELAY = null;
	}
	
	public enum FakeablesUsageE { 
//		NOT_FAKEABLE(false), DETECT_FAKES_NEEDED(false), FORCE_FAKE(true);
		DONT_CARE_IF_FAKE, //if fakeable device, will detect if fake needed, else ignored
		FORCE_FAKE; //if fakeable device, will use fake, else ignored
		public boolean isForcedFake() { return this.equals(FORCE_FAKE); }		
	}
	
	public enum RoDrvMtrPolarityE { NEG_VOLTAGE_FOR_ROBOT_FORWARD, POS_VOLTAGE_FOR_ROBOT_FORWARD, NOT_A_DRIVE_MOTOR;
		public double getMultiplierForDrvMtrPolarity() {
			double ans;
			ans = (this.equals(RoDrvMtrPolarityE.NEG_VOLTAGE_FOR_ROBOT_FORWARD)) ? -1.0 : 1.0;
			return ans;
		}
	}
	public enum RoNamedIoE {
		//----ATTENTION!!! if the joysticks don't come up on the driverstation in the proper locations, it will
		//                 make it appear that the info here is wrong.  Check joysticks before modifying this!!!!
		//                 It was verified correct in the gym 1/31/17!!!
		//----ATTENTION!!! TmSsDriveTrain/TmRobotDriveSixMotors provide the ability to swap the drive direction
		//                 via buttons.  Verify that setting before changing things here.
		//for 2017, the transmissions are closer to the back than to the front
		//for 2017, gear placer is at the front, shooter is at the back
		DRV_MTR_FRONT_LEFT(RoNamedModulesE.CAN_TALON_27, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.POS_VOLTAGE_FOR_ROBOT_FORWARD),
		DRV_MTR_CENTER_LEFT(RoNamedModulesE.CAN_TALON_28, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.NEG_VOLTAGE_FOR_ROBOT_FORWARD),
		DRV_MTR_REAR_LEFT(RoNamedModulesE.CAN_TALON_25, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.POS_VOLTAGE_FOR_ROBOT_FORWARD),
		DRV_MTR_FRONT_RIGHT(RoNamedModulesE.CAN_TALON_23, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.NEG_VOLTAGE_FOR_ROBOT_FORWARD),
		DRV_MTR_CENTER_RIGHT(RoNamedModulesE.CAN_TALON_24, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.POS_VOLTAGE_FOR_ROBOT_FORWARD),
		DRV_MTR_REAR_RIGHT(RoNamedModulesE.CAN_TALON_26, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX, RoDrvMtrPolarityE.NEG_VOLTAGE_FOR_ROBOT_FORWARD),

		DRIVE_TRAIN_GYRO(RoNamedModulesE.RIO, RoNamedConnectionsE.SPI_CS0),

		//use these to verify that configuration errors will be caught and reported properly
//		SOME_SPI(RoNamedModulesE.RIO, RoNamedConnectionsE.SPI_MXP),		
//		SOME_DIO(RoNamedModulesE.RIO, RoNamedConnectionsE.MXP_PIN21_DIO5_SPICLK),		
//		SOME_I2C(RoNamedModulesE.RIO, RoNamedConnectionsE.I2C_MXP),		
//		SOME_DIO_A(RoNamedModulesE.RIO, RoNamedConnectionsE.MXP_PIN32_DIO14_I2CSCL),		
//		SOME_PWM(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_PWM0),
//		SOME_MOTOR(RoNamedModulesE.RIO_R_PWM_TALONS, RoNamedConnectionsE.RIO_PWM0),
		
		CAMERA_LEDS_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY0, Relay.Direction.kBoth),
		FLASHLIGHT_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY1, Relay.Direction.kBoth),
		SHOOTER_ABACUS_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY2, Relay.Direction.kBoth),
		GEAR_FLIPPER_LED_STRIP_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY3, Relay.Direction.kBoth),
//		BALL_INTAKE_ABACUS_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY2, Relay.Direction.kBoth),
//		BALL_INTAKE_GRABBER_RELAY(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_RELAY3, Relay.Direction.kBoth),
		
		BALL_INTAKE_MOTOR(RoNamedModulesE.CAN_TALON_21, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX),
//		BALL_INTAKE_ABACUS_MOTOR(RoNamedModulesE.CAN_TALON_22, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX),

		SHOOTER_DRUM_MOTOR(RoNamedModulesE.CAN_TALON_22, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX),
		SHOOTER_TRIGGER_MOTOR(RoNamedModulesE.CAN_TALON_31, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX),
		SHOOTER_ABACUS_MOTOR(RoNamedModulesE.CAN_TALON_32, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX),
		
		CLIMBER_MOTOR_A(RoNamedModulesE.CAN_TALON_34, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX,
												(Tm17Opts.isClimberTalonsABInstalled() ? FakeablesUsageE.DONT_CARE_IF_FAKE : FakeablesUsageE.FORCE_FAKE )),
		CLIMBER_MOTOR_B(RoNamedModulesE.CAN_TALON_35, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX,
												(Tm17Opts.isClimberTalonsABInstalled() ? FakeablesUsageE.DONT_CARE_IF_FAKE : FakeablesUsageE.FORCE_FAKE )),
		CLIMBER_MOTOR_40(RoNamedModulesE.CAN_TALON_33, RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX,
												(Tm17Opts.isClimberTalon40AInstalled() ? FakeablesUsageE.DONT_CARE_IF_FAKE : FakeablesUsageE.FORCE_FAKE )),
		CLIMBER_LIMIT_SWITCH(RoNamedModulesE.RIO, RoNamedConnectionsE.RIO_DIO0),
		
//		//bag night, high gear was set to SOL0, low gear was set to SOL1, drivers reported the buttons were backwards
		DRV_SHIFTER_HIGH_GEAR(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL1),
		DRV_SHIFTER_LOW_GEAR(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL0),
		//revert to what was in "Bagged" code
//		DRV_SHIFTER_HIGH_GEAR(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL0),
//		DRV_SHIFTER_LOW_GEAR(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL1),
		
		GEAR_FLIPPER_EXTENDER(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL2),
		GEAR_FLIPPER_RETRACTOR(RoNamedModulesE.PCM0, RoNamedConnectionsE.PCM_SOL3),
		;
		
		private final RoNamedModulesE eNamedModDef;
		private final RoNamedConnectionsE eNamedConnDef;
		private final RoDrvMtrPolarityE eDrvMtrPolarityDef;
		private final Relay.Direction eRelayDir; 
		private final boolean eValid;
		private final boolean eConfigErrs;
		private final FakeablesUsageE eFakeableUsage;
		
		//simple getters, messier ones are coded elsewhere
		public boolean isValid() { return eValid; }
		public RoNamedModulesE getNamedModuleDef() { return eNamedModDef;}
		public RoNamedConnectionsE getNamedConnDef() { return eNamedConnDef; }
		public Relay.Direction getRelayDirection() { return eRelayDir; }
		public RoDrvMtrPolarityE getDrvMtrPolarityDef() { return eDrvMtrPolarityDef; }
//		public FakeablesUsageE getFakeablesUsage() { return eFakeableUsage; }
		public boolean isForcedFakeRequested() { return eFakeableUsage.isForcedFake(); }

		public boolean isNegToMoveRobotForward() { return eDrvMtrPolarityDef.equals(RoDrvMtrPolarityE.NEG_VOLTAGE_FOR_ROBOT_FORWARD); }
		public double getMultiplierForDrvMtrPolarity() {
			return eDrvMtrPolarityDef.getMultiplierForDrvMtrPolarity();
		}
		private RoModConnIoPair eModConnIoPair;
		public RoModConnIoPair getModConnIoPairObj() { 
			if(eModConnIoPair==null) {
//				eModConnIoPair = TmHdwrRoPhys.getInstance().new RoModConnIoPair(this.name(), eNamedModDef, eNamedConnDef, TtDebuggingE.NO_DEBUG);
//				eModConnIoPair = TmHdwrRoPhys.getInstance().getModConnIoPairManager().getModConnPairingObj(this);
				eModConnIoPair = TmHdwrRoPhys.getModConnIoPairManager().getModConnPairingObj(this);
			}
			return eModConnIoPair;
		}
		
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn) {
			this(mod, conn, RoMapCnst.NOT_A_RELAY, RoDrvMtrPolarityE.NOT_A_DRIVE_MOTOR, FakeablesUsageE.DONT_CARE_IF_FAKE);
		}
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn, FakeablesUsageE fakeableUsage) {
			this(mod, conn, RoMapCnst.NOT_A_RELAY, RoDrvMtrPolarityE.NOT_A_DRIVE_MOTOR, fakeableUsage);
		}
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn, Relay.Direction relayDir) {
			this(mod, conn, relayDir, RoDrvMtrPolarityE.NOT_A_DRIVE_MOTOR, FakeablesUsageE.DONT_CARE_IF_FAKE);
		}
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn, RoDrvMtrPolarityE drvMtrPolarity) {
			this(mod, conn, RoMapCnst.NOT_A_RELAY, drvMtrPolarity, FakeablesUsageE.DONT_CARE_IF_FAKE);
		}
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn, RoDrvMtrPolarityE drvMtrPolarity, FakeablesUsageE fakeableUsage) {
			this(mod, conn, RoMapCnst.NOT_A_RELAY, drvMtrPolarity, fakeableUsage);
		}
		private RoNamedIoE(RoNamedModulesE mod, RoNamedConnectionsE conn, Relay.Direction relayDir, 
															RoDrvMtrPolarityE drvMtrPolarity, FakeablesUsageE fakeableUsage) {
			String itemName = Tt.extractClassName(this) + "." + this.name();
			eNamedModDef = mod;
			eNamedConnDef = conn;
			eDrvMtrPolarityDef = drvMtrPolarity;
			eRelayDir = relayDir;
			eFakeableUsage = fakeableUsage;
			
//			eModConnIoPair = TmHdwrRoPhys.getInstance().new RoModConnIoPair(this, mod, conn, TtDebuggingE.NO_DEBUG);
//			RoModConnIoPairRegistration.register(this);
			
			boolean cfgErrs = false;
			boolean valid = true;
			
			//RoFeatureTypesE connFeatureType = conn.getFeatureTypeDef();
			//refactor expectedConnFeatType to expectedConnModFeatType
			RoFeatureTypesE expectedModFeatType = conn.getFeatureTypeDef().getExpectedModFeatType();

			if( ! mod.getModFeatureTypeDef().equals(expectedModFeatType)) {
				if( ! mod.getTrueModuleFor().getModFeatureTypeDef().equals(expectedModFeatType)) {
					cfgErrs = true;
					String msg = itemName + " config err: " + mod.getModFeatureTypeDef().name() + " is not the the type of module " +
							conn.name() + " expects (" + expectedModFeatType.name() + ")";
					P.println(msg);
				}
			}
			if( ! mod.isValid()) {
				valid = false;
				String msg = itemName + " not valid because " + mod.name() + " is not valid.";
				P.println(msg);				
			}
			if( ! conn.isValid()) {
				valid = false;
				String msg = itemName + " not valid because " + conn.name() + " is not valid.";
				P.println(msg);				
			}
			
			if(conn.isRelay()) {
				if(relayDir == null) {
					cfgErrs = true;
					String msg = itemName + " has config errors. Needs Relay.Direction item.";
					P.println(msg);
				}
			} else {
				if( ! (relayDir == null)) {
					cfgErrs = true;
					String msg = itemName + " has config errors. Relay.Direction item should be null.";
					P.println(msg);					
				}
			}

			eConfigErrs = cfgErrs;
			eValid = valid & ( ! cfgErrs);
		}
		
		public static RoNamedIoE getNamedIoDef(String namedIoDefName) {
			RoNamedIoE ans = null;
			for(RoNamedIoE i : RoNamedIoE.values()) {
				if(i.name().equals(namedIoDefName)) { ans = i; break;}
			}
			if(ans==null) {
    			throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("getModDef() called for invalid RoNamedIoE enum value " + namedIoDefName);
			}
			return ans;
		}	

		public int getCanId() {
			int ans;
			if( ! getNamedModuleDef().getModFeatureTypeDef().isCanDevice()) {
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx(
						this.name() + " is not a CAN device");
			}
			ans = getNamedModuleDef().getModuleIndex();
			return ans;
		}

	}

	public static void showAllRoIo() {
		String msg;
		String conn;
		P.println(T744Robot2017.getBuildInfoToShow());
		for(RoNamedIoE i : RoNamedIoE.values()) {
			msg = String.format("  Robot IO: %-25s", i.name());
			msg += ": mod: " + i.getNamedModuleDef().toStringFull();
			conn = i.getNamedConnDef().toStringFull();
			if( ! conn.equals("")) {
				msg += "conn: " + i.getNamedConnDef().toStringFull();
			}
			P.println(msg);
		}
	}	


}
