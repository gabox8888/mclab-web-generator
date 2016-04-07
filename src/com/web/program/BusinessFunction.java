package com.web.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.web.tools.FormatingTools;

public class BusinessFunction implements Function {
	
	private String aName;
	private List<Params> aParamsVar;
	private List<Params> aParams;
	private Types aType; 
	private Command aCommand;
	private Function aFormatFunction;
	private Language aLanguage;
	private String aLanguageDir;
	private String aMainPath = null;
	
	
	/**
	 * 
	 * @param pName
	 */
	public BusinessFunction(String pName, Types pType, Command pCommand) {
		
		aName = "apply" + FormatingTools.getNickname(pName);
		aType = pType;
		aCommand = pCommand;
		aParamsVar = new ArrayList<Params>();
		aParams = new ArrayList<Params>();
		aFormatFunction = null;
		aLanguage = null;
		
		initFunction();
		
	}
	
	public BusinessFunction(String pName, Types pType, Command pCommand,Language pLanguage) {
		
		aName = "apply" + FormatingTools.getNickname(pName);
		aType = pType;
		aCommand = pCommand;
		aParamsVar = new ArrayList<Params>();
		aParams = new ArrayList<Params>();
		aFormatFunction = null;
		aLanguage = pLanguage;
		
		initFunction();
		
	}
	
	public BusinessFunction(String pName, Types pType, Command pCommand, Function pFunction) {
		
		aName = "apply" + FormatingTools.getNickname(pName);
		aType = pType;
		aCommand = pCommand;
		aParamsVar = new ArrayList<Params>();
		aParams = new ArrayList<Params>();
		aFormatFunction = pFunction;
		
		initFunction();
		
	}
	
	private void initFunction() {
		
		aParams.add(new HeaderParams("SessionID"));
		
		if (aType != Types.ANALYSIS) {
			addParam(new UserFileParams("genRootPath", "genRoot"));
			addParam(new UserFileParams("userRootPath", "userRoot"));
			addParam(new UserFileParams(aLanguage.getName().toLowerCase() + "RootPath", aLanguage.getName().toLowerCase() + "Root"));
			aLanguageDir = aParamsVar.get(aParamsVar.size()-1).getName();
			
			for(String s : aCommand.getFileParams()) {
				addParam(new FileParams(s));
				aParams.add(new BodyParams(s));
			}
			
			
			for(String s : aCommand.getArgsParams()) {
				aParams.add(new ArgBodyParams(s));
			}
			
			
		} else {
			
			for(String s : aCommand.getFileParams()) {
				addParam(new ParamParams(s));
				addParam(new FileParams(s));
			}
			
			for(String s : aCommand.getArgsParams()) {
				addParam(new ParamParams(s));
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

	
	/**
	 * @return aName
	 */
	public String getName() {
		return aName;
	}
	
	/**
	 * Add body parameters to the function.
	 * @param pParam
	 */
	public void addParam(Params pParam) { 
		aParamsVar.add(pParam);
	}
	
	public String declareName() {
		return "function " + aName + "(" + getParamString() + "cb){";
	}
	
	public String[] declareVariables() {
		List<String> aLines = new ArrayList<String>();
		for (Params param : aParamsVar) {
			aLines.add(param.toString());
			if (param.getName().contains("Main"))aMainPath = param.getName();
		}
		return aLines.toArray(new String[aLines.size()]);	}
	
	@Override
	public String[] declareBody() {
		
		List<String> aLines = new ArrayList<String>();
		
		switch (aType) {
			case ANALYSIS:
				aLines.add("const command = `" + aCommand.toString() + "`;");
				aLines.add("child_process.exec(command, function(error, stdout) {");
				aLines.add("\ttry {");
				aLines.add("\t\tlet output = JSON.parse(stdout);");
				
				if (aFormatFunction != null) {
					aLines.add("\t\tlet formatedOutput = " + aFormatFunction.getName() + "(output);");
					aLines.add("\t\tcb(null,formatedOutput);");
				} else {
					aLines.add("\t\tcb(output,null);");
				}

				aLines.add("\t} catch(err) {");
				aLines.add("\t\tcb({msg: 'Mclab-core failed to do " + aName + " analysis on this file. Is this a valid matlab file?'},null)");
				aLines.add("\t}");
				aLines.add("});");
				break;
			case COMPILE:
				aLines.add("const mainDir = path.dirname(" + aMainPath + ");");
				aLines.add("//Compile to " + aLanguage.getName());
				aLines.add("const command = `" + aCommand.toString() + "`;");
				aLines.add("child_process.exec(command, (err, stdout) =>{");
				aLines.add("\tif(!err){");
				aLines.add("\t\t// Make a gen folder for the user; if it exists already, just ignore the error");
				aLines.add("\t\tfs.mkdir(genRootPath, (err)=>{");
				aLines.add("\t\t\t// Remove the " + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-code subfolder, if it exists, and make a new one");
				aLines.add("\t\t\tchild_process.exec('rm -r ' + " + aLanguageDir + ", (err)=>{");
				aLines.add("\t\t\t\tfs.mkdir(" + aLanguageDir + ",(err)=>{");
				aLines.add("\t\t\t\t\t// Read all the files in the directory where our " + aLanguage.getExt() + " files are located");
				aLines.add("\t\t\t\t\tconst toCopy = mainDir + '/*" + aLanguage.getExt() + "'; // pattern of files to copy");
				aLines.add("\t\t\t\t\tchild_process.exec(`mv ${toCopy} ${"+ aLanguageDir + "}`,(err)=>{");
				aLines.add("\t\t\t\t\t\tconst archiveUUID = sessions.createUUID();");
				aLines.add("\t\t\t\t\t\tconst archiveName = '" + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-package-${archiveUUID}';");
				aLines.add("\t\t\t\t\t\tconst archivePath = path.join(genRootPath, archiveName + '.zip');");
				aLines.add("\t\t\t\t\t\tconst relPathToArchive = path.relative(genRootPath, archivePath);");
				aLines.add("\t\t\t\t\t\tconst package_path = `files/download/${relPathToArchive}`;");
				aLines.add("\t\t\t\t\t\t// Zip the files and return the path to the zip file (relative to /session, since this is the API call to be made)");
				aLines.add("\t\t\t\t\t\tchild_process.exec(`zip -j ${archivePath} ${" + aLanguageDir + "}/" + aLanguage.getExt() + "`, (err)=>{");
				aLines.add("\t\t\t\t\t\t\tcb(null,{package_path: package_path});");
				aLines.add("\t\t\t\t\t\t});");
				aLines.add("\t\t\t\t\t});");
				aLines.add("\t\t\t\t});");
				aLines.add("\t\t\t});");
				aLines.add("\t\t});");
				aLines.add("\t} else {");
				aLines.add("\t\tcb({msg: 'Mclab-core failed to compile to " + aLanguage.getName() + ". Is this a valid matlab file?'},null)");
				aLines.add("\t}");
				aLines.add("});");
				break;
			case FILE:
				aLines.add("//Compile to " + aLanguage.getName());
				aLines.add("const command = `" + aCommand.toString() + "`;");
				aLines.add("child_process.exec(command, (err, stdout) =>{");
				aLines.add("\tif(!err){");
				aLines.add("\t\t// Make a gen folder for the user; if it exists already, just ignore the error");
				aLines.add("\t\tfs.mkdir(genRootPath, (err)=>{");
				aLines.add("\t\t\t// Remove the " + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-code subfolder, if it exists, and make a new one");
				aLines.add("\t\t\tchild_process.exec('rm -r ' + " + aLanguageDir + ", (err) =>{");
				aLines.add("\t\t\t\tfs.mkdir(" + aLanguageDir + ", (err)=>{");
				aLines.add("\t\t\t\t\tconst pathToResult = path.join(" + aLanguageDir +  ", '" + aLanguage.getExt() + "'");
				aLines.add("\t\t\t\t\tfs.writeFile(pathToResult, stdout, (err)=>{");
				aLines.add("\t\t\t\t\t\tcb(null,{package_path: pathToResult});");
				aLines.add("\t\t\t\t\t});");
				aLines.add("\t\t\t\t});");
				aLines.add("\t\t\t});");
				aLines.add("\t\t});");
				aLines.add("\t} else {");
				aLines.add("\t\tcb({msg: 'Mclab-core failed to compile to " + aLanguage.getName() + ". Is this a valid matlab file?'},null)");
				aLines.add("\t}");
				aLines.add("});");
				break;
		}
		return aLines.toArray(new String[aLines.size()]);
	}

	
	/**
	 * @return 
	 */
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
	
	public String getRoute() {
		
		String aRoute = null;
		
		switch (aType) {
			case ANALYSIS:
				aRoute = "app.get('/analysis/" + FormatingTools.removeDescriptor(aName, "Analysis") +
						 ":filepath([\\w-]*)/?', analysis." + aName + ");";
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
