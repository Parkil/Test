package test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;


public class FileUtil {
	Logger	log = Logger.getLogger(getClass());
	/*
	 * append옵션을 사용할 경우 지역변수로는 append를 사용할 수 없기때문에 append용 전역변수를 따로 설정한다.
	 */
	private FileOutputStream	append_stream	= null;
	private FileChannel			append_channel	= null;

	/**
	 * append용 stream,Channel을 close처리한다.
	 */
	public void close() {
		try {
			if(append_stream	!= null) {
				append_stream.close();
			}
			if(append_channel	!= null) {
				append_channel.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**파일을 복사한다. 30MB가 넘는 파일을 복사하려고 할때에는 UnsupportedOperationException이 발생한다.
	 * @param orgpath 원래파일의 경로
	 * @param copypath 복사하고자하는 파일의 경로
	 * @return 복사성공여부
	 */
	public boolean fileCopy(String orgpath, String copypath) {
		boolean result = false;

		FileInputStream  org	= null;
		FileChannel      in		= null;

		RandomAccessFile copy	= null;
		FileChannel		 out	= null;

		try {
			org	  = new FileInputStream(orgpath);
			in	  = org.getChannel();

			long filesize = in.size();
			//30MB가 넘는 파일은 복사할수 없음
			if(filesize > 31457280) {
				throw new UnsupportedOperationException("This Method not Supported File Size Over 30MB(org file size:"+filesize/(1024*1024)+"MB)");
			}

			copy   = new RandomAccessFile(copypath,"rw");
			out	   = copy.getChannel();

			in.transferTo(0, in.size(), out);

			result = true;
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(in   != null) {
					in.close();
				}
				if(org  != null) {
					org.close();
				}
				if(out  != null) {
					out.close();
				}
				if(copy != null) {
					copy.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	private void initAppendFile(String path) {
		try {
			append_stream	= new FileOutputStream(path, true);
			append_channel	= append_stream.getChannel();
			log.info("Append File Initialzied");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**파일 내용을 문자열로 읽어들임 기본 encoding은 utf-8로 설정되어 있음
	 * @param path
	 * @return
	 */
	public String readFile(String path) {
		return readFile(path, "utf-8");
	}

	/**파일내용을 문자열로 읽어들임 큰 파일을 읽어들일경우 out of memory에러가 발생하므로 실행시 최소한 -Xmx1024m 옵션으로 실행할것
	 * @param path 읽어들일 파일경로
	 * @param encoding 문자열로 변환시 사용할 인코딩
	 * @return 파일의 내용
	 */
	public String readFile(String path, String encoding) {
		FileInputStream fis = null;
		FileChannel		fc  = null;
		String			str = null;

		try {
			fis = new FileInputStream(path);
			fc	= fis.getChannel();
			MappedByteBuffer	mmb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			ByteBufferUtil.setEncoding(encoding);
			str = ByteBufferUtil.bb_to_str(mmb);
			//log.info("Read Data From File [Size : "+mmb.limit()+"]");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis 	!= null) {
					fis.close();
				}
				if(fc 	!= null) {
					fc.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return str;
	}

	/**파일을 저장한다 동일한 경로로 method 재호출시 기존내용은 삭제된다.
	 * @param path
	 * @param content
	 * @return
	 */
	public int writeFile(String path, String content) {
		FileOutputStream fos 		= null;
		FileChannel		 out 		= null;
		int 			 writebyte 	= 0;

		try {
			fos = new FileOutputStream(path);
			out = fos.getChannel();

			ByteBufferUtil.setEncoding("8859_1");
			writebyte = out.write(ByteBufferUtil.str_to_bb(content));
			log.info("Non Append File Write Data [Size : "+writebyte+"]");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.close();
				}
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return writebyte;
	}

	/** append된 파일을 저장한다.
	 * @param path 저장할 파일경로
	 * @param content 저장할 내용
	 * @return
	 */
	public int writeFileAppend(String path, String content) {
		if(append_stream == null) {
			initAppendFile(path);
		}

		int writebyte = 0;

		try {
			writebyte = append_channel.write(ByteBufferUtil.str_to_bb(content));
			log.info("Append File Write Data [Size : "+writebyte+"]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writebyte;
	}


	/**파일을 읽어들여 ByteBuffer형식으로 반환한다.
	 * @param path 파일경로
	 * @return 파일의 내용을 담은 ByteBuffer
	 */
	public ByteBuffer readFileBinary(String path) {
		FileInputStream fis = null;
		FileChannel		fc  = null;
		ByteBuffer		ret = null;

		try {
			fis = new FileInputStream(path);
			fc	= fis.getChannel();
			ret = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis 	!= null) {
					fis.close();
				}
				if(fc 	!= null) {
					fc.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**인자로 받아들인 ByteBuffer를 목적경로파일에 저장한다.
	 * @param path 저장하고자하는 파일경로
	 * @param orgbuffer 저장하고자 하는 내용을 가진 Bytebuffer
	 */
	public void writeFileBinary(String path, ByteBuffer orgbuffer) {
		// TODO Auto-generated method stub
		RandomAccessFile fos = null;
		FileChannel		 fc	 = null;
		try {
			fos = new RandomAccessFile(path, "rw");
			fc	= fos.getChannel();
			fc.truncate(orgbuffer.limit()); //남는 파일용량이 있는경우 현재 파일 용량으로 줄임

			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, orgbuffer.limit());

			mbb.put(orgbuffer);
			mbb.flip();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
				if(fc  != null) {
					fc.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
