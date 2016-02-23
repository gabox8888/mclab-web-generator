package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class RepositoryFunction implements Function {
	
	private String aName;
	private List<Params> aParams;
	
	/**
	 * 
	 * @param pName
	 */
	public RepositoryFunction(String pName) {
		aName = pName;
		aParams = new ArrayList<Params>();
	}
	
	public String getName() {
		return aName;
	}
	
	/**
	 * Add body parameters to the function.
	 * @param pParam
	 */
	public void addParam(Params pParam) { 
		aParams.add(pParam);
	}
	
	@Override
	public String declareName() {
		
		StringBuilder sBParams = new StringBuilder();
		boolean initial = true;
		for (Params param : aParams) {
			if (!initial) { 
				sBParams.append(",");
			}
			sBParams.append(param.getName());
			initial = false;
		}
		String params = sBParams.toString();
		return "module.exports.run" + aName + " = function(" + params+") {";
	}
	
	@Override
	public String[] declareVariables() {
		List<String> dVars = new ArrayList<String>();
		for (Params param : aParams) {
			dVars.add(param.toString());
		}
		return dVars.toArray(new String[aParams.size()]);
	}

	@Override
	public String[] toFile() {
		return null;
	}

	@Override
	public String[] declareBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
