package com.enjoybt.mng.db;

import java.util.HashMap;
import java.util.Map;

public class DB_TYPE {
	private static Map<String, String> DB_DRIVER = new HashMap<String, String>();
	
	static {
		DB_DRIVER.put("p", "org.postgresql.Driver");
		DB_DRIVER.put("o", "oracle.jdbc.driver.OracleDriver");
		DB_DRIVER.put("t", "com.tmax.tibero.jdbc.TbDriver");
	}
	
	public static String getDriver(String type) {
		return DB_DRIVER.get(type);
	}
}
