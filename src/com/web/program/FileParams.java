package com.web.program;

import com.web.tools.FormatingTools;

public class FileParams implements Params{
	
	private String aName;
		
	public FileParams(String pName) {
		aName = FormatingTools.capitalizeName(pName);
		
	}
	
	public String toString() {
		return "const pathTo" + aName + " = userfile_utils.fileInWorkspace(sessionID, " + aName + ");";
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
