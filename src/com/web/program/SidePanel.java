package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class SidePanel implements Writable {
	
	private CompilePanelContainer aContainer;
	private CompilePanel aPanel;
	private Module aModule;
	
	public SidePanel(CompilePanelContainer pContainer, CompilePanel pPanel) {
		aContainer = pContainer;
		aPanel = pPanel;
		aModule = new FrontEndModule(aContainer.getName(), "./" + aContainer.getName() + ".react");
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
				aLines.add("[SidePanelKeys." + aPanel.getConstant() + ", " + aContainer.getName() + "],");
				break;
			default:
				break;
		}
		
		return aLines.toArray(new String[aLines.size()]);
	}

}
