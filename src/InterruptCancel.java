public class InterruptCancel extends Thread {
	public volatile boolean is_run = true;

	@Override
	public void run() {
		int i = 0;
		// TODO Auto-generated method stub
		while (is_run) {
			i++;
			if (i == 500) {
				try {
					sleep(50000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Thread.currentThread().interrupt();
			}
			System.out.println(i + " : " + Thread.currentThread().isInterrupted());
		}
	}
	public static void main(String[] args) throws Exception {
		InterruptCancel ic = new InterruptCancel();
		ic.start();
		Thread.sleep(5000);
		ic.interrupt();
		ic.is_run = false;
		ic.join(5000);
	}
}