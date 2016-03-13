package com.web.program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.web.program.BusinessFunction.Types;

public class Driver {

	public static void main(String[] args) {
		
		JSONParser parser = new JSONParser();
		String jsonName = null;
		String jsonType = null;
		String jsonCommand = null;

		try {

			Object obj = parser.parse(new FileReader("test.json"));

			JSONObject jsonObject = (JSONObject) obj;

			jsonName = (String) jsonObject.get("name");
			jsonType = (String) jsonObject.get("type");
			jsonCommand = (String) jsonObject.get("command");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Program aProgram = new Program();
						
		Function aFunction = new BusinessFunction(jsonName,Types.valueOf(jsonType),new Command(jsonCommand));
		
		aProgram.addFunction(aFunction);
		
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
