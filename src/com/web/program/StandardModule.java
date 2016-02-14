package com.web.program;

public class StandardModule implements Module {
	
	private String aName;
	private String aVarName;
	private final int aPriority;
	
	public StandardModule(String pName) {
		aName = pName;
		aVarName = null;
		aPriority = 1;
	}
	
	public StandardModule(String pName, String pVarName) {
		aName = pName;
		aVarName = pVarName;
		aPriority = 1;
	}
	
	public String toString() {
		if (aVarName != null) {
			return "var " + aVarName + " = require('" + aName + "');";
		} else {
			return "var " + aName + " = require('" + aName + "');";
		}
	}

	@Override
	public int getPriority() {
		return aPriority;
	}

	@Override
	public int compareTo(Module pModule) {
		return pModule.getPriority() - aPriority;
	}

}
