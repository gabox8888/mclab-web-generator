package com.web.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web.tools.FormatingTools;


public class Command {
	
	private final String aPath;
	private final List<String> aParsedPath;
	private final List<String> aArgs;
	private final List<String> aFile;
		
	public Command(String pPath) {
		aPath = pPath;
		aParsedPath = new ArrayList<String>(Arrays.asList(aPath.split(" ")));
		aArgs = new ArrayList<String>();
		aFile = new ArrayList<String>();
				
		findArgs();
		findFile();
		
	}
	
	public String toString() {
		
		StringBuilder aStrBuilder = new StringBuilder();
		for (String s : aParsedPath) {
			aStrBuilder.append(s + " ");
		}
		
		
		return aStrBuilder.toString();
	}
	
	private void findArgs() {
		int k = 0;
		for (String s : aParsedPath) {
			String[] tempStrings = s.split("@");
			if (tempStrings.length > 1) {
				aArgs.add(tempStrings[tempStrings.length-1]);
				aParsedPath.set(k, "${" + tempStrings[tempStrings.length-1] + "}");
			}
			k++;
		}
	}
	
	private void findFile() {
		int k = 0;
		for (String s : aParsedPath) {
			String[] tempStrings = s.split("%");
			if (tempStrings.length > 1) {
				aFile.add(tempStrings[tempStrings.length-1]);
				aParsedPath.set(k,"${pathTo" + FormatingTools.capitalizeName(tempStrings[tempStrings.length-1]) + "}");
			}
			k++;
		}
	}

	public String[] getFileParams () { 
		return aFile.toArray(new String[aFile.size()]);
	}
	
	public String[] getArgsParams () { 
		return aArgs.toArray(new String[aArgs.size()]);
	}
	
}

