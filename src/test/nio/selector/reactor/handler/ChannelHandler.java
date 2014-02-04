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
	private boolean isnew = false; // process��ü�� ���� �����ؾ� �ϴ��� ���� �Ǻ�

	/*
	 * SelectionKey�� canceló���ϰ� key�� ������ Connection�� closeó����
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
	 * header �����͸� �о����
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
	 * header�� ������ ���(�ӽ� �޼ҵ�)
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

	// ���ڵ�
	@Override
	public void accept(Processor processor, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		this.key = key;

		// READ ����϶��� header�� �о����
		if (mode.equals(Mode.READ)) {
			long read_size = readHeader();

			// �о���� ũ�Ⱑ 0 �̰ų� -1(END OF STREAM)�� ���� �ط����� �������� �ʰ� ó��
			if (read_size == 0 || read_size == -1) {
				if (read_size == -1) {
					cancelKey();
				}
				return;
			}

			printHeader();
		}

		HeaderType headertype = HeaderType.getType(header.getType());

		// process�� null�̰ų� processŸ�԰� heade���� ������ Ÿ���� �ٸ���� isnew flag�� true�� ����
		if (process == null || (process.getType().equals(headertype) == false)) {
			isnew = true;
		} else {
			isnew = false;
		}

		// ����� ���� headertype�� ����� ��� process��ü�� �����ϰ� ������ ���ɼ��� �ִ� process��ü��
		// �����ΰ� �̿��ϴ� ����� ������ ����
		// �׷��� process��ü�� ������ ��쿡�� process��ü�� �� ����� �δ� ����� ������ �� ������ �ִ�.
		switch (headertype) {
			case MESSAGE :
				System.out.println("�޽�����");
				if (isnew) {
					process = new MessageProcessor();
				}
				break;

			case FILESEND :
				System.out.println("��������(server->client)");
				if (isnew) {
					process = new FileSendProcessor();
				}
				break;

			case FILERECV :
				System.out.println("���ϼ���(client->server)");
				if (isnew) {
					process = new FileRecvProcessor();
				}
				break;
		}
		process.process(header, key, mode);
	}
}
