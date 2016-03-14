package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.tools.FormatingTools;

public class BusinessFunction implements Function {
	
	private String aName;
	private List<Params> aParams;
	private Types aType; 
	private Command aCommand;
		
	public enum Types
	{ANALYSIS,COMPILE};
	
	
	/**
	 * 
	 * @param pName
	 */
	public BusinessFunction(String pName, Types pType, Command pCommand) {
		
		aName = FormatingTools.uncapitalizeName(pName);
		aType = pType;
		aCommand = pCommand;
		aParams = new ArrayList<Params>();
				
		addParam(new ParamParams("sessionID"));
		
		for(String s : aCommand.getArgsParams()) {
			addParam(new ParamParams(s));
		}
		
		for(String s : aCommand.getFileParams()) {
			addParam(new ParamParams(s));
			addParam(new FileParams(s));
		}
		
	}
	
	/**
	 * @return aName
	 */
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
	
	public String declareName() {
		return "function " + aName + "(req,res){";
	}
	
	public String[] declareVariables() {
		List<String> dVars = new ArrayList<String>();
		for (Params param : aParams) {
			dVars.add(param.toString());
		}
		return dVars.toArray(new String[aParams.size()]);
	}
	
	@Override
	public String[] declareBody() {
		
		List<String> aLines = new ArrayList<String>();
		
		switch (aType) {
			case ANALYSIS:
				aLines.add("const command = '" + aCommand.toString() + "';");
				aLines.add("child_process.exec(command, function(error, stdout) {");
				aLines.add("\t try {");
				aLines.add("\t\t let output = JSON.parse(stdout);");
				aLines.add("\t\t res.json(output);");
				aLines.add("\t } catch(err) {");
				aLines.add("\t \t res.status(400).json({msg: 'Mclab-core failed to do " + aName + " analysis on this file. Is this a valid matlab file?'})");
				aLines.add("\t }");
				aLines.add("});");
				break;
			case COMPILE:
				
				break;
		}
		return aLines.toArray(new String[aLines.size()]);
	}

	
	/**
	 * @return 
	 */
	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		aLines.add(this.declareName());
		for(String s : this.declareVariables()) {
			aLines.add("\t" + s);
		}
		
		for (String s : this.declareBody()) {
			aLines.add("\t" + s);
		}
		
		aLines.add("}");
		
		return aLines.toArray(new String[aLines.size()]);
	}
	



}
