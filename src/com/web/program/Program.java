package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class Program implements Writable{
	
	enum ProgramType {
		FRONT,BACK
	}
	
	List<Function> aFunctions;
	ProgramType aType;
	Header aHeader;
	
	public Program(ProgramType pType) {
		aFunctions = new ArrayList<Function>();
		aType = pType;
		aHeader = new Header(aType);
		
		if (aType == ProgramType.BACK) {
			aHeader.addStandardModule("child_process");
			aHeader.addStandardModule("path");
		}
	}
	
	public void addFunction(Function pFunction) {
		aFunctions.add(pFunction);
	}
	
	public String[] declareExport() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("module.export = {");
		
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
			else aLines.add("\t" + f.getName());
			
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
