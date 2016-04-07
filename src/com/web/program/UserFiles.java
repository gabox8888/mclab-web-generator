package com.web.program;

import java.util.ArrayList;
import java.util.List;

import com.web.tools.FormatingTools;

public class UserFiles implements Writable {
	
	private String aLanguage;
	private String aPath;
	
	public UserFiles(Language pLanguage) {
		aLanguage = pLanguage.getName().toLowerCase();
		aPath = "pathTo" + FormatingTools.capitalizeName(aLanguage);
	}

	@Override
	public String[] toFile() {
		return null;
	}

	@Override
	public String[] getPartial(PartialParts pPart) {
		List<String> aLines = new ArrayList<String>();
		switch (pPart) {
			case USER_FILES:
				aLines.add("// McLab-Web/user-files/{sessionID}/gen/" + aLanguage + "-code");
				aLines.add("function " + aLanguage + "Root(sessionID){");
				aLines.add("\tconst pathToWorkspace = this.userWorkspace(sessionID);");
				aLines.add("\tconst " + aPath + " = path.join(pathToWorkspace, '" + aLanguage + "-code');"); 
				aLines.add("\treturn path.resolve(" + aPath + ");");
				aLines.add("}");
				break;
			case EXPORTS:
				aLines.add("\t" + aLanguage + "Root,");
				break;
			default:
				break;
		}
		return aLines.toArray(new String[aLines.size()]);
	}

}
