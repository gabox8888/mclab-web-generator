package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Types;
import com.web.tools.FormatingTools;

public class FrontEndFunction implements Function {
	
	private String aName;
	private Command aCommand;
	private BusinessFunction aFunction;
	private Language aLanguage;
	private Types aType;
	
	public FrontEndFunction(String pName,Command pCommand,BusinessFunction pFunction, Language pLanguage,Types pType) {
		aName = pName;
		aCommand = pCommand;
		aFunction = pFunction;
		aLanguage = pLanguage;
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
		aLines.add("TerminalActions.println(");
		aLines.add("\t'Sent request to server for compilation.' +");
		aLines.add("\t'Compiled files should be ready in a few seconds.'");
		aLines.add(");");
		if (aType != Types.ANALYSIS) {
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
		}
		aLines.add("const baseURL = window.location.origin;");
		aLines.add("const sessionID = OnLoadActions.getSessionID();");
		
		if (aType != Types.ANALYSIS) {
			aLines.add("request.post(baseURL + '" + aFunction.getEndPoint() + "')");
		} else {
			aLines.add("request.get(baseURL + '" + aFunction.getEndPoint() + "/' + filePath.substring(10))");
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
		aLines.add("\t\t\t\t\t{ \"Failed to compile :( \" }");
		aLines.add("\t\t\t\t\t{ \"This could be due to a network issue. \" }");
		aLines.add("\t\t\t\t\t{ \"If you believe this is a bug please open an issue \" }");
		aLines.add("\t\t\t\t\t<a href=\"https://github.com/Sable/McLab-Web/issues\">here</a>");
		aLines.add("\t\t\t\t\t{ \"or send us an email.\" }");
		aLines.add("\t\t\t\t\t</div>");
		aLines.add("\t\t\t\t);");
		aLines.add("\t\t\t}");
		aLines.add("\t\t} else {");
		
		switch (aType) {
			case ANALYSIS: 
				aLines.add("\t\t\tconst stdout = JSON.parse(res.text);");
				aLines.add("\t\t\tTerminalActions.println(");
				aLines.add("\t\t\t\t<div>Analysis complete! {' '}");
				aLines.add("\t\t\t\t\tstout");
				aLines.add("\t\t\t\t</div>");
				aLines.add("\t\t\t);");
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
