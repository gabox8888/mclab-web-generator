package com.web.program;

public class FrontEndModule implements Module {
	
	private String aName;
	private String aFrom;
	private final int aPriority;
	
	public FrontEndModule(String pName,String pFrom) {
		aName = pName;
		aFrom = pFrom;
		aPriority = 2;
	}
	
	public String toString() {
		return "import " + aName + " from '" + aFrom + "';"; 
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
