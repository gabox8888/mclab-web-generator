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
		
		String BASE_PATH = null;
		
		JSONParser parser = new JSONParser();
		String jsonName = null;
		String jsonType = null;
		String jsonCommand = null;
		String jsonLanguageName = null;
		String jsonLanguageExt = null;
		String jsonBasePath = null;
		JSONObject jsonInputFormat = null;
		JSONObject jsonOutoutFormat = null;
		JSONObject jsonLanguage = null;

		try {

			Object obj = parser.parse(new FileReader("test.json"));
			
			JSONObject jsonObject = (JSONObject) obj;
			
			jsonName = (String) jsonObject.get("name");
			jsonType = (String) jsonObject.get("type");
			jsonCommand = (String) jsonObject.get("command");
			jsonBasePath = (String) jsonObject.get("basePath");
			
			if (jsonBasePath != null) {
				BASE_PATH = jsonBasePath;
			} else {
				BASE_PATH = "./TEST/";
			}
			
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
		File fMainFrontend = null;
		File fCompileArgSelector = null;
		File fCompilePanel = null;
		File fCompileStore = null;
		File fATComponent = null;
		File fCompilePanelContainer = null;
		File fTopNav = null;
		File fIndexRoute = new File(BASE_PATH + "mcnode/app/route/index.js");
						
		switch (aType) {
			case ANALYSIS:
				Function aFormatFunction = new FormatFunction("extract"+jsonName,jsonInputFormat,jsonOutoutFormat);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand, aFormatFunction);
				aProgram.addFunction(aFormatFunction);
				
				fMainBackend = new File(BASE_PATH + "mcnode/app/logic/tools/analysis.js");
				break;
			case COMPILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aArgSelector = new CompileArgSelector(aCommand, aLanguage);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand,aLanguage);
				
				fMainBackend = new File(BASE_PATH + "mcnode/app/logic/tools/compile.js");
				break;
			case FILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aArgSelector = new CompileArgSelector(aCommand, aLanguage);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand,aLanguage);
				
				fMainBackend = new File(BASE_PATH + "mcnode/app/logic/tools/compile.js");
				break;
		}
		
		String aATName = null;
		
		if (aType != Types.ANALYSIS) {
			 aATName = aLanguage.getName() + " Compile Panel";
		} else {
			 aATName = aLanguage.getName() + " " + aType.name() + " Panel";
		}
				
		Function aFrontFunction = new FrontEndFunction("begin" + jsonName , aCommand, (BusinessFunction)aBusinessFunction, aLanguage,aType);
		Function aOpenPanelFunction = new OpenPanelFunction(aLanguage.getName());
		EndPointProgram aEndPoint = new EndPointProgram((BusinessFunction) aBusinessFunction,aType);
		Program aProgramFront = new Program(aLanguage.getName() + "CompileActions",ProgramType.FRONT);
		CompilePanel aCompilePanel = new CompilePanel(aLanguage,aCommand,aArgSelector,aProgramFront);
		CompileConfigStore aConfigStore = new CompileConfigStore(aLanguage.getName(),aCommand);
		CompilePanelContainer aCompilePanelContainer = new CompilePanelContainer(aCommand,aLanguage.getName(), aConfigStore.getName(),aCompilePanel.getName());
		ATComponent aATComponent = new ATComponent(aATName, aType,aCommand);
		FrontEndButton aButton = new FrontEndButton(aProgramFront,aType,jsonName);
				
		fCompileArgSelector = new File(BASE_PATH + "js/" + aArgSelector.getName()+ ".react.js");
		fCompilePanel = new File(BASE_PATH + "js/" + aCompilePanel.getName() + ".react.js");
		fCompileStore = new File(BASE_PATH + "js/stores/" + aConfigStore.getName() + ".js");
		fMainFrontend = new File(BASE_PATH + "js/actions/" + aProgramFront.getName() + ".js");
		fATComponent = new File(BASE_PATH + "js/constants/AT.js");
		fCompilePanelContainer = new File(BASE_PATH + "js/" + aCompilePanelContainer.getName() + ".react.js");
		fTopNav = new File(BASE_PATH + "js/TopNav.react.js");
		
		aProgramFront.addMainFunction(aFrontFunction);
		aProgramFront.addFunction(aOpenPanelFunction);
		aProgram.addFunction(aBusinessFunction);

		TextEditor aMainProgramEditor = new TextEditor(fMainBackend, aProgram,true);
		aMainProgramEditor.writeToFile();
		
		TextEditor aIndexEditor = new TextEditor(fIndexRoute, aEndPoint,true);
		aIndexEditor.writeToFile();
		
		TextEditor aFrontEditor = new TextEditor(fMainFrontend, aProgramFront,false);
		aFrontEditor.writeToFile();
		
		TextEditor aArgSelectorEditor = new TextEditor(fCompileArgSelector, aArgSelector,false);
		aArgSelectorEditor.writeToFile();
		
		TextEditor aCompilePanelEditor = new TextEditor(fCompilePanel, aCompilePanel,false);
		aCompilePanelEditor.writeToFile();
		
		TextEditor aConfigStoreEditor = new TextEditor(fCompileStore, aConfigStore,false);
		aConfigStoreEditor.writeToFile();
		
		TextEditor aATComponenEditor = new TextEditor(fATComponent, aATComponent,true);
		aATComponenEditor.writeToFile();
		
		TextEditor aCompilePanelContainerEditor = new TextEditor(fCompilePanelContainer, aCompilePanelContainer,false);
		aCompilePanelContainerEditor.writeToFile();
		
		TextEditor aTopNavEditor = new TextEditor(fTopNav, aButton,true);
		aTopNavEditor.writeToFile();
	}

}
