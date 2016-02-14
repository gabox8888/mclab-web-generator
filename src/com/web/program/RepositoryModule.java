package com.web.program;

public class RepositoryModule implements Module {
	
	private String aName;
	private final int aPriority;
	
	public RepositoryModule(String pName) {
		aName = pName;
		aPriority = 2;
	}
	
	public String toString() {
		return "var " + aName + "Repository = repositoryFactory.get" + aName + ";"; 
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
