package com.web.program;

public class FileParams implements Params{
	
	private String aName;
	
	public FileParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "let pathTo" + aName + " = userfile_utils.fileInWorkspace(sessionID, " + aName + ");";
	}
	
	@Override
	public String getName() {
		return "pathTo" + aName;
	}

	@Override
	public int compareTo(Params pParams) {
		// TODO Auto-generated method stub
		return 0;
	}

}
