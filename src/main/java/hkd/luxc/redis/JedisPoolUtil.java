package hkd.luxc.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {

	private JedisPoolUtil() {}
	
	private static volatile JedisPool jedisPool;
	
	public static JedisPool getJedisPoolInstance() {

		if(jedisPool==null) {
			synchronized (JedisPoolUtil.class) {
				if(jedisPool==null) {
					JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
					jedisPoolConfig.setMaxActive(10);
					jedisPoolConfig.setMaxIdle(32);
					jedisPoolConfig.setMaxWait(10*1000);
					jedisPoolConfig.setTestOnBorrow(true);
					jedisPool=new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
				}
			}
		}
		
		return jedisPool;
	}
	
	
	public static void release(Jedis jedis) {
		if(null!=jedis && jedisPool!=null) {
			jedisPool.returnResourceObject(jedis);
		}
	}
	
}
