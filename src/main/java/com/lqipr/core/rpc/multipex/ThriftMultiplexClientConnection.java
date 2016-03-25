package com.lqipr.core.rpc.multipex;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * thrift 连接
* @Title: JhssRPCConnection.java 
* @Package com.jhss.core.util.thrift 
* @author wangd   
* @date Sep 16, 2013 4:14:30 PM 
* @version V1.0
 */
public class ThriftMultiplexClientConnection implements RPCMultiplexConnection {

	Logger logger = Logger.getLogger(ThriftMultiplexClientConnection.class);
	
	private final TTransport transport;
	
	private final Map<Class<?>, Object> clientMap;
	
	private boolean idle = true;
	
	/**
	 * 负责心跳的接口
	 */
	private String heartbeatClass;
	/**
	 * 心跳的方法
	 */
	private Method heartBeatMethod;
	/**
	 * 心跳的实现对象
	 */
	private Object heartBeatObject;
	
	//TODO
	public ThriftMultiplexClientConnection(Map<String,Class<?>> map,TProtocol protocol,String heartbeatClass){
		try {
			this.heartbeatClass = heartbeatClass;
			this.clientMap = new HashMap<Class<?>, Object>(map.size());
			
			for(Entry<String,Class<?>> entry:map.entrySet()){
				TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol,entry.getKey());
				Constructor<?> ctor=entry.getValue().getConstructor(TProtocol.class);
				clientMap.put(entry.getValue(), ctor.newInstance(mp1));
				
			}
			
			transport=protocol.getTransport();
			transport.open();
		} catch (Throwable e) {
			idle = false;
			throw new ExceptionInInitializerError(e);
		}
	}
	
	@Override
	public <T> T getClient(Class<T> clazz) {
		return (T) clientMap.get(clazz);
	}
	

	public TTransport getTransport() {
		return transport;
	}

	/* (non-Javadoc)
	 * @see com.youguu.core.util.RPCConnection2#isIdle()
	 */
	@Override
	public boolean isIdle() {
		return idle;
	}

	/* (non-Javadoc)
	 * @see com.youguu.core.util.RPCConnection2#setIdle(boolean)
	 */
	@Override
	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	@Override
	public void close() {
		transport.close();
		
	}
	
	@Override
	public boolean heartbeat() {
		boolean result = true;
		long stime = System.currentTimeMillis();
		if(this.heartbeatClass==null){
			logger.info("心跳接口Service未配资，心跳无效");
			result =  true;
		}else{
			if(this.heartBeatMethod==null || this.heartBeatObject==null){
				synchronized (this) {
					if(this.heartBeatMethod==null || this.heartBeatObject==null){
						try {
							Class clazz = Class.forName(this.heartbeatClass);
							Object client = clientMap.get(clazz);
							Method method = client.getClass().getMethod("heartBeat", null);
							this.heartBeatObject = client;
							this.heartBeatMethod = method;
						} catch (ClassNotFoundException e) {
							logger.error(e);
							result =  false;
						} catch (NoSuchMethodException e) {
							logger.error(e);
							result =  false;
						} catch (SecurityException e) {
							logger.error(e);
							result =  false;
						}
						
					}
				}
			}
			
			if(this.heartBeatMethod!=null &&  this.heartBeatObject!=null){
				try {
					int value =(int) this.heartBeatMethod.invoke(this.heartBeatObject,null);
					if(value==1){
						result =   true;
					}
				} catch (Exception e) {
					logger.error(e);
					result =  false;
				}
			}
			
		}
		if(logger.isDebugEnabled()){
			logger.debug("heartbeat times" + (System.currentTimeMillis()-stime));
		}
		
		return result;
	}
}
