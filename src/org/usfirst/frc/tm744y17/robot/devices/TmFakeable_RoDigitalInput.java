package org.usfirst.frc.tm744y17.robot.devices;

import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap.RoNamedIoE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

import edu.wpi.first.wpilibj.DigitalInput;

public class TmFakeable_RoDigitalInput {
	
	private RoNamedIoE m_namedIoDef = null;
	private DigitalInput m_realObj = null;
	private boolean m_beingFaked = false;

	private void setupAsFake() {
		m_realObj = null;
		m_beingFaked = true;
	}

	public boolean isFake() {
		return m_beingFaked;
	}

	public boolean isReal() {
		return !m_beingFaked;
	}

	private FakeParms m_fakeParms;

	public class FakeParms {
		//any fields or methods that the 'fake' object needs to
		//emulate should be coded here.
		boolean f_thing;

		public boolean get() {
			return f_thing;
		}

//		public void set(int newVal) {
//			f_thing = newVal;
//		}

		public FakeParms() {
			f_thing = false;
		}
	}

	//top-level constructor
	public TmFakeable_RoDigitalInput(RoNamedIoE namedIoDef) {
		
		m_namedIoDef = namedIoDef;
		
		m_fakeParms = new FakeParms();
		String thisClassName = Tt.extractClassName(this);
		String nameForObj = Tt.extractClassName(namedIoDef) + "." + namedIoDef.name();
		
		String errMsgPrefix = thisClassName + " " + nameForObj + " got an exception:";

		try {
			int chan = namedIoDef.getNamedConnDef().getConnectionIndex();
			m_realObj = new DigitalInput(chan);
			//set up whatever else is needed here...
		}
		//some devices don't generate errors or exceptions when they're not
		//present.  For those, need to use preferences file or some other 
		//means of telling the code to use 'fake' devices
//		catch (Fixme_ExceptionIndicatingRealDeviceNotAvailable t) {
//			TmExceptions.reportExceptionOneLine(t, errMsgPrefix);
//			setupAsFake();
//		} 
		catch (Throwable t) {
			TmExceptions.reportExceptionMultiLine(t, errMsgPrefix);
			setupAsFake();
		}
//		if (Fixme_somePreferencesSettingOrOptionFlag == "use fake") {
//			Tt.println("Per ... setting, " + thisClassName + " " + nameForObj + " will be a FAKE Fixme_ClassName");
//		} else 
		if (isFake()) {
			P.println(thisClassName + " " + nameForObj + " will be a FAKE DigitalInput");
		}
	}

	//--- helper methods for methods used with real device
	public boolean get() {
		if (isFake()) {
			return m_fakeParms.get();
		} else {
			return m_realObj.get();
		}
	}

//	public void set(boolean newVal) {
//		if (isFake()) {
//			m_fakeParms.set(newVal);
//		} else {
//			m_realObj..set(newVal);
//		}
//	}
}
