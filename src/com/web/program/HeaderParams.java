package com.web.program;

import com.web.tools.FormatingTools;

public class HeaderParams implements Params{
	
	private String aName;
	
	public HeaderParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "const " + aName + " = req.header.('" + aName + "');";
	}
	
	@Override
	public String getName() {
		return FormatingTools.uncapitalizeName(aName);
	}

	@Override
	public int compareTo(Params pParams) {
		return 0;
	}

}
