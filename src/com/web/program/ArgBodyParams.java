package com.web.program;

public class ArgBodyParams implements Params{
	
	private String aName;
	
	public ArgBodyParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "const " + aName + " = req.body.arg." + aName + ";";
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
