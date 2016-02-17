package com.web.program;

public class BodyParams implements Params{
	
	private String aName;
	
	public BodyParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "var " + aName + " = req.body." + aName + ";";
	}
	
	@Override
	public String getName() {
		return aName;
	}

	@Override
	public int compareTo(Params pParams) {
		// TODO Auto-generated method stub
		return 0;
	}

}
