package com.lqipr.core.rpc.multipex;

import java.util.Map;

/**
 * 
* @Title: IClientLoad.java 
* @Package com.youguu.core.util.rpc.multipex 
* @Description: 实现客户端加载接口 
* @author wangd
* @date 2014年10月22日 下午5:23:58 
* @version V1.0
 */
public interface IClientLoad {
	/**
	 * 加载支持的client
	 * @return
	 */
	Map<String,Class<?>> loadClient();
}
