package com.enjoybt.mng;

import java.util.Map;

import com.enjoybt.mng.manager.LinkManager;
import com.enjoybt.mng.util.LOG;

public class MainProcess {
	public static void main(String[] args) throws Exception {
		Integer linkSn = null;
		String reqUrl = null;
		
		if(args.length > 0) {
			linkSn = Integer.parseInt(args[0]);
		}
		
		if(args.length > 1) {
			reqUrl = args[1];
		}
		
		System.out.println(linkSn + " : " + reqUrl);
		
		
		LinkManager linkManager = new LinkManager(linkSn, reqUrl);
		Map<String, Object> result = linkManager.process();
		LOG.debug(result.toString());
		
	}
}
