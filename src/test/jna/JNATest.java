package test.jna;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

/*
 * java native access 라이브러리를 이용하여 현재 기동중인 윈도우 프로세스를 가져오는 예제 234===23536	javaw.exe
 */
public class JNATest {
	public static void main(String[] args) {
		Kernel32 kernel32 = Native.load("Kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
		Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
		WinNT.HANDLE snapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
		
		int i =0 ;
		try {
			while (kernel32.Process32Next(snapshot, processEntry)) {
				System.out.println((i++)+"==="+processEntry.th32ProcessID + "\t" + Native.toString(processEntry.szExeFile));
			}
		} finally {
			kernel32.CloseHandle(snapshot);
		}
	}
}
