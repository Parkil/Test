import org.apache.log4j.Logger;

public class TargetThread implements Runnable {
	private final Logger	log	= Logger.getLogger(getClass());
	private Target			t	= null;

	public TargetThread(Target t) {
		this.t = t;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		log.info(String.format("current status : %s", t.sss));
		if (t.sss == true) {
			t.sss = false;
			// log.info("flag changed false");
		} else {
			t.sss = true;
			// log.info("flag changed true");
		}
	}
}
