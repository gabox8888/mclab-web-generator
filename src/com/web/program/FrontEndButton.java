package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Types;
import com.web.tools.FormatingTools;

public class FrontEndButton implements Writable {
	
	private Program aAction;
	private Module aModule;
	private String aName;
	private boolean aPanel;
	
	public FrontEndButton (Program pAction, boolean pPanel, String pName) {
		aAction = pAction;
		aModule = new FrontEndModule(pAction.getName(),"./actions/" + aAction.getName());
		aName = pName;
		aPanel = pPanel;
	}
	
	@Override
	public String[] toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		List<String> aLines = new ArrayList<String>();
		switch (pPart) {
			case HEADER:
				aLines.add(aModule.toString());
				break;
			case FUNCTIONS:
				aLines.add("\t\t\t\t\t<a className=\"pure-button topnav-button\"");
				if (aPanel) {
					aLines.add("\t\t\t\t\t\tonClick={" + aAction.getName() + ".openPanel}>");
					aLines.add("\t\t\t\t\t" + FormatingTools.namifyArg(aName));
				} else {
					aLines.add("\t\t\t\t\t\tonClick={" + aAction.getName() + "." + aAction.getMainFunction().getName() + "}>");
					aLines.add("\t\t\t\t\t\tRun " + aName);
				}
		        aLines.add("\t\t\t\t\t</a>");
				break;
			default:
				break;
		}
		return aLines.toArray(new String[aLines.size()]);
	}

}
