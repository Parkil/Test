package test.nio.selector.reactor.emuration;


/*
 * Handler 작동모드를 규정하는 enum
 * ACCEPT - Server Accept
 * READ	  - Read data from socket
 * WRITE  - Write date to socket
 */
public enum Mode {
	ACCEPT(1),READ(2),WRITE(3);

	private int mode;

	private Mode(int mode) {
		this.mode = mode;
	}

	public int getMode(Mode m) {
		return m.mode;
	}
}
