package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.program.Types;
import com.web.tools.FormatingTools;

public class RequestFunction implements Function {
	
	private String aName;
	private List<Params> aParams;
	private Types aType; 
	private Command aCommand;
	private Function aApplyFunction;
	
	public RequestFunction(String pName, Types pType, Command pCommand,Function pFunction) {
		
		aName = FormatingTools.uncapitalizeName(pName);
		aType = pType;
		aCommand = pCommand;
		aParams = new ArrayList<Params>();
		aApplyFunction = pFunction;
		
		initFunction();
				
	}
	
	private void initFunction() {
		
		addParam(new HeaderParams("SessionID"));
		
		if (aType != Types.ANALYSIS) {			
			for(String s : aCommand.getFileParams()) {
				addParam(new BodyParams(s));
			}
			
			for(String s : aCommand.getArgsParams()) {
				addParam(new ArgBodyParams(s));
			}
			
			
		} else {
			
			for(String s : aCommand.getFileParams()) {
				addParam(new ParamParams(s));
			}
			
			for(String s : aCommand.getArgsParams()) {
				addParam(new QueryParams(s));
			}
		}	
	}

	private String getParamString() {
		String result = "";
		for (Params p : aParams) {
			result = result + p.getName() + ",";
		}
		return result;
	}

	@Override
	public String[] declareVariables() {
		List<String> aLines = new ArrayList<String>();
		for (Params param : aParams) {
			aLines.add(param.toString());
		}
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String declareName() {
		return "function " + aName + "(req,res){";
	}

	@Override
	public String[] declareBody() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add(aApplyFunction.getName() + "(" + getParamString() + " (err, package_path) => {");
		aLines.add("\tif(!err){");
		aLines.add("\t\tres.json(package_path);");
		aLines.add("\t}else{");
		aLines.add("\t\tres.status(400).json({msg: \"Failed to perform action.\"});");
		aLines.add("\t};");
		aLines.add("});");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String[] toFile() {
		List<String> aLines = new ArrayList<String>();
		
		aLines.add(this.declareName());
		for(String s : this.declareVariables()) {
			aLines.add("\t" + s);
		}
		
		for (String s : this.declareBody()) {
			aLines.add("\t" + s);
		}
		
		aLines.add("}");
		
		return aLines.toArray(new String[aLines.size()]);
	}

	@Override
	public String getName() {
		return aName;
	}

	@Override
	public void addParam(Params pParam) {
		aParams.add(pParam);
	}
	
	public String getRoute() {
		
		String aRoute = null;
		
		switch (aType) {
			case ANALYSIS:
				aRoute = "app.get('/analysis/" + FormatingTools.removeDescriptor(aName, "Analysis") +
						 "/:filepath([\\\\w-]*)/?', analysis." + aName + ");";
				break;
			case COMPILE: 
				aRoute = "app.post('/compile/" + FormatingTools.removeDescriptor(aName, "Compile") +
						 "', compile." + aName + ");";
				break;
			case FILE:
				aRoute = "app.post('/compile/" + FormatingTools.removeDescriptor(aName, "Compile") +
				 "', compile." + aName + ");";
				break;
		}
		
		return aRoute;
	}
	
	public String getEndPoint() {
		String aEndPoint = null;
		
		switch (aType) {
			case ANALYSIS:
				aEndPoint = "/analysis/" + FormatingTools.removeDescriptor(aName, "Analysis") ;
				break;
			case COMPILE: 
				aEndPoint = "/compile/" + FormatingTools.removeDescriptor(aName, "Compile");
				break;
			case FILE:
				aEndPoint = "/compile/" + FormatingTools.removeDescriptor(aName, "Compile");
				break;
		}
		
		return aEndPoint;
	}

}
