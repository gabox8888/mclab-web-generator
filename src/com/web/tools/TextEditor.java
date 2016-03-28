package com.web.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.web.program.*;

public class TextEditor {
	
	private final String FLAG = " FLAG ";
	
	BufferedWriter aOutput;
	BufferedReader aInput;
	BufferedReaderIterator aInputIter;
	Writable aWritable;
	File aFile;
	List<String> aLines;
	
	boolean aPartialEdit = false;
	
	public TextEditor(File pFile,Writable pWritable, boolean pPartialEdit) {
		
		aWritable = pWritable;
		aPartialEdit = pPartialEdit;
		aLines = new ArrayList<String>();
		aFile = pFile;
				
		if (aPartialEdit) {
			try {
				aInput = new BufferedReader(new FileReader(aFile));
				aInputIter = new BufferedReaderIterator(aInput);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.fillList();
		
	}
	
	private void fillList() {
		int tIndex = 0;
		if (aPartialEdit) {
			for (String s : aInputIter){
				PartialParts aFlag = hasFlag(s);
				if (aFlag != null) {
					for (String pS : aWritable.getPartial(aFlag)) {
						aLines.add(tIndex , pS);
						++tIndex;
					}
					aLines.add(tIndex,s);
					++tIndex;
				} else {
					aLines.add(s);
					++tIndex;
				}
			}
		} else {
			for (String s : aWritable.toFile()) {
				aLines.add(s);
			}
		}
	}
	
	private PartialParts hasFlag(String pLine) {
		if (pLine.contains(FLAG)) {
			String[] temp = pLine.split(FLAG);
			return PartialParts.valueOf(temp[1]);
		}
		return null;
	}
	
	public void changeSettings(File pFile, Writable pWritable, boolean pPartialEdit) {
		aFile = pFile;
		aWritable = pWritable;
		aPartialEdit = pPartialEdit;
	}
	
	public void writeToFile(){
		try {
			aOutput = new BufferedWriter(new FileWriter(aFile));
            for (String s : aLines) {
            	aOutput.write(s+ "\n");
    		}
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( aOutput != null )
				try {
					aOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
	}


}
