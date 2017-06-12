package test.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.crypto.IllegalBlockSizeException;


public class Object1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int test		= 0;
	public int test2	= 0;
	
	//직렬화도중 Data에 수정을 가해야 할때 재정의하는 메소드
	private void writeObject(ObjectOutputStream oos) throws IOException, IllegalBlockSizeException{
		System.out.println("직렬화도중 중간처리");
		test *= 100;
		oos.defaultWriteObject();
	}
	
	//역직렬화도중 Data에 수정을 가해야 할때 재정의하는 메소드
	private void readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException{
		System.out.println("역직렬화도중 중간처리");
		ois.defaultReadObject();
		test /= 100;
	}
}
