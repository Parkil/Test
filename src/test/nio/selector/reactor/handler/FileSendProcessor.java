package test.nio.selector.reactor.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;
import test.util.ByteBufferUtil;
import test.util.MD5Hash;

public class FileSendProcessor implements Processor {
	private boolean isexist	= false; //해당파일이 존재하는지 여부
	private final String  basepath	= "d:/server/";

	private File file;

	private final static Header header = new Header(); //Client 전송용 Header

	private void read(String FileName) {
		File file = new File(basepath+FileName);

		if(file.exists()) {
			this.file = file;
			isexist = true;
		}else {
			isexist = false;
		}
	}

	private void write(SocketChannel sc, String FileName) {
		FileInputStream fis = null;
		FileChannel		fc	= null;

		try {
			if(!isexist) {
				sc.write(ByteBufferUtil.str_to_bb("File Not Exists : "+FileName));
				return;
			}else {
				sc.write(ByteBufferUtil.str_to_bb("File Found Send Procedure Start"));
			}

			fis = new FileInputStream(file);
			fc	= fis.getChannel();

			FileSendProcessor.header.initHeader((byte)1, (byte)2, fc.size());
			FileSendProcessor.header.setFileName(FileName);
			FileSendProcessor.header.setFileHash(MD5Hash.getMD5Checksum(file.getPath()));
			FileSendProcessor.header.write(sc);

			fc.transferTo(0, fc.size(), sc);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if(fc  != null) {
					fc.close();
				}
				if(fis != null) {
					fis.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void process(Header header, SelectionKey key, Mode mode) {
		// TODO Auto-generated method stub
		SocketChannel sc = (SocketChannel)key.channel();
		String	fileName = header.getFileName();

		switch(mode) {
			case READ	:
				read(fileName);
				key.interestOps(SelectionKey.OP_WRITE);
				break;

			case WRITE	:
				write(sc, fileName);
				key.interestOps(SelectionKey.OP_READ);
				break;
		}
	}

	@Override
	public HeaderType getType() {
		// TODO Auto-generated method stub
		return HeaderType.FILESEND;
	}
}
