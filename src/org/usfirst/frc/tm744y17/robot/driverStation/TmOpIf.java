package org.usfirst.frc.tm744y17.robot.driverStation;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsPhys.T744HidButton;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDriveTrain.DriveJoysticksCenterMotorOptE;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsDrvGearShift;

import java.util.Arrays;

import org.usfirst.frc.tm744y17.robot.commands.TmCCmdIntakeOff;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdIntakeOn;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdIntakeOnReverse;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterManualToggleAbacus;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterManualToggleMotor;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterManualToggleTrigger;
import org.usfirst.frc.tm744y17.robot.commands.TmCCGrpGearFlippersExtendAndBackAway;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdDriveJoysticksAndCenterMotorCoast;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdDriveShiftGear;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdGearFlippersExtend;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdGearFlippersRetract;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterStart;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterStop;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterStartManualMode;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterUseHighSpeed;
import org.usfirst.frc.tm744y17.robot.commands.TmCCmdShooterUseLowSpeed;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdCameraLedsToggle;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdCameraSelectFront;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdCameraSelectRear;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdClimberToggleEnable;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdDriveSetDirectionForward;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdDriveSetDirectionReverse;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdShowAllAutonAlgAndPrefs;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdShowAllDsIoOnConsole;
import org.usfirst.frc.tm744y17.robot.commands.TmTCmdShowAllRoIoOnConsole;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap;
//import org.usfirst.frc.tm744y16.robot.config.Tm16Opts;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrDsMap.DsInputSourceDefE;

public class TmOpIf {

	/*---------------------------------------------------------------
	 * getInstance stuff
	 *--------------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmOpIf m_instance;

	public static synchronized TmOpIf getInstance() {
		if (m_instance == null) {
			m_instance = new TmOpIf();
		}
		return m_instance;
	}

	private TmOpIf() {
	}
	/*--------------------end of getInstance stuff-------------------*/

	private static DsInputSourceDefE m_leftDriveDsInput; // = DsInputSourceDefE.ROBOT_DRIVE_LEFT_SPEED_INPUT;
	private static DsInputSourceDefE m_rightDriveDsInput; // = DsInputSourceDefE.ROBOT_DRIVE_RIGHT_SPEED_INPUT;

	public static DsInputSourceDefE getLeftDriveDsInput() { return m_leftDriveDsInput; }
	public static DsInputSourceDefE getRightDriveDsInput() { return m_rightDriveDsInput; }
	
    static T744HidButton btnCameraLedsToggle;
    
    static T744HidButton btnCameraSelectFront;
    static T744HidButton btnCameraSelectRear;
    
    static T744HidButton btnSetDrvToHighGear;
    static T744HidButton btnSetDrvToLowGear;
    static T744HidButton btnSetDrvDirectionForward;
    static T744HidButton btnSetDrvDirectionReverse;
    
    static T744HidButton btnStartDrvJoysticksAndCenterMotorCoast; //TmCCmdDriveJoysticksAndCenterMotorCoast
    static T744HidButton btnStopDrvJoysticksAndCenterMotorCoast;
    
    static T744HidButton btnBallIntakeOnReverse;
    static T744HidButton btnBallIntakeOn;
    static T744HidButton btnBallIntakeOff;
    
    static T744HidButton btnGearFlippersExtendAndDrvBkwd;
    static T744HidButton btnGearFlippersExtend;
    static T744HidButton btnGearFlippersRetract;
    
    static T744HidButton btnShooterStart;
    static T744HidButton btnShooterStop;
    static T744HidButton btnShooterHighSpeed;
    static T744HidButton btnShooterLowSpeed;
    static T744HidButton btnShooterStartManual;
    static T744HidButton btnShooterManualToggleDrum;
    static T744HidButton btnShooterManualToggleAbacus;
    static T744HidButton btnShooterManualToggleTrigger;
    
    static T744HidButton btnClimberToggleEnable;
    
    static T744HidButton btnShowAllDsIo;
    static T744HidButton btnShowAllRoIo;
    static T744HidButton btnShowAllAutonAndPrefs;
   


	
	public static void doInstantiate() {
		try {
			TmHdwrDsMap.inspectDsInputSources();
		}
		catch(TmExceptions.InvalidMappedIoDefEx t) {
			String msg = "Caught exception: ";
			TmExceptions.reportExceptionMultiLine(t, msg + t.getMessage());
			System.exit(-2);
		}
		catch(Throwable t) {
			String msg = "Caught exception: ";
			TmExceptions.reportExceptionMultiLine(t, msg + t.getMessage());
			System.exit(-2);
		}
		
		String errMsgLocationDescr;
		errMsgLocationDescr = "setting up left/right drive inputs";
		try {
			
				m_leftDriveDsInput = DsInputSourceDefE.ROBOT_DRIVE_LEFT_SPEED_INPUT;
				m_rightDriveDsInput = DsInputSourceDefE.ROBOT_DRIVE_RIGHT_SPEED_INPUT;
				
			} catch (NullPointerException t) {
				String errMsg = "ERROR NullPointerException " + errMsgLocationDescr + ": " + 
									t.toString() + Arrays.toString(t.getStackTrace());
				System.out.println(errMsg);
				t.printStackTrace();
			} catch (ExceptionInInitializerError t) {
				String errMsg = "ERROR ExceptionInInitializerError " + errMsgLocationDescr + ": " + 
						t.toString() + Arrays.toString(t.getStackTrace());
				System.out.println(errMsg);
				t.printStackTrace();
			} catch (Throwable t) {
				String errMsg = "ERROR Unhandled exception " + errMsgLocationDescr + ": " + 
						t.toString() + Arrays.toString(t.getStackTrace());
				System.out.println(errMsg);
				t.printStackTrace();
			}
		
		//Note: x=a?b:c; means if(a){x=b;}else{x=c;}
//		m_useJoystickAsAlternateMechanismController =  ! Tm16Opts.isRunningMechWithGameController();
//		m_haveBothControllers = Tm16Opts.isDrivingWithGameController() && Tm16Opts.isRunningMechWithGameController(); //Tm16Opts.isRunningOnSwTestFixture() ? false : true;

//		errMsgLocationDescr = "while making buttons";
//		try {
//			DsInputSourceDefE thing = DsInputSourceDefE.CAMERA_LEDS_TOGGLE_ON_OFF_BTN;
//			btnToggleCameraLeds = DsInputSourceDefE.CAMERA_LEDS_TOGGLE_ON_OFF_BTN.makeButton();
//			
//		} catch (NullPointerException t) {
//			String errMsg = "ERROR NullPointerException " + errMsgLocationDescr + ": " + 
//					t.toString() + Arrays.toString(t.getStackTrace());
//			System.out.println(errMsg);
//			t.printStackTrace();
//		} catch (ExceptionInInitializerError t) {
//			String errMsg = "ERROR ExceptionInInitializerError " + errMsgLocationDescr + ": " + 
//					t.toString() + Arrays.toString(t.getStackTrace());
//			System.out.println(errMsg);
//			t.printStackTrace();
//		} catch (Throwable t) {
//			String errMsg = "ERROR Unhandled exception " + errMsgLocationDescr + ": " + 
//					t.toString() + Arrays.toString(t.getStackTrace());
//			System.out.println(errMsg);
//			t.printStackTrace();
//		}

	}

	public void createCommands() {
		String errMsgLocationDescr = "while making buttons";
		try {
			
//			DsInputSourceDefE thing = DsInputSourceDefE.CAMERA_LEDS_TOGGLE_ON_OFF_BTN;
			btnCameraLedsToggle = DsInputSourceDefE.CAMERA_LEDS_TOGGLE_ON_OFF_BTN.makeButton();
			
		    btnCameraSelectFront = DsInputSourceDefE.CAMERA_SELECTION_FRONT_BTN.makeButton();
		    btnCameraSelectRear = DsInputSourceDefE.CAMERA_SELECTION_REAR_BTN.makeButton();
			
		    btnSetDrvToHighGear = DsInputSourceDefE.DRIVE_IN_HIGH_GEAR_BTN.makeButton();
		    btnSetDrvToLowGear = DsInputSourceDefE.DRIVE_IN_LOW_GEAR_BTN.makeButton();
		    btnSetDrvDirectionForward = DsInputSourceDefE.DRIVE_DIRECTION_FORWARD_BTN.makeButton();
		    btnSetDrvDirectionReverse = DsInputSourceDefE.DRIVE_DIRECTION_REVERSE_BTN.makeButton();

		    btnStartDrvJoysticksAndCenterMotorCoast = DsInputSourceDefE.DRIVE_START_JOYSTICKS_AND_CENTER_MOTOR_COAST_BTN.makeButton();
		    btnStopDrvJoysticksAndCenterMotorCoast = DsInputSourceDefE.DRIVE_STOP_JOYSTICKS_AND_CENTER_MOTOR_COAST_BTN.makeButton();
		    
		    btnBallIntakeOnReverse = DsInputSourceDefE.BALL_INTAKE_MOTORS_ON_REVERSE_BTN.makeButton();
		    btnBallIntakeOn = DsInputSourceDefE.BALL_INTAKE_MOTORS_ON_BTN.makeButton();
		    btnBallIntakeOff = DsInputSourceDefE.BALL_INTAKE_MOTORS_OFF_BTN.makeButton();
		    
		    btnGearFlippersExtendAndDrvBkwd = DsInputSourceDefE.GEAR_FLIPPER_EXTEND_AND_DRIVE_BACKWARDS_BTN.makeButton();
		    btnGearFlippersExtend = DsInputSourceDefE.GEAR_FLIPPER_EXTEND_BTN.makeButton();
		    btnGearFlippersRetract = DsInputSourceDefE.GEAR_FLIPPER_RETRACT_BTN.makeButton();
		    
		    btnShooterStart = DsInputSourceDefE.SHOOTER_START_STATE_MACHINE_BTN.makeButton();
		    btnShooterStop = DsInputSourceDefE.SHOOTER_OFF_BTN.makeButton();
		    btnShooterHighSpeed = DsInputSourceDefE.SHOOTER_HIGH_SPEED_BTN.makeButton();
		    btnShooterLowSpeed = DsInputSourceDefE.SHOOTER_LOW_SPEED_BTN.makeButton();
		    btnShooterStartManual = DsInputSourceDefE.SHOOTER_START_MANUAL_MODE_BTN.makeButton();
		    btnShooterManualToggleDrum = DsInputSourceDefE.SHOOTER_MANUAL_TOGGLE_DRUM_MOTOR_BTN.makeButton();
		    btnShooterManualToggleAbacus = DsInputSourceDefE.SHOOTER_MANUAL_TOGGLE_ABACUS_BTN.makeButton();
		    btnShooterManualToggleTrigger = DsInputSourceDefE.SHOOTER_MANUAL_TOGGLE_TRIGGER_BTN.makeButton();

		    btnClimberToggleEnable = DsInputSourceDefE.CLIMBER_TOGGLE_ENABLE_BTN.makeButton();
		    
		    btnShowAllDsIo = DsInputSourceDefE.SHOW_ALL_BUTTON_SETTINGS_IN_CONSOLE.makeButton();
		    btnShowAllRoIo = DsInputSourceDefE.SHOW_ALL_ROBOT_IO_SETTINGS_IN_CONSOLE.makeButton();
		    btnShowAllAutonAndPrefs = DsInputSourceDefE.SHOW_ALL_AUTON_ALGS_AND_PREFERENCES_IN_CONSOLE.makeButton();


		} catch (NullPointerException t) {
			String errMsg = "ERROR NullPointerException " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		} catch (ExceptionInInitializerError t) {
			String errMsg = "ERROR ExceptionInInitializerError " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		} catch (Throwable t) {
			String errMsg = "ERROR Unhandled exception " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		}
    	errMsgLocationDescr = "creating commands";
		try {
    	
     	btnCameraLedsToggle.whenPressed(new TmTCmdCameraLedsToggle());
     	
        btnCameraSelectFront.whenPressed(new TmTCmdCameraSelectFront());
        btnCameraSelectRear.whenPressed(new TmTCmdCameraSelectRear());
        
	    btnSetDrvToHighGear.whenPressed(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.HIGH));
	    btnSetDrvToLowGear.whenPressed(new TmCCmdDriveShiftGear(TmSsDrvGearShift.DrvGearsE.LOW));
	    btnSetDrvDirectionForward.whenPressed(new TmTCmdDriveSetDirectionForward());
	    btnSetDrvDirectionReverse.whenPressed(new TmTCmdDriveSetDirectionReverse());

	    btnStartDrvJoysticksAndCenterMotorCoast.whenPressed(new TmTCmdDriveJoysticksAndCenterMotorCoast(
	    									DriveJoysticksCenterMotorOptE.DRIVE_JOYSTICKS_CENTER_MOTOR_COAST));
	    btnStopDrvJoysticksAndCenterMotorCoast.whenPressed(new TmTCmdDriveJoysticksAndCenterMotorCoast(
	    									DriveJoysticksCenterMotorOptE.DRIVE_JOYSTICKS_CENTER_MOTOR_MATCH_FRONT_AND_REAR));
	    
	    btnShooterStart.whenPressed(new TmCCmdShooterStart());
	    btnShooterStop.whenPressed(new TmCCmdShooterStop());
	    btnShooterHighSpeed.whenPressed(new TmCCmdShooterUseHighSpeed());
	    btnShooterLowSpeed.whenPressed(new TmCCmdShooterUseLowSpeed());
	    btnShooterStartManual.whenPressed(new TmCCmdShooterStartManualMode());
	    btnShooterManualToggleDrum.whenPressed(new TmCCmdShooterManualToggleMotor());
	    btnShooterManualToggleAbacus.whenPressed(new TmCCmdShooterManualToggleAbacus());
	    btnShooterManualToggleTrigger.whenPressed(new TmCCmdShooterManualToggleTrigger());
	    
	    btnBallIntakeOnReverse.whenPressed(new TmCCmdIntakeOnReverse());
	    btnBallIntakeOn.whenPressed(new TmCCmdIntakeOn());
	    btnBallIntakeOff.whenPressed(new TmCCmdIntakeOff());
	    
	    btnGearFlippersExtendAndDrvBkwd.whenPressed(new TmCCGrpGearFlippersExtendAndBackAway());
	    btnGearFlippersExtend.whenPressed(new TmCCmdGearFlippersExtend());
	    btnGearFlippersRetract.whenPressed(new TmCCmdGearFlippersRetract());
	    
	    btnShowAllDsIo.whenPressed(new TmTCmdShowAllDsIoOnConsole());
	    btnShowAllRoIo.whenPressed(new TmTCmdShowAllRoIoOnConsole());
	    btnShowAllAutonAndPrefs.whenPressed(new TmTCmdShowAllAutonAlgAndPrefs());
	    
		btnClimberToggleEnable.whenPressed(new TmTCmdClimberToggleEnable());


        
        //(we use conditional ops here to make it easy to avoid creating commands in
        //memory if we decide we don't want to have them on SmartDashboard
        //Note: x=a?b:c; means if(a){x=b;}else{x=c;}
//        m_Cmd_ShowAllDsInputs = true ? new TmTCmdShowAllDsInputDefinitions() : null;
//        m_Cmd_ShowAllMotorCfgs = true ? new TmTCmdShowAllMotorCfgInfo() : null;
//        m_Cmd_ShowAllRoInputs = true ? new TmHdwr16RoMap.LocalCmdShowAll() : null;
//        m_Cmd_ToggleCameraLeds = true ? new TmTCmdToggleCameraLeds() : null;
//
//        m_Cmd_RunAutonAlgE = true ? new TmACgrpAlgE_CrossTerrainAndShootP4() : null;
//        
//        m_Cmd_ToggleShooter = true ? new TmCCmdToggleShooter() : null;
//        m_Cmd_ToggleSail = true ? new TmCCmdToggleSail() : null;
//        m_Cmd_ToggleIntake = true ? new TmTCmdToggleIntake() : null;
//        m_Cmd_ToggleRepel = true ? new TmTCmdToggleRepel() : null;
////        m_Cmd_RunArmWithJoystick = true ? new TmTCmdRunArmWithJoystick() : null;
        
//        TmSdDbgSD.dbgPutSendableByName(M.ALWAYS, m_Cmd_ShowAllDsInputs);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ShowAllMotorCfgs);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ShowAllRoInputs);
//        TmSdDbgSD.dbgPutSendableByName(M.ALWAYS, m_Cmd_ToggleCameraLeds);
//        
//        TmSdDbgSD.dbgPutSendableByName(M.ALWAYS, m_Cmd_RunAutonAlgE);
//
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ToggleShooter);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ToggleSail);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ToggleIntake);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_ToggleRepel);
//        TmSdDbgSD.dbgPutSendableByName(F.D, m_Cmd_RunArmWithJoystick);
		} catch (NullPointerException t) {
			String errMsg = "ERROR NullPointerException " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		} catch (ExceptionInInitializerError t) {
			String errMsg = "ERROR ExceptionInInitializerError " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		} catch (Throwable t) {
			String errMsg = "ERROR Unhandled exception " + errMsgLocationDescr + ": " + 
					t.toString() + Arrays.toString(t.getStackTrace());
			System.out.println(errMsg);
			t.printStackTrace();
		}
	}

}
