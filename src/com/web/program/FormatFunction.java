package com.web.program;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.web.tools.FormatingTools;

public class FormatFunction implements Function {
	
	private String aName;
	private JSONObject aNewJSON;
	private String aJSONName;
	
	public FormatFunction(String pName, JSONObject pInputFormat, JSONObject pOutputFormat) {
		aName = pName;
		aJSONName = "jsonObj";
		aNewJSON = FormatingTools.mapToFormat(pInputFormat, pOutputFormat, aJSONName);
		
		for (String s : FormatingTools.writeJSON(aNewJSON, 0)) {
			System.out.println(s);
		}
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add(declareName());
		
		for(String s : declareBody()) {
			aLines.add(s);
		}
		
		aLines.add("}");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] declareVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String declareName() {
		return "function " + aName + "(" + aJSONName + ") {";
	}

	@Override
	public String[] declareBody() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("\tif (typeof " + aJSONName + " === 'object') {");
		aLines.add("\t\tlet result = { ");
		aLines.addAll(FormatingTools.writeJSON(aNewJSON, 2));
		aLines.set(aLines.size() - 1, aLines.get(aLines.size() - 1)+ ";");
		aLines.add("\t\treturn result;");
		aLines.add("\t} else if ( typeof " + aJSONName + " === 'array') {");
		aLines.add("\t\tlet result = []");
		aLines.add("\t\tfor(let item of " + aJSONName + "){");
		aLines.add("\t\t\tresult.push(" + aName + "(" + aJSONName + "));");
		aLines.add("\t\t}");
		aLines.add("\t\treturn result");
		aLines.add("\t}");
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String getName() {
		return aName;
	}

	@Override
	public void addParam(Params pParam) {

	}

}
