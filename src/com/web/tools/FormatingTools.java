package com.web.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

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
		for (Object k : pOut.keySet()) {
			if (pOut.get(k).toString().indexOf("{") != -1) {
				pOut.replace(k, mapToFormat(pIn,(JSONObject) pOut.get(k),pName));
			} else {
				pOut.replace(k, pName + "." + findPathToParam(pIn,pOut.get(k).toString()));
			}
		}
		return pOut;
	}
	
//	public static String[] writeJSON(JSONObject pObject) {
//		List<String> aLines = new ArrayList<String>();
//		
//	}

}
