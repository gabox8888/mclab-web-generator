package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Program.ProgramType;
import com.web.tools.FormatingTools;

public class AnalysisConfigStore implements Writable, ConfigStore{
	
	private String aAction;
	private Header aHeader;
	private String aName;
	
	public AnalysisConfigStore(String pAction)  {
		aAction = FormatingTools.parseByCamelCase(pAction);
		aHeader = new Header(ProgramType.FRONT);
		aHeader.addModule(new FrontEndModule("Dispatcher","../Dispatcher"));
		aHeader.addModule(new FrontEndModule("AT","../constants/AT"));
		aHeader.addModule(new FrontEndModule("{Store}", "flux/utils"));		
		
		aName = pAction + "ConfigStore";
	}
	
	/* (non-Javadoc)
	 * @see com.web.program.ConfigStore#getName()
	 */
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
		
		aLines.add("class " + aName + " extends Store {");
		aLines.add("\tconstructor(dispatcher) {");
		aLines.add("\t\tsuper(dispatcher);");
		aLines.add("\t\tthis._argumentList = immutable.List();");
		aLines.add("\t}");
		
		aLines.add("\t__onDispatch(payload) {");
		aLines.add("\t\tswitch (payload.action) {");
		aLines.add("\t\t\tcase AT." + aAction + "PANEL.ADD_ARGUMENT:");
		aLines.add("\t\t\t\tthis._argumentList =  this._argumentList.push(payload.data['arg']);");
		aLines.add("\t\t\t\tthis.__emitChange();");
		aLines.add("\t\t\t\tbreak;");
		aLines.add("\t\t\tcase AT." + aAction + "PANEL.EDIT_ARGUMENT:");
		aLines.add("\t\t\t\tthis._argumentList =  this._argumentList.set(");
		aLines.add("\t\t\t\t\tpayload.data.argIndex,");
		aLines.add("\t\t\t\t\tpayload.data.arg");
		aLines.add("\t\t\t\t);");
		aLines.add("\t\t\t\tthis.__emitChange();");
		aLines.add("\t\t\t\tbreak;");
		aLines.add("\t\t}");
		aLines.add("\t}");
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
