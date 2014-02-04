package test.nio.selector.reactor.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;

public class ChannelHandler implements Handler {
	private SelectionKey key;

	private final Header header = new Header();
	private Processor process = null;
	private boolean isnew = false; // process객체를 새로 생성해야 하는지 여부 판별

	/*
	 * SelectionKey를 cancel처리하고 key에 연관된 Connection을 close처리함
	 */
	private void cancelKey() {
		key.cancel();
		try {
			key.channel().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * header 데이터를 읽어들임
	 */
	private long readHeader() {
		long read_size = 0;

		SocketChannel sc = (SocketChannel) key.channel();

		try {
			read_size = header.read(sc);
		} catch (IOException e) {
			cancelKey();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return read_size;
	}

	/*
	 * header의 정보를 출력(임시 메소드)
	 */
	private void printHeader() {
		System.out.println("==Client Header Infomation==");
		System.out.println("flag	   : " + header.getFlag());
		System.out.println("type	   : " + header.getType());
		System.out.println("total size : " + header.getTotalSize());
		System.out.println("read size  : " + header.getReadSize());
		System.out.println("write size : " + header.getWriteSize());
		System.out.println("file name  : " + header.getFileName());
		System.out.println("file hash  : " + header.getFileHash());
	}

	// 새코드
	@Override
	public void accept(Processor processor, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		this.key = key;

		// READ 모드일때만 header를 읽어들임
		if (mode.equals(Mode.READ)) {
			long read_size = readHeader();

			// 읽어들인 크기가 0 이거나 -1(END OF STREAM)일 경우는 밑로직을 실행하지 않게 처리
			if (read_size == 0 || read_size == -1) {
				if (read_size == -1) {
					cancelKey();
				}
				return;
			}

			printHeader();
		}

		HeaderType headertype = HeaderType.getType(header.getType());

		// process가 null이거나 process타입과 heade에서 가져온 타입이 다를경우 isnew flag를 true로 설정
		if (process == null || (process.getType().equals(headertype) == false)) {
			isnew = true;
		} else {
			isnew = false;
		}

		// 현재는 자주 headertype이 변경될 경우 process객체가 과도하게 생성될 가능성이 있다 process객체를
		// 만들어두고 이용하는 방법이 좋을것 같음
		// 그러나 process객체가 많아질 경우에는 process객체를 다 만들어 두는 방법은 문제가 될 소지가 있다.
		switch (headertype) {
			case MESSAGE :
				System.out.println("메시지용");
				if (isnew) {
					process = new MessageProcessor();
				}
				break;

			case FILESEND :
				System.out.println("파일전송(server->client)");
				if (isnew) {
					process = new FileSendProcessor();
				}
				break;

			case FILERECV :
				System.out.println("파일수신(client->server)");
				if (isnew) {
					process = new FileRecvProcessor();
				}
				break;
		}
		process.process(header, key, mode);
	}
}
