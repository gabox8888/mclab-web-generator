package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.tools.FormatingTools;

public class AnalysisArg implements Arg {
	
	String aName;
	String aAction;
	Command aCommand;
	
	public AnalysisArg(String pName, String pAction, Command pCommand) {
		aName = pName;
		aAction = pAction;
		aCommand = pCommand;
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("<div className={classnames(");
		aLines.add("\t\"fortran-compile-argument-type-block\",");
		aLines.add("\t\"fortran-compile-argument-dimension\",");
		aLines.add(")}>");
		aLines.add("<div className=\"fortran-compile-argument-type-content\">");
		aLines.add("<Input");
		aLines.add("\ttype=\"text\"");
		aLines.add("\tbsStyle={props." + aName + " ? null : 'error' }");
		aLines.add("\tvalue={props." + aName + "}");
		aLines.add("\tonChange={(event) => {");
		aLines.add("\t\tconst newValue = event.target.value;");
		aLines.add("\t\tDispatcher.dispatch({");
		aLines.add("\t\t\taction: AT." +  FormatingTools.parseByCamelCase(aAction) +"PANEL.EDIT_ARGUMENT,");
		aLines.add("\t\t\tdata: {");
		aLines.add("\t\t\t\targIndex: props.argIndex,");
		aLines.add("\t\t\t\targ: {");
		
		for (String s : aCommand.getArgsParams()) {
			if (s.equals(aName)) {
				aLines.add("\t\t\t\t\t" + s + ": newValue,");
			} else {
				aLines.add("\t\t\t\t\t" + s + ": props." + s + ",");
			}
		}
		
		aLines.add("\t\t\t\t},");
		aLines.add("\t\t\t}");
		aLines.add("\t\t});");
		aLines.add("\t}} />");
		aLines.add("\t</div>");
		aLines.add("\t<div className=\"fortran-compile-argument-type-title\">");
		aLines.add("\t\t"+ FormatingTools.parseByCapitals(aName));
		aLines.add("\t</div>");
		aLines.add("</div>");

		return aLines.toArray(new String[aLines.size()]);
	}

}
