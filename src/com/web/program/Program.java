package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class Program implements Writable{
	
	enum ProgramType {
		FRONT,BACK
	}
	
	private List<Function> aFunctions;
	private ProgramType aType;
	private Header aHeader;
	private String aName;
	private Function aMainFunction;
	private ConfigStore aStore;
	
	public Program(String pName, ProgramType pType) {
		aFunctions = new ArrayList<Function>();
		aType = pType;
		aHeader = new Header(aType);
		aName = pName;
		aMainFunction = null;
		
		if (aType == ProgramType.BACK) {
			aHeader.addStandardModule("child_process");
			aHeader.addStandardModule("path");
		} else {
			aHeader.addModule(new FrontEndModule("Dispatcher","../Dispatcher"));
			aHeader.addModule(new FrontEndModule("AT","../constants/AT"));
			aHeader.addModule(new FrontEndModule("OpenFileStore","../stores/OpenFileStore"));
			aHeader.addModule(new FrontEndModule("TerminalActions","./TerminalActions"));
			aHeader.addModule(new FrontEndModule("OnLoadActions","./OnLoadActions"));
			aHeader.addModule(new FrontEndModule("request","superagent"));
		}
	}
	
	public Program(String pName, ProgramType pType, ConfigStore pStore) {
		aFunctions = new ArrayList<Function>();
		aType = pType;
		aHeader = new Header(aType);
		aName = pName;
		aStore = pStore;
		aMainFunction = null;
		
		if (aType == ProgramType.BACK) {
			aHeader.addStandardModule("child_process");
			aHeader.addStandardModule("path");
		} else {
			aHeader.addModule(new FrontEndModule("Dispatcher","../Dispatcher"));
			aHeader.addModule(new FrontEndModule("AT","../constants/AT"));
			aHeader.addModule(new FrontEndModule(aStore.getName(),"../stores/" + aStore.getName()));
			aHeader.addModule(new FrontEndModule("OpenFileStore","../stores/OpenFileStore"));
			aHeader.addModule(new FrontEndModule("TerminalActions","./TerminalActions"));
			aHeader.addModule(new FrontEndModule("request","superagent"));
			aHeader.addModule(new FrontEndModule("OnLoadActions","./OnLoadActions"));

		}
	}
	
	public String getName() {
		return aName;
	}
	
	public void addFunction(Function pFunction) {
		aFunctions.add(pFunction);
	}
	
	public void addMainFunction(Function pFunction) {
		aMainFunction = pFunction;
		addFunction(pFunction);
	}
	
	public Function getMainFunction() {
		return aMainFunction;
	}
	
	public String[] declareExport() {
		List<String> aLines = new ArrayList<String>();
		if (aType == ProgramType.BACK) {
			aLines.add("module.export = {");
		} else {
			aLines.add("export default{");
		}
		
		int k = 0;
		for (Function f: aFunctions) {
			if (k ==  aFunctions.size() - 2) aLines.add("\t" + f.getName() + ",");
			else aLines.add("\t" + f.getName());
			
		}
		aLines.add("}");
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declarePartialExport() {
		List<String> aLines = new ArrayList<String>();		
		int k = 0;
		for (Function f: aFunctions) {
			if (k ==  aFunctions.size() - 2) aLines.add("\t" + f.getName() + ",");
			else aLines.add("\t" + f.getName() + ",");
			
		}
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declareFunctions() {
		List<String> aLines = new ArrayList<String>();
		for(Function f : aFunctions) {
			for (String s: f.toFile()) {
				aLines.add(s);
			}
		}
		return aLines.toArray(new String[aLines.size()]);
	}
	
	public String[] declareHeader() {
		List<String> aLines = new ArrayList<String>();
		for(String s : aHeader.toFile()) {
			aLines.add(s);
		}
		return aLines.toArray(new String[aLines.size()]);
	}


	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		for(String s : this.declareHeader()) {
			aLines.add(s);
		}
		
		for (String s : this.declareFunctions()) {
			aLines.add(s);
		}
		
		for (String s : this.declareExport()) {
			aLines.add(s);
		}
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		List<String> aLines = new ArrayList<String>();
		switch (pPart) {
			case HEADER:
				for (String s : this.declareHeader()) {
					aLines.add(s);
				}
				break;
			case FUNCTIONS:
				for (String s : this.declareFunctions()) {
					aLines.add(s);
				}
				break;
			case EXPORTS:
				for(String s : this.declarePartialExport()) {
					aLines.add(s);
				}
				break;
			default:
				break;
		}
		
		return aLines.toArray(new String[aLines.size()]);
	}
	
	

}
