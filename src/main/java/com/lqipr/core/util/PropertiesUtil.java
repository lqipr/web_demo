package com.lqipr.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;


/**
 * 返回指定位置文件的对应的Properties
 * @author lqipr
 *
 */
public class PropertiesUtil {

	/**
	 * 根据指定路径获取Properties
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 * @throws Exception
	 */
	public static Properties getProperties(String filePath) throws IOException{
		Properties pro = new Properties();
		InputStream is = null;
		try{
//			is = ClassLoader.getSystemResourceAsStream(filePath);
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			pro.load(is);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pro;
	}
	
	/**
	 * 带编码，防止中文乱码
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static Properties getProperties(String filePath, String charset){
		Properties pro = new Properties();
		Reader reader = null;
		InputStream is = null;
		try{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			reader = new InputStreamReader(is, Charset.forName(charset));
			pro.load(reader);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pro;
	}
	
}
