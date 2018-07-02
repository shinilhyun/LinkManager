package com.enjoybt.mng.db;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

public class DBManager {
	static private DBManager instance;
	private Vector<String> drivers = new Vector<String>();
	private Hashtable<String, DBConnection> pools = new Hashtable<String, DBConnection>();
	
	static synchronized public DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		
		return instance;
	}
	
	private DBManager() {}
	
	public void freeConnection(String name, Connection con) {
		DBConnection pool = (DBConnection) pools.get(name);
		
		if(pool != null) {
			pool.freeConnection(con);
		}
	}
	
	public Connection getConnection(String name) {
		DBConnection pool = (DBConnection) pools.get(name);
		if(pool != null) {
			return pool.getConnection(10000);
		}
		return null;
	}
	
	private void createPools(String poolName, String url, String user, String password, int maxConn, int initConn, int maxWait) {
		DBConnection pool = new DBConnection(poolName, url, user, password, maxConn, initConn, maxWait);
		pools.put(poolName, pool);
	}
	
	private void loadDrivers(String driverClassName) {
		try {
			Class.forName(driverClassName);
			drivers.addElement(driverClassName);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init(String poolName, String driver, String url, String user, String password, int maxConn, int initConn, int maxWait) {
		loadDrivers(driver);
		createPools(poolName, url, user, password, maxConn, initConn, maxWait);
	}
}
