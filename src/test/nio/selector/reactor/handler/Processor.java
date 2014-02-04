package test.nio.selector.reactor.handler;

import java.nio.channels.SelectionKey;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;

/*
 * Processor 인터페이스 Visitor패턴의 Visitor에 해당
 */
interface Processor {
	public void process(Header header, SelectionKey key, Mode mode);
	public HeaderType getType();
}
