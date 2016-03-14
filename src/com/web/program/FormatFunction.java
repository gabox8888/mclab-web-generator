package com.web.program;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class FormatFunction implements Function {
	
	private String aName;
	private JSONObject aOutputFormat;
	
	public FormatFunction(String pName, JSONObject pOutputFormat) {
		aName = pName;
		aOutputFormat = pOutputFormat;
	}

	@Override
	public String[] toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] declareVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String declareName() {
		return "function " + aName + "(inObject) {";
	}

	@Override
	public String[] declareBody() {
		List<String> aLines = new ArrayList<String>();
		for (Object k : aOutputFormat.keySet()) { 
			if (aOutputFormat.get(k).toString().indexOf("{") != -1) {
				//aLines.add(e)
			}
			//aLines.addAll(k.toString() + )
		}
		return null;
	}

	@Override
	public String getName() {
		return aName;
	}

	@Override
	public void addParam(Params pParam) {
		// TODO Auto-generated method stub

	}

}
