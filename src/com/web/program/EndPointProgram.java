package com.web.program;

import java.util.*;

import com.web.program.Types;

public class EndPointProgram implements Writable {
	
	RequestFunction aFunction;
	Types aType;
	
	public EndPointProgram(RequestFunction pFunction, Types pType) {
		aFunction = pFunction;
		aType = pType;
	}
	
	@Override
	public String[] toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		List<String> aLines = new ArrayList<String>();
		
		if ((aType == Types.ANALYSIS && pPart == PartialParts.END_POINT_ANALYSIS) || (aType != Types.ANALYSIS && pPart == PartialParts.END_POINT_COMPILE)) {
			aLines.add("\t\t" + aFunction.getRoute());
		} 
		
		return aLines.toArray(new String[aLines.size()]);
	}

}
