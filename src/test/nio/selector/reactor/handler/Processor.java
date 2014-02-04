package test.nio.selector.reactor.handler;

import java.nio.channels.SelectionKey;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;

/*
 * Processor �������̽� Visitor������ Visitor�� �ش�
 */
interface Processor {
	public void process(Header header, SelectionKey key, Mode mode);
	public HeaderType getType();
}
