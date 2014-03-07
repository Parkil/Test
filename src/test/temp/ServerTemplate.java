package test.temp;

/*
 * Visitor Pattern�� ���� ���� ������ ����� Ŭ���� 
 */

/*
 * Element : Visitor Pattern���� Object�� ����
 */
interface ChannelElement {
	void accept(Processor visitor);
}

class AcceptElement implements ChannelElement {
	@Override
	public void accept(Processor processor) {
		processor.process(this);
	}
}

class ReadElement implements ChannelElement {
	@Override
	public void accept(Processor processor) {
		processor.process(this);
	}
}

class WriteElement implements ChannelElement {
	@Override
	public void accept(Processor processor) {
		processor.process(this);
	}
}

//=========================================================//

/*
 * Visitor : Object���� ������ ������ ����
 */
interface Processor {
	void process(AcceptElement a);
	void process(ReadElement a);
	void process(WriteElement a);
}

class Processor1 implements Processor {
	@Override
	public void process(AcceptElement a) {
		System.out.println("Server Accept ����");
	}

	@Override
	public void process(ReadElement a) {
		System.out.println("Read ����");
		System.out.println("������ ���� �б�ó�� �ʿ�");
	}

	@Override
	public void process(WriteElement a) {
		System.out.println("Write ����");
		System.out.println("������ ���� �б�ó�� �ʿ�");
	}	
}


//=========================================================//

public class ServerTemplate {
	public static void main(String[] args) {
		AcceptElement ae = new AcceptElement();
		ReadElement	re = new ReadElement();
		WriteElement we = new WriteElement();
		
		ae.accept(new Processor1());
		re.accept(new Processor1());
		we.accept(new Processor1());
	}
}
