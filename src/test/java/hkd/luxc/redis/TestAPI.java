package hkd.luxc.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestAPI {

	private Jedis jedis = null;

	@Before
	public void init() {
		jedis = new Jedis("localhost", 6379);
	}

	@After
	public void destory() {
		jedis.disconnect();
	}

	@Test
	public void testConnect() {
		System.out.println("服务正在运行: " + jedis.ping());
	}

	@Test
	public void testSetGet() {
		jedis.set("k1", "v1");
		jedis.set("k2", "v2");
		jedis.set("k3", "v3");
		jedis.mset("str1", "v1", "str2", "v2", "str3", "v3");

		String v1 = jedis.get("k1");
		String v2 = jedis.get("k2");
		String v3 = jedis.get("k3");

		System.out.println(v1 + "," + v2 + "," + v3);
	}

	@Test
	public void testKeys() {
		Set<String> set = jedis.keys("*");
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = jedis.get(key);
			System.out.println(key + "->" + value);
		}
	}

	@Test
	public void testList() {
		jedis.ltrim("Llist", 1, 0);
		jedis.lpush("Llist","v1","v2","v3","v4","v5");
		List<String> llist = jedis.lrange("Llist",0,-1);
		llist.forEach(item->{
			System.out.println(item);
		});
		
		System.out.println("-----------------------");
		
		jedis.ltrim("Rlist", 1, 0);
		jedis.rpush("Rlist", "v1","v2","v3","v4","v5");
		List<String> rlist = jedis.lrange("Llist",0,-1);
		rlist.forEach(item->{
			System.out.println(item);
		});
	}
	
	@Test
	public void testSet() {
		jedis.sadd("orders", "set001");
		jedis.sadd("orders", "set002");
		jedis.sadd("orders", "set003");
		jedis.sadd("orders", "set004");
		
		Set<String>set=jedis.smembers("orders");
		String item=null;
		for(Iterator<String> iterator=set.iterator();iterator.hasNext();) {
			item=iterator.next();
			System.out.println(item);
		}
		System.out.println(jedis.smembers("orders").size());
		
		System.out.println("-----------------------");
		
		jedis.srem("orders","set002");
		System.out.println(jedis.smembers("orders").size());
	}
	
	@Test
	public void testZset() {
		jedis.zadd("zset01", 60,"v1");
		jedis.zadd("zset01", 70,"v2");
		jedis.zadd("zset01", 80,"v3");
		jedis.zadd("zset01", 90,"v4");
		jedis.zadd("zset01", 100,"v5");
		
		Set<String>zset=jedis.zrange("zset01", 0, -1);
		for(String item:zset) {
			System.out.println(item);
		}
	}
	
	@Test
	public void testHash() {
		jedis.hset("hash", "userName", "Tom");
		System.out.println(jedis.hget("hash", "userName"));
		
		System.out.println("-----------------------");
		
		Map<String, String>map=new HashMap<String, String>();
		map.put("set", "male");
		map.put("phone", "123456");
		map.put("email", "abc@123.com");
		jedis.hmset("hash", map);
		List<String>list=jedis.hmget("hash", "set","phone");
		for(String item:list) {
			System.out.println(item);
		}
	}
	
	
	@Test
	public void testInfo() {
		System.out.println(jedis.info());
	}
	
	/**
	 * 主从复制
	 * @throws InterruptedException 
	 */
	@Test
	public void testMS() {
		Jedis jedis_M=new Jedis("127.0.0.1", 6379);
		Jedis jedis_S=new Jedis("127.0.0.1", 6380);
		
		jedis_M.flushAll();
		
		jedis_S.slaveof("127.0.0.1", 6379);
		
		jedis_M.set("k1", "v1");
		
		System.out.println(jedis_S.get("k1"));
	}
	
	
	/**
	 * 测试Redis连接池
	 */
	@Test
	public void testPool() {
		
	}
	
	

}
