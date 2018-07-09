package com.enjoybt.mng.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.enjoybt.mng.db.DBManager;
import com.enjoybt.mng.db.DB_TYPE;
import com.enjoybt.mng.util.CONFIG;
import com.enjoybt.mng.util.LOG;
import com.enjoybt.mng.util.WebUtil;

public class LinkManager {

	private DBManager dbPool;
	private String dbType;
	private Integer linkSn = null;
	private String linkUrl = null;
	private Integer webMaxConnectionTime = null;
	
	public LinkManager(Integer linkSn, String linkUrl) {
		dbPool = DBManager.getInstance();
		dbType = CONFIG.getConfig("db.type");
		String url = CONFIG.getConfig("db.url");
		String user = CONFIG.getConfig("db.user");
		String passwrd = CONFIG.getConfig("db.password");
		Integer maxConn = Integer.parseInt(CONFIG.getConfig("db.max_conn"));
		Integer initConn = Integer.parseInt(CONFIG.getConfig("db.init_conn"));
		Integer maxWait = Integer.parseInt(CONFIG.getConfig("db.wait_time"));
		webMaxConnectionTime = Integer.parseInt(CONFIG.getConfig("web.max_time"));
		dbPool.init(dbType, DB_TYPE.getDriver(dbType), url, user, passwrd, maxConn, initConn, maxWait);
		this.linkSn = linkSn;
		this.linkUrl = linkUrl;
	}
	
	public Map<String, Object> process() {
		Map<String, Object> output = new HashMap<String, Object>();
		Date sdt = new Date();
		output.put("linkSn", linkSn);
		output.put("linkUrl", linkUrl);
		output.put("start", sdt);
//		String result = WebUtil.getResponseText(linkUrl, "UTF-8");
		Connection conn = dbPool.getConnection(dbType);
		PreparedStatement pstmt= null;
		String result = "";
		Date edt = null;
		
		try{
			pstmt = conn.prepareStatement("INSERT INTO TBL_LINK_LOG (LINK_SN, RSLT_VAL, RSLT_MSG, REGIST_DT, START_DT, END_DT, LONG_TIME) VALUES (?, ?, ?, NOW(), ?, ?, ?)");
			pstmt.setInt(1, linkSn);
			result = WebUtil.getResponseText(linkUrl, webMaxConnectionTime, "GET", "UTF-8", null, null);
			edt = new Date();
//			output.put("result", result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJson = new JSONObject(result);
				String val = resultJson.getString("RESULT");
				String errVal = null;
				if(resultJson.has("MSG")) {
					Object msg = resultJson.get("MSG");
					errVal = msg.toString();
				}
				String paramVal = "N";
				if("SUCCESS".equals(val)) {
					paramVal = "Y";
				}
				pstmt.setString(2, paramVal);
				pstmt.setString(3, errVal);
			}else{
				pstmt.setString(2, "N");
				pstmt.setString(3, "Network Connection Error");
			}
			pstmt.setTimestamp(4, new Timestamp(sdt.getTime()));
			pstmt.setTimestamp(5, new Timestamp(edt.getTime()));
			pstmt.setLong(6, edt.getTime() - sdt.getTime());
			pstmt.execute();
		}catch(Exception e) {
			output.put("Error", exportErrorMessage(e));
			output.put("result", result);
			edt = new Date();
			try {
				pstmt.setString(2, "N");
				pstmt.setString(3, result + System.lineSeparator() + exportErrorMessage(e));
				pstmt.setTimestamp(4, new Timestamp(sdt.getTime()));
				pstmt.setTimestamp(5, new Timestamp(edt.getTime()));
				pstmt.setLong(6, edt.getTime() - sdt.getTime());
				pstmt.execute();
			} catch (SQLException e1) {
				output.put("Error", exportErrorMessage(e));
			}
		}finally {
			try{if(pstmt != null) pstmt.close();}catch(SQLException e){LOG.error("PSTMT Close Error", e);}
			dbPool.freeConnection(dbType, conn);
		}
		output.put("end", edt);
		output.put("time", (edt.getTime() - sdt.getTime()) + "ms");
		return output;
	}
	
	public String exportErrorMessage(Exception e) {
		String output = null;
		ByteArrayOutputStream out = null;
		PrintStream ps = null;
		try{
			out = new ByteArrayOutputStream();
			ps = new PrintStream(out);
			e.printStackTrace(ps);
			output = out.toString();
		}catch(Exception e1) {
			
		}
		return output;
	}
	
}
