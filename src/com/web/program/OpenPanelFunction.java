package com.web.program;
import java.util.ArrayList;
import java.util.List;


public class OpenPanelFunction implements Function {
	
	private String aName;
	private String aLanguage;
	
	public OpenPanelFunction(String pLanguage) {
		aName = "openPanel";
		aLanguage = pLanguage;
	}

	@Override
	public String[] declareVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String declareName() {
		return "const " + aName + "= function() {";
	}

	@Override
	public String[] declareBody() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("\tDispatcher.dispatch({");
		aLines.add("\t\taction: AT." + aLanguage.toUpperCase() + "_COMPILE_PANEL.OPEN_PANEL");
		aLines.add("\t});");
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add(this.declareName());
		
		for (String s : this.declareBody()) {
			aLines.add(s);
		}
		
		aLines.add("};");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String getName() {
		return aName;
	}

	@Override
	public void addParam(Params pParam) {

	}

}
