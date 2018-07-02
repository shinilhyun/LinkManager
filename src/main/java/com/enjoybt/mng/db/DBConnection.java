package com.enjoybt.mng.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Vector;

public class DBConnection {
	private int checkedOut;
	private Vector<Connection> freeConnections = new Vector<Connection>();
	private int maxConn;
	private int initConn;
	private int maxWait;
	private String name;
	private String password;
	private String url;
	private String user;
	
	public DBConnection(String name, String url, String user, String password, int maxConn, int initConn, int waitTime) {
		this.name = name;
		this.url = url;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
		this.maxWait = waitTime;
		
		for(int i=0;i<initConn; i++) {
			freeConnections.addElement(newConnection());
		}
	}
	
	private Connection newConnection() {
		Connection con = null;
		
		try{
			if(user == null) {
				con = DriverManager.getConnection(url);
			}else{
				con = DriverManager.getConnection(url, user, password);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	public synchronized Connection getConnection() {
		Connection con = null;
		
		if(freeConnections.size() > 0) {
			con = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			
			try {
				//connection이 닫혀있느냐??
				if(con.isClosed()) {
					con = getConnection();
				}
			}catch(Exception e) {
				//con.isCloased()에 문제가 있는 경우 정상적인 Connection이 아니므로 다시...
				con = getConnection();
			}
		}else if( maxConn == 0 || checkedOut < maxConn) {
			con = newConnection();
		}
		
		if( con != null) {
			checkedOut++;
		}
		
		return con;
	}
	
	public synchronized Connection getConnection(long timeout) {
		long startTime = new Date().getTime();
		Connection con = null;

		while((con = getConnection()) == null) {
			try {
				wait(maxWait);
			}catch(Exception e) {
				
			}
			
			if((new Date().getTime() - startTime) >= timeout) {
				return null;
			}
		}
		
		return con;
	}
	
	public synchronized void freeConnection(Connection con) {
		freeConnections.addElement(con);
		checkedOut--;
		notifyAll();
	}
}
