package test.nio.selector.reactor.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;

import test.nio.selector.reactor.buffer.Header;
import test.nio.selector.reactor.emuration.HeaderType;
import test.nio.selector.reactor.emuration.Mode;
import test.util.ByteBufferUtil;
import test.util.MD5Hash;

class FileRecvProcessor implements Processor {
	private boolean file_process		= false; //전에 파일을 전송했는지 여부
	private boolean file_process_result = false; //전에 수행한 파일전송 성공여부

	private final ByteBuffer msgbb = ByteBuffer.allocateDirect(1000); //파일전송 메시지 송신용 버퍼

	/*
	 * 서버에 파일을 저장하기 전에 파일이름을 변경한다.
	 */
	private String getFileName(String fileName) {
		if(fileName == null || "".equals(fileName.trim())) {
			fileName = String.format("%1$tY-%1$tm-%1$te", new Date());
		}

		String files[] = fileName.split("[.]");

		String name = files[0];
		String ext	= (files.length == 1) ? "" : "."+files[1];

		fileName = (name + System.currentTimeMillis())+ext;
		return fileName;
	}

	private boolean read(SocketChannel sc, long total_size, String fileName, String hash) {
		// TODO Auto-generated method stub
		FileOutputStream fos = null;
		FileChannel 	 fc	 = null;

		String path = "d:/server/"+getFileName(fileName);
		try {

			fos = new FileOutputStream(path);
			fc	= fos.getChannel();

			while(total_size != fc.size()) {
				fc.transferFrom(sc, fc.size(), total_size - fc.size());
				System.out.println("File Transfering ("+fc.size()+" / "+total_size+")");
			}

			file_process		= true;

			if(total_size == fc.size()) {
				System.out.println("File Transfer Complete");
				file_process_result = true;
			}else {
				System.out.println("File Transfer Failed!");
				file_process_result = false;
			}

			String newhash = MD5Hash.getMD5Checksum(path);
			if(newhash.intern() == hash.intern()) {
				System.out.println("Hash Matched(server - client)"+newhash+" - "+hash);
			}else {
				System.out.println("Hash UnMatched(server - client)"+newhash+" - "+hash);
			}
		} catch(IOException ioe) {
			file_process		= false;
			file_process_result = false;
			ioe.printStackTrace();
		} finally {
			try {
				fc.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return file_process;
	}

	private long writeProcessMsg(SocketChannel sc) throws IOException{
		String msg = "";

		if(file_process_result) {
			msg = "File Transfer Success";
		}else {
			msg = "File Transfer Fail";
		}

		msgbb.clear();
		msgbb.put(ByteBufferUtil.str_to_bb(msg));
		msgbb.flip();

		return sc.write(msgbb);
	}


	@Override
	public void process(Header header, SelectionKey key, Mode mode) {
		SocketChannel sc = (SocketChannel)key.channel();
		// TODO Auto-generated method stub
		try {
			switch(mode) {
				case READ	:
					read(sc, header.getTotalSize(), header.getFileName(), header.getFileHash());
					key.interestOps(SelectionKey.OP_WRITE);
					break;
				case WRITE	:
					writeProcessMsg(sc);
					key.interestOps(SelectionKey.OP_READ);
					break;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HeaderType getType() {
		// TODO Auto-generated method stub
		return HeaderType.FILERECV;
	}
}
