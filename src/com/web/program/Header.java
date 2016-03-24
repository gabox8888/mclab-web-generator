package com.web.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.web.program.Program.ProgramType;

public class Header implements Writable {
	
	//Standard set of backend libraries
	private final String TOOL = "var tool_usage = require(__base + 'app/logic/util/tool_usage');";
	private final String USER_UTILS = "var userfile_utils = require(__base + 'app/logic/util/userfile_utils');";
	private final String CONFIG = "var config = require(__base + 'config/config');";
	private final String SESSION = "var session = require(__base + 'app/logic/util/session_utils');";
	
	//Standard set of fronend libraries
	private final String AT = "import AT from '../constants/AT';";
	private final String DISPATCHER = "import Dispatcher from '../Dispatcher';";
	private final String IMMUTABLE = "import Immutable from 'immutable';";
	private final String OPENFILE = "import OpenFileStore from '../stores/OpenFileStore';";
	private final String TERMINAL = "import TerminalActions from './TerminalActions';";
	private final String ONLOAD = "import OnLoadActions from './OnLoadActions';";
	private final String EDITOR = "import EditorMarkerActions from './EditorMarkerActions';";
	private final String REQUEST = "import request from 'superagent';";
	private final String REACT = "import React from 'react';";
		
	private List<Module> Includes;
	private ProgramType aType;
	
	public Header(ProgramType pType) {
		Includes = new ArrayList<Module>();
		aType = pType;
		
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
	
	@Override
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
				stringList.add(AT);
				stringList.add(DISPATCHER);
				stringList.add(IMMUTABLE);
				stringList.add(OPENFILE);
				stringList.add(TERMINAL);
				stringList.add(ONLOAD);
				stringList.add(EDITOR);
				stringList.add(REQUEST);
				stringList.add(REACT);
				break;
		}
		for(Module module : Includes){
			stringList.add(module.toString());
		}
		return stringList.toArray(new String[stringList.size()]);
	}

}
