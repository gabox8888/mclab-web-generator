package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class BusinessFunction implements Writable,Function {
	
	private String aName;
	private List<Params> aParams;
	
	
	/**
	 * 
	 * @param pName
	 */
	public BusinessFunction(String pName) {
		aName = pName;
		aParams = new ArrayList<Params>();
	}
	
	/**
	 * Add body parameters to the function.
	 * @param pParam
	 */
	public void addParam(Params pParam) { 
		aParams.add(pParam);
	}
	
	public String declareName() {
		return "module.exports.run" + aName + " = function(req,res){";
	}
	
	public String[] declareVariables() {
		List<String> dVars = new ArrayList<String>();
		for (Params param : aParams) {
			dVars.add(param.toString());
		}
		return dVars.toArray(new String[aParams.size()]);
	}
	/**
	 * @return 
	 */
	@Override
	public String[] toFile() {
		return null;
	}

	public static void main(String[] args) {
		
		Header test = new Header();
		test.addRepositoryModule("ExtractFunction");
		test.addStandardModule("q","Q");
		test.addRepositoryModule("ExtractVariable");
		test.addStandardModule("path");
		BusinessFunction ftest = new BusinessFunction("ExtractFunction");
		ftest.addParam(new BodyParams("code"));
		ftest.addParam(new BodyParams("selection"));
		ftest.addParam(new BodyParams("newName"));
		for(String s : test.toFile()) {
			System.out.println(s);
		}
		System.out.println(ftest.declareName());
		for(String s : ftest.declareVariables()) {
			System.out.println(s);
		}

	}

	@Override
	public String[] declareBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
