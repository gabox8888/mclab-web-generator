package com.web.program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.web.program.BusinessFunction.Types;
import com.web.program.Program.ProgramType;

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
		
		Program aProgram = new Program(ProgramType.BACK);
		Function aBusinessFunction = null;
		Language aLanguage = null;
		Types aType = Types.valueOf(jsonType);
		
		switch (aType) {
			case ANALYSIS:
				Function aFormatFunction = new FormatFunction("extract"+jsonName,jsonInputFormat,jsonOutoutFormat);
				aBusinessFunction = new BusinessFunction(jsonName,aType,new Command(jsonCommand), aFormatFunction);
				aProgram.addFunction(aFormatFunction);
				break;
			case COMPILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aBusinessFunction = new BusinessFunction(jsonName,aType,new Command(jsonCommand),aLanguage);
				break;
			case FILE:
				aLanguage = new Language(jsonLanguageName,jsonLanguageExt);
				aBusinessFunction = new BusinessFunction(jsonName,aType,new Command(jsonCommand),aLanguage);
				break;
		}
		
		System.out.println(((BusinessFunction)aBusinessFunction).getRoute());
		
		aProgram.addFunction(aBusinessFunction);
		
		BufferedWriter output = null;
        try {
            File file = new File("example.js");
            output = new BufferedWriter(new FileWriter(file));
            for (String s : aProgram.toFile()) {
    			output.write(s+ "\n");
    		}
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null )
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }

        
		
	}

}
