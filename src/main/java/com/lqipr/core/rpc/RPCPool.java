package com.lqipr.core.rpc;

import org.apache.commons.pool.BaseObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * rpc连接池
 * 
 * @Description: TODO
 * @Title: JhssRPCPool.java
 * @Package com.jhss.core.util.thrift
 * @author wangd
 * @date Sep 16, 2013 3:53:20 PM
 * @version V1.0
 */
public class RPCPool<Client> extends BaseObjectPool<RPCConnection<Client>> {
	private final GenericObjectPool<RPCConnection<Client>> pool;

	public RPCPool(String className) {
		final RPCPoolConfig config = RPCPoolConfig.getConfig(className);
		pool = new GenericObjectPool<RPCConnection<Client>>(
				new RPCPoolFacroty<Client>(config));
		pool.setMaxActive(config.getMaxActive());
		pool.setMaxIdle(config.getMaxIdle());
		pool.setMaxWait(config.getMaxWait());
		pool.setMinIdle(config.getMinIdle());
		
		//空闲线程每次检查数量  -1 代表全部
		pool.setNumTestsPerEvictionRun(-1);
		//空闲线程检查时间
		pool.setTimeBetweenEvictionRunsMillis(config.getMaxIdleTime()/2);
		//最大空闲时间
		pool.setMinEvictableIdleTimeMillis(config.getMaxIdleTime());
		
		
		
		
		
		pool.setTestOnReturn(true);
	}

	@Override
	public RPCConnection<Client> borrowObject() throws Exception {
		return pool.borrowObject();
	}

	@Override
	public void invalidateObject(RPCConnection<Client> conn) throws Exception {
		pool.invalidateObject(conn);

	}

	@Override
	public void returnObject(RPCConnection<Client> conn) throws Exception {
		pool.returnObject(conn);
	}

	@Override
	public void addObject() throws Exception {
		pool.addObject();
	}

	@Override
	public int getNumIdle() {
		return pool.getNumIdle();
	}

	@Override
	public int getNumActive() {
		return pool.getNumActive();
	}

	public int getMaxActive() {
		return pool.getMaxActive();
	}

	public int getMaxIdle() {
		return pool.getMaxIdle();
	}

	public long getMaxWait() {
		return pool.getMaxWait();
	}

	public int getMinIdle() {
		return pool.getMinIdle();
	}

	@Override
	public void close() throws Exception {
		pool.close();
	}

	@Override
	public void clear() {
		pool.clear();
	}

}
