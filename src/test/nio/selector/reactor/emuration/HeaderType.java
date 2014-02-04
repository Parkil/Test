package test.nio.selector.reactor.emuration;


import java.util.Hashtable;

/*
 * Header의 type 열거 클래스
 * MESSAGE	- 메시지 전송
 * FILERECV - 파일수신(client->server)
 * FILESEND	- 파일전송(server->client)
 */
public enum HeaderType {
	MESSAGE(0),FILERECV(1),FILESEND(2),ACCEPT(3);

	private static Hashtable<Integer,HeaderType> table = null;
	private int type;

	HeaderType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	private static void inittable() {
		HeaderType.table = new Hashtable<Integer,HeaderType>();
		for (HeaderType type : HeaderType.values()) {
			HeaderType.table.put(new Integer(type.type), type);
		}
	}

	public static HeaderType getType(int type) {
		if(HeaderType.table == null) {
			HeaderType.inittable();
		}

		return HeaderType.table.get(new Integer(type));
	}
}
