package hkd.luxc.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class TestTX {
	private Jedis jedis = null;

	@Before
	public void init() {
		// 连接本地的 Redis 服务
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

	/**
	 * 事务提交
	 */
	@Test
	public void testExec() {
		Transaction transaction = jedis.multi();

		transaction.set("k5", "v5");
		transaction.set("k6", "v6");

		transaction.exec();
		System.out.println(jedis.get("k5"));
	}

	/**
	 * 事务回滚
	 */
	@Test
	public void testDiscard() {
		Transaction transaction = jedis.multi();

		transaction.set("k7", "v7");
		transaction.set("k8", "v8");

		transaction.discard();
		System.out.println(jedis.get("k7"));
	}

	/**
	 * Redis事务的执行结果是在exec之后才统一返回，所以Jedis会用一个Response对象最为事务对象
	 * transaction的执行放回值。如果我们在transaction执行exec方法之前调用response对象的
	 * get方法会出现异常
	 */
	@Test
	public void test() {
		// 监控key，如果该动了事务就被放弃
		
		jedis.watch("serialNum");
		jedis.set("serialNum","123456"); 
		jedis.unwatch();
		 

		Transaction transaction = jedis.multi();
		Response<String> response = transaction.get("serialNum");
		transaction.set("serialNum", "s002");
		response = transaction.get("serialNum");
		transaction.lpush("list3", "a");
		transaction.lpush("list3", "b");
		transaction.lpush("list3", "c");

		transaction.exec();
		//transaction.discard();
		System.out.println("serialNum:" + response.get());
	}

	@Test
	public void Testtx() {
		TestTransaction test = new TestTransaction();
		boolean retValue = test.transMethod();
		System.out.println("retValue: " + retValue);
	}
	

}
