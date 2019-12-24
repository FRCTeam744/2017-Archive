package org.usfirst.frc.tm744y17.robot.exceptions;

import java.util.Arrays;

import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;

/**
 * Keep all our exceptions in a single file for easier reference.
 * Throw exception Aaa via: throw TmExceptions.getInstance().new Aaa(...);
 * Catch exception Aaa via: catch(TmException.Aaa t)
 * @author JudiA
 *
 */
public class TmExceptions {
	/*---------------------------------------------------------
	 * getInstance stuff                                      
	 *---------------------------------------------------------*/
	/** 
	 * handle making the singleton instance of this class and giving
	 * others access to it
	 */
	private static TmExceptions m_instance;

	public static synchronized TmExceptions getInstance() {
		if (m_instance == null) {
			m_instance = new TmExceptions();
		}
		return m_instance;
	}

	private TmExceptions() {
	}
	/*----------------end of getInstance stuff----------------*/

	public class InappropriatePreferenceRequestEx extends RuntimeException {
		public InappropriatePreferenceRequestEx(String msg) {
			super(msg);
		}
	}
	public class MotorSafetyTimeoutEx extends RuntimeException {
		public MotorSafetyTimeoutEx(String msg) {
			super(msg);
		}
	}
	public class InappropriateMappedIoDefEx extends RuntimeException {
		public InappropriateMappedIoDefEx(String msg) {
			super(msg);
		}
	}
	public class InvalidMappedIoDefEx extends RuntimeException {
		public InvalidMappedIoDefEx(String msg) {
			super(msg);
		}
	}
	public class DuplicateMappedIoDefEx extends RuntimeException {
		public DuplicateMappedIoDefEx(String msg) {
			super(msg);
		}
	}	
	public class MappedIoRegistrationErrorsEx extends RuntimeException {
		public MappedIoRegistrationErrorsEx(String msg) {
			super(msg);
		}
	}	
	public class DuplicateAssignmentOfMappedIoDefEx extends RuntimeException {
		public DuplicateAssignmentOfMappedIoDefEx(String msg) {
			super(msg);
		}
	}	
	public class MappedIoDefNoFeatureIndexEx extends RuntimeException {
		public MappedIoDefNoFeatureIndexEx(String msg) {
			super(msg);
		}
	}
	public class UnsupportedEntityIndexTypeEx extends RuntimeException {
		public UnsupportedEntityIndexTypeEx(String msg) {
			super(msg);
		}
	}
	
	public class CannotSimulateCANTalonEx extends RuntimeException {
		public CannotSimulateCANTalonEx(String msg) {
			super(msg);
		}
	}
	
	public class DuplicateInstanceOfSingletonClassEx extends RuntimeException {
		public DuplicateInstanceOfSingletonClassEx(String msg) {
			super(msg);
		}
	}
	
	public class CameraGetVideoFailedEx extends RuntimeException {
		public CameraGetVideoFailedEx(String msg) {
			super(msg);
		}
	}
	
	public static void reportExceptionOneLine(Throwable t, String msgPrefix) {
		P.println(msgPrefix + ": " + Arrays.toString(t.getStackTrace()));
	}
	public static void reportExceptionMultiLine(Throwable t, String msgPrefix) {
		P.println(msgPrefix + ": " + t.toString());
		t.printStackTrace();
	}

}
