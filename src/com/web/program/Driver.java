package com.web.program;

import com.web.program.BusinessFunction.Types;

public class Driver {

	public static void main(String[] args) {
		Program aProgram = new Program();
		
		Function aFunction = new BusinessFunction("ExtractFunction",Types.ANALYSIS);
		aFunction.addParam(new ParamParams("sessionID"));
		aFunction.addParam(new ParamParams("pathToFile"));
		
		aProgram.addFunction(aFunction);
		
		for (String s : aProgram.toFile()) {
			System.out.println(s);
		}
		
		Command aCommand = new Command("JAVA test --json -r y -r --temp");
	}

}
