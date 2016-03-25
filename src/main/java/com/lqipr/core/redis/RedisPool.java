package com.lqipr.core.redis;

import com.alibaba.fastjson.JSONObject;
import com.lqipr.core.util.StringUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * redis连接池管理
 */
public class RedisPool {
	private static Logger log = Logger.getLogger(RedisPool.class);
	private String ip;
	private String port;
	private int maxWait;
	private int maxActive;
	private int maxIdle;
	public static final String NOT_FOUND = "nil";
	private String name;
	
	/**
	 * redis 连接池对象
	 */
	private JedisPool jedisPool;
	
	/**
	 * 建立连接池
	 * @param ip ip地址
	 * @param port 端口
	 * @param maxWait 等待时间
	 */
	public RedisPool(String name, String ip, String port, int maxWait, int maxActive, int maxIdle){
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.maxWait = maxWait;
		this.maxActive = maxActive;
		this.maxIdle = maxIdle;
		createPool();

		   
	}
	/**
	 * 建立连接池
	 */
	private void createPool(){
		JedisPoolConfig jConfig = new JedisPoolConfig();
		jConfig.setMaxActive(maxActive);
		jConfig.setMaxIdle(maxIdle);
		jConfig.setMaxWait(maxWait);
		jConfig.setTestOnBorrow(false);
        jedisPool = new JedisPool(jConfig, ip, StringUtil.convert(port, 6378),maxWait);
	}
	/**
	 * 获取redis连接
	 * @return
	 */
	public Jedis getJedis(){
		Jedis jedis  = jedisPool.getResource();
		return jedis;
	}
	/**
	 * 释放redis连接
	 * @param jedis
	 */
	public void releaseJedisInstance(Jedis jedis){
		if(jedis!=null){
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 销毁Jedis Resource，通常在catch中使用
	 * @Description	destoryJedisResource  
	 * @param jedis void
	 * @throws
	 */
	public void destoryJedisResource(Jedis jedis){
		if(jedis!=null){
			jedisPool.returnBrokenResource(jedis);
		}
	}
	
	/**
	 * 自增函数
	 * @param key
	 * @return
	 */
	public long incr(String key){
		long result = -1;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.incr(key);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}
	
	/***********功能方法**************/
	/**
	 * 得到指导key的值列表。此方法返回一个指定类型的数据列表。
	 * @param key Key
	 * @param clazz Class， 指导返回的类型。
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz){
		String json = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			json = jedis.get(key);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		if(json==null){
			return null;
		}
		else{	
			return JSONObject.parseObject(json, clazz);
		}
	}
	/**
	 * 存储key value
	 * @param key
	 * @param o
	 */
	public void set(String key, Object o){
		String s = JSONObject.toJSONString(o);
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key,s);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		
	}
	/**
	 * 删除指定的key
	 * @param key
	 */
	public long del(String key){
		Jedis jedis = null;
		long rt = 0;
		try {
			jedis = getJedis();
			rt = jedis.del(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	/**
	 * 设置值并设置过期时间 单位为秒
	 * @param key
	 * @param o
	 * @param expire  秒
	 */
	public void setAndExpire(String key, Object o, int expire) 
	{
		String s = JSONObject.toJSONString(o);
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key,s);
			jedis.expire(key, expire);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	
	}
	
	public void setex(String key, int seconds,String value) 
	{
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.setex(key, seconds, value);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	
	}
	
	public Long hlen(String key) {
		Long len = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			len = jedis.hlen(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return len;
	}

	public Long hset(String key, String field, String value){
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hset(key, field, value);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public String hget(String key, String field){
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String str = jedis.hget(key, field);
			if (!NOT_FOUND.equals(str))
			{
				result = str;
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public String hmset(String key, Map<String, String> hash){
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hmset(key, hash);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}
	
	
    public List<String> hmget(final String key, final String... fields){
    	List<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hmget(key, fields);
			if (result != null && !result.isEmpty())
			{
				result.remove(NOT_FOUND);
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
    }

	public Map<String, String> hgetAll(String key){
		Map<String, String> result = new HashMap<String, String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hgetAll(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}
	
	public long hincrby(String key,String field, long val) {
		Jedis jedis = null;
		long rt = 0;
		try {
			jedis = getJedis();
			rt = jedis.hincrBy(key, field, val);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}

	public Long rpush(String key, String string){
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.rpush(key, string);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public Long lpush(String key, String string){
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.lpush(key, string);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public String rpop(String key){
		String result= null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.rpop(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public Long lpush(String key, Object o) {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String string = JSONObject.toJSONString(o);

			result = jedis.lpush(key, string);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public <T> void lpush(String key, List<T> oList) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			T o = null;
			
			if (oList != null)
			{
				for (int i = oList.size() - 1; i >= 0; i--) {
					o = oList.get(i);
					String string = null;
					if (o instanceof String) {
						string = (String)o;
					} else {
						string = JSONObject.toJSONString(o);
					}
					
					jedis.lpush(key, string);
				}
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}

	public Long llen(String key) {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.llen(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public List<String> lrange(String key, int start, int end){
		List<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.lrange(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
			result = new ArrayList<String>();
		}
		return result;
	}
	

	public <T> List<T> lrange(String key, Class<T> clazz, int start, int end){
		List<T> result = new ArrayList<T>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			List<String> strings = jedis.lrange(key, start, end);
			
			if (strings != null && !strings.isEmpty()) {
				for (final String string : strings) {
					result.add(JSONObject.parseObject(string, clazz));
				}
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public Long expire(String key, int seconds) {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.expire(key, seconds);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public Boolean exists(String key){
		Boolean result = Boolean.FALSE;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.exists(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public Long hdel(String key, String field){
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hdel(key, field);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	
	public String get(String key){
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String str = jedis.get(key);
			if ( !NOT_FOUND.equals(str) )
			{
				result = str;
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}

	public String set(String key, String value){
		String result = "";
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}
	
	public <T> void rpush(String key, List<T> oList) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for(Object o : oList) {
				String s = JSONObject.toJSONString(o);
				jedis.rpush(key, s);
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	
	
	public <T> T lrange(String key,int start,int end,Class<T> clazz){
		List<String> jsonList = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jsonList = jedis.lrange(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		if(jsonList == null || jsonList.isEmpty() ){
			return null;
		}else{	
			return JSONObject.parseObject(jsonList.toString(), clazz);
		}
		
	}
	
	
	public <T> List<T> lpop(String key, Class<T> clazz, int size) {
		String json = null;
		Jedis jedis = null;
		int count = 0;
		List<T> results = null;
		
		try {
			jedis = getJedis();
			long exists = jedis.llen(key);
			count = (int)(exists > size ? size : exists);
			
			if(count > 0) {
				results = new ArrayList<T>();
			}
			
			for(int i = 0; i < count; i++) {
				json = jedis.lpop(key);
				
				if(json!=null){	
					results.add(JSONObject.parseObject(json, clazz));
				}
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		
		return results;
	}
	
	public String lpop(String key) {
		Jedis jedis = null;
		String rt=null;
		try {
			jedis = getJedis();
			rt= jedis.lpop(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public String lindex(String key, int index) {
		Jedis jedis = null;
		String rt=null;
		try {
			jedis = getJedis();
			rt= jedis.lindex(key, index);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public String lset(String key, int index, String value) {
		Jedis jedis = null;
		String rt=null;
		try {
			jedis = getJedis();
			rt= jedis.lset(key, index, value);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Long lrem(String key, int count, String value) {
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt= jedis.lrem(key, count, value);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public void sadd(String key, String member){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.sadd(key, member);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		
	}
	
	public void saddAll(String key, Set<String> members) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (final String member : members) {
				jedis.sadd(key, member);
			}
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		
	}
	
	public Set<String> smembers(String key) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt =  jedis.smembers(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}

	public Long scard(String key) {
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt= jedis.scard(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public void srem(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.srem(key, member);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	
	public void sremMany(String key, Set<String> members) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (final String member : members) {
				jedis.srem(key, member);
			}
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	
	public Long ttl(String key){
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt = jedis.ttl(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		Jedis jedis = null;
		Set<Tuple> rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrangeWithScores(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrangeByScore(key, min, max, offset, count);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		Jedis jedis = null;
		Set<Tuple> rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	
	public Long zrank(String key, String member) {
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrank(key, member);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Long zremrangeByRank(String key, long start, long end) {
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zremrangeByRank(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrevrange(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		Jedis jedis = null;
		Set<Tuple> rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zrevrangeWithScores(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Long zcard(String key) {
		Jedis jedis = null;
		Long rt=null;
		try {
			jedis = getJedis();
			rt= jedis.zcard(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}

	public Set<String> zrange(String key, int start, int end) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt = jedis.zrange(key, start, end);
			releaseJedisInstance(jedis);
			
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	
	
	public Set<String> zrevrange(String key, int start, int end) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt = jedis.zrevrange(key, start, end);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<String> zrevrangeByScore(String key, double max, double min,int offset,int count) {
		Jedis jedis = null;
		Set<String> rt=null;
		try {
			jedis = getJedis();
			rt = jedis.zrevrangeByScore(key, max, min, offset, count);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}
	
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,int offset,int count) {
		Jedis jedis = null;
		Set<Tuple> rt=null;
		try {
			jedis = getJedis();
			rt = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			releaseJedisInstance(jedis);
		}catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return rt;
	}

	public void zrem(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zrem(key, member);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	@Deprecated
	public void zadd(String key, long score, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zadd(key, score, member);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	
	public void zadd(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zadd(key, score, member);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}

	public void zremrangeByScore(String key, Double start, Double end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zremrangeByScore(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		
	}
	
	public void zremrangeByRank(String key, int start, int end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zremrangeByRank(key, start, end);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
	}
	
	/**
	 * 设置key过期时间
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public long expireAt(String key,long unixTime){
		Jedis jedis = null;
		long result = 0;
		try {
			jedis = getJedis();
			result = jedis.expireAt(key, unixTime);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return result;
	}
	
	
	public String type(String key) {
		Jedis jedis = null;
		String type = null;
		try {
			jedis = getJedis();
			type = jedis.type(key);
			releaseJedisInstance(jedis);
		} catch(Throwable e){
			log.error(name,e);
			destoryJedisResource(jedis);
		}
		return type;
	}
}
