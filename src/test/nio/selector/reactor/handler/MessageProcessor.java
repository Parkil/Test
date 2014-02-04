package test.nio.selector.reactor.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;
import test.util.ByteBufferUtil;

class MessageProcessor implements Processor {
	private final ByteBuffer body = ByteBuffer.allocateDirect(1000);

	private String read(SocketChannel sc, long total_size) throws IOException {
		long read_size	= 0;

		while(total_size != read_size) {
			body.clear();
			read_size += sc.read(body);
			body.flip();
		}

		ByteBufferUtil.setEncoding("utf-8");
		return ByteBufferUtil.bb_to_str(body);
	}

	private long write(SocketChannel sc) throws IOException {
		long write_size = 0;
		System.out.println(body.toString());
		write_size = sc.write(body);

		return write_size;
	}

	@Override
	public void process(Header header, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		try {
			switch(mode) {
				case READ	:
					String msg = read((SocketChannel)key.channel(), header.getTotalSize());
					System.out.println("읽어들인 메시지 : "+msg);
					key.interestOps(SelectionKey.OP_WRITE);
					break;

				case WRITE	:
					long write_size = write((SocketChannel)key.channel());
					System.out.println("메시지를 클라이언트에게 전송 : "+write_size);
					key.interestOps(SelectionKey.OP_READ);
					break;
			}
		}catch(IOException ie) {
			ie.printStackTrace();
		}
	}

	@Override
	public HeaderType getType() {
		// TODO Auto-generated method stub
		return HeaderType.MESSAGE;
	}
}
