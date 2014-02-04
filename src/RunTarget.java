public class RunTarget {
	public static void main(String[] args) {
		Target t = new Target();

		for (int i = 0; i < 30; i++) {
			Thread th = new Thread(new TargetThread(t));
			th.run();
		}
	}
}
