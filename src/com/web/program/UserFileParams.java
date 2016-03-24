package com.web.program;

public class UserFileParams implements Params{
	
	private String aName;
	private String aFolder;
	
	public UserFileParams(String pName, String pFolder) {
		aName = pName;
		aFolder = pFolder;
	}
	
	public String toString() {
		return "const " + aName + " = userfile_utils." + aFolder + "(sessionID);";
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
