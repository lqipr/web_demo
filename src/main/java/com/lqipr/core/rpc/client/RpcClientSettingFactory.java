package com.lqipr.core.rpc.client;


import com.lqipr.core.rpc.client.impl.RpcClientFileSettingLoadImpl;

/**
 * 
 * @ClassName: RpcClientSettingFactory
 * @Description: 获取配置属性
 * @author wangdong
 * @date 2015年7月15日 下午5:28:45
 *
 */
public class RpcClientSettingFactory {
	
	public static IRpcClientSettingLoad get(){
		return new RpcClientFileSettingLoadImpl();

//		InputStream input = null;
//		try{
//			input=Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/poolConfig.properties");
//			if(input!=null){
//				return new RpcClientFileSettingLoadImpl();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(input!=null){
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				input=null;
//			}
//		}
//		return new RpcClientZkSettingLoadImpl();
	}
}
