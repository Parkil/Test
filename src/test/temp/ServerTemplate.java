package test.temp;

/*
 * Visitor Pattern에 맞춰 서버 동작을 모식한 클래스 
 */

/*
 * Element : Visitor Pattern에서 Object를 규정
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
 * Visitor : Object별로 수행할 동작을 지정
 */
interface Processor {
	void process(AcceptElement a);
	void process(ReadElement a);
	void process(WriteElement a);
}

class Processor1 implements Processor {
	@Override
	public void process(AcceptElement a) {
		System.out.println("Server Accept 실행");
	}

	@Override
	public void process(ReadElement a) {
		System.out.println("Read 실행");
		System.out.println("유형에 따른 분기처리 필요");
	}

	@Override
	public void process(WriteElement a) {
		System.out.println("Write 실행");
		System.out.println("유형에 따른 분기처리 필요");
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
