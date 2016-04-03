package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;

public class CompileConfigStore implements Writable{
	
	private String aLanguage;
	private Command aCommand;
	private Header aHeader;
	private String aName;
	private List<CompileConfigFile> aFiles;
	
	public CompileConfigStore(String pLanguage, Command pCommand)  {
		aLanguage = pLanguage.toUpperCase();
		aCommand = pCommand;
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("{Store}", "flux/utils"));
		aFiles  = new ArrayList<CompileConfigFile>();
		
		for (String s : aCommand.getFileParams()) {
			aFiles.add(new CompileConfigFile(s,aLanguage));
		}
		
		aName = aLanguage + "CompileConfigStore";
	}
	
	public String getName() {
		return aName;
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		
		for (String s : aHeader.toFile()) {
			aLines.add(s);
		}
		
		aLines.add("class " + aName + " extends Store {");
		aLines.add("\tconstructor(dispatcher) {");
		aLines.add("\t\tsuper(dispatcher);");
		aLines.add("\t\tthis._argumentList = immutable.List();");
		for (CompileConfigFile aFile : aFiles) {
			for (String s : aFile.declareConstructor()) {
				aLines.add("\t\t" + s);
			}
		}
		aLines.add("\t}");
		
		aLines.add("\t__onDispatch(payload) {");
		aLines.add("\t\tswitch (payload.action) {");
		aLines.add("\t\t\tcase AT." + aLanguage + "_COMPILE_PANEL.ADD_ARGUMENT:");
		aLines.add("\t\t\t\tthis._argumentList =  this._argumentList.push(payload.data['arg']);");
		aLines.add("\t\t\t\tthis.__emitChange();");
		aLines.add("\t\t\t\tbreak;");
		aLines.add("\t\t\tcase AT." + aLanguage + "COMPILE_PANEL.EDIT_ARGUMENT:");
		aLines.add("\t\t\t\tthis._argumentList =  this._argumentList.set(");
		aLines.add("\t\t\t\t\tpayload.data.argIndex,");
		aLines.add("\t\t\t\t\tpayload.data.arg");
		aLines.add("\t\t\t\t);");
		aLines.add("\t\t\t\tthis.__emitChange();");
		aLines.add("\t\t\t\tbreak;");
		
		for (CompileConfigFile aFile : aFiles) {
			for (String s : aFile.declareDispatch()) {
				aLines.add("\t\t\t" + s);
			}
		}
		aLines.add("\t\t}");
		aLines.add("\t}");
		
		for (CompileConfigFile aFile : aFiles) {
			for (String s : aFile.declareFunctions()) {
				aLines.add("\t" + s);
			}
		}
		
		aLines.add("\tgetArgumentList() {");
		aLines.add("\t\treturn this._argumentList;");
		aLines.add("\t}");
		aLines.add("}");
		
		aLines.add("module.exports = new " + aName + "(Dispatcher);");

		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		// TODO Auto-generated method stub
		return null;
	}
}
