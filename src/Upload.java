import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.sf.json.JSONObject;

/**
 * 
 * 包名称： 
 * 类名称：Upload<br/>   
 * 类描述：上传文件的验证方法类<br/> 
 * @version 0.0.1
 * @author lvchao  
 * TODO
 */

public class Upload {
	private String serverUrl;
	
	public Upload(String url) {
		if(url.indexOf("href") != -1) {
			Pattern p = Pattern.compile(">http.*<");
			Matcher m = p.matcher(url);
			if(m.find()) {
				String matched = m.group();
				url = matched.substring(1, matched.length() - 1);
			}
		}
		while(url.indexOf("&amp;") != -1) {
			url = url.replace("&amp;", "&");
		}
		serverUrl = url;
	}
	
	/**
	 * 
	 * @param localFullPath 文件的路径	
	 * @param url 上传的地址
	 * @param fileType 文件的类型
	 * @return boolean 是否成功上传
	 * @throws HttpException
	 * @throws IOException
	 * @author lvchao
	 * @date 2015年8月14日 上午9:21:55
	 */
	public boolean verifyUploadPictureWithLocalPathAndFileType(String localFullPath,
			String fileType) throws HttpException, IOException {
		String responseString = Access.getRespByPost(serverUrl, fileType, localFullPath);
		
		JSONObject respJson = JSONObject.fromObject(responseString);
		
		if(!respJson.containsKey("filePath")) {
			System.out.println("Fail to upload the file. Please check");
		}
		
		System.out.println("Upload successfully! File path is "  + respJson.get("filePath"));
		//这个可以写到配置文件，以求灵活多变
		final String base = "http://192.168.3.67/upload/";
		
		if(Access.getRespStatusCodeByGet(base + respJson.get("filePath")) == 200)
			return true;
			
		return false;
	}
}
