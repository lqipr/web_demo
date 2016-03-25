package com.lqipr.core.rpc;


import com.lqipr.core.rpc.multipex.RPCMultiplexPool;

import java.util.HashMap;
import java.util.Map;

/**
 * RPC服务提供
* @Title: RPCProvide.java 
* @Package com.jhss.youguu.qoute.rpc.client 
* @author wangd   
* @date Sep 16, 2013 7:31:00 PM 
* @version V1.0
 */
public class RPCServiceClient {
	
	/**
	 * 单客户端
	 */
	private final static Map<String,RPCPool> pool = new HashMap<String,RPCPool>();
	
	/**
	 * 多客户端
	 */
	private final static Map<String,RPCMultiplexPool> multiplexPool = new HashMap<String,RPCMultiplexPool>();
	
	/**
	 * 获取连接池对象
	 * @return
	 */
	public static <Client> RPCPool<Client> getRPCPool(String className){
		RPCPool<Client> rpc = pool.get(className);
		if(rpc!=null) return rpc;
		synchronized (pool) {
			rpc = pool.get(className);
			if(rpc==null){
				rpc = new RPCPool<Client>(className);
				pool.put(className, rpc);
			}
		}
		return rpc;
	}
	
	/**
	 * 获取链接池对象
	 * @param clazz
	 * @return
	 */
	public static <Client> RPCPool<Client> getRPCPool(Class<?> clazz){
		
		return getRPCPool(clazz.getName());
		
	}
	
	/**
	 * 返回多客户端版本的连接
	 * @param poolName
	 * @return
	 */
	public static RPCMultiplexPool getMultiplexCPool(String poolName){
		RPCMultiplexPool rmp = multiplexPool.get(poolName);
		if(rmp!=null) return rmp;
		synchronized (multiplexPool) {
			rmp = multiplexPool.get(poolName);
			if(rmp==null){
				rmp = new RPCMultiplexPool(poolName);
				multiplexPool.put(poolName, rmp);
			}
		}
		
		return rmp;
	}
	
}
