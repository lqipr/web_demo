package com.lqipr.core.rpc;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.Constructor;

/**
 * thrift 连接
* @Title: JhssRPCConnection.java 
* @Package com.jhss.core.util.thrift 
* @author wangd   
* @date Sep 16, 2013 4:14:30 PM 
* @version V1.0
 */
public class ThriftClientConnection<Client> implements RPCConnection<Client> {
	
	private final TTransport transport;
	private final Client client;
	
	private boolean idle = true;
	
	public ThriftClientConnection(Class<?> clazz, TProtocol protocol){
		
		try {
			Constructor<?> ctor=clazz.getConstructor(TProtocol.class);
			client=(Client) ctor.newInstance(protocol);
			transport=protocol.getTransport();
			transport.open();
		} catch (Throwable e) {
			idle = false;
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.youguu.core.util.RPCConnection2#getClient()
	 */
	@Override
	public Client getClient(){
        return client;
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
}
