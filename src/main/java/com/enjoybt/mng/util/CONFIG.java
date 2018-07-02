package com.enjoybt.mng.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CONFIG {
	private static String filePath = null;
	private static File file = null;
	private static Map<String, String> config = new HashMap<String, String>();
	
	static {
		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		filePath = jarDir.getAbsolutePath();
		file = new File(filePath, "config.txt");
		System.out.println(file.getPath());
		read();
	}
	
	private static void lineConfig(String line) {
		String[] val = line.split("=");
		if(val.length > 1) {
			String key = val[0];
			String value = val[1];
			
			key = key.toLowerCase();
			config.put(key, value);
		}
	}
	
	private static void read() {
		if(file.exists()) {
			FileReader fr = null;
			BufferedReader br = null;
			
			try{
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line;
				
				while((line = br.readLine()) != null) {
					if(StringUtils.isEmpty(line) || "#".equals(line.substring(0, 1))) {
						continue;
					}
					lineConfig(line);
					//System.out.println(line);
				}
				
			}catch(IOException e) {
				
			}finally {
				try{if(br != null) br.close();}catch(Exception e){};
				try{if(fr != null) fr.close();}catch(Exception e){};
			}
		}else{
			System.out.println("noFile");
		}
	}
	
	public static String getConfig(String key) {
		key = key.toLowerCase();
		if(config.containsKey(key)) {
			return config.get(key);
		}else{
			return null;
		}
	}
}
