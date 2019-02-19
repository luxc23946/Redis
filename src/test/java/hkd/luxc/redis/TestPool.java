package hkd.luxc.redis;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestPool {
	
	private Jedis jedis = null;
	
	@Before
	public void init() {
		jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
	}

	@After
	public void destory() {
		JedisPoolUtil.release(jedis);
	}

	@Test
	public void testConnect() {
		System.out.println("服务正在运行: " + jedis.ping());
	}
}
