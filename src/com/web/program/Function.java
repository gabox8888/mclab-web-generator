package com.web.program;

import java.util.List;

public interface Function{
	
	public String[] declareVariables();
	public String declareName();
	public String[] declareBody();
	public String[] toFile();
	public String getName();
	public void addParam(Params pParam);
	
}
