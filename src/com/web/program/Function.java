package com.web.program;

public interface Function{
	
	public String[] declareVariables();
	public String declareName();
	public String[] declareBody();
	public String[] toFile();
	public String getName();
	public void addParam(Params pParam);
	
}
