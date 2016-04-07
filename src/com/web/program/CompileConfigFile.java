package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.tools.FormatingTools;

public class CompileConfigFile {
	
	private String aName;
	private String aArg;
	private String aLanguage;
	
	public CompileConfigFile(String pArg, String pLanguage) {
		aName = FormatingTools.namifyArg(pArg).toUpperCase().replace(" ", "_");
		aArg = pArg;
		aLanguage = pLanguage;
	}
	
	public String[] declareConstructor() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add("this._" + aArg + "Path = null;");
	    aLines.add("this._unconfirmed" + FormatingTools.capitalizeName(aArg) + "Path = null;");
	    aLines.add("this._" + aArg + "SelectMode = false;");
		
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declareDispatch() {
		List<String> aLines = new ArrayList<String>();

		aLines.add("case AT." + aLanguage + "_COMPILE_PANEL.OPEN" + aName + "_SELECTION_MODE:");
		aLines.add("\tthis._" + aArg + "SelectMode = true;");
		aLines.add("\tbreak;");
		aLines.add("case AT." + aLanguage + "_COMPILE_PANEL.CLOSE" + aName + "_SELECTION_MODE:");
		aLines.add("\tthis._" + aArg + "SelectMode = false;");
		aLines.add("\tbreak;");
		aLines.add("case AT." + aLanguage + "_COMPILE_PANEL.CONFIRM" + aName + ":");
		aLines.add("\tif (this._" + aArg  + "SelectMode) {");
		aLines.add("\t\tthis._" + aArg + "Path = this._unconfirmed" + FormatingTools.capitalizeName(aArg) + "Path;");
		aLines.add("\t\tthis._unconfirmed" + FormatingTools.capitalizeName(aArg) + "Path = null;");
		aLines.add("\t\tthis._" + aArg + "SelectMode = false;");
		aLines.add("\t\tthis.__emitChange();");
		aLines.add("\t}");
		aLines.add("\tbreak;");
		
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declareFunctions() {
		List<String> aLines = new ArrayList<String>();
		String aCapitalizedArg = FormatingTools.capitalizeName(aArg);
		
		aLines.add("get" + aCapitalizedArg + "Path() {");
		aLines.add("\treturn this._"+ aArg + "Path;");
		aLines.add("}");
		
		aLines.add("getUnconfirmed" + aCapitalizedArg + "Path() {");
		aLines.add("\treturn this._unconfirmed"+ aCapitalizedArg + "Path;");
		aLines.add("}");
		
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declareSelectFile() {
		List<String> aLines = new ArrayList<String>();

		aLines.add("if (this._" + aArg  + "SelectMode) {");
		aLines.add("\tif (payload.data.type === 'FILE') {");
		aLines.add("\t\tthis._unconfirmed" + FormatingTools.capitalizeName(aArg) + "Path = payload.data.selection;");
		aLines.add("\t\tthis.__emitChange();");
		aLines.add("\t}");
		aLines.add("}");
	
		return aLines.toArray(new String[aLines.size()]);
	}

}
