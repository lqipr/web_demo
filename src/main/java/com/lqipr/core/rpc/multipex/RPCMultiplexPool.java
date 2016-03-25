package com.lqipr.core.rpc.multipex;

import com.lqipr.core.rpc.RPCPoolConfig;
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
public class RPCMultiplexPool extends BaseObjectPool<RPCMultiplexConnection> {
	private final GenericObjectPool<RPCMultiplexConnection> pool;

	public RPCMultiplexPool(String className) {
		final RPCPoolConfig config = RPCPoolConfig.getConfig(className);
		/****** 扫描指定类 *****/
		IClientLoad load = null;
		try {
			load = (IClientLoad) Class.forName(config.getLoadClass())
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("加载失败 [class:" + config.getLoadClass());
		}

		pool = new GenericObjectPool<RPCMultiplexConnection>(
				new RPCMultiplexPoolFacroty(config, load.loadClient()));
		pool.setMaxActive(config.getMaxActive());
		pool.setMaxIdle(config.getMaxIdle());
		pool.setMaxWait(config.getMaxWait());
		pool.setMinIdle(config.getMinIdle());
		pool.setTestOnBorrow(true);
		
		//空闲线程每次检查数量  -1 代表全部
		pool.setNumTestsPerEvictionRun(-1);
		//空闲线程检查时间
		pool.setTimeBetweenEvictionRunsMillis(config.getMaxIdleTime()/2);
		//最大空闲时间
		pool.setMinEvictableIdleTimeMillis(config.getMaxIdleTime());
	}

	@Override
	public RPCMultiplexConnection borrowObject() throws Exception {
		return pool.borrowObject();
	}

	@Override
	public void invalidateObject(RPCMultiplexConnection conn) throws Exception {
		pool.invalidateObject(conn);

	}

	@Override
	public void returnObject(RPCMultiplexConnection conn) throws Exception {
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
