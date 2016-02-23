package com.web.program;

public interface Function extends Writable{
	
	public String[] declareVariables();
	public String declareName();
	public String[] declareBody();
	public String getName();
	public void addParam(Params pParam);
	
}
