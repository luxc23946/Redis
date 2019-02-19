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
		// ���ӱ��ص� Redis ����
		jedis = new Jedis("localhost", 6379);
	}

	@After
	public void destory() {
		jedis.disconnect();
	}

	@Test
	public void testConnect() {
		System.out.println("������������: " + jedis.ping());
	}

	/**
	 * �����ύ
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
	 * ����ع�
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
	 * Redis�����ִ�н������exec֮���ͳһ���أ�����Jedis����һ��Response������Ϊ�������
	 * transaction��ִ�зŻ�ֵ�����������transactionִ��exec����֮ǰ����response�����
	 * get����������쳣
	 */
	@Test
	public void test() {
		// ���key������ö�������ͱ�����
		
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
