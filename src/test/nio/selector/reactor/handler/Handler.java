package test.nio.selector.reactor.handler;

import java.nio.channels.SelectionKey;

import test.nio.selector.reactor.emuration.Mode;

/*
 * Handler 인터페이스 Visitor패턴의 Elements에 해당
 */
public interface Handler {
	public void accept(Processor processor, SelectionKey key, Mode mode);
}
