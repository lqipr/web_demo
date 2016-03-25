package com.lqipr.core.rpc.multipex;

import com.lqipr.core.rpc.RPCPoolConfig;
import com.lqipr.core.rpc.RPCProtocol;
import com.lqipr.core.rpc.RPCTransport;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.transport.TTransport;

import java.util.Map;

/**
 * rpc连接池工厂类
* @Title: JhssRPCPoolFacroty.java 
* @Package com.jhss.core.util.thrift 
* @author wangd   
* @date Sep 16, 2013 3:53:30 PM 
* @version V1.0
 */
public class RPCMultiplexPoolFacroty extends BasePoolableObjectFactory<RPCMultiplexConnection> {
	private final RPCPoolConfig config;
	private final Map<String,Class<?>> map;
	
	
	public RPCMultiplexPoolFacroty(RPCPoolConfig config,Map<String,Class<?>> map){
		super();
		this.config = config;
		this.map = map;
	}
	
	RPCPoolConfig getConfig(){
		return config;
	}
	
	@Override
	public RPCMultiplexConnection makeObject() throws Exception {
		String trans=config.getTransport();
		String prot=config.getProtocol();
		RPCTransport transport=(trans==null? RPCTransport.DEFAULT:RPCTransport.valueOf(trans.toUpperCase()));
		TTransport p=transport.get(config.getIp(), config.getPort());
		RPCProtocol protocol=(prot==null? RPCProtocol.DEFAULT:RPCProtocol.valueOf(prot.toUpperCase()));
		return new ThriftMultiplexClientConnection(map,protocol.get(p),config.getHeartbeatService());
	}
	
	@Override
	public boolean validateObject(RPCMultiplexConnection conn) {
		if(config.isHeartbeat()){
			/**
			 * 注意判断顺序  不能颠倒
			 */
			return conn.isIdle() && conn.heartbeat();
		}else{
			return conn.isIdle();
		}
		
	}
	
	/**
	 * 销毁连接
	 */
	@Override
	public void destroyObject(RPCMultiplexConnection conn) throws Exception {
		try{
			if(conn!=null){
				
				conn.close();
				
				conn = null;
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
		super.destroyObject(conn);
		
	}
	

}
