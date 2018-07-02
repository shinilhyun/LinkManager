import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class TestProject {
	public static void main(String[] args) throws Exception {
		String result = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><Response><Header><SuccessYN>Y</SuccessYN><ReturnCode>00</ReturnCode><ErrMsg></ErrMsg></Header><Body><MinuteModel><awsId>116</awsId><awsDt>201709041500</awsDt><rnYn>0</rnYn><rn15M>0</rn15M><rn60M>0</rn60M><rn12H>0</rn12H><rnDay>0</rnDay><ta>23.8</ta><wd1M>32.2</wd1M><ws1M>0.5</ws1M><wdMax>5.6</wdMax><wsMax>1.1</wsMax><wd10M>55</wd10M><ws10M>1.2</ws10M><hm>53.7</hm><ps>-99.8</ps></MinuteModel></Body></Response>";
		
		DocumentBuilderFactory docBuldFac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuld = docBuldFac.newDocumentBuilder();
		Document doc = docBuld.parse(new InputSource(new StringReader(result)));
		Node successYN = doc.getElementsByTagName("SuccessYN").item(0);
		String resultYN = successYN.getTextContent();
		Map<String, Object> param = new HashMap<String, Object>();
		if("Y".equals(resultYN)) {
			String awsId = getElementValueByDataType(doc, "awsId", "String");
			String awsDt = getElementValueByDataType(doc, "awsDt", "String");
			String rnYn = getElementValueByDataType(doc, "rnYn", "String");
			Double rn15M = getElementValueByDataType(doc, "rn15M", "Double");
			Double rn60M = getElementValueByDataType(doc, "rn60M", "Double");
			Double rn12H = getElementValueByDataType(doc, "rn12H", "Double");
			Double rnDay = getElementValueByDataType(doc, "rnDay", "Double");
			Double ta = getElementValueByDataType(doc, "ta", "Double");
			Double wd1M = getElementValueByDataType(doc, "wd1M", "Double");
			Double ws1M = getElementValueByDataType(doc, "ws1M", "Double");
			Double wdMax = getElementValueByDataType(doc, "wdMax", "Double");
			Double wsMax = getElementValueByDataType(doc, "wsMax", "Double");
			Double wd10M = getElementValueByDataType(doc, "wd10M", "Double");
			Double ws10M = getElementValueByDataType(doc, "ws10M", "Double");
			Double hm = getElementValueByDataType(doc, "hm", "Double");
			Double ps = getElementValueByDataType(doc, "ps", "Double");
			param.put("awsId", awsId);
			param.put("awsDt", awsDt);
			param.put("rnYn", rnYn);
			param.put("rn15M", rn15M);
			param.put("rn60M", rn60M);
			param.put("rn12H", rn12H);
			param.put("rnDay", rnDay);
			param.put("ta", ta);
			param.put("wd1M", wd1M);
			param.put("ws1M", ws1M);
			param.put("wdMax", wdMax);
			param.put("wsMax", wsMax);
			param.put("wd10M", wd10M);
			param.put("ws10M", ws10M);
			param.put("hm", hm);
			param.put("ps", ps);
		}
		System.out.println(param);
//		NodeList stnListNode = doc.getElementsByTagName("info");
//		List<Map<String, Object>> stnList = new ArrayList<Map<String, Object>>();
//		if(stnListNode.getLength() > 0) {
//			for(int i=0,cnt=stnListNode.getLength(); i<cnt; i++) {
//				Node stnNode = stnListNode.item(i);
//				Map<String, Object> stn = new HashMap<String, Object>();
//				stnList.add(stn);
//				NodeList infoList = stnNode.getChildNodes();
//				for(int j=0,cnt1=infoList.getLength(); j<cnt1; j++) {
//					Node info = infoList.item(j);
//					stn.put(info.getNodeName(), info.getTextContent());
//				}
//			}
//		}
	}
	
	@SuppressWarnings("unchecked")
	public static <Any>Any getElementValueByDataType(Document doc, String tagName, String type) {
		String val = doc.getElementsByTagName(tagName).item(0).getTextContent();
		if(!StringUtils.isEmpty(val)) {
			if("Integer".equals(type)) {
				return (Any)(Integer)Integer.parseInt(val);
			}else if("Double".equals(type)) {
				return (Any)(Double)Double.parseDouble(val);
			}else if("Float".equals(type)) {
				return (Any)(Float)Float.parseFloat(val);
			}else if("Boolean".equals(type)) {
				return (Any)(Boolean)Boolean.parseBoolean(val);
			}else{
				return (Any)val;
			}
		}else{
			return null;
		}
	}
}
