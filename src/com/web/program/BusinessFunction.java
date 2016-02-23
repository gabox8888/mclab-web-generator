package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class BusinessFunction implements Function {
	
	private String aName;
	private List<Params> aParams;
	private Types aType; 
	
	public enum Types
	{ANALYSIS,COMPILE};
	
	
	/**
	 * 
	 * @param pName
	 */
	public BusinessFunction(String pName, Types pType) {
		aName = pName;
		aType = pType;
		aParams = new ArrayList<Params>();
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
				aLines.add("let pathToFile = userfile_utils.fileInWorkspace(sessionID, filepath);");
				aLines.add("const command = `java -jar ${config.MCLAB_CORE_JAR_PATH} --json ${pathToFile}`;");
				aLines.add("child_process.exec(command, function(error, stdout){\n"
						+ "try {\n"
						+ "let jsonTree = JSON.parse(stdout);\n"
						+ "let output = {};\n"
						+ "tool_usage.extractKinds(jsonTree, output);\n"
						+ "res.json(output);\n"
						+ "}\n"
						+ "catch(err){\n"
						+ "res.status(400).json({msg: 'Mclab-core failed to do kind analysis on this file. Is this a valid matlab file?'});\n"
						+ "}\n"
						+ " });");
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
			aLines.add(s);
		}
		
		for (String s : this.declareBody()) {
			aLines.add(s);
		}
		return aLines.toArray(new String[aLines.size()]);
	}
	

	public static void main(String[] args) {
		
		Header test = new Header();
		test.addStandardModule("q","Q");
		test.addStandardModule("child_process");
		BusinessFunction ftest = new BusinessFunction("ExtractFunction",Types.ANALYSIS);
		ftest.addParam(new ParamParams("sessionID"));
		ftest.addParam(new BodyParams("selection"));
		ftest.addParam(new BodyParams("newName"));
		for(String s : test.toFile()) {
			System.out.println(s);
		}
		System.out.println(ftest.declareName());
		for(String s : ftest.declareVariables()) {
			System.out.println(s);
		}
		
		for (String s : ftest.declareBody()) {
			System.out.println(s);
		}

	}


}
