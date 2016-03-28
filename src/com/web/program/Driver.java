package com.web.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.web.program.BusinessFunction.Types;
import com.web.program.Program.ProgramType;
import com.web.tools.FormatingTools;
import com.web.tools.TextEditor;

public class Driver {

	public static void main(String[] args) {
		
		JSONParser parser = new JSONParser();
		String jsonName = null;
		String jsonType = null;
		String jsonCommand = null;
		String jsonLanguageName = null;
		String jsonLanguageExt = null;
		JSONObject jsonInputFormat = null;
		JSONObject jsonOutoutFormat = null;
		JSONObject jsonLanguage = null;

		try {

			Object obj = parser.parse(new FileReader("test.json"));
			
			JSONObject jsonObject = (JSONObject) obj;
			
			jsonName = (String) jsonObject.get("name");
			jsonType = (String) jsonObject.get("type");
			jsonCommand = (String) jsonObject.get("command");
			
			if (jsonType.equals("COMPILE") || jsonType.equals("FILE")) {
				jsonLanguage = (JSONObject) jsonObject.get("compileLanguage");
				jsonLanguageName = (String) jsonLanguage.get("name");
				jsonLanguageExt = (String) jsonLanguage.get("ext");
			} else if (jsonType.equals("ANALYSIS")) {
				jsonInputFormat = (JSONObject) jsonObject.get("inFormat");
			 	jsonOutoutFormat = (JSONObject) jsonObject.get("outFormat");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Types aType = Types.valueOf(jsonType);
		
		Program aProgram = new Program(aType.name(),ProgramType.BACK);
		Command aCommand = new Command(jsonCommand);
		Function aBusinessFunction = null;
		Language aLanguage = null;
		CompileArgSelector aArgSelector = null;

		File fMainBackend = null;
		File fCompileArgSelector = null;
		File fIndexRoute = new File("index.js");
		File fCompileComponent = new File("test.js");
		
		TextEditor aCompilePanel = null;
				
		switch (aType) {
			case ANALYSIS:
				Function aFormatFunction = new FormatFunction("extract"+jsonName,jsonInputFormat,jsonOutoutFormat);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand, aFormatFunction);
				aProgram.addFunction(aFormatFunction);
				
				fMainBackend = new File("analysis.js");
				break;
			case COMPILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aArgSelector = new CompileArgSelector(aCommand, aLanguage);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand,aLanguage);
				
				fMainBackend = new File("compile.js");
				fCompileArgSelector = new File(aLanguage.getName() + "CompileArgumentSelector.react.js");
				
				aCompilePanel = new TextEditor(fCompileArgSelector, aArgSelector,false);
				aCompilePanel.writeToFile();
				break;
			case FILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aArgSelector = new CompileArgSelector(aCommand, aLanguage);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand,aLanguage);
				
				fMainBackend = new File("compile.js");
				fCompileArgSelector = new File(aLanguage.getName() + "CompileArgumentSelector.react.js");

				aCompilePanel = new TextEditor(fCompileArgSelector, aArgSelector,false);
				aCompilePanel.writeToFile();
				break;
		}
				
		Function aFrontFunction = new FrontEndFunction("begin" + jsonName , aCommand, (BusinessFunction)aBusinessFunction, aLanguage,aType);
		EndPointProgram aEndPoint = new EndPointProgram((BusinessFunction) aBusinessFunction,aType);
		Program aProgramFront = new Program(aLanguage.getName() + "CompileActions",ProgramType.FRONT);
		
		aProgramFront.addMainFunction(aFrontFunction);
		aProgram.addFunction(aBusinessFunction);

		TextEditor aMainProgramEditor = new TextEditor(fMainBackend, aProgram,true);
		//aMainProgramEditor.writeToFile();
		
		TextEditor aIndexEditor = new TextEditor(fIndexRoute, aEndPoint,true);
		//aIndexEditor.writeToFile();
		
		CompilePanel aTest = new CompilePanel(aLanguage,aCommand,aArgSelector,aProgramFront);
		TextEditor aTestEditor = new TextEditor(fCompileComponent, aTest,false);
		aTestEditor.writeToFile();
	}

}
