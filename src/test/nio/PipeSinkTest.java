package test.nio;

import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;

import test.util.ByteBufferUtil;

public class PipeSinkTest {
	public static void main(String[] args) throws Exception{
		Pipe pipe = Pipe.open();
		SinkChannel sink = pipe.sink();
		
		sink.write(ByteBufferUtil.str_to_bb("г╙гогого!"));
	}
}
