package com.web.program;

public class ParamParams implements Params{
	
	private String aName;
	
	public ParamParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "const " + aName + " = req.params." + aName + ";";
	}
	
	@Override
	public String getName() {
		return aName;
	}

	@Override
	public int compareTo(Params pParams) {
		return 0;
	}

}
