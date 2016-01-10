package org.usfirst.frc.team1124.robot.tools;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConfigIO {
	// saves and imports from and to "/home/lvuser/config/robot.cfg"
	private static File file;
	private final static String filePath = "/home/lvuser/config/robot.cfg";
	static Map<String, String> config;
	static BufferedReader br;

	public ConfigIO(){
		file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		config = new HashMap<String, String>();
		try { 
			br = new BufferedReader(new FileReader(filePath)); 
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			String line = br.readLine();

			while (line != null) {
				if(line.charAt(0) != '#'){
					String value1 = "";
					String value2 = "";
					boolean encounteredSpace = false;
					for(int l = 0; l < line.length() && !encounteredSpace; l++){
						if(line.charAt(l) != ' '){
							value1 += line.charAt(l);
						}
						else {
							value2 = line.substring(l+1);
							encounteredSpace = true;
						}
					}
					if(encounteredSpace)
						config.put(value1, value2);
				}
				line = br.readLine();
			}
		} 
		catch (IOException e) { e.printStackTrace(); } 
		finally { 
			try { 
				br.close(); 
			} 
			catch (IOException e) { 
				e.printStackTrace(); 
			} 
		}

//		for(int v = 0; v < config.size(); v++)
//			System.out.println(config.keySet().toArray()[v] + " = " + config.get(config.keySet().toArray()[v]));
	}
	
	public String getStringVal(String key){
		return config.get(key);
	}
	
	public int getIntVal(String key){
		return Integer.parseInt(config.get(key));
	}
}
