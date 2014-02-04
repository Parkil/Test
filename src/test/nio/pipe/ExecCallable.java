package test.nio.pipe;
import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecCallable {
	private static final ExecutorService es = Executors.newCachedThreadPool();
	private static final PipeSinkThread si = new PipeSinkThread();
	private static final PipeSourceThread so = new PipeSourceThread();

	public void runCall() {
		Pipe pipe = null;
		try {
			pipe = Pipe.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExecCallable.si.setChannel(pipe.sink());
		ExecCallable.so.setChannel(pipe.source());
		ExecCallable.es.execute(ExecCallable.si);
		ExecCallable.es.execute(ExecCallable.so);
	}

	public String sinkMessage() {
		return ExecCallable.si.getMessage();
	}

	public String sourceMessage() {
		return ExecCallable.so.getMessage();
	}

	public void shutdown() {
		ExecCallable.es.shutdown();
	}

	public static void main(String[] args) {
		new ExecCallable().runCall();
	}
}
