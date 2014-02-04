package test.nio.pipe;

import java.nio.channels.Pipe.SinkChannel;

public class PipeSinkThread implements Runnable {
	String message = "sink-empty";
	SinkChannel sc = null;

	public void setChannel(SinkChannel sc) {
		this.sc = sc;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public String getMessage() {
		return message;
	}
}
