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
	
	//����ȭ���� Data�� ������ ���ؾ� �Ҷ� �������ϴ� �޼ҵ�
	private void writeObject(ObjectOutputStream oos) throws IOException, IllegalBlockSizeException{
		System.out.println("����ȭ���� �߰�ó��");
		test *= 100;
		oos.defaultWriteObject();
	}
	
	//������ȭ���� Data�� ������ ���ؾ� �Ҷ� �������ϴ� �޼ҵ�
	private void readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException{
		System.out.println("������ȭ���� �߰�ó��");
		ois.defaultReadObject();
		test /= 100;
	}
}
