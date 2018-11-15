package test.processBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * ProcessBuilder�� �̿��Ͽ� �ܺ����α׷��� �����ϴ� ����
 */
public class ProcessBuilderTest {
	public static void main(String[] args) {
	
		try {
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "start;c:\\HAL_Teleport\\setup.bat");
			pb.directory(new File("c:\\HAL_Teleport\\")); //�ش� ��ɾ ������ ���丮 ����
			Process process = pb.start();
			
			/*
			 * process�� getInputStream�� ���� I/O�̹Ƿ� �ش� ���μ����� ������ ���� ���������� �������� ���Ѵ�.
			 */
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			isr.read();
			
			System.out.println("��������");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
