package com.web.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import com.web.program.Types;

public class FormatingTools {
	
	public static String capitalizeName(String pName) {
		
		String[] aChars = pName.split("");
		aChars[0] = aChars[0].toUpperCase();
		
		String rString = "";
		
		for (String s : aChars) { 
			rString = rString + s;
		}
		
		return rString;
	}
	
	public static String uncapitalizeName(String pName) {
		
		String[] aChars = pName.split("");
		aChars[0] = aChars[0].toLowerCase();
		
		String rString = "";
		
		for (String s : aChars) { 
			rString = rString + s;
		}
		
		return rString;
	}
	
	public static String findPathToParam(JSONObject pObject, String pParam) {
		for (Object k: pObject.keySet()) {
			if (pObject.get(k).toString().equals(pParam)) {
				return k.toString();
			} else if (pObject.get(k).toString().indexOf("{") != -1) {
				if (findPathToParam ((JSONObject) pObject.get(k), pParam) != null){
					return k.toString() + "." + findPathToParam ((JSONObject) pObject.get(k), pParam);
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject mapToFormat (JSONObject pIn, JSONObject pOut, String pName) {
		
		JSONObject tempObj = (JSONObject) pOut.clone();
		
		for (Object k : tempObj.keySet()) {
			if (tempObj.get(k).toString().indexOf("{") != -1) {
				tempObj.replace(k, mapToFormat(pIn,(JSONObject) tempObj.get(k),pName));
			} else {
				tempObj.replace(k, pName + "." + findPathToParam(pIn,tempObj.get(k).toString()));
			}
		}
		
		return tempObj;
	}
	
	public static List<String> writeJSON(JSONObject pObject, int pTabsNum) {
		List<String> aLines = new ArrayList<String>();
		
		pTabsNum++;
		
		String aTabs = "";
		String aTabsMinus = "";
				
		for (int i = 0; i< pTabsNum; i++) {
			aTabs = aTabs + "\t ";
		}
		
		for (int i = 0; i< pTabsNum-1; i++) {
			aTabsMinus = aTabsMinus + "\t ";
		}
				
		for (Object k : pObject.keySet()) {
			if (pObject.get(k).toString().indexOf("{") != -1) {
				aLines.add(aTabs + k.toString() + ": { ");
				aLines.addAll(writeJSON((JSONObject)pObject.get(k),pTabsNum));
				aLines.set(aLines.size()-1, aLines.get(aLines.size() - 1)+ ",");
			} else {
				aLines.add(aTabs + k.toString() + ": " + pObject.get(k).toString() + ","); 
			}
		}
		
		aLines.add(aTabsMinus + "}");
		
		return aLines;
		
	}
	
	public static String removeDescriptor(String pName, String pType) {
		String[] aParsed = pName.split(pType);
		String result = null;
		
		for(String s : aParsed) {
			if(!s.equals(pType)) result = s;
		}
		
		return result;
		
	}
	
	public static String getNickname(String pName) {
		String result = null;
		String tName = pName.toLowerCase(); 
		for(Types t : Types.values()) {
			String tType = t.name().toLowerCase();
			String[] aParsed = tName.split(tType);
	
			for(String s : aParsed) {
				if(!s.equals(tName) && !s.equals("")) return capitalizeName(s);
			}

		}
			
		return result;
	}
	
	public static String parseByCapitals(String pName) {
		String[] aParsed = pName.split("(?<=.)(?=\\p{Lu})");
		return aParsed[aParsed.length -1];
	}
	
	public static String namifyArg(String pName) {
		String[] aParsed = pName.split("(?<=.)(?=\\p{Lu})");
		for (int i = 0; i < aParsed.length; i++) {
			aParsed[i] = capitalizeName(aParsed[i]);
		}
		String result = "";
		for (String s : aParsed) {
			result = result + " " + s;
		}
		return result;
	}
	
	public static String ififyFiles(String[] pFiles) {
		String tempResult = "this.props." + pFiles[0] + "Path && ";
		for (int i = 1; i < pFiles.length-1; i++) {
			tempResult = tempResult + "this.props." + pFiles[i] + "Path && ";
		}
		
		return tempResult + "this.props." + pFiles[pFiles.length-1] + "Path";
	}

}
