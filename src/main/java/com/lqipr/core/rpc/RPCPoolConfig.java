package com.lqipr.core.rpc;

import com.lqipr.core.rpc.client.RpcClientSettingFactory;

import java.util.Map;


/**
 * 连接池配置文件
* @Description: TODO
* @Title: PoolConfig.java 
* @Package com.jhss.youguu.qoute.rpc.client 
* @author wangd   
* @date Sep 16, 2013 7:25:28 PM 
* @version V1.0
 */
public class RPCPoolConfig{
	private final String className;
	
	
	private String ip;
	
	private int port = 0;
	
	private int maxActive = 8;
	
	
	private int maxIdle = 8;
	
	
	private int minIdle = 8;
	
	
	private long maxWait = -1L;
	
	/**
	 * 最大空闲时间 ， 默认是一小时
	 */
	private long maxIdleTime = 60*60*1000;
	

	private String protocol;
	
	private String transport;
	/**
	 * 加载全部client的类
	 */
	private String loadClass;
	
	/**
	 * 是否需要心跳包 -- 默认不需要
	 */
	private boolean heartbeat = false;
	
	/**
	 * 心跳Service全名
	 */
	private String heartbeatService;
	
	public static RPCPoolConfig getConfig(String section){
		
		Map<String,String> map= RpcClientSettingFactory.get().load(section);
		
		String ip=(map==null? null:map.get("ip"));
		String port=(map==null? null:map.get("port"));
		if(ip==null||port==null){
			throw new ExceptionInInitializerError("RPC连接池的IP和Port均不能为空");
		}
		RPCPoolConfig pconfig = new RPCPoolConfig(section);
		pconfig.setIp(ip);
		pconfig.setPort(Integer.parseInt(port));
		String s=map.get("maxActive");
		if(s!=null&&s.length()>0){
			pconfig.setMaxActive(Integer.parseInt(s));
		}
		s=map.get("maxIdle");
		if(s!=null&&s.length()>0){
			pconfig.setMaxIdle(Integer.parseInt(s));
		}
		s=map.get("minIdle");
		if(s!=null&&s.length()>0){
			pconfig.setMinIdle(Integer.parseInt(s));
		}
		s=map.get("maxWait");
		if(s!=null&&s.length()>0){
			pconfig.setMaxWait(Long.parseLong(s));
		}
		s=map.get("protocol");
		if(s!=null&&s.length()>0){
			pconfig.setProtocol(s);
		}
		s=map.get("transport");
		if(s!=null&&s.length()>0){
			pconfig.setTransport(s);
		}
		s=map.get("loadClass");
		if(s!=null&&s.length()>0){
			pconfig.setLoadClass(s);
		}
		s = map.get("heartbeat");
		if(s!=null && s.length()>0){
			try{
				pconfig.setHeartbeat(Boolean.valueOf(s));
			}catch(Exception e){
				pconfig.setHeartbeat(false);
			}
		}
		s=map.get("heartbeatService");
		if(s!=null&&s.length()>0){
			pconfig.setHeartbeatService(s);
		}
		
		s=map.get("maxIdleTime");
		if(s!=null&&s.length()>0){
			pconfig.setMaxIdleTime(Integer.parseInt(s));
		}
		
		return pconfig;
		
	}
	private RPCPoolConfig(String className){
		this.className=className;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the transport
	 */
	public String getTransport() {
		return transport;
	}

	/**
	 * @param transport the transport to set
	 */
	public void setTransport(String transport) {
		this.transport = transport;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	public String getLoadClass() {
		return loadClass;
	}
	public void setLoadClass(String loadClass) {
		this.loadClass = loadClass;
	}
	public boolean isHeartbeat() {
		return heartbeat;
	}
	public void setHeartbeat(boolean heartbeat) {
		this.heartbeat = heartbeat;
	}
	public String getHeartbeatService() {
		return heartbeatService;
	}
	public void setHeartbeatService(String heartbeatService) {
		this.heartbeatService = heartbeatService;
	}
	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	
	
}
