package test.jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/*
 * jsch 라이브러리를 이용하여 sftp upload를 수행하는 예제 
 * 해당예제는 sftp모드를 이용하여 업로드를 수행
 * 
 * 업로드 속도를 위해 ssh compression을 설정
 * 
 * 관련 jar
 * jsch-0.1.54.jar
 * jzlib-1.1.3.jar
 */
public class JSchUploadSftp {
	public static void main(String[] args) {
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		
		try {
			session = jsch.getSession("uni_ftp", "210.122.111.15", 22);
			
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword("!@12uni_ftp");
			
			//ssh compression 설정
			session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
			session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
			session.setConfig("compression_level", "9");
			
			session.connect();
			
			channel = session.openChannel("sftp");
			channel.connect();
			
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.cd("/data/uni_ftp/test");
			sftp.mkdir("zzz"); //이미 있는 디렉토리를 생성하려고 시도시 [4: Failure]에러발생
			
			UploadMonitor f1 = new UploadMonitor();
			UploadMonitor f2 = new UploadMonitor();
			
			sftp.put("d:/music/24bit Flac.flac", "/data/uni_ftp/test/zzz/24bit Flac.flac", f1);
			sftp.put("d:/music/24bit wav.wav", "/data/uni_ftp/test/zzz/24bit wav.wav", f2);
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		} finally {
			if(channel != null) channel.disconnect();
			if(session != null) session.disconnect();
		}
	}
}

