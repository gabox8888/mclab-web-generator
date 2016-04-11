package com.web.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.web.program.Types;
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
				BASE_PATH = jsonBasePath + "/";
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
		boolean aUsePanel = false;
		
		Program aProgram = new Program(aType.name(),ProgramType.BACK);
		Program aProgramFront = null;
		Command aCommand = new Command(jsonCommand);
		Function aBusinessFunction = null;
		Language aLanguage = null;
		CompileArgSelector aArgSelector = null;
		CompileConfigStore aConfigStore = null;
		AnalysisConfigStore aActionStore = null;
		CompilePanel aCompilePanel = null;
		SidePanel aSidePanel = null;
		CompilePanelContainer aCompilePanelContainer = null;
		AnalysisPanelContainer aAnalysisPanelContainer = null;
		UserFiles aUserFiles = null;
		Function aFrontFunction = null;
		Function aRequestFunction = null;

		File fMainBackend = null;
		File fUserFiles = null;
		File fMainFrontend = null;
		File fCompileArgSelector = null;
		File fCompilePanel = null;
		File fCompileStore = null;
		File fActivePanelStore = null;
		File fATComponent = null;
		File fPanelKeys = null;
		File fSidePanel = null;
		File fCompilePanelContainer = null;
		File fTopNav = null;
		File fIndexRoute = new File(BASE_PATH + "mcnode/app/route/index.js");
		
		List<TextEditor> aEditors = new ArrayList<TextEditor>();
		
		String aATName = null;
						
		switch (aType) {
			case ANALYSIS:
				Function aFormatFunction = new FormatFunction("extract"+jsonName,jsonInputFormat,jsonOutoutFormat);
				aProgramFront = new Program(jsonName + "Actions",ProgramType.FRONT);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand, aFormatFunction);
				aRequestFunction = new RequestFunction(jsonName,aType,aCommand,aBusinessFunction);
				aFrontFunction = new FrontEndFunction("begin" + jsonName , aCommand, (RequestFunction)aRequestFunction,jsonName,aType);
				aProgram.addFunction(aFormatFunction);
				aProgramFront.addMainFunction(aFrontFunction);
				
				fMainBackend = new File(BASE_PATH + "mcnode/app/logic/tools/analysis.js");
				aATName = FormatingTools.parseByCamelCase(jsonName+"Panel").replaceFirst(".$","");
				
				if (aCommand.getArgsParams().length >0) {
					aUsePanel = true;
					
					aActionStore = new AnalysisConfigStore(jsonName);
					aProgramFront = new Program(jsonName + "Actions",ProgramType.FRONT,aActionStore);
					
					aProgramFront.addMainFunction(aFrontFunction);
					
					aArgSelector = new CompileArgSelector(aCommand, jsonName);
					aCompilePanel = new CompilePanel(jsonName,aCommand,aArgSelector,aProgramFront);
					aAnalysisPanelContainer = new AnalysisPanelContainer(aCommand,jsonName, aActionStore.getName(),aCompilePanel.getName());
					aSidePanel = new SidePanel(aAnalysisPanelContainer,aCompilePanel);
					
					Function aOpenPanelFunction = new OpenPanelFunction(aATName);
					aProgramFront.addFunction(aOpenPanelFunction);
					
					fCompileArgSelector = new File(BASE_PATH + "js/" + aArgSelector.getName()+ ".react.js");
					fCompilePanel = new File(BASE_PATH + "js/" + aCompilePanel.getName() + ".react.js");
					fCompileStore = new File(BASE_PATH + "js/stores/" + aActionStore.getName() + ".js");
					fCompilePanelContainer = new File(BASE_PATH + "js/" + aAnalysisPanelContainer.getName() + ".react.js");
					fSidePanel = new File(BASE_PATH + "js/SidePanel.react.js");
					fActivePanelStore = new File(BASE_PATH + "js/stores/ActiveSidePanelStore.js");
					fPanelKeys = new File(BASE_PATH + "js/constants/SidePanelKeys.js");
					
					aEditors.add(new TextEditor(fCompileStore, aActionStore,false));
					aEditors.add(new TextEditor(fCompileArgSelector, aArgSelector,false));
					aEditors.add(new TextEditor(fCompilePanel, aCompilePanel,false));
					aEditors.add(new TextEditor(fCompilePanelContainer, aAnalysisPanelContainer,false));
					aEditors.add(new TextEditor(fSidePanel, aSidePanel,true));
					aEditors.add(new TextEditor(fPanelKeys, aCompilePanel,true));
					aEditors.add(new TextEditor(fActivePanelStore, aCompilePanel,true));
					
				}

				break;
			default:
				aUsePanel = true;
				
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aConfigStore = new CompileConfigStore(aLanguage.getName(),aCommand);
				aProgramFront = new Program(aLanguage.getName() + "CompileActions",ProgramType.FRONT,aConfigStore);
				aArgSelector = new CompileArgSelector(aCommand, aLanguage);
				aCompilePanel = new CompilePanel(aLanguage,aCommand,aArgSelector,aProgramFront);
				aBusinessFunction = new BusinessFunction(jsonName,aType,aCommand,aLanguage);
				aRequestFunction = new RequestFunction(jsonName,aType,aCommand,aBusinessFunction);
				aFrontFunction = new FrontEndFunction("begin" + jsonName , aCommand, (RequestFunction) aRequestFunction,aLanguage,aType);
				aCompilePanelContainer = new CompilePanelContainer(aCommand,aLanguage.getName(), aConfigStore.getName(),aCompilePanel.getName());
				aSidePanel = new SidePanel(aCompilePanelContainer,aCompilePanel);
				aUserFiles = new UserFiles(aLanguage);
				
				aATName = aLanguage.getName().toUpperCase() + "_COMPILE_PANEL";
				
				Function aOpenPanelFunction = new OpenPanelFunction(aATName);
				aProgramFront.addFunction(aOpenPanelFunction);
				aProgramFront.addMainFunction(aFrontFunction);
				
				fMainBackend = new File(BASE_PATH + "mcnode/app/logic/tools/compile.js");
				fCompileArgSelector = new File(BASE_PATH + "js/" + aArgSelector.getName()+ ".react.js");
				fCompilePanel = new File(BASE_PATH + "js/" + aCompilePanel.getName() + ".react.js");
				fCompileStore = new File(BASE_PATH + "js/stores/" + aConfigStore.getName() + ".js");
				fActivePanelStore = new File(BASE_PATH + "js/stores/ActiveSidePanelStore.js");
				fPanelKeys = new File(BASE_PATH + "js/constants/SidePanelKeys.js");
				fSidePanel = new File(BASE_PATH + "js/SidePanel.react.js");
				fUserFiles = new File(BASE_PATH + "mcnode/app/logic/util/userfile_utils.js");
				fCompilePanelContainer = new File(BASE_PATH + "js/" + aCompilePanelContainer.getName() + ".react.js");
				
				aEditors.add(new TextEditor(fCompilePanelContainer, aCompilePanelContainer,false));
				aEditors.add(new TextEditor(fCompileArgSelector, aArgSelector,false));
				aEditors.add(new TextEditor(fCompilePanel, aCompilePanel,false));
				aEditors.add(new TextEditor(fPanelKeys, aCompilePanel,true));				
				aEditors.add(new TextEditor(fUserFiles, aUserFiles,true));
				aEditors.add(new TextEditor(fActivePanelStore, aCompilePanel,true));
				aEditors.add(new TextEditor(fSidePanel, aSidePanel,true));
				aEditors.add(new TextEditor(fCompileStore, aConfigStore,false));
				
				break;
		}
				 
		EndPointProgram aEndPoint = new EndPointProgram((RequestFunction) aRequestFunction,aType); 
		ATComponent aATComponent = new ATComponent(aATName, aType,aCommand);
		FrontEndButton aButton = new FrontEndButton(aProgramFront,aUsePanel,jsonName);
		
		aProgram.addFunction(aBusinessFunction);
		aProgram.addMainFunction(aRequestFunction);
		
		fMainFrontend = new File(BASE_PATH + "js/actions/" + aProgramFront.getName() + ".js");
		fATComponent = new File(BASE_PATH + "js/constants/AT.js");
		fTopNav = new File(BASE_PATH + "js/TopNav.react.js");

		aEditors.add(new TextEditor(fMainBackend, aProgram,true));		
		aEditors.add(new TextEditor(fIndexRoute, aEndPoint,true));
		aEditors.add(new TextEditor(fMainFrontend, aProgramFront,false));
		aEditors.add(new TextEditor(fATComponent, aATComponent,true));
		aEditors.add(new TextEditor(fTopNav, aButton,true));
		
		for(TextEditor e : aEditors) {
			e.writeToFile();
		}
	}

}
