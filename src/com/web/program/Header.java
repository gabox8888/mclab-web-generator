package com.web.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Header implements Writable {
	
	private final String LOGGER = "var logger = console;";
	private final String REPOSITORYFACTORY = "var repositoryFactory = require('../../repository/RepositoryFactory').getRepositoryFactory();";
	private final String ENV = "var env = process.env.NODE_ENV || 'development';";
	private final String CONFIG = "var config = require('../../../config/config')[env];";
	private final String ERROR = "var ErrorUtil = require('../util/ErrorUtil');";

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
		stringList.add(LOGGER);
		stringList.add(REPOSITORYFACTORY);
		stringList.add(ENV);
		stringList.add(CONFIG);
		stringList.add(ERROR);
		for(Module module : Includes){
			stringList.add(module.toString());
		}
		return stringList.toArray(new String[stringList.size()]);
	}

}
