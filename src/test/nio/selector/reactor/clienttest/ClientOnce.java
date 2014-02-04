package test.nio.selector.reactor.clienttest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

import test.nio.selector.reactor.buffer.Header;
import test.util.ByteBufferUtil;

public class ClientOnce {
	private static Header h = new Header();

	/*
	 * header�� ������ ���(�ӽ� �޼ҵ�)
	 */
	private static void printHeader() {
		System.out.println("==Client Header Infomation==");
		System.out.println("flag	   : "+ClientOnce.h.getFlag());
		System.out.println("type	   : "+ClientOnce.h.getType());
		System.out.println("total size : "+ClientOnce.h.getTotalSize());
		System.out.println("read size  : "+ClientOnce.h.getReadSize());
		System.out.println("write size : "+ClientOnce.h.getWriteSize());
		System.out.println("file name  : "+ClientOnce.h.getFileName());
		System.out.println("file hash  : "+ClientOnce.h.getFileHash());
	}

	/*
	 * ������ ������ �����ϱ� ���� �����̸��� �����Ѵ�.
	 */
	private static String getFileName(String fileName) {
		if(fileName == null || "".equals(fileName.trim())) {
			fileName = String.format("%1$tY-%1$tm-%1$te", new Date());
		}

		String files[] = fileName.split("[.]");

		String name = files[0];
		String ext	= (files.length == 1) ? "" : "."+files[1];

		fileName = (name + System.currentTimeMillis())+ext;
		return fileName;
	}

	public static void main(String[] args) {
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1",7);
		ByteBuffer b = ByteBuffer.allocateDirect(10000);
		SocketChannel sc = null;

		try {
			sc = SocketChannel.open();
			sc.connect(isa);

			//���� ���� �޽��� ����
			b.clear();
			sc.read(b);
			b.flip();

			System.out.println("InComing Message : "+ByteBufferUtil.bb_to_str(b));
			/* �޽��� �ۼ���			 */
			ByteBufferUtil.setEncoding("utf-8");
			//������ �޽��� ����
			ByteBuffer temp = ByteBufferUtil.str_to_bb("�����ٶ󸶹ٻ�");

			ClientOnce.h.initHeader((byte)0, (byte)0, temp.limit());

			ClientOnce.h.write(sc);
			b.clear();
			b.put(temp);
			b.flip();

			sc.write(b);

			//Echo �޽��� ����
			b.clear();
			sc.read(b);
			b.flip();

			System.out.println("Echo : "+ByteBufferUtil.bb_to_str(b));

			/*
			 * ��������
			ByteBufferUtil.setEncoding("utf-8");


			FileInputStream fis = new FileInputStream("d:/���Ŀ��,���쵥�ڳ��̷� ���̾�.xls");
			FileChannel		fc	= fis.getChannel();
			//������ �޽��� ����
			ClientOnce.h.initHeader((byte)0, (byte)1, fc.size());
			ClientOnce.h.setFileName("���Ŀ��,���쵥�ڳ��̷� ���̾�.xls");
			ClientOnce.h.setFileHash(MD5Hash.getMD5Checksum("d:/���Ŀ��,���쵥�ڳ��̷� ���̾�.xls"));
			ClientOnce.h.write(sc);

			fc.transferTo(0, fc.size(), sc);

			b.clear();
			sc.read(b);
			b.flip();

			String message = ByteBufferUtil.bb_to_str(b);
			System.out.println("File Send Result From Server : "+message);

			fis.close();
			fc.close();
			 */

			/*���ϼ���
			while(true) {
				Thread.sleep(5000);
				String FileName = "���Ŀ��,���쵥�ڳ��̷� ���̾�1363849485246.xls";
				ByteBufferUtil.setEncoding("utf-8");
				ClientOnce.h.initHeader((byte)0, (byte)2, 0);
				ClientOnce.h.setFileName(FileName);
				ClientOnce.h.write(sc);

				b.clear();
				sc.read(b);
				b.flip();

				String message = ByteBufferUtil.bb_to_str(b);
				System.out.println("File Recv Result From Server : "+message);

				ClientOnce.h.read(sc);
				ClientOnce.printHeader();

				long total_size = ClientOnce.h.getTotalSize();

				FileOutputStream fos = new FileOutputStream(ClientOnce.getFileName("d:/down/�ٿ�.xls"));
				FileChannel		 fc	 = fos.getChannel();

				while(total_size != fc.size()) {
					fc.transferFrom(sc, fc.size(), total_size - fc.size());
					System.out.println("File Transfering ("+fc.size()+" / "+total_size+")");
				}
				fos.close();
				fc.close();
			}
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				sc.close();
				System.out.println("Socket Closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
