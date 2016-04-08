package com.web.program;

public class QueryParams implements Params{
	
	private String aName;
	
	public QueryParams(String pName) {
		aName = pName;
	}
	
	public String toString() {
		return "const " + aName + " = req.query." + aName + ";";
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
