package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.tools.FormatingTools;

public class BusinessFunction implements Function {
	
	private String aName;
	private List<Params> aParams;
	private Types aType; 
	private Command aCommand;
	private Function aFormatFunction;
	private Language aLanguage;
	private String aLanguageDir;

	public enum Types
	{ANALYSIS,COMPILE,FILE};
	
	
	/**
	 * 
	 * @param pName
	 */
	public BusinessFunction(String pName, Types pType, Command pCommand) {
		
		aName = FormatingTools.uncapitalizeName(pName);
		aType = pType;
		aCommand = pCommand;
		aParams = new ArrayList<Params>();
		aFormatFunction = null;
		aLanguage = null;
		
		initFunction();
		
	}
	
	public BusinessFunction(String pName, Types pType, Command pCommand,Language pLanguage) {
		
		aName = FormatingTools.uncapitalizeName(pName);
		aType = pType;
		aCommand = pCommand;
		aParams = new ArrayList<Params>();
		aFormatFunction = null;
		aLanguage = pLanguage;
		
		initFunction();
		
	}
	
	public BusinessFunction(String pName, Types pType, Command pCommand, Function pFunction) {
		
		aName = FormatingTools.uncapitalizeName(pName);
		aType = pType;
		aCommand = pCommand;
		aParams = new ArrayList<Params>();
		aFormatFunction = pFunction;
		
		initFunction();
		
	}
	
	private void initFunction() {
		
		addParam(new ParamParams("sessionID"));
		
		if (aType != Types.ANALYSIS) {
			addParam(new UserFileParams("GenDir", "genRoot"));
			addParam(new UserFileParams(aLanguage.getName()+ "Dir", FormatingTools.uncapitalizeName(aLanguage.getName()) + "Root"));
			aLanguageDir = aParams.get(aParams.size()-1).getName();
			
			for(String s : aCommand.getFileParams()) {
				addParam(new BodyParams(s));
				addParam(new FileParams(s));
			}
			
			
		} else {
			
			for(String s : aCommand.getFileParams()) {
				addParam(new ParamParams(s));
				addParam(new FileParams(s));
			}
		}
		
		for(String s : aCommand.getArgsParams()) {
			addParam(new ParamParams(s));
		}
		
		
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
		aParams.add(pParam);
	}
	
	public String declareName() {
		return "function " + aName + "(req,res){";
	}
	
	public String[] declareVariables() {
		List<String> dVars = new ArrayList<String>();
		for (Params param : aParams) {
			dVars.add(param.toString());
		}
		return dVars.toArray(new String[aParams.size()]);
	}
	
	@Override
	public String[] declareBody() {
		
		List<String> aLines = new ArrayList<String>();
		
		switch (aType) {
			case ANALYSIS:
				aLines.add("const command = '" + aCommand.toString() + "';");
				aLines.add("child_process.exec(command, function(error, stdout) {");
				aLines.add("\ttry {");
				aLines.add("\t\tlet output = JSON.parse(stdout);");
				
				if (aFormatFunction != null) {
					aLines.add("\t\tlet formatedOutput = " + aFormatFunction.getName() + "(output);");
					aLines.add("\t\tres.json(formatedOutput);");
				} else {
					aLines.add("\t\tres.json(output);");
				}

				aLines.add("\t} catch(err) {");
				aLines.add("\t\tres.status(400).json({msg: 'Mclab-core failed to do " + aName + " analysis on this file. Is this a valid matlab file?'})");
				aLines.add("\t}");
				aLines.add("});");
				break;
			case COMPILE:
				aLines.add("const command = '" + aCommand.toString() + "';");
				aLines.add("child_process.exec(command, function(error, stdout) {");
				aLines.add("\tif(!err){");
				aLines.add("\t\t// Make a gen folder for the user; if it exists already, just ignore the error");
				aLines.add("\t\tfs.mkdir(GenDir, function(err){");
				aLines.add("\t\t\t// Remove the " + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-code subfolder, if it exists, and make a new one");
				aLines.add("\t\t\tchild_process.exec('rm -r ' + " + aLanguageDir + ", function (err) {");
				aLines.add("\t\t\t\tfs.mkdir(" + aLanguageDir + ", function(err){");
				aLines.add("\t\t\t\t\t// Read all the files in the directory where our " + aLanguage.getExt() + " files are located");
				aLines.add("\t\t\t\t\tconst toCopy = mainFileDir + '/*" + aLanguage.getExt() + "'; // pattern of files to copy");
				aLines.add("\t\t\t\t\tchild_process.exec('mv ${toCopy} ${"+ aLanguageDir + "}', function(err){");
				aLines.add("\t\t\t\t\t\tconst archiveUUID = sessions.createUUID();");
				aLines.add("\t\t\t\t\t\tconst archiveName = '" + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-package-${archiveUUID}';");
				aLines.add("\t\t\t\t\t\tconst archivePath = path.join(GenDir, archiveName + '.zip');");
				aLines.add("\t\t\t\t\t\tconst relPathToArchive = path.relative(GenDir, archivePath);");
				aLines.add("\t\t\t\t\t\tconst package_path = 'files/download/${relPathToArchive}';");
				aLines.add("\t\t\t\t\t\t// Zip the files and return the path to the zip file (relative to /session, since this is the API call to be made)");
				aLines.add("\t\t\t\t\t\tchild_process.exec('zip -j ${archivePath} ${" + aLanguageDir + "}/" + aLanguage.getExt() + "', function(err){");
				aLines.add("\t\t\t\t\t\t\tres.json({package_path: package_path});");
				aLines.add("\t\t\t\t\t\t});");
				aLines.add("\t\t\t\t\t});");
				aLines.add("\t\t\t\t});");
				aLines.add("\t\t\t});");
				aLines.add("\t\t});");
				aLines.add("\t} else {");
				aLines.add("\t\tres.status(400).json({msg: 'Mclab-core failed to compile to " + aLanguage.getName() + ". Is this a valid matlab file?'})");
				aLines.add("\t}");
				aLines.add("});");
				break;
			case FILE:
				aLines.add("// Make a gen folder for the user; if it exists already, just ignore the error");
				aLines.add("fs.mkdir(GenDir, function(err){");
				aLines.add("\t// Remove the " + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-code subfolder, if it exists, and make a new one");
				aLines.add("\tchild_process.exec('rm -r ' + " + aLanguageDir + ", function (err) {");
				aLines.add("\t\tfs.mkdir(" + aLanguageDir + ", function(err){");
				aLines.add("\t\t\tconst command = '" + aCommand.toString() + "';");
				aLines.add("\t\t\tchild_process.exec(command, function(error, stdout) {");
				aLines.add("\t\t\t\tif(!err){");
				aLines.add("\t\t\t\t\tfs.writeFile(path.join(mcvmRootPath, '" + aLanguage.getExt() + "'), stdout, function(err){");
				aLines.add("\t\t\t\t\t\tconst archiveUUID = sessions.createUUID();");
				aLines.add("\t\t\t\t\t\tconst archiveName = '" + FormatingTools.uncapitalizeName(aLanguage.getName()) + "-package-${archiveUUID}';");
				aLines.add("\t\t\t\t\t\tconst archivePath = path.join(GenDir, archiveName + '.zip');");
				aLines.add("\t\t\t\t\t\tconst relPathToArchive = path.relative(GenDir, archivePath);");
				aLines.add("\t\t\t\t\t\tconst package_path = 'files/download/${relPathToArchive}';");
				aLines.add("\t\t\t\t\t\t// Zip the files and return the path to the zip file (relative to /session, since this is the API call to be made)");
				aLines.add("\t\t\t\t\t\tchild_process.exec('zip -j ${archivePath} ${" + aLanguageDir + "}/" + aLanguage.getExt() + "', function(err){");
				aLines.add("\t\t\t\t\t\t\tres.json({package_path: package_path});");
				aLines.add("\t\t\t\t\t\t});");
				aLines.add("\t\t\t\t\t});");
				aLines.add("\t\t\t\t});");
				aLines.add("\t\t\t});");
				aLines.add("\t\t});");
				aLines.add("\t} else {");
				aLines.add("\t\tres.status(400).json({msg: 'Mclab-core failed to compile to " + aLanguage.getName() + ". Is this a valid matlab file?'})");
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
				aRoute = "app.get('/session/:sessionID/analysis/" + FormatingTools.removeDescriptor(aName, "Analysis") +
						 ":filepath([\\w-]*)/?', analysis." + aName + ");";
				break;
			case COMPILE: 
				aRoute = "app.post('/session/:sessionID/compile/" + FormatingTools.removeDescriptor(aName, "Compile") +
						 ", compile." + aName + ");";
				break;
			case FILE:
				aRoute = "app.post('/session/:sessionID/compile/" + FormatingTools.removeDescriptor(aName, "Compile") +
				 ", compile." + aName + ");";
				break;
		}
		
		return aRoute;
	}
	



}
