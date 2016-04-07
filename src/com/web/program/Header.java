package com.web.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.web.program.Program.ProgramType;

public class Header {
	
	//Standard set of back-end libraries
	private final String TOOL = "var tool_usage = require(__base + 'app/logic/util/tool_usage');";
	private final String USER_UTILS = "var userfile_utils = require(__base + 'app/logic/util/userfile_utils');";
	private final String CONFIG = "var config = require(__base + 'config/config');";
	private final String SESSION = "var session = require(__base + 'app/logic/util/session_utils');";
	
	//Standard set of front-end libraries
	private final String IMMUTABLE = "import immutable from 'immutable';";
	private final String REACT = "import React from 'react';";
		
	private List<Module> Includes;
	private ProgramType aType;
	
	public Header(ProgramType pType) {
		Includes = new ArrayList<Module>();
		aType = pType;
		
	}
	
	public void addModule(Module pModule) {
		Includes.add(pModule);
		Collections.sort(Includes);
	}
	
	public void addRepositoryModule(String pName) {
		Includes.add(new RepositoryModule(pName));
		Collections.sort(Includes);
	}
	
	public void addStandardModule(String pName) {
		Includes.add(new StandardModule(pName));
		Collections.sort(Includes);
	}
	
	public void addStandardModule(String pName, String pVarName) {
		Includes.add(new StandardModule(pName,pVarName));
		Collections.sort(Includes);
	}
	
	public String[] toFile() {
		List<String> stringList = new ArrayList<String>();
		switch (aType) {
			case BACK:
				stringList.add(TOOL);
				stringList.add(CONFIG);
				stringList.add(USER_UTILS);
				stringList.add(SESSION);
				break;
			case FRONT:
				stringList.add(IMMUTABLE);
				stringList.add(REACT);
				break;
		}
		for(Module module : Includes){
			stringList.add(module.toString());
		}
		return stringList.toArray(new String[stringList.size()]);
	}

}
