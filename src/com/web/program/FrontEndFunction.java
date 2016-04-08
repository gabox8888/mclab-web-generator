package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Types;
import com.web.tools.FormatingTools;

public class FrontEndFunction implements Function {
	
	private String aName;
	private String aAction;
	private Command aCommand;
	private RequestFunction aFunction;
	private Language aLanguage;
	private Types aType;
	
	public FrontEndFunction(String pName,Command pCommand,RequestFunction pFunction, Language pLanguage,Types pType) {
		aName = pName;
		aCommand = pCommand;
		aFunction = pFunction;
		aLanguage = pLanguage;
		aType = pType;
	}
	
	public FrontEndFunction(String pName,Command pCommand,RequestFunction pFunction,String pAction,Types pType) {
		aName = pName;
		aAction = pAction;
		aCommand = pCommand;
		aFunction = pFunction;
		aLanguage = null;
		aType = pType;
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		aLines.add(this.declareName());
		
		for (String s : this.declareVariables()) {
			System.out.println(s);
			aLines.add("\t" + s);
		}
		
		for (String s : this.declareBody()) {
			aLines.add("\t" + s);
		}
		
		aLines.add("}");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] declareVariables() {
		List<String> aLines = new ArrayList<String>();
		if (aType != Types.ANALYSIS) {
			for (String s : aCommand.getFileParams()) {
				aLines.add("const " + s + " = " + aLanguage.getName().toUpperCase() + "CompileConfigStore.get" + FormatingTools.capitalizeName(s) + "Path();" );
			}
			aLines.add("const arg = " + aLanguage.getName().toUpperCase() + "CompileConfigStore.getArgumentList().get(0, null);");

		} else {
			aLines.add("const filePath = OpenFileStore.getFilePath();");
			if (aCommand.getArgsParams().length > 0){
				aLines.add("const arg = " + aAction+ "ConfigStore.getArgumentList().get(0, null);");
				aLines.add("const postArg = arg");
				aLines.add("? {");
				
				for(String s : aCommand.getArgsParams()) {
					aLines.add("\t" + s + ": arg." + s + ",");
				}
				aLines.add("}");
				aLines.add(": null;");
			}
		}
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String declareName() {
		return "function " + aName + "() {";
	}

	@Override
	public String[] declareBody() {
		List<String> aLines = new ArrayList<String>();
		
		if (aType != Types.ANALYSIS) {
			aLines.add("TerminalActions.println(");
			aLines.add("\t'Sent request to server for compilation.' +");
			aLines.add("\t'Compiled files should be ready in a few seconds.'");
			aLines.add(");");
			aLines.add("const postArg = arg");
			aLines.add("? {");
			
			for(String s : aCommand.getArgsParams()) {
				aLines.add("\t" + s + ": arg." + s + ",");
			}
			aLines.add("}");
			aLines.add(": null;");
		
			aLines.add("// ffs fix the workspace hack");
			aLines.add("const postBody = {");
			for (String s : aCommand.getFileParams()) {
				aLines.add("\t" + s + ": " + s + ".substring(10),");
			}
			aLines.add("\targ: postArg");
			aLines.add("};");
		} else {
			aLines.add("if (filePath === null) {");
			aLines.add("\tTerminalActions.printerrln(");
			aLines.add("\t\t\"You need to open a file before running analysis\"");
			aLines.add("\t);");
			aLines.add("\treturn;");
			aLines.add("}");
		}
		aLines.add("const baseURL = window.location.origin;");
		aLines.add("const sessionID = OnLoadActions.getSessionID();");
		
		if (aType != Types.ANALYSIS) {
			aLines.add("request.post(baseURL + '" + aFunction.getEndPoint() + "')");
		} else {
			aLines.add("request.get(baseURL + '" + aFunction.getEndPoint() + "/' + filePath.substring(10))");
			if (aCommand.getArgsParams().length > 0) {
				aLines.add("\t.query(postArg)");	
			}
		}
				
		aLines.add("\t.set({'SessionID': sessionID})");
		
		if (aType != Types.ANALYSIS) {
			aLines.add("\t.send(postBody)");
		}
		aLines.add("\t.end(function(err, res) {");
		aLines.add("\t\tif (err) {");
		aLines.add("\t\t\ttry {");
		aLines.add("\t\t\t\tconst msg = JSON.parse(res.text).msg;");
		aLines.add("\t\t\t\tTerminalActions.printerrln(msg);");
		aLines.add("\t\t\t} catch (e) {");
		aLines.add("\t\t\t\tTerminalActions.printerrln(");
		aLines.add("\t\t\t\t\t<div>");
		if (aType != Types.ANALYSIS) {
			aLines.add("\t\t\t\t\t{ \"Failed to compile :( \" }");
		} else {
			aLines.add("\t\t\t\t\t{ \"Failed to run analysis :( \" }");
		}
		aLines.add("\t\t\t\t\t{ \"This could be due to a network issue. \" }");
		aLines.add("\t\t\t\t\t{ \"If you believe this is a bug please open an issue \" }");
		aLines.add("\t\t\t\t\t<a href=\"https://github.com/Sable/McLab-Web/issues\">here</a>");
		aLines.add("\t\t\t\t\t{ \" or send us an email.\" }");
		aLines.add("\t\t\t\t\t</div>");
		aLines.add("\t\t\t\t);");
		aLines.add("\t\t\t}");
		aLines.add("\t\t} else {");
		
		switch (aType) {
			case ANALYSIS: 
				aLines.add("\t\t\tconst stdout = JSON.parse(res.text);");
				aLines.add("\t\t\tTerminalActions.println(");
				aLines.add("\t\t\t\t<div>Analysis complete! {' '}</div>");
				aLines.add("\t\t\t);");				
				aLines.add("\t\t\tTerminalActions.println(stdout);");
				break;
			default:
				aLines.add("\t\t\tconst package_path = JSON.parse(res.text)['package_path'];");
				aLines.add("\t\t\tTerminalActions.println(");
				aLines.add("\t\t\t\t<div>Compilation complete! {' '}");
				aLines.add("\t\t\t\t\t<a href={package_path}>");
				aLines.add("\t\t\t\t\t\tClick here to download the compiled package");
				aLines.add("\t\t\t\t\t</a>");
				aLines.add("\t\t\t\t</div>");
				aLines.add("\t\t\t);");
				break;
		}
		
		aLines.add("\t\t}");
		aLines.add("\t}");
		aLines.add(");");
		
		return aLines.toArray(new String[aLines.size()]);
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
