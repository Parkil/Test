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
	 * append�ɼ��� ����� ��� ���������δ� append�� ����� �� ���⶧���� append�� ���������� ���� �����Ѵ�.
	 */
	private FileOutputStream	append_stream	= null;
	private FileChannel			append_channel	= null;

	/**
	 * append�� stream,Channel�� closeó���Ѵ�.
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


	/**������ �����Ѵ�. 30MB�� �Ѵ� ������ �����Ϸ��� �Ҷ����� UnsupportedOperationException�� �߻��Ѵ�.
	 * @param orgpath ���������� ���
	 * @param copypath �����ϰ����ϴ� ������ ���
	 * @return ���缺������
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
			//30MB�� �Ѵ� ������ �����Ҽ� ����
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

	/**���� ������ ���ڿ��� �о���� �⺻ encoding�� utf-8�� �����Ǿ� ����
	 * @param path
	 * @return
	 */
	public String readFile(String path) {
		return readFile(path, "utf-8");
	}

	/**���ϳ����� ���ڿ��� �о���� ū ������ �о���ϰ�� out of memory������ �߻��ϹǷ� ����� �ּ��� -Xmx1024m �ɼ����� �����Ұ�
	 * @param path �о���� ���ϰ��
	 * @param encoding ���ڿ��� ��ȯ�� ����� ���ڵ�
	 * @return ������ ����
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

	/**������ �����Ѵ� ������ ��η� method ��ȣ��� ���������� �����ȴ�.
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

	/** append�� ������ �����Ѵ�.
	 * @param path ������ ���ϰ��
	 * @param content ������ ����
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


	/**������ �о�鿩 ByteBuffer�������� ��ȯ�Ѵ�.
	 * @param path ���ϰ��
	 * @return ������ ������ ���� ByteBuffer
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

	/**���ڷ� �޾Ƶ��� ByteBuffer�� ����������Ͽ� �����Ѵ�.
	 * @param path �����ϰ����ϴ� ���ϰ��
	 * @param orgbuffer �����ϰ��� �ϴ� ������ ���� Bytebuffer
	 */
	public void writeFileBinary(String path, ByteBuffer orgbuffer) {
		// TODO Auto-generated method stub
		RandomAccessFile fos = null;
		FileChannel		 fc	 = null;
		try {
			fos = new RandomAccessFile(path, "rw");
			fc	= fos.getChannel();
			fc.truncate(orgbuffer.limit()); //���� ���Ͽ뷮�� �ִ°�� ���� ���� �뷮���� ����

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
