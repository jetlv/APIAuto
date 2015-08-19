

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

/**
 * 
 * 包名称： com.milanoo.main.services 
 * 类名称：Access<br/>
 * 类描述：业务类，负责利用HttpClient获取接口返回数据.只要是使用Http交互数据的操作都在这个类中<br/>
 * 
 * @version <br/>
 * TODO 暂未作重构
 */
public class Access {
	private static Logger logger = Logger.getLogger("APITestLogger");

	/**
	 * 
	 * @param addrInDetail
	 * @return String 目标接口返回的Json字符串
	 * @author lvchao
	 * @date 2015年8月11日 上午9:33:42
	 */
	public static String getResp(String addrInDetail) {
		GetMethod method = new GetMethod(addrInDetail);
		HttpClient client = new HttpClient();
		String rs = "";
		try {
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			Header deflate = method.getResponseHeader("Content-Encoding");
			String compressType = deflate.getValue();
			if (compressType.indexOf("deflate") != -1
					|| compressType.indexOf("gzip") != -1) {
				Inflater inflater = new Inflater();
				inflater.setInput(b);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
						b.length);
				byte[] buffer = new byte[1024];
				while (!inflater.finished()) {
					int count = inflater.inflate(buffer);
					outputStream.write(buffer, 0, count);
				}
				byte[] output = outputStream.toByteArray();
				outputStream.close();
				rs = new String(output, "UTF-8");
			} else {
				rs = new String(b, "UTF-8");
			}
		} catch (HttpException e) {
			logger.info("捕获了Http异常 " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			logger.info("捕获了I/O异常 " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (DataFormatException e) {
			logger.info("捕获了DataFormatException异常 " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return rs;
	}
	
	
	
	/**
	 * 
	 * @param addrInDetail 服务器URL
	 * @param fileType 文件类型
	 * @param localPath 本地文件的绝对路径
	 * @return String 从服务端获取的文件所在服务器上的相对路径
	 * @author lvchao
	 * @throws FileNotFoundException 
	 * @date 2015年8月13日 下午5:50:42
	 */
	public static String getRespByPost(String addrInDetail, String fileType, String localPath) throws FileNotFoundException {
		
		File file = new File(localPath);
		
		PostMethod method = new PostMethod(addrInDetail);
		Part[] parts = {
			      new StringPart("fileType", fileType),
			      new FilePart("fileData", file)
		};
		
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		HttpClient client = new HttpClient();
		String rs = "";
		try {
			
			client.executeMethod(method);
			rs = method.getResponseBodyAsString();
//			byte[] b = method.getResponseBody();
//			Header deflate = method.getResponseHeader("Content-Encoding");
//			String compressType = deflate.getValue();
//			if (compressType.indexOf("deflate") != -1
//					|| compressType.indexOf("gzip") != -1) {
//				Inflater inflater = new Inflater();
//				inflater.setInput(b);
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
//						b.length);
//				byte[] buffer = new byte[1024];
//				while (!inflater.finished()) {
//					int count = inflater.inflate(buffer);
//					outputStream.write(buffer, 0, count);
//				}
//				byte[] output = outputStream.toByteArray();
//				outputStream.close();
//				rs = new String(output, "UTF-8");
//			} else {
//				rs = new String(b, "UTF-8");
//			}
		} catch (HttpException e) {
			logger.info("捕获了Http异常 " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			logger.info("捕获了I/O异常 " + e.getMessage());
			e.printStackTrace();
			return null;
//		} catch (DataFormatException e) {
//			logger.info("捕获了DataFormatException异常 " + e.getMessage());
//			e.printStackTrace();
//			return null;
		}
		return rs;
	}
	
	/**
	 * 
	 * @param url 访问的链接
	 * @return 返回的Http状态码
	 * @throws HttpException
	 * @throws IOException
	 * @author lvchao
	 * @date 2015年8月14日 上午9:22:55
	 */
	public static int getRespStatusCodeByGet(String url) throws HttpException, IOException {
		GetMethod method = new GetMethod(url);
		HttpClient client = new HttpClient();
		
		client.executeMethod(method);
		
		return method.getStatusCode();
	}
	
	public static void main(String[] args) throws HttpException, IOException {
//		System.out.println(new MainService().getResp("http://192.168.11.16:8080/mecoo/drama/getRecommedDramas.htm?num=3"));
//		System.out.println(new Access().getRespByPost("http://192.168.11.13:8080/mecoo/file/uploadPicturesOfRole.htm", "png", "D:\\duyao.png"));
//		Access.getRespStatusCodeByGet("http://192.168.3.67/upload/mecoo/role/2015/201508/20150813/5447a3bd9-5214-478c-adb2-3cf1e9274738.png");
	}
}
