package org.usfirst.frc.tm744y17.robot.commands;

import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefKeysE;
import org.usfirst.frc.tm744y17.robot.config.TmRoFilesAndPrefKeys.PrefTypeE;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.P;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.PrefCreateE;
import org.usfirst.frc.tm744y17.robot.helpers.TmToolsI.Tt;
import org.usfirst.frc.tm744y17.robot.subsystems.TmSsAutonomous;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TmTCmdShowAllAutonAlgAndPrefs extends Command {
	public TmTCmdShowAllAutonAlgAndPrefs() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P.println(Tt.extractClassName(this) + " initializing");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	TmSsAutonomous.getInstance().showAlgInfo();
    	showPreferenceSettings(); //P.println("[Preferences info TBD]");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private void showPreferenceSettings() {
//    	Tt.getPreference(key, defaultSetting, printFlags, createAsNeeded)
    	for(PrefKeysE k: PrefKeysE.values()) {
    		String key = k.getKey();
    		boolean isFound = Tt.isPreferencePresent(key);
    		int prtFlags = 0; //don't print from getPreference()
//    		boolean valB;
//    		int valI;
//    		double valD;
    		String valFoundStr = "";
    		String prefix = String.format("%-40s: %-7s: found? %s, default ", key, k.getType().name(), (isFound ? "Y" : "N"));
    		switch(k.getType()) {
    		case BOOLEAN:
    			boolean dfltB = k.getDefaultBoolean();
    			valFoundStr += dfltB;
    			if(isFound) {
    				boolean val = Tt.getPreference(k, dfltB, prtFlags, PrefCreateE.DO_NOT_CREATE);
    				valFoundStr += ", current " + val;
    			}
    			break;
    		case INT:
    			int dfltI = k.getDefaultInt();
    			valFoundStr += dfltI;
    			if(isFound) {
    				int val = Tt.getPreference(k, dfltI, prtFlags, PrefCreateE.DO_NOT_CREATE);
    				valFoundStr += ", current " + val;
    			}
    			break;
    		case DOUBLE:
    			double dfltD = k.getDefaultDouble();
    			valFoundStr = String.format("% 1.4f", dfltD);
    			if(isFound) {
    				double val = Tt.getPreference(k, dfltD, prtFlags, PrefCreateE.DO_NOT_CREATE);
    				valFoundStr += String.format(", current % 1.4f", val);
    			}
    			break;
    		case STRING:
    			String dfltS = k.getDefaultString();
				valFoundStr += "'" + dfltS + "'";
    			if(isFound) {
    				String val = Tt.getPreference(k, dfltS, prtFlags, PrefCreateE.DO_NOT_CREATE);
    				valFoundStr += ", current '" + val + "'";
    			}
    			break;
    		}
			P.println(prefix + valFoundStr);

//    		if(k.getType().equals(PrefTypeE.BOOLEAN)) 
    	}
    }
}
