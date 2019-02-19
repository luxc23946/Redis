package hkd.luxc.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * ͨ�׵㽲��watch������Ǳ��һ��������������һ������ ���ύ����ǰ����ü��������޸Ĺ���������ͻ�ʧ�ܣ��������ͨ�������ڳ�����
 * �����ٳ���һ�Ρ�
 * ���ȱ���˼�balance��Ȼ��������Ƿ��㹻�������ȡ����ǣ��������ۼ��� �㹻�Ļ���������������и��²�����
 * ����ڴ��ڼ��balance���������޸ģ� �����ύ����ִ��exec��ʱ�ͻᱨ�� ������ͨ�����Բ����������������ִ��һ�Σ�ֱ���ɹ���
 */
public class TestTransaction {
	
	private Jedis jedis= new Jedis("127.0.0.1", 6379);
	
	 public boolean transMethod() {
	     int balance;// �������
	     int debt;// Ƿ��
	     int amtToSubtract = 10;// ʵˢ���


	     jedis.watch("balance");
	     //jedis.set("balance","5");//�˾䲻�ó��֣����η��㡣ģ�����������Ѿ��޸��˸���Ŀ
	     balance = Integer.parseInt(jedis.get("balance"));
	     if (balance < amtToSubtract) {
	       jedis.unwatch();
	       System.out.println("modify");
	       return false;
	     } else {
	       System.out.println("***********begin transaction***********");
	       Transaction transaction = jedis.multi();
	       transaction.decrBy("balance", amtToSubtract);
	       transaction.incrBy("debt", amtToSubtract);
	       transaction.exec();
	       System.out.println("***********end transaction***********");
	       balance = Integer.parseInt(jedis.get("balance"));
	       debt = Integer.parseInt(jedis.get("debt"));


	       System.out.println("balance:" + balance);
	       System.out.println("debt:" + debt);
	       return true;
	     }
	  }

	}
