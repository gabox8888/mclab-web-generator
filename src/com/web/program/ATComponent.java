package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.BusinessFunction.Types;

public class ATComponent implements Writable {
	
	private String aName;
	private Types aType;
	private Command aCommand;
	
	public ATComponent (String pName, Types pType,Command pCommand) {
		aName = generateName(pName);
		aType = pType;
		aCommand = pCommand;
	}
	
	private String generateName(String pName) {
		return pName.toUpperCase().replace(" ", "_");
	}

	@Override
	public String[] toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		
		List<String> aLines = new ArrayList<String>();
		
		switch (pPart){
			case JSON:
				aLines.add(aName + ": {");
				if (aType != Types.ANALYSIS) {
					aLines.add("OPEN_PANEL: '',");
				    aLines.add("ADD_ARGUMENT: '',");
				    aLines.add("DELETE_ARGUMENT: '',");
				    aLines.add("EDIT_ARGUMENT: '',");
				    for (String s : aCommand.getFileParams()) {
				    	aLines.add("CONFIRM_" + generateName(s) + ": '',");
					    aLines.add("OPEN_" + generateName(s) + "_SELECTION_MODE: '',");
					    aLines.add("CLOSE_" + generateName(s) + "_SELECTION_MODE: ''");
				    }
				    
				} else {
					aLines.add("OPEN_PANEL: '',");
				}
				aLines.add("}");
				break;
			default:
				break;	
		}
		
		return aLines.toArray(new String[aLines.size()]);
			
	}

}
