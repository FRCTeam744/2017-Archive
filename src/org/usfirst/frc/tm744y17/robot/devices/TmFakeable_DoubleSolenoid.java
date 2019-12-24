package org.usfirst.frc.tm744y17.robot.devices;

import org.usfirst.frc.tm744y17.robot.config.Tm17Opts;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoMap;
import org.usfirst.frc.tm744y17.robot.config.TmHdwrRoPhys.RoNamedModulesE;
import org.usfirst.frc.tm744y17.robot.exceptions.TmExceptions;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SolenoidBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

//public class DoubleSolenoid extends SolenoidBase implements LiveWindowSendable {
public class TmFakeable_DoubleSolenoid {
	private DoubleSolenoid m_realObj = null;
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
		//any parms or methods that the 'fake' object needs to
		//emulate should be coded here.
		private DoubleSolenoid.Value f_solDirection = DoubleSolenoid.Value.kOff;
		
		public DoubleSolenoid.Value get() {
			return f_solDirection;
		}
		
		public void set(DoubleSolenoid.Value value) {
			f_solDirection = value;
		}
		
	}

	private static TmHdwrRoMap.RoNamedIoE m_forward;
	private static TmHdwrRoMap.RoNamedIoE m_reverse;
	
	public TmFakeable_DoubleSolenoid(TmHdwrRoMap.RoNamedIoE forward, 
									 TmHdwrRoMap.RoNamedIoE reverse) {
		String thisClassName = Tt.extractClassName(this);
		m_fakeParms = new FakeParms();
		m_forward = forward;
		m_reverse = reverse;
		if(forward==null || reverse==null) {
			String msg = thisClassName + ": " +
					"forward " + (forward==null?"null":"OK") + 
					", reverse " + (reverse==null?"null":"OK");
			throw new NullPointerException(msg);
		}
		if( ! forward.getNamedModuleDef().equals(reverse.getNamedModuleDef())) {
			String msg = thisClassName + ": RoNamedIoE." + forward.name() +
					" and RoNamedIoE." + reverse.name() +
					" are not on the same PCM module";
			throw TmExceptions.getInstance().new InappropriateMappedIoDefEx(msg);
		}
				
		String msgPrefix = thisClassName + " " + forward.name() + "/" + reverse.name()
		+ " got an exception";
		try {
			int modNdx = forward.getNamedModuleDef().getModuleIndex();
			int fwdNdx = forward.getNamedConnDef().getConnectionIndex();
			int revNdx = reverse.getNamedConnDef().getConnectionIndex();
			m_realObj = new DoubleSolenoid(modNdx, fwdNdx, revNdx);
			//set up whatever else is needed here...
		} catch (UnsatisfiedLinkError t) {
			//the exception we get when running in our simulation mode on a PC.
			//we don't need a big eye-catching message.  There were no exceptions
			//when running on a roboRIO with no PCMs connected.
			TmExceptions.reportExceptionOneLine(t, msgPrefix);
			setupAsFake();
		} catch (Throwable t) {
			TmExceptions.reportExceptionMultiLine(t, msgPrefix);
			setupAsFake();
		}

		
		if(forward.getNamedModuleDef().equals(RoNamedModulesE.PCM0) && ! Tm17Opts.isPcm0Installed()) {
			String msg = thisClassName + " " + forward.name() + "/" + reverse.name() + ": options/preferences say PCM0 not installed.";
			P.println(msg);
			setupAsFake();
		}
		
		if (isFake()) {
			P.println(thisClassName + " " + forward.name() + "/" + reverse.name() 
					+ " will be a FAKE DoubleSolenoid");
		} else {
			try {
				byte allSols = m_realObj.getAll();
				DoubleSolenoid.Value seed = m_realObj.get();
			} catch(Throwable t) {
				TmExceptions.reportExceptionMultiLine(t, msgPrefix);
				setupAsFake();
			}
		}
		
		if (isFake()) {
			P.println(thisClassName + " " + forward.name() + "/" + reverse.name() 
					+ " will be a FAKE DoubleSolenoid");
		}
	}

	public DoubleSolenoid.Value get() {
		DoubleSolenoid.Value ans;
		if(isFake()) { ans = m_fakeParms.get(); }
		else { ans = m_realObj.get(); }
		return ans;
	}
	
	public void set(DoubleSolenoid.Value value) {
		m_fakeParms.set(value); //an easy way to debug
		if(isReal()) { m_realObj.set(value); }
	}

}
