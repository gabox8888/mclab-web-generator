package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;
import com.web.tools.FormatingTools;

public class CompilePanelContainer implements Writable {
	
	private Command aCommand;
	private String aLanguage;
	private String aConfigStore;
	private String aPanel;
	private Header aHeader;
	private String aName;
	
	public CompilePanelContainer(Command pCommand, String pLanguage, String pConfigStore, String pPanel) {
		aCommand = pCommand;
		aLanguage = pLanguage;
		aConfigStore = pConfigStore; 
		aPanel = pPanel;
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("{Container}","flux/utils"));
		aHeader.addModule(new FrontEndModule(aConfigStore,"./stores/" + aConfigStore));
		aHeader.addModule(new FrontEndModule(aPanel,"./" + aPanel + ".react"));
		
		aName = aLanguage + "CompilePanelContainer";
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
		
		aLines.add("const { Component } = React;");
		aLines.add("class " + aName + " extends Component {");
		aLines.add("\tstatic getStores() {");
		aLines.add("\t\treturn [");
		aLines.add("\t\t\t" + aConfigStore);
		aLines.add("\t\t];");
		aLines.add("\t}");
		aLines.add("\tstatic calculateState(prevState) {");
		aLines.add("\t\treturn {");
		for (String s : aCommand.getFileParams()) {
			aLines.add("\t\t\t" + s + "Path: " + aConfigStore + ".get" + FormatingTools.capitalizeName(s) + "Path(),");
			aLines.add("\t\t\tunconfirmed" + FormatingTools.capitalizeName(s) + "Path: " + aConfigStore + ".getUnconfirmed" + FormatingTools.capitalizeName(s) + "Path(),");
		}
		aLines.add("\t\t\targumentList: " + aConfigStore + ".getArgumentList(),");
		aLines.add("\t\t};");
		aLines.add("\t}");
		aLines.add("\trender() {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<" + aPanel);
		for(String s : aCommand.getFileParams()) {
			aLines.add("\t\t\t\t" + s + "Path={this.state." + s + "Path}");
			aLines.add("\t\t\t\tunconfirmed" + FormatingTools.capitalizeName(s) + "Path={this.state.unconfirmed" + FormatingTools.capitalizeName(s) + "Path}");
		}
		aLines.add("\t\t\t\targumentList={this.state.argumentList}");
		aLines.add("\t\t\t/>");
		aLines.add("\t\t);");
		aLines.add("\t}");
		aLines.add("}");

		aLines.add("export default Container.create(" + aName + ");");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		// TODO Auto-generated method stub
		return null;
	}

}
