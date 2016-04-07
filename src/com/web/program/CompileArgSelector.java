package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;

public class CompileArgSelector implements Writable {
	
	private Command aCommand;
	private Language aLanguage;
	private Header aHeader;
	private List<CompileArg> aArgs;
	private String aName;
	
	public CompileArgSelector (Command pCommand, Language pLanguage) {
		aCommand = pCommand;
		aLanguage = pLanguage;
		aArgs = new ArrayList<CompileArg>();
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("Dispatcher","./Dispatcher"));
		aHeader.addModule(new FrontEndModule("AT","./constants/AT"));
		aHeader.addModule(new FrontEndModule("classnames","classnames"));
		aHeader.addModule(new FrontEndModule("DropdownSelect","./DropdownSelect.react"));
		aHeader.addModule(new FrontEndModule("{ DropdownButton, Input, MenuItem }","react-bootstrap"));
		
		aName = aLanguage.getName() + "CompileArgumentSelector";
		
		for (String s : aCommand.getArgsParams()) {
			aArgs.add(new CompileArg(s,aLanguage.getName(),aCommand));
		}
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

		aLines.add("const { PropTypes, Component } = React;");
		aLines.add("const " + aName + " = (props) => {");
		
		aLines.add("\treturn (");
		aLines.add("\t\t<div>");
		
		for (CompileArg arg : aArgs) {
			for (String s : arg.toFile()) {
				aLines.add("\t\t\t" + s);
			}
		}
		
		aLines.add("\t\t</div>");
		aLines.add("\t);");
		aLines.add("};");
		
		aLines.add(aLanguage.getName() + "CompileArgumentSelector.propTypes = {");
		aLines.add("\targIndex: PropTypes.number.isRequired,");
		for(String s : aCommand.getArgsParams()) {
			aLines.add("\t" + s + ": PropTypes.number.isRequired,");
		}
		aLines.add("};");
		
		aLines.add("module.exports = " + aLanguage.getName() + "CompileArgumentSelector;");

		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		// TODO Auto-generated method stub
		return null;
	}

}
