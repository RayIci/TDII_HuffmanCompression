package compressor;

import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileItemsCounter {

	//Return a dictionary where for each char in a file count the frequency.
	//At the end of the dictionary char 'eof' is added
	public static Map<String, Integer> countItems(String filename) throws FileNotFoundException{
		
		BufferedReader bufReader = FileOpener.openRead(filename);
		Map<String, Integer> hsMap = new HashMap<>();
		
		String ReadString;
		try {
			while ( (ReadString = bufReader.readLine()) != null ) 
			{
				for(int i = 0; i < ReadString.length(); i++)
				{
					String s = "" + ReadString.charAt(i);
					
					Integer value;
					if(hsMap.containsKey(s))
						value = hsMap.get(s) + 1;
					else
						value = 1;
					
					hsMap.put(s, value);
				}
			}
			
			hsMap.put("eof", 1);  // end of file
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hsMap;
	}

	
	//Return a dictionary where for each char in a file count the frequency.
	//At the end of the dictionary char 'eof' is not added
	public static Map<String, Integer> countItemsNoEOF(String filename) throws FileNotFoundException{
		
		BufferedReader bufReader = FileOpener.openRead(filename);
		Map<String, Integer> hsMap = new HashMap<>();
		
		String ReadString;
		try {
			while ( (ReadString = bufReader.readLine()) != null ) 
			{
				for(int i = 0; i < ReadString.length(); i++)
				{
					String s = "" + ReadString.charAt(i);
					
					Integer value;
					if(hsMap.containsKey(s))
						value = hsMap.get(s) + 1;
					else
						value = 1;
					
					hsMap.put(s, value);
				}
			}
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hsMap;
	}
	
}
