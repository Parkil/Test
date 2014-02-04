package test.nio.selector.reactor.handler;

import java.nio.channels.SelectionKey;

import test.nio.selector.reactor.emuration.Mode;

/*
 * Handler �������̽� Visitor������ Elements�� �ش�
 */
public interface Handler {
	public void accept(Processor processor, SelectionKey key, Mode mode);
}
