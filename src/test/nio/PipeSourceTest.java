package test.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SourceChannel;

import test.util.ByteBufferUtil;

public class PipeSourceTest {
	public static void main(String[] args) throws Exception{
		
		Pipe pipe = Pipe.open();
		SourceChannel source = pipe.source();
		
		ByteBuffer bb = ByteBuffer.allocateDirect(1000);
		
		while(true) {
			bb.clear();
			source.read(bb);
			bb.flip();
			System.out.println("Incoming Message : "+ByteBufferUtil.bb_to_str(bb));
		}
	}
}
