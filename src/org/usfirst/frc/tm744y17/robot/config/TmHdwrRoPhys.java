package org.usfirst.frc.tm744y17.robot.config;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.devices.TmFakeable_CANTalon;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.TtDebuggingE;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedController;


public class TmHdwrRoPhys {
	
	protected static RoModConnIoPairManager m_mgr; //can't do 'new' until have an instance = new RoModConnIoPairManager();
	
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmHdwrRoPhys m_instance; // = new TmHdwrRoPhys();

	public static synchronized TmHdwrRoPhys getInstance() {
		if (m_instance == null) {
			m_instance = new TmHdwrRoPhys(true);
			if(m_mgr==null) {
				m_mgr = new RoModConnIoPairManager();
			}
		}
		return m_instance;
	}

	protected TmHdwrRoPhys() {
		String msg = "time to debug!!";
	}
	protected TmHdwrRoPhys(boolean junk) {
		String msg = "if not first time, time to debug!!";		
	}


	public static RoModConnIoPairManager getModConnIoPairManager() { 
		if(m_mgr==null) {
			String msg = "time to debug!!";
			P.println("getModConnIoPairManager() called before TmHdwrRoPhys instantiated.");
		}
		return m_mgr; 
	}

	/*
	 * PWM Talons could be defined as module RIO and a PWM connection, but for
	 * clarity and to be able to flag them as SpeedControllers and more specifically
	 * as Talon speed controllers, it works better to use a separate named module 
	 * entry for them.  So, in RoNamedModules, we created RIO_R_PWM_TALONS as an alias
	 * for RIO.  TmHdwrRoMap and TmHdwrRoPhys will treat it as RIO when checking for 
	 * duplicate assignment of resources, etc.  Other code can treat it as a separate
	 * module. But error messages may ref RIO instead of RIO_R_PWM_TALONS.  To help 
	 * cut down on the resulting confusion, we use these enum values as a way to tag 
	 * RIO_R_PWM_TALONS as an alias of RIO.
	 */
	public static enum RoNamedModuleAliasesE { ALIAS_OF_RIO, NOT_AN_ALIAS }
	/** 
	 * a "module" here is some hardware object that may or may not
	 * be installed/powered/connected on the robot itself.  Some
	 * modules (e.g. RIO, PCM) have multiple connections that can
	 * be used independently of each other, others don't.
	 * @author JudiA
	 *
	 */
	public static enum RoNamedModulesE {
		RIO(RoFeatureTypesE.RIO_MODULE, 0),
		PCM0(RoFeatureTypesE.CAN_PCM_MODULE, 0),

		//for 2017, the transmissions are closer to the back than to the front
		//for 2017, the gear placer is at the front, the shooter is at the back
		CAN_TALON_27(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 27),
		CAN_TALON_28(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 28),
		CAN_TALON_25(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 25),
		CAN_TALON_23(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 23),
		CAN_TALON_24(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 24),
		CAN_TALON_26(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 26),
		CAN_TALON_21(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 21),
		CAN_TALON_22(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 22),

//		CAN_TALON_30(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 30),
		CAN_TALON_31(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 31),
		CAN_TALON_32(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 32),
		CAN_TALON_33(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 33, BreakerSizeE.BREAKER_40_AMP),
		CAN_TALON_34(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 34, BreakerSizeE.BREAKER_40_AMP),//BreakerSizeE.BREAKER_30_AMP),
		CAN_TALON_35(RoFeatureTypesE.CAN_TALON_SRX_MODULE, 35, BreakerSizeE.BREAKER_40_AMP),//BreakerSizeE.BREAKER_30_AMP),
		
		RIO_R_PWM_TALONS(RoFeatureTypesE.R_PWM_TALON_MODULE, 0, RoNamedModuleAliasesE.ALIAS_OF_RIO), //an alias for RIO used so can flag as Talon SpeedController
		RIO_M_PWM_TALONS(RoFeatureTypesE.M_PWM_TALON_MODULE, 0, RoNamedModuleAliasesE.ALIAS_OF_RIO), //an alias for RIO used so can flag as Talon SpeedController
//		RIO_R_DIO_RELAYS(RoFeatureTypesE.R_DIO, 0, RoNamedModuleAliasesE.ALIAS_OF_RIO),
		;
		private final RoFeatureTypesE eFeatureTypeDef;
		private final int eModuleIndex;
		private final RoNamedModuleAliasesE eAliasOf;
		private final BreakerSizeE eBreakerDef;
		private final boolean eValid;
		private RoNamedModulesE(RoFeatureTypesE featTypeDef, int modNdx) {
			this(featTypeDef, modNdx, RoNamedModuleAliasesE.NOT_AN_ALIAS, BreakerSizeE.DONT_CARE);
		}
		private RoNamedModulesE(RoFeatureTypesE featTypeDef, int modNdx, RoNamedModuleAliasesE aliasOf) {
			this(featTypeDef, modNdx, aliasOf, BreakerSizeE.DONT_CARE);
		}
		private RoNamedModulesE(RoFeatureTypesE featTypeDef, int modNdx, BreakerSizeE breaker) {
			this(featTypeDef, modNdx, RoNamedModuleAliasesE.NOT_AN_ALIAS, breaker);
		}
		private RoNamedModulesE(RoFeatureTypesE featTypeDef, int modNdx, RoNamedModuleAliasesE aliasOf, BreakerSizeE breaker) {
			boolean valid = true;
			eFeatureTypeDef = featTypeDef;
			eModuleIndex = modNdx;
			eAliasOf = aliasOf;
			eBreakerDef = breaker;
			String msg = Tt.extractClassName(this) + "." + this.name() + 
					" has config errors.";
			if( ! featTypeDef.getFeatureCategory().equals(RoFeatureCategoryE.MODULE)) {
				valid = false;
				msg += " " + featTypeDef.name() + " is not a 'module'.";
				//				P.println(msg);
			}
			if( ! eFeatureTypeDef.getIndexType().isIndexInRange(modNdx)) {
				valid = false;
				msg += " Index is out of range for " + featTypeDef.name() + ".";
				//				P.println(msg);
			}
			eValid = valid;
			if( ! valid) { throw TmExceptions.getInstance().new InvalidMappedIoDefEx(msg); }
		}

		public boolean isValid() { return eValid; }
		public RoFeatureTypesE getModFeatureTypeDef() { return eFeatureTypeDef; }
		public int getModuleIndex() { return eModuleIndex; }
		public BreakerSizeE getBreakerSizeDef() { return eBreakerDef; }
		
		public boolean isModuleAlias() { return isModuleAlias(this); }
		public static boolean isModuleAlias(RoNamedModulesE possibleAlias) {
			boolean ans;
			switch(possibleAlias) {
			case RIO_R_PWM_TALONS:
//			case RIO_R_DIO_RELAYS:
				ans = true;
				break;
			default:
				ans = false; //not an alias, use as-is
				break;
			}
			return ans;
		}
		
		public RoNamedModulesE getTrueModuleFor() { return getTrueModuleFor(this); }
		public static RoNamedModulesE getTrueModuleFor(RoNamedModulesE possibleAlias) {
			RoNamedModulesE ans;
			switch(possibleAlias) {
			case RIO_R_PWM_TALONS:
//			case RIO_R_DIO_RELAYS:
				ans = RIO;
				break;
			default:
				ans = possibleAlias; //not an alias, use as-is
				break;
			}

			return ans;
		}
		
		public String getAliasDescription() { return getAliasDescription(this); }
		public static String getAliasDescription(RoNamedModulesE possibleAlias) {
			String ans = "";
			if(possibleAlias.isModuleAlias()) {
				ans = " (using mod " + possibleAlias.name() + " as an alias for " +
						possibleAlias.getTrueModuleFor().name() + ")";
			}
			return ans;
		}
		
		public SpdCntlrTypeE getSpdCntlrType() {
			SpdCntlrTypeE ans = null;
			if(this.getModFeatureTypeDef().equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE)) {
				ans = SpdCntlrTypeE.kTM_FAKEABLE_CAN_TALON;
			} else {
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx(this.name() + " is not a speed controller");
			}
			return ans;
		}
		
		public String toStringFull() {
			String ans;	
			String amps;
			//minus sign here means "left justify" (pad on right)
			amps = "" + (eBreakerDef.equals(BreakerSizeE.DONT_CARE)? "" : String.format(" (max %1.1fA)", eBreakerDef.getMaxAmpsAllowed())); 
			ans = String.format("%-17s%-12s", getTrueModuleFor().name(), amps); 
			return ans;
		}
	}

	private static enum ConnectionCheckingE { CHECK_CONNECTION, NO_CHECK_CONNECTION }

	/**
	 * Robot module I/O Definitions Enum
	 * @author JudiA
	 *
	 */
	public static enum RoNamedConnectionsE {
		//we have things that expect to have non-null named connections.  These are provided to satisfy those
		//requirements. They represent modules, not connections, so no error messages for these!!
		NO_CONNECTIONS_ON_CAN_TALON_SRX(RoFeatureTypesE.CAN_TALON_SRX_MODULE, -1, ConnectionCheckingE.NO_CHECK_CONNECTION),

		RIO_DIO0(RoFeatureTypesE.R_DIO, 0),
		RIO_DIO1(RoFeatureTypesE.R_DIO, 1),
		RIO_DIO2(RoFeatureTypesE.R_DIO, 2),
		RIO_DIO3(RoFeatureTypesE.R_DIO, 3),
		RIO_DIO4(RoFeatureTypesE.R_DIO, 4),
		RIO_DIO5(RoFeatureTypesE.R_DIO, 5),
		RIO_DIO6(RoFeatureTypesE.R_DIO, 6),
		RIO_DIO7(RoFeatureTypesE.R_DIO, 7),
		RIO_DIO8(RoFeatureTypesE.R_DIO, 8),
		RIO_DIO9(RoFeatureTypesE.R_DIO, 9),
		MXP_PIN11_DIO0_PWM0(RoFeatureTypesE.M_DIO, 0 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_11_DIO0_PWM0),
		MXP_PIN13_DIO1_PWM1(RoFeatureTypesE.M_DIO, 1 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_13_DIO1_PWM1),
		MXP_PIN15_DIO2_PWM2(RoFeatureTypesE.M_DIO, 2 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_15_DIO2_PWM2),
		MXP_PIN17_DIO3_PWM3(RoFeatureTypesE.M_DIO, 3 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_17_DIO3_PWM3),
		MXP_PIN19_DIO4_SPICS(RoFeatureTypesE.M_DIO, 4 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_19_DIO4_SPI_CS),
		MXP_PIN21_DIO5_SPICLK(RoFeatureTypesE.M_DIO, 5 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_21_DIO5_SPI_CLK),
		MXP_PIN23_DIO6_SPIMISO(RoFeatureTypesE.M_DIO, 6 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_23_DIO6_SPI_MISO),
		MXP_PIN25_DIO7_SPIMOSI(RoFeatureTypesE.M_DIO, 7 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_25_DIO7_SPI_MOSI),
		MXP_PIN27_DIO8_PWM4(RoFeatureTypesE.M_DIO, 8 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_27_DIO8_PWM4),
		MXP_PIN29_DIO9_PWM5(RoFeatureTypesE.M_DIO, 9 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_29_DIO9_PWM5),
		MXP_PIN31_DIO10_PWM6(RoFeatureTypesE.M_DIO, 10 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_31_DIO10_PWM6),
		MXP_PIN18_DIO11_PWM7(RoFeatureTypesE.M_DIO, 11 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_18_DIO11_PWM7),
		MXP_PIN22_DIO12_PWM8(RoFeatureTypesE.M_DIO, 12 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_22_DIO12_PWM8),
		MXP_PIN26_DIO13_PWM9(RoFeatureTypesE.M_DIO, 13 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_26_DIO13_PWM9),
		MXP_PIN32_DIO14_I2CSCL(RoFeatureTypesE.M_DIO, 14 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_32_DIO14_I2C_SCL),
		MXP_PIN34_DIO15_I2CSDA(RoFeatureTypesE.M_DIO, 15 + Cnst.kRIO_DIO_CNT, MxpPinNbrsE.MXP_PIN_34_DIO15_I2C_SDA),

		RIO_PWM0(RoFeatureTypesE.R_PWM, 0),
		RIO_PWM1(RoFeatureTypesE.R_PWM, 1),
		RIO_PWM2(RoFeatureTypesE.R_PWM, 2),
		RIO_PWM3(RoFeatureTypesE.R_PWM, 3),
		RIO_PWM4(RoFeatureTypesE.R_PWM, 4),
		RIO_PWM5(RoFeatureTypesE.R_PWM, 5),
		RIO_PWM6(RoFeatureTypesE.R_PWM, 6),
		RIO_PWM7(RoFeatureTypesE.R_PWM, 7),
		RIO_PWM8(RoFeatureTypesE.R_PWM, 8),
		RIO_PWM9(RoFeatureTypesE.R_PWM, 9),   
		MXP_PIN11_PWM0_DIO0(RoFeatureTypesE.M_PWM, 0 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_11_DIO0_PWM0),
		MXP_PIN13_PWM1_DIO1(RoFeatureTypesE.M_PWM, 1 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_13_DIO1_PWM1),
		MXP_PIN15_PWM2_DIO2(RoFeatureTypesE.M_PWM, 2 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_15_DIO2_PWM2),
		MXP_PIN17_PWM3_DIO3(RoFeatureTypesE.M_PWM, 3 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_17_DIO3_PWM3),
		MXP_PIN27_PWM4_DIO8(RoFeatureTypesE.M_PWM, 4 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_27_DIO8_PWM4),
		MXP_PIN29_PWM5_DIO9(RoFeatureTypesE.M_PWM, 5 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_29_DIO9_PWM5),
		MXP_PIN31_PWM6_DIO10(RoFeatureTypesE.M_PWM, 6 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_31_DIO10_PWM6),
		MXP_PIN18_PWM7_DIO11(RoFeatureTypesE.M_PWM, 7 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_18_DIO11_PWM7),
		MXP_PIN22_PWM8_DIO12(RoFeatureTypesE.M_PWM, 8 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_22_DIO12_PWM8),
		MXP_PIN26_PWM9_DIO13(RoFeatureTypesE.M_PWM, 9 + Cnst.kRIO_PWM_CNT, MxpPinNbrsE.MXP_PIN_26_DIO13_PWM9),

		RIO_RELAY0(RoFeatureTypesE.R_RELAY, 0),
		RIO_RELAY1(RoFeatureTypesE.R_RELAY, 1),
		RIO_RELAY2(RoFeatureTypesE.R_RELAY, 2),
		RIO_RELAY3(RoFeatureTypesE.R_RELAY, 3),

		PCM_SOL0(RoFeatureTypesE.PCM_SOLENOID, 0),
		PCM_SOL1(RoFeatureTypesE.PCM_SOLENOID, 1),
		PCM_SOL2(RoFeatureTypesE.PCM_SOLENOID, 2),
		PCM_SOL3(RoFeatureTypesE.PCM_SOLENOID, 3),
		PCM_SOL4(RoFeatureTypesE.PCM_SOLENOID, 4),
		PCM_SOL5(RoFeatureTypesE.PCM_SOLENOID, 5),
		PCM_SOL6(RoFeatureTypesE.PCM_SOLENOID, 6),
		PCM_SOL7(RoFeatureTypesE.PCM_SOLENOID, 7),
		
		SPI_CS0(RoFeatureTypesE.R_SPI_PORT, SPI.Port.kOnboardCS0),
		SPI_CS1(RoFeatureTypesE.R_SPI_PORT, SPI.Port.kOnboardCS1),
		SPI_CS2(RoFeatureTypesE.R_SPI_PORT, SPI.Port.kOnboardCS2),
		SPI_CS3(RoFeatureTypesE.R_SPI_PORT, SPI.Port.kOnboardCS3),
		SPI_MXP(RoFeatureTypesE.M_SPI_PORT, SPI.Port.kMXP),

		I2C_ONBOARD(RoFeatureTypesE.R_I2C_PORT, I2C.Port.kOnboard),
		I2C_MXP(RoFeatureTypesE.M_I2C_PORT, I2C.Port.kMXP),
		;

		private final RoFeatureTypesE eFeatureTypeDef;
		private int eConnectionIndex;
		private final MxpPinNbrsE eMxpPinNbr;
		private final SPI.Port eSpiPort;
		private final I2C.Port eI2cPort;
		private final boolean eValid;
		private final ConnectionCheckingE eConnCheck;

		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, I2C.Port i2cPortDef) {
			this(featTypeDef, 0, null, null, i2cPortDef, ConnectionCheckingE.CHECK_CONNECTION); 
		}
		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, SPI.Port spiPortDef) {
			this(featTypeDef, 0, null, spiPortDef, null, ConnectionCheckingE.CHECK_CONNECTION); 
		}
//		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, MxpPinNbrsE mxpPinDef, SPI.Port spiPortDef) {
//			this(featTypeDef, 0, mxpPinDef, spiPortDef, ConnectionCheckingE.CHECK_CONNECTION); 
//		}
		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, int featIndex) {
			this(featTypeDef, featIndex, null, null, null, ConnectionCheckingE.CHECK_CONNECTION); 
		}
		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, int featIndex, ConnectionCheckingE connCheck) {
			this(featTypeDef, featIndex, null, null, null, connCheck);
		}
		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, int featIndex, MxpPinNbrsE mxpPinDef) {
			this(featTypeDef, featIndex, mxpPinDef, null, null, ConnectionCheckingE.CHECK_CONNECTION);
		}
		private RoNamedConnectionsE(RoFeatureTypesE featTypeDef, int featIndex, MxpPinNbrsE mxpPinDef, SPI.Port spiPortDef, 
				I2C.Port i2cPortDef, ConnectionCheckingE connCheck) {
			eFeatureTypeDef = featTypeDef;
			eConnectionIndex = featIndex;
			eMxpPinNbr = mxpPinDef;
			eSpiPort = spiPortDef;
			eI2cPort = i2cPortDef;
			eConnCheck = connCheck;

			boolean configErrs = false;
			String itemName = Tt.extractClassName(this) + "." + this.name();

			//we have some dummy named connections to use to avoid having to deal with null named connection parms.
			//we don't want to display error messages for those.
			if( ! featTypeDef.getFeatureCategory().equals(RoFeatureCategoryE.CONNECTION_ON_MODULE)) {
				if(connCheck.equals(ConnectionCheckingE.CHECK_CONNECTION)) {
					configErrs = true;
					String msg = itemName + " has config errors. " + 
							featTypeDef.name() + " is not a connection on a module";
					P.println(msg);
				}
			}

			if(connCheck.equals(ConnectionCheckingE.CHECK_CONNECTION)) {
				if(featTypeDef.equals(RoFeatureTypesE.R_SPI_PORT) || featTypeDef.equals(RoFeatureTypesE.M_SPI_PORT)) {
					if(spiPortDef==null) {
						configErrs = true;
						String msg = itemName + " has config errors. Needs SPI.Port item.";
						P.println(msg);
					} else {
						eConnectionIndex = spiPortDef.value;
						if(featTypeDef.equals(RoFeatureTypesE.R_SPI_PORT) && spiPortDef.equals(SPI.Port.kMXP)) {
							configErrs = true;
							String msg = itemName + " has config errors. " + RoFeatureTypesE.R_SPI_PORT.toString() + 
									" cannot use SPI.Port." + SPI.Port.kMXP.toString();
							P.println(msg);
						}
						else if(featTypeDef.equals(RoFeatureTypesE.M_SPI_PORT) && ! spiPortDef.equals(SPI.Port.kMXP)) {
							configErrs = true;
							String msg = itemName + " has config errors. " + RoFeatureTypesE.M_SPI_PORT.toString() + 
									" must use SPI.Port." + SPI.Port.kMXP.toString();
							P.println(msg);
						}
					}
				}
				if(featTypeDef.equals(RoFeatureTypesE.R_I2C_PORT) || featTypeDef.equals(RoFeatureTypesE.M_I2C_PORT)) {
					if(i2cPortDef==null) {
						configErrs = true;
						String msg = itemName + " has config errors. Needs I2C.Port item.";
						P.println(msg);
					} else {
						eConnectionIndex = i2cPortDef.value;
						if(featTypeDef.equals(RoFeatureTypesE.R_I2C_PORT) && ! i2cPortDef.equals(I2C.Port.kOnboard)) {
							configErrs = true;
							String msg = itemName + " has config errors. " + RoFeatureTypesE.R_I2C_PORT.toString() + 
									" must use I2C.Port." + I2C.Port.kOnboard.toString();
							P.println(msg);
						}
						else if(featTypeDef.equals(RoFeatureTypesE.M_I2C_PORT) && ! i2cPortDef.equals(I2C.Port.kMXP)) {
							configErrs = true;
							String msg = itemName + " has config errors. " + RoFeatureTypesE.M_I2C_PORT.toString() + 
									" must use I2C.Port." + I2C.Port.kMXP.toString();
							P.println(msg);
						}
					}
				}
				if(featTypeDef.isRioMxpPin()) {
					if(mxpPinDef == null) {
						configErrs = true;
						String msg = itemName + " has config errors. Needs MxpPinNbrsE item.";
						P.println(msg);
					}				
				} else {
					if( ! (mxpPinDef == null)) {
						configErrs = true;
						String msg = itemName + " has config errors. MxpPinNbrsE item should be null";
						P.println(msg);					
					}				
				}
			}

			eValid = ! configErrs;
		}

		public boolean isValid() { return eValid; }
		public RoFeatureTypesE getFeatureTypeDef() { return eFeatureTypeDef; }
		public MxpPinNbrsE getMxpPinDef() { return eMxpPinNbr; }
		
		public SPI.Port getSpiPort() { return eSpiPort; }
		public I2C.Port getI2cPort() { return eI2cPort; }

		public int getConnectionIndex() {
			int ans;
			return getConnectionIndex(this);
		}
		public static int getConnectionIndex(RoNamedConnectionsE namedConn) {
			int ans;
			//causes exceptions? switch(namedConn) {
			if(namedConn.equals(RoNamedConnectionsE.NO_CONNECTIONS_ON_CAN_TALON_SRX)) {
				throw TmExceptions.getInstance().new MappedIoDefNoFeatureIndexEx("conn " + namedConn.name() + 
						" has no useable feature index.  Try RoNamedModulesE.getModuleIndex() instead.");
			} else {
				ans = namedConn.eConnectionIndex;
			}
			return ans; 
		}

		public boolean isRelay() {
			return isRelay(eFeatureTypeDef);
		}

		public static boolean isRelay(RoFeatureTypesE featType) {
			boolean ans;

			//exceptions: switch(eFeatureTypeDef) {
			if(featType.equals(RoFeatureTypesE.M_RELAY) ||
				featType.equals(RoFeatureTypesE.R_RELAY) ) {
				ans = true;
			} else {
				ans = false;
			}
			
			return ans;
		}
		public String toStringFull() {
			String ans;
			if(eConnCheck.equals(ConnectionCheckingE.NO_CHECK_CONNECTION)) {
				ans = ""; //clues caller that there is no useful connection info for this enum value
			} else {
				//minus sign here means "left justify" (pad on right)
				ans = String.format("%-23s", this.name());
			}
			return ans;
		}
	}


	public static enum RoFeatureIndexTypeE {
		CAN_ID, RIO_NDX,
		R_NDX_AI, R_NDX_AO, R_NDX_DIO, R_NDX_PWM, R_NDX_RELAY, R_NDX_SPI_PORT, R_NDX_I2C_PORT, R_NDX_USB_PORT, //R_ for roboRIO
		M_NDX_AI, M_NDX_AO, M_NDX_DIO, M_NDX_PWM, M_NDX_RELAY, M_NDX_SPI_PORT, M_NDX_I2C_PORT, //M_ for MXP
		PCM_SOL_NDX, IP_ADDR, SPI_DEV_ADDR, I2C_DEV_ADDR;
		private RoFeatureIndexTypeE() {}

		public boolean isIndexInRange(int testIndexVal) {
			return isIndexInRange(this, testIndexVal);
		}
		public static boolean isIndexInRange(RoFeatureIndexTypeE entNdxType, int testIndexVal) {
			boolean ans = false;
			//switch(entNdxType) {
			if(entNdxType.equals(RoFeatureIndexTypeE.CAN_ID) ) {
				if(Tt.isInRange(testIndexVal, Cnst.CAN_ID_MIN, Cnst.CAN_ID_MAX)) {ans = true; }
			} else if(entNdxType.equals(RoFeatureIndexTypeE.RIO_NDX) ) {
				if(testIndexVal == 0) { ans = true; }
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_AI) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_AI_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_AO) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_AO_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_DIO) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_DIO_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_PWM) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_PWM_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_RELAY) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_RELAY_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_SPI_PORT) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_SPI_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_I2C_PORT) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_I2C_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.R_NDX_USB_PORT) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_RIO_MIN_INDEX_VAL, Cnst.kRIO_USB_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_AI) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_AI_CNT, Cnst.kMXP_AI_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_AO) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_AO_CNT, Cnst.kMXP_AO_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_DIO) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_DIO_CNT, Cnst.kMXP_DIO_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_PWM) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_PWM_CNT, Cnst.kMXP_PWM_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_RELAY) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_RELAY_CNT, Cnst.kMXP_RELAY_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_SPI_PORT) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_SPI_CNT, Cnst.kMXP_SPI_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.M_NDX_I2C_PORT) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.kRIO_I2C_CNT, Cnst.kMXP_I2C_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.PCM_SOL_NDX) ) {
				if(Tt.isWithinCount(testIndexVal, Cnst.FRC_PCM_MIN_INDEX_VAL, Cnst.kPCM_SOL_CNT)) {ans = true; }
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.IP_ADDR) ) {
				ans = false;//				ans = true;
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.SPI_DEV_ADDR) ) {
				ans = false;//				ans = I2c7BitRjAddrE.isValidI2CAddr(testIndexVal);
			
			} else if(entNdxType.equals(RoFeatureIndexTypeE.I2C_DEV_ADDR) ) {
				ans = I2c7BitRjAddrE.isValidI2CAddr(testIndexVal);
			} else {
				ans = false;
			
			}
			return ans;
		}
	}

	public static enum RoFeatureCategoryE { MODULE, CONNECTION_ON_MODULE }

	public static enum RoFeatureTypesE {
		RIO_MODULE,
		R_AI, R_AO, R_DIO, R_PWM, R_RELAY, R_SPI_PORT, R_I2C_PORT, R_USB_PORT, //R_ for roboRIO
		M_AI, M_AO, M_DIO, M_PWM, M_RELAY, M_SPI_PORT, M_I2C_PORT, //M_ for MXP
		CAN_TALON_SRX_MODULE, CAN_PDP_MODULE, CAN_PCM_MODULE, PCM_SOLENOID,
		USB_CAMERA, IP_CAMERA, 
		R_PWM_TALON_MODULE, M_PWM_TALON_MODULE, //these are here so we can flag these PWM's as SpeedControllers
		SPI_DEV, I2C_DEV, IP_NETWORK;

		public RoFeatureCategoryE getFeatureCategory() {
			return getFeatureCategory(this);
		}
		public static RoFeatureCategoryE getFeatureCategory(RoFeatureTypesE featType) {
			RoFeatureCategoryE ans;
			//switch(featType) causes exception here
			if(featType.equals(RoFeatureTypesE.RIO_MODULE) ||
					featType.equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE) ||
					featType.equals(RoFeatureTypesE.CAN_PCM_MODULE) ||
					featType.equals(RoFeatureTypesE.CAN_PDP_MODULE) ||
					featType.equals(RoFeatureTypesE.R_PWM_TALON_MODULE) ||
					featType.equals(RoFeatureTypesE.M_PWM_TALON_MODULE)		) {
				ans = RoFeatureCategoryE.MODULE;
			} else {
				ans = RoFeatureCategoryE.CONNECTION_ON_MODULE;
			}
			return ans;
		}

		public RoFeatureIndexTypeE getIndexType() {
			return getIndexType(this);
		}

		public static RoFeatureIndexTypeE getIndexType(RoFeatureTypesE featType) {
			RoFeatureIndexTypeE ans = null;
			if(featType.name().equals("RIO_MODULE")) {
				String msg = "time to debug";
			}
			//switch(featType) causes exception here
			if( featType.equals(RoFeatureTypesE.RIO_MODULE) ) {
				ans = RoFeatureIndexTypeE.RIO_NDX;
			} else if( featType.equals(RoFeatureTypesE.R_AI) ) {
				ans = RoFeatureIndexTypeE.R_NDX_AI;
			} else if( featType.equals(RoFeatureTypesE.R_AO) ) {
				ans = RoFeatureIndexTypeE.R_NDX_AO;
			} else if( featType.equals(RoFeatureTypesE.R_DIO) ) {
				ans = RoFeatureIndexTypeE.R_NDX_DIO;
			} else if( featType.equals(RoFeatureTypesE.R_PWM) ) {
				ans = RoFeatureIndexTypeE.R_NDX_PWM;
			} else if( featType.equals(RoFeatureTypesE.R_PWM_TALON_MODULE) ) {
				ans = RoFeatureIndexTypeE.RIO_NDX; //NAMED_CONNECTION_NO_NDX; //R_NDX_PWM;
			} else if( featType.equals(RoFeatureTypesE.R_RELAY) ) {
				ans = RoFeatureIndexTypeE.R_NDX_RELAY;
			} else if( featType.equals(RoFeatureTypesE.R_SPI_PORT) ) {
				ans = RoFeatureIndexTypeE.R_NDX_SPI_PORT;
			} else if( featType.equals(RoFeatureTypesE.R_I2C_PORT) ) {
				ans = RoFeatureIndexTypeE.R_NDX_I2C_PORT;
			} else if( featType.equals(RoFeatureTypesE.R_USB_PORT) ) {
				ans = RoFeatureIndexTypeE.R_NDX_USB_PORT;
			} else if( featType.equals(RoFeatureTypesE.M_AI) ) {
				ans = RoFeatureIndexTypeE.M_NDX_AI;
			} else if( featType.equals(RoFeatureTypesE.M_AO) ) {
				ans = RoFeatureIndexTypeE.M_NDX_AO;
			} else if( featType.equals(RoFeatureTypesE.M_DIO) ) {
				ans = RoFeatureIndexTypeE.M_NDX_DIO;
			} else if( featType.equals(RoFeatureTypesE.M_PWM) ) {
				ans = RoFeatureIndexTypeE.M_NDX_PWM;
			} else if( featType.equals(RoFeatureTypesE.M_PWM_TALON_MODULE) ) {
				ans = RoFeatureIndexTypeE.RIO_NDX; //M_NDX_PWM;
			} else if( featType.equals(RoFeatureTypesE.M_RELAY) ) {
				ans = RoFeatureIndexTypeE.M_NDX_RELAY;
			} else if( featType.equals(RoFeatureTypesE.M_SPI_PORT) ) {
				ans = RoFeatureIndexTypeE.M_NDX_SPI_PORT;
			} else if( featType.equals(RoFeatureTypesE.M_I2C_PORT) ) {
				ans = RoFeatureIndexTypeE.M_NDX_I2C_PORT;
			} else if( featType.equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE) ) {
				ans = RoFeatureIndexTypeE.CAN_ID;
			} else if( featType.equals(RoFeatureTypesE.CAN_PDP_MODULE) ) {
				ans = RoFeatureIndexTypeE.CAN_ID;
			} else if( featType.equals(RoFeatureTypesE.CAN_PCM_MODULE) ) {
				ans = RoFeatureIndexTypeE.CAN_ID;
			} else if( featType.equals(RoFeatureTypesE.PCM_SOLENOID) ) {
				ans = RoFeatureIndexTypeE.PCM_SOL_NDX;
			} else if( featType.equals(RoFeatureTypesE.USB_CAMERA) ) {
				ans = RoFeatureIndexTypeE.R_NDX_USB_PORT;
			} else if( featType.equals(RoFeatureTypesE.IP_CAMERA) ) {
				ans = RoFeatureIndexTypeE.IP_ADDR;
			} else if( featType.equals(RoFeatureTypesE.SPI_DEV) ) {
				ans = RoFeatureIndexTypeE.SPI_DEV_ADDR;
			} else if( featType.equals(RoFeatureTypesE.I2C_DEV) ) {
				ans = RoFeatureIndexTypeE.I2C_DEV_ADDR;
			} else if( featType.equals(RoFeatureTypesE.IP_NETWORK) ) {
				ans = RoFeatureIndexTypeE.IP_ADDR;
			} else {
				throw TmExceptions.getInstance().new UnsupportedEntityIndexTypeEx("unsupported RoEntityIndexTypeE." + featType.name());
			}
			return ans;
		}

//		public boolean isRioSpi() {
//			return isRioSpi(this);
//		}
//		public static boolean isRioSpi(RoFeatureTypesE featType) {
//			boolean ans = false;
//			if( featType.equals(RoFeatureTypesE.R_SPI_PORT)	{
//				ans = true;
//			}
//			return ans;
//		}

		public boolean isRioMxpPin() {
			return isRioMxpPin(this);
		}

		public static boolean isRioMxpPin(RoFeatureTypesE featType) {
			boolean ans;
			//switch(featType) has caused exceptions elsewhere, so remove it here
			if( featType.equals(RoFeatureTypesE.M_AI) ||
			    featType.equals(RoFeatureTypesE.M_AO) ||
			    featType.equals(RoFeatureTypesE.M_DIO) ||
			    featType.equals(RoFeatureTypesE.M_PWM) ||
// 			    featType.equals(RoFeatureTypesE.M_PWM_TALON_MODULE) ||
//			    featType.equals(RoFeatureTypesE.M_SPI_PORT) || //multiple MXP pins, registration handles it
//			    featType.equals(RoFeatureTypesE.M_I2C_PORT) || //multiple MXP pins, registration handles it
			    featType.equals(RoFeatureTypesE.M_RELAY) 
			    ) {
				ans = true;
			} else {
				ans = false;
			}
			return ans;
		}

		public RoFeatureTypesE getExpectedModFeatType() {
			return getExpectedModFeatType(this);
		}
		public static RoFeatureTypesE getExpectedModFeatType(RoFeatureTypesE featType) {
			RoFeatureTypesE ans = null;
			//switch(featType) causes exceptions
			if( featType.equals(RoFeatureTypesE.RIO_MODULE) ||
			    featType.equals(RoFeatureTypesE.R_AI) ||
			    featType.equals(RoFeatureTypesE.R_AO) ||
			    featType.equals(RoFeatureTypesE.R_DIO) ||
			    featType.equals(RoFeatureTypesE.R_PWM) ||
			    featType.equals(RoFeatureTypesE.R_PWM_TALON_MODULE) ||
			    featType.equals(RoFeatureTypesE.R_RELAY) ||
			    featType.equals(RoFeatureTypesE.R_SPI_PORT) ||
			    featType.equals(RoFeatureTypesE.R_I2C_PORT) ||
			    featType.equals(RoFeatureTypesE.R_USB_PORT) ||
			    featType.equals(RoFeatureTypesE.M_AI) ||
			    featType.equals(RoFeatureTypesE.M_AO) ||
			    featType.equals(RoFeatureTypesE.M_DIO) ||
			    featType.equals(RoFeatureTypesE.M_PWM) ||
			    featType.equals(RoFeatureTypesE.M_PWM_TALON_MODULE) ||
			    featType.equals(RoFeatureTypesE.M_RELAY) ||
			    featType.equals(RoFeatureTypesE.M_SPI_PORT) ||
			    featType.equals(RoFeatureTypesE.M_I2C_PORT)     ) {
				ans = RoFeatureTypesE.RIO_MODULE;
			} else if(featType.equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE)) {
				ans = RoFeatureTypesE.CAN_TALON_SRX_MODULE;
			} else if(featType.equals(RoFeatureTypesE.CAN_PDP_MODULE) ) {
				ans = RoFeatureTypesE.CAN_PDP_MODULE;
			} else if(featType.equals(RoFeatureTypesE.CAN_PCM_MODULE) ||
			      featType.equals(RoFeatureTypesE.PCM_SOLENOID)      ) {
				ans = RoFeatureTypesE.CAN_PCM_MODULE;
			} else if(featType.equals(RoFeatureTypesE.USB_CAMERA) ) {
				ans = RoFeatureTypesE.RIO_MODULE;
			} else if(featType.equals(RoFeatureTypesE.IP_CAMERA) ) {
				ans = RoFeatureTypesE.IP_NETWORK;
			} else if(featType.equals(RoFeatureTypesE.SPI_DEV) ||
			          featType.equals(RoFeatureTypesE.I2C_DEV)    ) {
				ans = RoFeatureTypesE.RIO_MODULE;
			} else if(featType.equals(RoFeatureTypesE.IP_NETWORK) ) {
				ans = RoFeatureTypesE.IP_NETWORK;
			}
			return ans;
		}

		public boolean isSpeedController() {
			return isSpeedController(this);
		}
		public static boolean isSpeedController(RoFeatureTypesE featType) {
			boolean ans;
//			switch(featType) causes exceptions
			if( featType.equals(RoFeatureTypesE.R_PWM_TALON_MODULE) ||
			    featType.equals(RoFeatureTypesE.M_PWM_TALON_MODULE) ||
			    featType.equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE) ) {
				ans = true;
			} else {
				ans = false;
			}
			return ans;
		}

		public boolean isCanDevice() {
			return isCanDevice(this);
		}

		public static boolean isCanDevice(RoFeatureTypesE featType) {
			boolean ans;
			//switch(featType) {
			if( featType.equals(RoFeatureTypesE.RIO_MODULE) ||
			    featType.equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE) ||
			    featType.equals(RoFeatureTypesE.CAN_PDP_MODULE) ||
			    featType.equals(RoFeatureTypesE.CAN_PCM_MODULE) ) {
				ans = true;
			} else {
				ans = false;
			}

			return ans;
		}
		
	}

	/**
	 * this enum lists reserved I2C addresses using the format required by
	 * FRC code: 7-bit, right-justified in a byte (although we use int here).
	 * It also provides a method for testing an address for validity.
	 * @author JudiA
	 *
	 */
	public static enum I2c7BitRjAddrE {
		GENERAL_CALL_OR_START(0x00),
		CBUS_ADDR(0x01),
		ALT_BUS_FORMAT(0x02),
		FOR_FUTURE_USE_1(0x03),
		HS_MODE_MASTER_A(0x04),
		HS_MODE_MASTER_B(0x05),
		HS_MODE_MASTER_C(0x06),
		HS_MODE_MASTER_D(0x07),
		FOR_FUTURE_USE_2(0x7C),
		FOR_FUTURE_USE_3(0x7D),
		FOR_FUTURE_USE_4(0x7E),
		FOR_FUTURE_USE_5(0x7F),
		FOR_10_BIT_ADDR_A(0x78),
		FOR_10_BIT_ADDR_B(0x79),
		FOR_10_BIT_ADDR_C(0x7A),
		FOR_10_BIT_ADDR_D(0x7B)
		;
		private final int eReservedAddr;
		private I2c7BitRjAddrE(int addr) {
			eReservedAddr = addr;
		}

		public static boolean isValidI2CAddr(int testAddr) {
			boolean ans = false;
			boolean reserved = false;
			if(testAddr >= 0 && testAddr <= 0x7F) { //if 7-bits right-justified
				for(I2c7BitRjAddrE item : I2c7BitRjAddrE.values()) {
					if(testAddr == item.eReservedAddr) { reserved = true; }
				}
				if( ! reserved) { ans = true; }
			}
			return ans;
		}
	}

	public enum BreakerSizeE {
		DONT_CARE(200.0), BREAKER_40_AMP(40.0), BREAKER_30_AMP(30.0), MAX_AMPS_20A(20.0);
		private final double eMaxAmpsAllowed;
		private BreakerSizeE(double maxAmps) {
			eMaxAmpsAllowed = maxAmps;
		}
		public double getMaxAmpsAllowed() { return eMaxAmpsAllowed; }
	}

	/**
	 * just holds constants so that we can type Cnst. and see a list
	 * of what's available.
	 * @author robotics
	 *
	 */
	public static class Cnst {

		public static final boolean IS_MXP_INSTALLED = true;

		/*======== the rest of this class shouldn't need to change ========*/
		public static final int CAN_ID_MIN = 0;
		public static final int CAN_ID_MAX = 62; //per FRC description of CAN bus.
		
		//see I2c7BitRjAddrE for SPI and I2C addressing
		//		public static final int kSPI_MIN_ADDR = 0x00;
		//		public static final int kSPI_MAX_ADDR = 0x00;
		//		
		//		//FRC I2C class expects 7-bit address right-justified in a byte.
		//		//the address must be even to allow for the least significant byte (LSB)
		//		//  to indicate read or write
		//		public static final int kI2C_MIN_ADDR = 0x00;
		//		public static final int kI2C_MAX_ADDR = 0x7E;

		//indexes for roboRIO connections begin at this value
		public static final int FRC_RIO_MIN_INDEX_VAL = 0; 
		public static final int FRC_PCM_MIN_INDEX_VAL = 0; 

		/* numbering of the channel numbers for MXP I/O picks up where the numbering
		 * on the roboRio leaves off.  These constants show where the roboRio leaves
		 * off. Should never need to be changed.
		 * See FRC_Java_Programming document, "MXP IO Numbering" under "Java Basics"
		 */
		//the number of each type of IO available on the roboRIO
		public static final int kRIO_USB_CNT = 2; //USB ports

		public static final int kRIO_AI_CNT = 4; //analog inputs
		public static final int kRIO_AO_CNT = 0; //analog outputs
		public static final int kRIO_DIO_CNT = 10; //digital input/output
		public static final int kRIO_PWM_CNT = 10; //PWMs
		public static final int kRIO_RELAY_CNT = 4; //Relays
		public static final int kRIO_SPI_CNT = 4; //SPI ports
		public static final int kRIO_I2C_CNT = 1; //I2C ports

		//the number of each type of IO available on the MXP (MyRIO Expansion Port)
		//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
		public static final int kMXP_AI_CNT = IS_MXP_INSTALLED ? 4 : 0; //analog inputs
		public static final int kMXP_AO_CNT = IS_MXP_INSTALLED ? 4 : 0; //analog outputs
		public static final int kMXP_DIO_CNT = IS_MXP_INSTALLED ? 16 : 0; //digital input/output
		public static final int kMXP_PWM_CNT = IS_MXP_INSTALLED ? 10 : 0; //PWMs
		public static final int kMXP_RELAY_CNT = IS_MXP_INSTALLED ? 0 : 0; //Relays
		public static final int kMXP_SPI_CNT = IS_MXP_INSTALLED ? 1 : 0; //SPI ports
		public static final int kMXP_I2C_CNT = IS_MXP_INSTALLED ? 1 : 0; //I2C ports

		//the number of solenoids available on a PCM (Pneumatics Control Module)
		public static final int kPCM_SOL_CNT = 8; //solenoid channels

		//these were added to facilitate simulation in SensorBase.java
		public static final int kPDP_MOD_CNT = 1;
		public static final int kPDP_CHAN_CNT = 16; //8 40A, 8 30A
		public static final int kPCM_MAX_MOD_CNT = 2;
	}

	public static enum TalonSRXBrakeModeE { kBRAKE, kCOAST, kNOT_APPLICABLE }

	/** 
	 * SpeedController types supported in our code
	 */
	public static enum SpdCntlrTypeE {
		kTM_FAKEABLE_CAN_TALON,
//		kTM_FAKEABLE_PWM_TALON,
//		kTM_FAKE_TALON,
//		kTM_FAKE_CAN_TALON,
//		kTM_TALON_SRX, 
//		kTM_CAN_TALON,
//		kTALON, //for both Talon and TalonSR
//		kTALON_SRX, 
//		kCAN_TALON,
//		kUNKNOWN_TYPE
		;
	}
	
	public static SpdCntlrTypeE getSpdCntlrType(SpeedController spdCntlr) {
		SpdCntlrTypeE ans = null;
		//Note: x=a?b:c; means if(a){x=b;}else{x=c;} -- conditional operator [744conditionalOp]
		ans = (spdCntlr instanceof TmFakeable_CANTalon) ? SpdCntlrTypeE.kTM_FAKEABLE_CAN_TALON :
//			(spdCntlr instanceof TmFakeable_PwmTalon) ? SpdCntlrTypeE.kTM_FAKEABLE_PWM_TALON :
				null;
		return ans;
	}

	/**
	 * documents MXP pins, which ones have dual-use, and makes it possible to ensure
	 * that the code isn't trying to use a pin as a PWM one place and a DIO somewhere
	 * else and other such issues.  This enum should never need to change except to
	 * correct errors.
	 * @author JudiA
	 *
	 */
	public static enum MxpPinNbrsE {
		MXP_PIN_01_5VOLTS(1, "+5V"),
		MXP_PIN_02_AO0(2, "AO0"),
		MXP_PIN_03_AI0(3, "AI4 (MXP AI0)"),
		MXP_PIN_04_AO1(4, "AO1"),
		MXP_PIN_05_AI1(5, "AI5 (MXP AI1)"),
		MXP_PIN_06_AGND(6, "AGND"),
		MXP_PIN_07_AI2(7, "AI6 (MXP AI2)"),
		MXP_PIN_08_DGND(8, "DGND"),
		MXP_PIN_09_AI3(9, "AI7 (MXP AI3)"),
		MXP_PIN_10_UART_RX(10, "UART.RX"),
		MXP_PIN_11_DIO0_PWM0(11, "DIO10/PWM10 (MXP DIO0/PWM0)"),
		MXP_PIN_12_DGND(12, "DGND"),
		MXP_PIN_13_DIO1_PWM1(13, "DIO11/PWM11 (MXP DIO1/PWM1)"),
		MXP_PIN_14_UART_TX(14, "UART.TX"),
		MXP_PIN_15_DIO2_PWM2(15, "DIO12/PWM12 (MXP DIO2/PWM2)"),
		MXP_PIN_16_DGND(16, "DGND"),
		MXP_PIN_17_DIO3_PWM3(17, "DIO13/PWM13 (MXP DIO3/PWM3)"),
		MXP_PIN_18_DIO11_PWM7(18, "DIO21/PWM17 (MXP DIO11/PWM7)"),
		MXP_PIN_19_DIO4_SPI_CS(19, "DIO14 (MXP DIO4/SPI.CS)"),
		MXP_PIN_20_DGND(20, "DGND"),
		MXP_PIN_21_DIO5_SPI_CLK(21, "DIO15 (MXP DIO5/SPI.CLK)"),
		MXP_PIN_22_DIO12_PWM8(22, "DIO22/PWM18 (MXP DIO12/PWM8)"),
		MXP_PIN_23_DIO6_SPI_MISO(23, "DIO16 (MXP DIO6/SPI.MISO)"),
		MXP_PIN_24_DGND(24, "DGND"),
		MXP_PIN_25_DIO7_SPI_MOSI(25, "DIO17 (MXP DIO7/SPI.MOSI)"),
		MXP_PIN_26_DIO13_PWM9(26, "DIO23/PWM19 (MXP DIO13/PWM9)"),
		MXP_PIN_27_DIO8_PWM4(27, "DIO18/PWM14 (MXP DIO8/PWM4)"),
		MXP_PIN_28_DGND(28, "DGND"),
		MXP_PIN_29_DIO9_PWM5(29, "DIO19/PWM15 (MXP DIO9/PWM5)"),
		MXP_PIN_30_DGND(30, "DGND"),
		MXP_PIN_31_DIO10_PWM6(31, "DIO20/PWM16 (MXP DI1O/PWM6)"),
		MXP_PIN_32_DIO14_I2C_SCL(32, "DIO24 (MXP DIO14/I2C.SCL)"),
		MXP_PIN_33_3VOLTS(33, "+3.3V"),
		MXP_PIN_34_DIO15_I2C_SDA(34, "DIO25 (MXP DIO15/I2C.SDA)"),
		;
		private final int ePinNbr;
		private final String eDescription;
		private final TmToolsI.TtNamedAssignments eNamedAssignments; 
		private MxpPinNbrsE(int pinNbr, String description) { 
			ePinNbr = pinNbr;
			eDescription = description;
			eNamedAssignments = new TmToolsI.TtNamedAssignments("MXP Pin Nbr");
		}

		public void assign(String assignTo) { eNamedAssignments.assign(assignTo); }
		public boolean isAssigned() { return eNamedAssignments.isAssigned(); }
		public String showAssignedTo() { return eNamedAssignments.showAssignedTo(); }

		public String toString() {
			String ans = "MXP pin " + ePinNbr + " [" + eDescription + "]";
			return ans;
		}
	}

	/** This class pairs a module with a connection on that module, verifying validity of the pairing
	 * 
	 * @author JudiA
	 *
	 */
	public class RoModConnIoPair {
		private RoNamedModulesE l_namedModDef;
		private RoNamedConnectionsE l_namedConnDef;
		private RoFeatureTypesE l_modFeatureType;
		private RoFeatureTypesE l_connFeatureType;
		private RoFeatureTypesE l_connExpectedModFeatType;
		//		private String l_namedIoDefName;
		private TmHdwrRoMap.RoNamedIoE l_namedIoDef;
		private TmFakeable_CANTalon l_fakeableCanTalonObj = null;
		//		private TmFakeable_PwmTalon l_fakeablePwmTalonObj = null;



		public RoModConnIoPair(TmHdwrRoMap.RoNamedIoE namedIoDef, TtDebuggingE debugFlag) {
			l_namedIoDef = namedIoDef;
			if(namedIoDef==null) {
				throw TmExceptions.getInstance().new InvalidMappedIoDefEx("RoNamedIoE parm cannot be null");
			}
			
			try {
				l_namedModDef = namedIoDef.getNamedModuleDef();
				l_namedConnDef = namedIoDef.getNamedConnDef();
				l_modFeatureType = l_namedModDef.getModFeatureTypeDef();
				l_connFeatureType = l_namedConnDef.getFeatureTypeDef();
				l_connExpectedModFeatType = l_namedConnDef.getFeatureTypeDef().getExpectedModFeatType();
			} catch (Throwable t) {
				TmExceptions.reportExceptionMultiLine(t, "Exception for RoNamedIoE." + namedIoDef.name());
			}
		}

		public RoNamedIoE getNamedIoDef() {	return l_namedIoDef; }
		public RoNamedModulesE getNamedModDef() { return l_namedModDef; }
		public RoNamedConnectionsE getNamedConnDef() { return l_namedConnDef; }
		public RoFeatureTypesE getModFeatureTypeDef() { return l_modFeatureType; }
		public RoFeatureTypesE getConnFeatureTypeDef() { return l_connFeatureType; }
		public RoFeatureTypesE getConnExpectedModFeatureTypeDef() { return l_connExpectedModFeatType; }

		public SpeedController getMotorObject() {
			SpeedController ans;

			switch(l_namedModDef.getModFeatureTypeDef()) {
			case CAN_TALON_SRX_MODULE:
				if(l_fakeableCanTalonObj == null) {
					l_fakeableCanTalonObj = new TmFakeable_CANTalon(getNamedIoDef()); //l_namedModDef);
				}
				ans = l_fakeableCanTalonObj;
				break;
			default:
				throw TmExceptions.getInstance().new InappropriateMappedIoDefEx("getMotorObject() called for invalid RoModConnIoPair (RoNamedIoE." + 
						getNamedIoDef().name() + ") - not a motor");
				//break;		
			}
			return ans;
		}

		//we'll add more methods to actually read/write the i/o once we know what we need 
	}

	public static class RoModConnIoPairManager {

		private static RoModConnIoPair[][] m_modConnPairings;
		private static int m_namedModulesCount;
		private static int m_namedConnectionsCount;
//		static { //kind of like a constructor, but doesn't create or require an instance
//			m_namedModulesCount = RoNamedModulesE.values().length;
//			m_namedConnectionsCount = RoNamedConnectionsE.values().length;
//			m_modConnPairings = new RoModConnIoPair[m_namedModulesCount][m_namedConnectionsCount];
//			for(int m=0; m<m_namedModulesCount; m++) {
//				for(int c=0; c<m_namedConnectionsCount; c++) {
//					m_modConnPairings[m][c] = null;
//				}
//			}
//			l_populated = false;
//		}
		
		protected RoModConnIoPairManager() {
//			m_namedModulesCount = RoNamedModulesE.values().length;
//			m_namedConnectionsCount = RoNamedConnectionsE.values().length;
//			m_modConnPairings = new RoModConnIoPair[m_namedModulesCount][m_namedConnectionsCount];
//			for(int m=0; m<m_namedModulesCount; m++) {
//				for(int c=0; c<m_namedConnectionsCount; c++) {
//					m_modConnPairings[m][c] = null;
//				}
//			}
			l_populated = false;
		}

		private static boolean l_populated = false;
		public void populate() {
			populate(TtDebuggingE.NO_DEBUG);
		}
		public void populate(TtDebuggingE dbg) {
			boolean haveRegistrationErrors = false;
			if( ! l_populated) {
				m_namedModulesCount = RoNamedModulesE.values().length;
				m_namedConnectionsCount = RoNamedConnectionsE.values().length;
				m_modConnPairings = new RoModConnIoPair[m_namedModulesCount][m_namedConnectionsCount];
				for(int m=0; m<m_namedModulesCount; m++) {
					for(int c=0; c<m_namedConnectionsCount; c++) {
						m_modConnPairings[m][c] = null;
					}
				}
				for(RoNamedIoE e : RoNamedIoE.values()) {
					String errPrefix = "Exception for RoNamedIoE." + e.name();
					int modNdx = -1;
					int connNdx = -1;
					String msgAliasNfo = "";
					RoModConnIoPair curPairing = null;
					int itemRegCnt;
					if(e.name().equals("CLIMBER_MOTOR")) {
						String msg = "time to debug";
					}
					else if(e.name().equals("SOME_MOTOR")) {
						String msg = "time to debug";
					}
					try {
						RoNamedModulesE namedMod = e.getNamedModuleDef();
						RoNamedModulesE trueNamedMod = namedMod.getTrueModuleFor();
						msgAliasNfo = namedMod.getAliasDescription();
						modNdx = trueNamedMod.ordinal();
						connNdx = e.getNamedConnDef().ordinal();

						curPairing = m_modConnPairings[modNdx][connNdx];
						if(curPairing != null) {
							throw TmExceptions.getInstance().new DuplicateMappedIoDefEx(errPrefix + ": duplicates mod/conn of " +
									curPairing.getNamedIoDef().name() + msgAliasNfo);
						}
						m_modConnPairings[modNdx][connNdx] = getInstance().new RoModConnIoPair(e, dbg);
						itemRegCnt = RoModConnIoPairRegistration.register(e);
						if(itemRegCnt > 1) { haveRegistrationErrors = true; }

					} catch (Throwable t) {
						TmExceptions.reportExceptionMultiLine(t, "Exception for RoNamedIoE." + e.name() + msgAliasNfo);
					}
				}
				if(haveRegistrationErrors) {
					throw TmExceptions.getInstance().new MappedIoRegistrationErrorsEx("RoNamedIoE configuration errors found during registration");
				}

				l_populated = true;
			}
		}

		public RoModConnIoPair getModConnPairingObj(RoNamedIoE namedIoDef) {
			RoModConnIoPair ans = null;

			RoNamedModulesE namedMod;
			RoNamedConnectionsE namedConn;
			String modAliasNfo = "";
			int modNdx = -1;
			int connNdx = -1;
			//			RoModConnIoPair curPairing = null;

			if(namedIoDef==null) { 
				throw TmExceptions.getInstance().new InvalidMappedIoDefEx("RoNamedIoE parm cannot be null");
			}

			try {
				namedMod = namedIoDef.getNamedModuleDef();
				if(namedMod.isModuleAlias()) {
					modAliasNfo = namedMod.getAliasDescription();
					namedMod = namedMod.getTrueModuleFor();
				}
				namedConn = namedIoDef.getNamedConnDef();
				modNdx = namedMod.ordinal();
				connNdx = namedConn.ordinal();
				ans = m_modConnPairings[modNdx][connNdx];
			} catch (Throwable t) {
				String msg = "Exception for RoNamedIoE." + namedIoDef.name() + modAliasNfo + " ";
				TmExceptions.reportExceptionMultiLine(t, msg);
			}

			if(ans==null) { 
				throw TmExceptions.getInstance().new InvalidMappedIoDefEx("for RoNamedIoE." + namedIoDef.name() + 
						modAliasNfo + ": RoModConnIoPair["+modNdx+"]["+connNdx+"] is null");
			}

			return ans;
		}
	}

	private static RoModConnIoPairRegistration m_modConnPairRegistration = new RoModConnIoPairRegistration(TtDebuggingE.NO_DEBUG);
	public static class RoModConnIoPairRegistration {

		//this only sets up a pointer to an array of pointers to something, 
		//have to fill in the individual pointers later and only then
		//create the things those pointers point to. 
		private static RegistrationInfo[][] c_thingsRegisteredArray;
		private static RegistrationInfo[] c_canTalonRegisteredArray; //module feature type = CAN_TALON_SRX_MODULE, [canid]
		private static int c_maxMods; //c_maxHids;
		private static int c_maxConns; //c_maxHidInputs;
		private static int c_maxCanIds; 

		private static RegistrationInfo[] c_mxpPinsRegisteredArray;
		private static int c_maxMxpPins;

		private class RegistrationInfo {
			private String registrationList = "<<no one>>";
			private int registrationCnt = 0;

			public int getCnt() {return registrationCnt;}
			public String getRegistrationList() {return registrationList; }

			public int register(String who) {
				if(registrationCnt == 0) { registrationList = who; }
				else { registrationList += " " + who; }
				return ++registrationCnt;
			}
		}

		//		RoNamedModulesE namedMod, RoNamedConnectionsE namedConn;
		private static boolean c_isPopulated = false;
		public static void populate() {
			populate(TtDebuggingE.NO_DEBUG);
		}
		public static void populate(/*RoModConnIoPair namedPairInstance,*/ TtDebuggingE debugFlag) {
			if( ! c_isPopulated) {
				c_maxMods = RoNamedModulesE.values().length;
				c_maxConns = RoNamedConnectionsE.values().length;
				c_maxMxpPins = MxpPinNbrsE.values().length;
				c_maxCanIds = Cnst.CAN_ID_MAX + 1;

				c_thingsRegisteredArray = new RegistrationInfo[c_maxMods][c_maxConns]; //TODO handle module aliases
				c_mxpPinsRegisteredArray = new RegistrationInfo[c_maxMxpPins];
				c_canTalonRegisteredArray = new RegistrationInfo[c_maxCanIds];

				for(int m=0; m<c_maxMods; m++)
				{
					for(int c=0; c<c_maxConns; c++)
					{
						//this is where we set up the individual entries
						c_thingsRegisteredArray[m][c] = m_modConnPairRegistration.new RegistrationInfo();
					}
				}
				for(int p=0; p<c_maxMxpPins; p++)
				{
					c_mxpPinsRegisteredArray[p] = m_modConnPairRegistration.new RegistrationInfo();
				}
				for(int c=0; c<c_maxCanIds; c++)
				{
					c_canTalonRegisteredArray[c] = m_modConnPairRegistration.new RegistrationInfo();
				}
				c_isPopulated = true;
			}
		}
		public RoModConnIoPairRegistration(/*RoModConnIoPair namedPairInstance,*/ TtDebuggingE debugFlag) {
			//let populate() do the work later
		}

		/**
		 * Registers the module/connection pairing used by namedIoDef.  If the connection is one
		 * of the MXP pins on the roboRIO, it registers the pin too.
		 * @param namedIoDef
		 * @return the number of times this module/connection has been registered or, if it's a MXP pin, the number of
		 *         times the MXP pin has been registered, whichever is greater.
		 */
		public static int register(RoNamedIoE namedIoDef) {
			int cnt = 0;
			int mxpPinRegistrationCnt = 0;
			int canTalonCanidRegistrationCnt = 0;
			//			RoModConnIoPair modConnIoPair = namedIoDef.getModConnIoPairObj();

			//			RoNamedModulesE namedModDef = namedIoDef.getNamedModuleDef();
			RoNamedConnectionsE namedConnDef = namedIoDef.getNamedConnDef();
			RoFeatureTypesE connFeatureTypeDef = namedConnDef.getFeatureTypeDef();

			int modNdx = namedIoDef.getNamedModuleDef().ordinal(); //modConnIoPair.getNamedModDef().ordinal();
			int connNdx = namedConnDef.ordinal(); //namedIoDef.getNamedConnDef().ordinal(); //modConnIoPair.getNamedConnDef().ordinal();

			String whoIsRegistering = namedIoDef.name();

			if( ! c_isPopulated) { populate(); }

			RoNamedModulesE namedMod = namedIoDef.getNamedModuleDef();
			if(namedMod.getModFeatureTypeDef().equals(RoFeatureTypesE.CAN_TALON_SRX_MODULE)) {
				int canId = namedMod.getModuleIndex();
				canTalonCanidRegistrationCnt = c_canTalonRegisteredArray[canId].register(whoIsRegistering);
				if(canTalonCanidRegistrationCnt > 1) {
					cnt = canTalonCanidRegistrationCnt;
					P.println("[T744 WARNING] Duplicate canId found for input/output source defs in RoNamedIoE (check RoNamedModulesE item definitions): " + 
							c_canTalonRegisteredArray[canId].getRegistrationList());
				}
			}
			else
			if(Tt.isWithinCount(modNdx, 0, c_maxMods) && Tt.isWithinCount(connNdx, 0, c_maxConns)) {
				cnt = c_thingsRegisteredArray[modNdx][connNdx].register(whoIsRegistering);
				if(cnt > 1) {
					P.println("[T744 WARNING] Duplicate RO input/output source defs in RoNamedIoE: " + 
							c_thingsRegisteredArray[modNdx][connNdx].getRegistrationList());
				}
				if(connFeatureTypeDef.isRioMxpPin()) {
					mxpPinRegistrationCnt = registerMxpPin(namedConnDef.getMxpPinDef(), whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
				}
				if(connFeatureTypeDef.equals(RoFeatureTypesE.M_SPI_PORT)) {
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_19_DIO4_SPI_CS, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_21_DIO5_SPI_CLK, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_23_DIO6_SPI_MISO, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_25_DIO7_SPI_MOSI, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
				}
				if(connFeatureTypeDef.equals(RoFeatureTypesE.M_I2C_PORT)) {
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_32_DIO14_I2C_SCL, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
					mxpPinRegistrationCnt = registerMxpPin( MxpPinNbrsE.MXP_PIN_34_DIO15_I2C_SDA, whoIsRegistering);
					if(mxpPinRegistrationCnt > cnt) { cnt = mxpPinRegistrationCnt; }
				}

			} else {
				String msg = "TmHdwrRoPhys.register() found enum ordinals that were greater than the number of enum items. " +
						modNdx + ">" + (c_maxMods-1) + ", " + connNdx + ">" + (c_maxConns-1) +
						namedIoDef.name() + " being registered by " + whoIsRegistering;
				P.println(msg);
			}

			return cnt;
		}
		private static int registerMxpPin(MxpPinNbrsE pinToRegister, String whoIsRegistering) {
			int mxpPinNdx = pinToRegister.ordinal();
			int mxpPinRegistrationCnt = c_mxpPinsRegisteredArray[mxpPinNdx].register(whoIsRegistering);
			if(mxpPinRegistrationCnt > 1) {
				P.println("[T744 WARNING] Duplicate roboRIO MXP Pin source defs in RoNamedIoE items: " + 
						c_mxpPinsRegisteredArray[mxpPinNdx].getRegistrationList());
			}
			return mxpPinRegistrationCnt;
		}

	}


}
