package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;
import com.web.tools.FormatingTools;

public class CompilePanel implements Writable {
	
	private Language aLanguage;
	private Command aCommand;
	private CompileArgSelector aArgSelector;
	private Program aActionProgram;
	private Header aHeader;
	private String aName;
	private List<CompilePanelFile> aFiles;
	
	public CompilePanel (Language pLanguage, Command pCommand, CompileArgSelector pArgSelector, Program pActionProgram) {
		aLanguage = pLanguage;
		aCommand = pCommand;
		aArgSelector = pArgSelector;
		aActionProgram = pActionProgram;
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("SidePanelBase","'./SidePanelBase.react'"));
		aHeader.addModule(new FrontEndModule(aActionProgram.getName(),"./actions/" + aActionProgram.getName() ));
		aHeader.addModule(new FrontEndModule(aArgSelector.getName(),"./" + aArgSelector.getName() + ".react"));
		aHeader.addModule(new FrontEndModule("{ DropdownButton, MenuItem }","react-bootstrap"));
		
		aName = aLanguage.getName() + "CompilePanel";
		
		aFiles = new ArrayList<CompilePanelFile>();
		for(String s : aCommand.getFileParams()) {
			aFiles.add(new CompilePanelFile(s, aLanguage.getName().toUpperCase()));
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
		aLines.add("class " + aName + " extends Component {");
		aLines.add("\trender() {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<SidePanelBase title=\"Compile to " + aLanguage.getName() + "\">");
		for (CompilePanelFile aFile : aFiles ) {
			for (String s : aFile.declareFileContainer()) {
				aLines.add("\t\t\t\t" + s);
			}
		}
		aLines.add("\t\t\t\t{this._getArgumentCard()}");
		aLines.add("\t\t\t\t{this._getCompileButton()}");
		aLines.add("\t\t\t</SidePanelBase>");
		aLines.add("\t\t);");	          
		aLines.add("\t}");	     
		
		aLines.add("\t_getCompileButton() {");
		aLines.add("\t\tif (" + FormatingTools.ififyFiles(aCommand.getFileParams()) + ") {");
		aLines.add("\t\t\treturn null");
		aLines.add("\t\t}");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<div className=\"fortran-compile-final-button-container\">");
		aLines.add("\t\t\t\t<a");
		aLines.add("\t\t\t\t\tclassName=\"pure-button side-panel-button\"");
		aLines.add("\t\t\t\t\tonClick={" + aActionProgram.getName() + "." + aActionProgram.getMainFunction().getName() + "}>");
		aLines.add("\t\t\t\t\tCompile");
		aLines.add("\t\t\t\t</a>");
		aLines.add("\t\t\t</div>");
		aLines.add("\t\t);");
		aLines.add("\t}");
		
		aLines.add("\t_getArgumentCard() {");
		aLines.add("\t\tif (" + FormatingTools.ififyFiles(aCommand.getFileParams()) + ") {");
		aLines.add("\t\t\treturn null");
		aLines.add("\t\t}");
		aLines.add("\t\t// This allows for multiple arguments");	    
		aLines.add("\t\tlet argumentSelectors = [];");
		aLines.add("\t\tlet count = 0;");
		aLines.add("\t\tfor (let arg of this.props.argumentList) {");
		aLines.add("\t\t\targumentSelectors.push(");
		aLines.add("\t\t\t\t<" + aArgSelector.getName());
		aLines.add("\t\t\t\t\tkey={'argselector-' + count}");
		aLines.add("\t\t\t\t\targIndex={count}");
		for(String s : aCommand.getArgsParams()) {
			aLines.add("\t\t\t\t\t" + s + "={arg." + s + "}");
		}
		aLines.add("\t\t\t\t/>");
		aLines.add("\t\t\t);");
		aLines.add("\t\t\tcount += 1;");
		aLines.add("\t\t}");
		aLines.add("\t\treturn (");
 		aLines.add("\t\t\t<div className=\"side-panel-card\">");
		aLines.add("\t\t\t\t<div className=\"side-panel-card-header\">Argument</div>");
		aLines.add("\t\t\t\t<div>{argumentSelectors}</div>");
		aLines.add("\t\t\t\t<div>{this._getAddArgumentButton()}</div>");
		aLines.add("\t\t\t</div>");
		aLines.add("\t\t);");
		aLines.add("\t}");

		aLines.add("\t_getAddArgumentButton() {");
		aLines.add("\t\tif (this.props.argumentList.length === 0) {");
		aLines.add("\t\t\treturn (");
		aLines.add("\t\t\t\t<a className=\"fortran-compiler-select-main-file\"");
		aLines.add("\t\t\t\t\tonClick={() => Dispatcher.dispatch({");
		aLines.add("\t\t\t\t\t\taction: AT." + aLanguage.getName().toUpperCase() + "_COMPILE_PANEL.ADD_ARGUMENT,");
		aLines.add("\t\t\t\t\t\tdata: {");
		aLines.add("\t\t\t\t\t\t\t// IMPROVE: Hard coding a default arg here is icky.");
		aLines.add("\t\t\t\t\t\t\targ: {");
		for (String s : aCommand.getArgsParams()) {
			aLines.add("\t\t\t\t\t\t\t\t" + s + ": \"\", ");
		}
		aLines.add("\t\t\t\t\t\t\t}");
		aLines.add("\t\t\t\t\t\t}");
		aLines.add("\t\t\t\t\t})}");
		aLines.add("\t\t\t\t\t>");
		aLines.add("\t\t\t\t\tAdd argument");
		aLines.add("\t\t\t\t</a>");
		aLines.add("\t\t\t);");
		aLines.add("\t\t}");
		aLines.add("\t\treturn null");
		aLines.add("\t}");

		for (CompilePanelFile aFile : aFiles ) {
			for (String s : aFile.declareFileFunctions()) {
				aLines.add("\t" + s);
			}
		}
		
		aLines.add("}");
		
		aLines.add(aName + ".propTypes = {");
		for (CompilePanelFile aFile : aFiles ) {
			for (String s : aFile.declarePrototypes()) {
				aLines.add("\t" + s);
			}
		}
		aLines.add("\targumentList: PropTypes.object,  /* Should get Immutable list of arg types */");
		aLines.add("}");
		aLines.add("export default " + aName + ";");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		return null;
	}

}
