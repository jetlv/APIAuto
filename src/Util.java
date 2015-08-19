import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * 
 * 包名称： 
 * 类名称：Util<br/>   
 * 类描述：处理Json数据的工具类,有待优化<br/> 
 * @version 0.0.1<br/> 
 * @author lvchao 
 * TODO
 */

public class Util {
	
	private static Logger logger = Logger.getLogger("APITestLogger");
	
	/**
	 * 
	 * @param curJson 初始通过字符串转化而成的Json字符串
	 * @return List<JSONObject> 搜集完毕的Json的List
	 * @author lvchao
	 * @date 2015年8月11日 上午10:42:07
	 */
	public static List<JSONObject> collectJson(JSONObject curJson) {
		List<JSONObject> collected = new ArrayList<JSONObject>();
		//先把自己加进去
		collected.add(curJson);
		Set s = curJson.entrySet();
		Iterator jsonIt = s.iterator();
		while(jsonIt.hasNext()) {
			String strIt = jsonIt.next().toString();
			if(strIt.indexOf("=[") != -1) { 
				if(strIt.indexOf("{") == -1) {
					//非JSON数组含有 [] 的情况, 不处理
					continue;
				}
				JSONArray ja = JSONArray.fromObject(strIt.substring(strIt.indexOf("=") + 1));
				List<JSONObject> fromJA = extract(ja);
				collected.addAll(fromJA);
			} else if(strIt.indexOf("={") != -1) {
				JSONObject jo = JSONObject.fromObject(strIt.substring(strIt.indexOf("=") + 1));
				collected.add(jo);
				collected.addAll(collectJson(jo));
			}
		}
		return collected;
	}
	
	
	/**
	 * 
	 * @param ja 源json数组
	 * @return List<JSONObject> list 为从数组中提取出的Json对象
	 * @author lvchao
	 * @date 2015年8月11日 上午10:43:59
	 */
	public static List<JSONObject> extract(JSONArray ja) {
		int i=0;
		try {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(i=0; i < ja.size(); i++) {
			if(ja.get(i).toString().indexOf("=[") != -1) {
				JSONArray subJa = JSONArray.fromObject(ja.get(i).toString().substring(ja.get(i).toString().indexOf("=") + 1));
				list.addAll(extract(subJa));
			} else if(ja.get(i).toString().startsWith("{")) {
				list.addAll(collectJson(JSONObject.fromObject(ja.get(i))));
			} else {
				list.add(JSONObject.fromObject(ja.get(i).toString()));
			}
		}
		logger.info("从json数组中抽取的json队列为: " + list.toString());
		return list;
		}catch(JSONException e) {
			System.out.println("JSON解析出错, " + e.getMessage());
			System.out.println("出错的JSON为 "  + ja.get(i).toString());
			return null;
		}
	}
	
	
//	public static void main(String[] args) {
//		String test = "{\"id\":9, \"picUrl\" : [\"mecoo/subject/2015/201508/20150814/9dc451bb-9eeb-4664-8ce8-218fe4adc288.jpg\"]}";
//		List<JSONObject> collected = Util.collectJson(JSONObject.fromObject(test));
//		System.out.println(collected.toString());
//	}
}
