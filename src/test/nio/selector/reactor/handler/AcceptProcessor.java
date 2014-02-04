package test.nio.selector.reactor.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;
import test.util.ByteBufferUtil;

public class AcceptProcessor implements Processor {
	@Override
	public void process(Header header, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		ServerSocketChannel ssc = null;
		SocketChannel		sc  = null;

		/*
		 * Ŭ���̾�Ʈ�� ������ ������ �޾Ƽ� �ش� SocketChannel�� selector�� ���� ����Ѵ�.
		 */
		try {
			ssc = (ServerSocketChannel)key.channel();
			sc	= ssc.accept();
			sc.configureBlocking(false);
			sc.register(key.selector(), SelectionKey.OP_READ);

			sc.write(ByteBufferUtil.str_to_bb("Welcome To Server")); //���� ���� �޽��� ����
		}catch(IOException e) {
			key.cancel();
			try {
				if(sc != null) {
					sc.close();
				}
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	@Override
	public HeaderType getType() {
		// TODO Auto-generated method stub
		return HeaderType.ACCEPT;
	}
}
