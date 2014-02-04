package test.threadlocal;

public class LocalVar {
	private ThreadLocal<Integer>s = new ThreadLocal<Integer>();

	LocalVar(){}

	public int get() {
		if(s == null) {
			return 0;
		}else {
			if(s.get() == null) {
				return 0;
			}else {
				return s.get().intValue();
			}
		}
	}

	public void set(int value) {
		s.set(value);
	}
}
