package com.web.program;

import java.util.ArrayList;
import java.util.List;

public class Program implements Writable{
	
	List<Function> aFunctions;
	Header aHeader;
	
	public Program() {
		aFunctions = new ArrayList<Function>();
		aHeader = new Header();
		aHeader.addStandardModule("child_process");
	}
	
	public void addFunction(Function pFunction) {
		aFunctions.add(pFunction);
	}
	
	public String[] declareExport() {
		List<String> aLines = new ArrayList<String>();
		aLines.add("module.export = {");
		for (Function f: aFunctions) {
			aLines.add(f.getName() + ",");
		}
		aLines.add("}");
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		for(String s : aHeader.toFile()) {
			aLines.add(s);
		}
		
		for(Function f : aFunctions) {
			for (String s: f.toFile()) {
				aLines.add(s);
			}
		}
		
		for (String s : this.declareExport()) {
			aLines.add(s);
		}
		
		return aLines.toArray(new String[aLines.size()]);
	}
	
	

}
