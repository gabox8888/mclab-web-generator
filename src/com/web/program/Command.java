package com.web.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Command {
	
	enum CommandTypes 
	{JAVA,BASH,OTHER}
	
	private final String aPath;
	private final String[] aParsedPath;
	private final CommandTypes aCommandType; 
	private final List<String> aArgs;
	private final int aFile;
	
	public Command(String pPath) {
		aPath = pPath;
		aParsedPath = aPath.split(" ");
		aArgs = new ArrayList<String>();
		if (aParsedPath[0].equals("java")) {
			aCommandType = CommandTypes.JAVA;
		} else {
			aCommandType = CommandTypes.BASH;
		}
		aFile = findFile();
		
		System.out.println(Arrays.toString(findArgs()));
		System.out.println(aArgs.toString());

	}
	
	public String toString() {
		
		StringBuilder aStrBuilder = new StringBuilder();
		aStrBuilder.append("'");
		
		switch (aCommandType) {
			case JAVA:
				aStrBuilder.append(" java -jar");
				aStrBuilder.append(" ${config." + aParsedPath[1]);
				break;
			case BASH:
				break;
			case OTHER:
				break;
		}
		
		return "'"+aParsedPath[0] + aParsedPath[1] + "${config."+ aParsedPath[2] + aParsedPath[3] + "${pathToFile}';";
	}
	
	public int[] findArgs() {
		int k = 0;
		List<Integer> tArgs = new ArrayList<Integer>();
		for (String s : aParsedPath) {
			String[] tempStrings = s.split("-");
			if (tempStrings.length > 1) {
				aArgs.add(tempStrings[tempStrings.length-1]);
				tArgs.add(k);
			}
			k++;
		}
		
		int j = 0;
		int[] result = new int[tArgs.size()];
		for (Integer i : tArgs){
			result[j]  = i.intValue();
			j++;
		}
		
		return result;
	}
	
	public int findFile() {
		int k = 0;
		for (String s : aParsedPath) {
			if (s.equals("FILE")){
				return k;
			}
			k++;
		}
		
		return -1;
	}
	
}

