package test.concurrent.threadpool;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * ThreadPoolExecutor �׽�Ʈ
 */
public class ThreadPoolExecutorTest {

	/**
	 * @param args
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		int core_cnt		= 1;//Runtime.getRuntime().availableProcessors(); //�ھ��
		int max_pool_size	= core_cnt + 0; //Pool ���� �ִ� Thread ����
		int alive_time		= 50; //Thread�� ���������� �۵��ϴ� �ð�

		TimeUnit unit = TimeUnit.MILLISECONDS; //alive_time �ð�����
		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1); //Queue�� ũ�Ⱑ �� ���� RejectedExecutionHandler�� �ߵ��ȴ�.(�ߵ��Ǵ� ������ ���� �ʿ䰡 ���� ThreadPoolExecutor�ҽ��м� �ʿ�)

		CustomHandler handle = new CustomHandler();
		//AbortHandler handle = new AbortHandler();

		/*
		 * ThreadPoolExecutor���� �⺻���� �����ϴ� RejectedExecutionHandler
		 */
		//ThreadPoolExecutor.AbortPolicy			handle = new ThreadPoolExecutor.AbortPolicy();			//�߻��� ����ó��
		//ThreadPoolExecutor.CallerRunsPolicy		handle = new ThreadPoolExecutor.CallerRunsPolicy();		//�߻��� ����Thread�� �켱������ �� �ڷ� ������ ���� Thread ����
		//ThreadPoolExecutor.DiscardOldestPolicy	handle = new ThreadPoolExecutor.DiscardOldestPolicy();  //�߻��� ��������� Thread�� ���� ���� �̹߻�
		//ThreadPoolExecutor.DiscardPolicy			handle = new ThreadPoolExecutor.DiscardPolicy();		//�߻��� ���� Thread�� ���� ���� �̹߻�

		/*
		 * ThreadPoolExecutor ����
		 * ExecutorService executor = Executors.newCachedThreadPool() �̷���ɾ� �����
		 * ������ �´� ThreadPoolExecutor�� ����ȴ�
		 * 
		 * Executors�� static mehtod ������
		 * ���� ThreadPoolExecutor�� �����ϴ°͵� �����ϴ�
		 *
		 * �⺻����
		 * 1.corePoolSize		- Thread Pool������ ������ �ּ����� Thread���� �۾��� �������� �ʾƵ� ���⿡�� ������ ������ŭ�� Thread�� �����ȴ�.
		 * 2.maximumPoolSize	- Thread Pool������ �����Ҽ� �ִ� �ִ����� Thread���� �̸� �Ѿ�� ��û�� ������ ������ ��å�� ���� ��û�� ó���ȴ�.
		 * 3.keepAliveTime		- corePoolSize�� �Ѿ�� Thread�� idle time out ����
		 * 4.unit				- keepAliveTime�� �ð����� ����(TimeUnit Class���)
		 * 5.workQueue			- ThreadPool���� Thread�� �����ϴ� queue �� ���ڰ� null�ε��� NullPointer Exceptino�� �߻���
		 * 
		 * �߰�����(���� 1���� ������ �ְ� �� ������ ����)
		 * 6.ThreadFactory				- Pool���� Thread������ ����� ThreadFactory����
		 * 7.RejectedExecutionHandler	- ThreadPool�� �� á���� ó���ϴ� Handler ����
		 */
		ThreadPoolExecutor sss = new ThreadPoolExecutor(core_cnt, max_pool_size, alive_time, unit, queue, handle);
		for(int i = 0 ; i<10 ; i++) {
			sss.execute(new PrintThread());
		}
		sss.shutdown(); //execute()���� ����Ǵ� Thread�� ����ȴٰ� �ؼ� ThreadPoolExecutor�� ����Ǵ� ���� �ƴ� ThreadPoolExecutor�� �����Ϸ��� shutdown() or shutdownNow()�� �����ؾ� ��
	}
}
