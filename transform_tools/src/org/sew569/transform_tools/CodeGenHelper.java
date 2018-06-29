package org.sew569.transform_tools;

import java.io.*;
import org.apache.commons.lang3.StringUtils;

public class CodeGenHelper {
	
	public CodeGenHelper() {
	}
	
	public static int countOccurrences(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}
	
	// https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
	public static String readFile(String path) {
		String result = "";
		try {
            byte[] buffer = new byte[1000];

            FileInputStream inputStream = 
                new FileInputStream(path);

            int nRead = 0;
            while((nRead = inputStream.read(buffer)) != -1) {
            	String bufferAsString = new String(buffer);
                result = result + bufferAsString;
                buffer = new byte[1000];
            }   

            inputStream.close();        
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                path + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + path + "'");                  
        }
		
		return result;
    }
}
