package com.lqipr.core.rpc.multipex;

import java.io.Closeable;

public interface RPCMultiplexConnection extends Closeable {

	<T> T getClient(Class<T> clazz);

	boolean isIdle();

	void setIdle(boolean idle);
	
	/**
	 * 
	* @Title: heartbeat
	* @Description: 心跳协议
	* @param @return    
	* @return boolean    返回类型
	* @throws
	 */
	boolean heartbeat();

}