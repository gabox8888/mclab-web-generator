package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;
import com.web.tools.FormatingTools;

public class AnalysisPanelContainer implements Writable, PanelContainer {
	
	private Command aCommand;
	private String aAction;
	private String aConfigStore;
	private String aPanel;
	private Header aHeader;
	private String aName;
	
	public AnalysisPanelContainer(Command pCommand, String pAction, String pConfigStore, String pPanel) {
		aCommand = pCommand;
		aAction = pAction;
		aConfigStore = pConfigStore; 
		aPanel = pPanel;
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("{Container}","flux/utils"));
		aHeader.addModule(new FrontEndModule(aConfigStore,"./stores/" + aConfigStore));
		aHeader.addModule(new FrontEndModule(aPanel,"./" + aPanel + ".react"));
		
		aName = aAction + "PanelContainer";
	}
	
	@Override
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
		aLines.add("\t\t\targumentList: " + aConfigStore + ".getArgumentList(),");
		aLines.add("\t\t};");
		aLines.add("\t}");
		aLines.add("\trender() {");
		aLines.add("\t\treturn (");
		aLines.add("\t\t\t<" + aPanel);
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
