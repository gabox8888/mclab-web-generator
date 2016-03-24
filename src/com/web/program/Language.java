package com.web.program;

public class Language {
	
	private String aName;
	private String aExtension;
	
	public Language(String pName, String pExtension) {
		aName = pName;
		aExtension = pExtension;
	}
	
	public String getName() {
		return aName;
	}
	
	public String getExt() {
		return aExtension;
	}

}
