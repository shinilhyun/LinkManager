package com.enjoybt.mng.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

public class LOG {
	
	private static String logFile;
	private static String logLevel;
	private static File file;
	private static PrintWriter writer = null;
	
	static {
		logFile = CONFIG.getConfig("log.file");
		logLevel = CONFIG.getConfig("log.level").toLowerCase();
		
		if(StringUtils.isEmpty(logFile)) {
			try {
				logFile = URLDecoder.decode(LOG.class.getClassLoader().getResource("").getPath(), "UTF-8") + "log.txt";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if(StringUtils.isEmpty(logLevel) && !"info".equals(logLevel) && !"debug".equals(logLevel) && !"error".equals(logLevel)) {
			logLevel = "info";
		}
		
		file = new File(logFile);
		

		try {
			if(!file.exists()) {
					file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void print(String text) {
		if(logFile != null) {
			try{
				writer = new PrintWriter(new FileOutputStream(file, true));
				writer.println(text);
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
				try{if(writer != null) writer.close(); writer = null;}catch(Exception e) {};
			}
		}
	}
	
	private static String getErrorText(Exception e) {
		StringBuffer sb = new StringBuffer();
		if(e == null) return "";
		StackTraceElement[] stackList = e.getStackTrace();
		sb.append(e.getMessage()+System.lineSeparator());
		for(int i=0,cnt=stackList.length; i<cnt;i++) {
			StackTraceElement stack = stackList[i];
			sb.append((stack.getClassName() + "." + stack.getMethodName() + "(line : " + stack.getLineNumber() + ")") + " --> " + stack.toString()+ System.lineSeparator());
		}
		return sb.toString();
	}
	
	public static void error(Exception e) {
		print(getErrorText(e));
	}
	
	public static void error(String output) {
		print(output);
	}
	
	public static void error(String output, Exception e) {
		print(output + " ERROR : ");
		error(e);
	}
	
	public static void info(String output) {
		if("error".equals(logLevel)) return;
		print(output);
	}
	
	public static void debug(String output) {
		if("error".equals(logLevel) || "info".equals(logLevel)) return;
		print(output);
	}
}
