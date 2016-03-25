package com.lqipr.core.rpc;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.transport.TTransport;

/**
 * rpc连接池工厂类
* @Title: JhssRPCPoolFacroty.java 
* @Package com.jhss.core.util.thrift 
* @author wangd   
* @date Sep 16, 2013 3:53:30 PM 
* @version V1.0
 */
public class RPCPoolFacroty<Client> extends BasePoolableObjectFactory<RPCConnection<Client>> {
	private final RPCPoolConfig config;
	
	
	public RPCPoolFacroty(RPCPoolConfig config){
		super();
		this.config = config;
	}
	
	RPCPoolConfig getConfig(){
		return config;
	}
	
	@Override
	public RPCConnection<Client> makeObject() throws Exception {
		String trans=config.getTransport();
		String prot=config.getProtocol();
		RPCTransport transport=(trans==null? RPCTransport.DEFAULT:RPCTransport.valueOf(trans.toUpperCase()));
		TTransport p=transport.get(config.getIp(), config.getPort());
		RPCProtocol protocol=(prot==null? RPCProtocol.DEFAULT:RPCProtocol.valueOf(prot.toUpperCase()));
		Class<?> clazz=Class.forName(config.getClassName());
		return new ThriftClientConnection<Client>(clazz,protocol.get(p));
	}
	
	@Override
	public boolean validateObject(RPCConnection<Client> conn) {
		
		return conn.isIdle();
	}
	
	/**
	 * 销毁连接
	 */
	@Override
	public void destroyObject(RPCConnection<Client> conn) throws Exception {
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
