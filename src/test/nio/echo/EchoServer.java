package test.nio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {

	public static int DEFAULT_PORT = 7;

	public static void main(String[] args) {
		ByteBuffer s = ByteBuffer.allocateDirect(10000);

		int port;
		try {
			port = Integer.parseInt(args[0]);
		}
		catch (Exception ex) {
			port = EchoServer.DEFAULT_PORT;
		}
		System.out.println("Listening for connections on port " + port);


		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		while (true) {

			try {
				selector.select();
			}
			catch (IOException ex) {
				ex.printStackTrace();
				break;
			}

			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while (iterator.hasNext()) {

				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel ) key.channel();
						SocketChannel client = server.accept();
						System.out.println("Accepted connection from " + client);
						client.configureBlocking(false);
						SelectionKey clientKey = client.register(
								selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
						ByteBuffer buffer = ByteBuffer.allocate(100);
						clientKey.attach(buffer);
					}
					if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						//						ByteBuffer output = (ByteBuffer) key.attachment();
						client.read(s);
					}
					if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						//						ByteBuffer output = (ByteBuffer) key.attachment();
						s.flip();
						client.write(s);
						s.compact();
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
					key.cancel();
					try {
						key.channel().close();
					}
					catch (IOException cex) {}
				}

			}

		}

	}

}