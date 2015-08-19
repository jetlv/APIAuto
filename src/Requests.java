import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * 
 * 包名称： 
 * 类名称：Requests<br/>   
 * 类描述：包含所有Fitnesses请求方法的类<br/> 
 * @version 0.0.1<br/> 
 * @author lvchao 
 * TODO
 */
public class Requests {
	private Logger logger = Logger.getLogger("APITestLogger");
	//暂不把任何逻辑分离，后期改善
//	final String serverAddr = "http://192.168.12.100:9001/APIAutomatedTest/";
	
	private String currentResult;
	private String currentFullPath;
	
	
	public Requests(String fullPath) {
		if(fullPath.indexOf("href") != -1) {
			Pattern p = Pattern.compile(">http.*<");
			Matcher m = p.matcher(fullPath);
			if(m.find()) {
				String matched = m.group();
				fullPath = matched.substring(1, matched.length() - 1);
			}
		}
		while(fullPath.indexOf("&amp;") != -1) {
			fullPath = fullPath.replace("&amp;", "&");
		}
		currentFullPath = fullPath;
	}
	
//	public static void main(String[] args) {
//		String full = "<a href=\"http://192.168.3.67:8080/mecoo/drama/getRecommedDramas.htm?num=3\">http://192.168.3.67:8080/mecoo/drama/getRecommedDramas.htm?num=3</a>";
//		Pattern p = Pattern.compile(">http.*<");
//		Matcher m = p.matcher(full);
////		System.out.println(m.find());
//		if(m.find()) {
//			String matched = m.group();
//			System.out.println(matched.substring(1, matched.length() - 1));
//		}
////		System.out.println(full.substring(full.indexOf("http")));
//	}
	
	/**
	 * 
	 * @param fullPath 目标接口
	 * @return String 源接口返回的Json字符串
	 * @author lvchao
	 * @throws UnsupportedEncodingException 
	 * @date 2015年8月11日 上午10:00:41
	 */
	public String getOriResult() throws UnsupportedEncodingException {
//		String paramLink = "main?fullPath=" + currentFullPath;
//		GetMethod method = new GetMethod(serverAddr + paramLink);
//		System.out.println(serverAddr + paramLink);
//		HttpClient client = new HttpClient();
//		String result = "";
//		try {
//			client.executeMethod(method);
//			result = method.getResponseBodyAsString();
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("捕获IO异常 " + e.getMessage());
//			return null;
//		}
		String result = Access.getResp(currentFullPath);
		currentResult = result;
		return result;
	}
	

	
	/**
	 * 
	 * @param key 要check的键
	 * @param value 要check的值
	 * @return flag boolean 是否符合预期
	 * @author lvchao
	 * @date 2015年8月11日 上午10:45:43
	 */
	public boolean verifyEqualWithKeyAndValue(String key, String value) {
		List<JSONObject> list = Util.collectJson(JSONObject.fromObject(currentResult));
		
		boolean flag = false;
		
		for(int i=0; i<list.size(); i++) {
			JSONObject cur = list.get(i);
			if(!cur.has(key)) {
				continue;
			}
			if(cur.get(key).equals(value)) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	/**
	 * 
	 * @param jsonExp 传入的被验证json表达式
	 * @return 接口返回值是否包含指定json
 	 * @author lvchao
	 * @date 2015年8月12日 下午2:44:54
	 */
	
	public boolean verifyJsonExists(String jsonExp) {
		int count=0;
		List<JSONObject> list = Util.collectJson(JSONObject.fromObject(currentResult));
		
		JSONObject toVerify = JSONObject.fromObject(jsonExp);
		
		List<String> keyList= new ArrayList<String>();
		List<String> valueList= new ArrayList<String>();
		
		Set<String> s = toVerify.keySet();
		for(String str : s) {
			keyList.add(str);
			valueList.add(String.valueOf(toVerify.get(str)));
		}
		
		for(int i=0; i<list.size(); i++) {
			count = keyList.size();
			JSONObject currentItem = list.get(i);
			for(int j=0; j<keyList.size(); j++) {
				if(!currentItem.containsKey(keyList.get(j))) {
					break;
				}
				if(currentItem.get(keyList.get(j)).toString().equals(valueList.get(j))) {
					count--;
				}
			}
			if(count == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param jsonExp 传入的被验证json表达式
	 * @return 接口返回值是否不包含指定json
 	 * @author lvchao
	 * @date 2015年8月12日 下午2:44:54
	 */
	
	public boolean verifyJsonNotExists(String jsonExp) {
		int count=0;
		List<JSONObject> list = Util.collectJson(JSONObject.fromObject(currentResult));
		
		JSONObject toVerify = JSONObject.fromObject(jsonExp);
		
		List<String> keyList= new ArrayList<String>();
		List<String> valueList= new ArrayList<String>();
		
		Set<String> s = toVerify.keySet();
		for(String str : s) {
			keyList.add(str);
			valueList.add(String.valueOf(toVerify.get(str)));
		}
		
		for(int i=0; i<list.size(); i++) {
			count = keyList.size();
			JSONObject currentItem = list.get(i);
			for(int j=0; j<keyList.size(); j++) {
				if(!currentItem.containsKey(keyList.get(j))) {
					break;
				}
				if(currentItem.get(keyList.get(j)).toString().equals(valueList.get(j))) {
					count--;
				}
			}
			if(count == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean verifyContains(String key) {
		return false;
	}
	
	public boolean verifyGreaterThan(String key, String value) {
		return false;
	}
	
	public boolean verifyLessThan(String key, String value) {
		return false;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		Requests r = new Requests("http://192.168.11.16:8080/mecoo/drama/getRecommedDramas.htm?num=3");
//		String utf = r.getOriResult();
//		System.out.println(utf.charAt(150));
//		System.out.println(utf.charAt(151));
//		System.out.println(utf.charAt(152));
//		JSONObject.fromObject("{\"desc\":\"忍�?成长的故�?\"}");
//		String utfNew=  new String(utf.getBytes("utf-8"), "UTF-8");
//		System.out.println(utfNew);
//		System.out.println(r.verifyEqualWithKeyAndValue("code", "0"));
//		System.out.println(new String("呵呵".getBytes(), "UTF-8"));
//		Requests r = new Requests("http://192.168.3.67:8080/mecoo/news/commentList.htm?newsId=9999&memberId=3&pageSize=10&pageNo=1");
//		r.getOriResult();
//		String test = "{\"commentId\":207,\"liked\":1}";
//		r.verifyJsonExists(test);
	}
}
