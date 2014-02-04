package test.nio.pipe;

import java.nio.channels.Pipe.SourceChannel;

public class PipeSourceThread implements Runnable {
	String message = "source-empty";
	SourceChannel sc = null;

	public void setChannel(SourceChannel sc) {
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
