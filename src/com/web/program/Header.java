package com.web.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Header implements Writable {
	
	private final String TOOL = "var tool_usage = require(__base + 'app/logic/util/tool_usage');";
	private final String USER_UTILS = "var userfile_utils = require(__base + 'app/logic/util/userfile_utils');";
	private final String CONFIG = "var config = require(__base + 'config/config');";

	private List<Module> Includes = new ArrayList<Module>();
	
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
		stringList.add(TOOL);
		stringList.add(CONFIG);
		stringList.add(USER_UTILS);
		for(Module module : Includes){
			stringList.add(module.toString());
		}
		return stringList.toArray(new String[stringList.size()]);
	}

}
