package test.nio.selector.reactor.clienttest;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Scanner;

import test.nio.selector.reactor.buffer.Header;
import test.util.ByteBufferUtil;


enum Option {
	Connect(1),Send(2),DisConnect(3);

	private int option;
	private static HashMap<Integer, Option> optionmap = null;

	Option(int option) {
		this.option = option;
	}

	static void initMap() {
		Option.optionmap = new HashMap<Integer, Option>();
		for(Option o : Option.values()) {
			Option.optionmap.put(new Integer(o.option), o);
		}
	}

	static Option getOption(int i) {
		if(Option.optionmap == null) {
			System.out.println("Init HashMap...");
			Option.initMap();
		}

		return Option.optionmap.get(new Integer(i));
	}
}

public class ClientWhile {
	private static StringBuffer sb = new StringBuffer();

	private static ByteBuffer	 bb = ByteBuffer.allocateDirect(1000);
	private static InetSocketAddress isa = new InetSocketAddress("127.0.0.1",7);
	private static SocketChannel sc;

	private static Header h = new Header();

	/*
	 * 서버에 접속
	 */
	private static void ConnectServer() {
		try {
			//서버에 접속
			ClientWhile.sc = SocketChannel.open();
			ClientWhile.sc.connect(ClientWhile.isa);

			//서버 접속 메시지 수신
			ClientWhile.bb.clear();
			ClientWhile.sc.read(ClientWhile.bb);
			ClientWhile.bb.flip();

			System.out.println("InComing Message : "+ByteBufferUtil.bb_to_str(ClientWhile.bb));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 서버로 메시지를 전송
	 */
	private static void SendMessage() {
		ByteBufferUtil.setEncoding("utf-8");
		//서버로 메시지 전송
		ByteBuffer temp = ByteBufferUtil.str_to_bb("가나다라마바사");

		ClientWhile.h.initHeader((byte)0, (byte)0, temp.limit());

		try {
			//Header 전송
			ClientWhile.h.write(ClientWhile.sc);

			//실 메시지 전송
			ClientWhile.bb.clear();
			ClientWhile.bb.put(temp);
			ClientWhile.bb.flip();

			ClientWhile.sc.write(ClientWhile.bb);

			//Echo 메시지 수신
			ClientWhile.bb.clear();
			ClientWhile.sc.read(ClientWhile.bb);
			ClientWhile.bb.flip();

			System.out.println("Echo : "+ByteBufferUtil.bb_to_str(ClientWhile.bb));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 서버와의 접속을 종료함
	 */
	private static void DisConnect() {
		try {
			ClientWhile.sc.close();
			System.out.println("Socket Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private static void initMenu() {
		ClientWhile.sb.append("======Menu======\n");
		ClientWhile.sb.append("1.Connect Server\n");
		ClientWhile.sb.append("2.Send Message\n");
		ClientWhile.sb.append("3.DisConnect\n");
	}


	public static void main(String[] args) {
		ClientWhile.initMenu();
		boolean is_run = true;
		while(is_run) {
			System.out.println(ClientWhile.sb.toString());
			System.out.print("Input Option : ");
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();

			Option op = Option.getOption(option);

			if(op == null) {
				System.out.println("Invalid Option");
				return;
			}

			switch(op) {
				case Connect : System.out.println("Connect");
				ClientWhile.ConnectServer();
				break;

				case Send : System.out.println("Send");
				ClientWhile.SendMessage();
				break;

				case DisConnect : System.out.println("DisConnect");
				ClientWhile.DisConnect();
				is_run = false;
				break;
			}
			System.out.println();
		}
	}
}

