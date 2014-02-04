package test.nio.selector.reactor.handler;

import java.nio.channels.SelectionKey;

import test.nio.selector.reactor.emuration.Mode;

public class AcceptHandler implements Handler {

	@Override
	public void accept(Processor processor, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		processor.process(null, key, Mode.ACCEPT);
	}
}
