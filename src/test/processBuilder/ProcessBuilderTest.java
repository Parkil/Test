package test.processBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * ProcessBuilder를 이용하여 외부프로그램을 실행하는 예제
 */
public class ProcessBuilderTest {
	public static void main(String[] args) {
	
		try {
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "start;c:\\HAL_Teleport\\setup.bat");
			pb.directory(new File("c:\\HAL_Teleport\\")); //해당 명령어를 실행할 디렉토리 지정
			Process process = pb.start();
			
			/*
			 * process의 getInputStream은 동기 I/O이므로 해당 프로세스가 동작할 동안 다음로직을 실행하지 못한다.
			 */
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			isr.read();
			
			System.out.println("다음로직");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
