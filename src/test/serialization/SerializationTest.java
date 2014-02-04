package test.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

public class SerializationTest {
	private static FileOutputStream fos = null;
	private static FileInputStream fis = null;
	private static ObjectOutputStream oos = null;
	private static ObjectInputStream ois = null;


	private void initialize(String type) {
		try {
			if(type.intern() == "save".intern() && fos == null && oos == null) {
				fos = new FileOutputStream(new File("d:/test.ser"),false);

				FileChannel fc = fos.getChannel();

				oos = new ObjectOutputStream(Channels.newOutputStream(fc));
			}else if(type.intern() == "load".intern() && fis == null && ois == null) {
				fis = new FileInputStream("d:/test.ser");

				FileChannel fc = fis.getChannel();

				ois = new ObjectInputStream(Channels.newInputStream(fc));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */

	public void save(Object object) {
		initialize("save");
		try {
			oos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object load() {
		initialize("load");
		Object value = null;
		try {
			value = ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return value;
	}

	public static void main(String[] args) throws Exception{
		SerializationTest test = new SerializationTest();
		//		Object1 o = new Object1();
		//		o.test = 1;
		//		test.save(o);
		Object1 recover = (Object1)test.load();
		System.out.println(recover.test);
		System.out.println(recover.test2);
	}
}
