package com.web.program;

import com.web.tools.FormatingTools;
import java.util.*;

public class CompilePanelFile  {

	private String aName;
	private String aArg;
	private String aLanguage;
	
	public CompilePanelFile(String pArg, String pLanguage) {
		aName = FormatingTools.namifyArg(pArg);
		aArg = FormatingTools.capitalizeName(pArg);
		aLanguage = pLanguage;
	}
	
	public String[] declareFileContainer() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("<div");
		aLines.add("\tclassName={classnames(");
		aLines.add("\t\t\"side-panel-card\",");
		aLines.add("\t\t\"fortran-compiler-main-file-selector\"");
		aLines.add("\t}");
		aLines.add(">");
		aLines.add("\t<div className=\"side-panel-card-header\">");
		aLines.add("\t\t" + aName);
		aLines.add("\t</div>");
		aLines.add("\t{this._get" + aArg + "Path()}");
		aLines.add("\t{this._get" + aArg + "Selection()}");
		aLines.add("\t{this._get" + aArg + "ChangeButton()}");
		aLines.add("</div>");
		
		return aLines.toArray(new String[aLines.size()]);

	}
	
	public String[] declareFileFunctions() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add("_get" + aArg + "Section() {");
		aLines.add("\tif (this.props.unconfirmed" + aArg + "Path) {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<div>");
		aLines.add("\t\t\t\t<div>{this.props.unconfirmed" + aArg + "Path}</div>");
		aLines.add("\t\t\t\t<a className=\"fortran-compiler-select-main-file\"");
		aLines.add("\t\t\t\t\tonClick={() => {");
		aLines.add("\t\t\t\t\t\t\tDispatcher.dispatch({action: AT." + aLanguage + "_COMPILE_PANEL.CONFIRM_" + aName.toUpperCase().replace(" ", "_") + "});");
		aLines.add("\t\t\t\t\t\t\tDispatcher.dispatch({action: AT.FILE_EXPLORER.CLOSE_SELECTION_MODE});");
		aLines.add("\t\t\t\t\t\t}");
		aLines.add("\t\t\t\t\t}");
		aLines.add("\t\t\t\t>");
		aLines.add("\t\t\t\t\t<strong>Confirm</strong>");
		aLines.add("\t\t\t\t</a>");
		aLines.add("\t\t\t</div>");
		aLines.add("\t\t);");
		aLines.add("\t}");
		aLines.add("}");
		
		aLines.add("_get" + aArg + "Path() {");
		aLines.add("\tif (this.props." + FormatingTools.uncapitalizeName(aArg) + "Path && !this.props.unconfirmed" + aArg + "Path) {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<div>");
		aLines.add("\t\t\t\t{' '  + this.props." + FormatingTools.uncapitalizeName(aArg) + "Path}");
		aLines.add("\t\t\t</div>");
		aLines.add("\t\t)");
		aLines.add("\t} else if (this.props.unconfirmed" + aArg + "Path) {");
		aLines.add("\t\treturn null;");
		aLines.add("\t} else {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<a className=\"fortran-compiler-select-main-file\"");
		aLines.add("\t\t\t\tonClick={() => {");
		aLines.add("\t\t\t\t\t\tDispatcher.dispatch({action: AT.FILE_EXPLORER.OPEN_SELECTION_MODE});");
		aLines.add("\t\t\t\t\t\tDispatcher.dispatch({action: AT." + aLanguage + "_COMPILE_PANEL.OPEN_" + aName.toUpperCase().replace(" ", "_") + "_SELECTION_MODE});");
		aLines.add("\t\t\t\t\t}");
		aLines.add("\t\t\t\t}");
		aLines.add("\t\t\t>");
		aLines.add("\t\t\t\tSelect" + aName);
		aLines.add("\t\t\t</a>");
		aLines.add("\t\t);");
		aLines.add("\t}");
		aLines.add("}");

		aLines.add("_get" + aArg + "ChangeButton() {");
		aLines.add("\tif (this.props." + FormatingTools.uncapitalizeName(aArg) + "Path && !this.props.unconfirmed" + aArg + "Path) {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<div>");
		aLines.add("\t\t\t\t<a className=\"fortran-compiler-select-main-file\"");
		aLines.add("\t\t\t\t\tonClick={() => {");
		aLines.add("\t\t\t\t\t\t\tDispatcher.dispatch({action: AT.FILE_EXPLORER.OPEN_SELECTION_MODE});");
		aLines.add("\t\t\t\t\t\t\tDispatcher.dispatch({action: AT." + aLanguage + "_COMPILE_PANEL.OPEN_" + aName.toUpperCase().replace(" ", "_") + "SELECTION_MODE});");
		aLines.add("\t\t\t\t\t\t}");
		aLines.add("\t\t\t\t\t}");
		aLines.add("\t\t\t\t>");
		aLines.add("\t\t\t\t\t{'Change'}");
		aLines.add("\t\t\t\t</a>");
		aLines.add("\t\t\t</div>");
		aLines.add("\t\t)");
		aLines.add("\t}");
		aLines.add("\treturn null");
		aLines.add("}");

		return aLines.toArray(new String[aLines.size()]);
		
	}
	
	public String[] declarePrototypes() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add(FormatingTools.uncapitalizeName(aArg) + "Path: PropTypes.string,");
		aLines.add("unconfirmed" + aArg + "Path: PropTypes.string,");
		
		return aLines.toArray(new String[aLines.size()]);
	}

}
