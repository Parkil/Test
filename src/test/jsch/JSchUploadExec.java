package test.jsch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/*
 * jsch 라이브러리를 이용하여 sftp upload를 수행하는 예제 
 * 해당예제는 exec모드를 이용하여 scp 커맨드를 실행하는 방식으로 업로드를 수행
 */
public class JSchUploadExec {
	static int checkAck(InputStream in) throws IOException{
		int b=in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if(b==0) return b;
		if(b==-1) return b;
		
		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}while(c!='\n');
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
	
	public static void main(String[] args) throws Exception{
		JSch jsch = new JSch();
		Session session = jsch.getSession("uni_ftp", "210.122.111.15", 22);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword("!@12uni_ftp");
		
		session.connect();
		
		boolean ptimestamp = true;

		// exec 'scp -t rfile' remotely
		String command="scp " + (ptimestamp ? "-p" :"") +" -t "+"/data/uni_ftp/test/UMG_Metadata_mnetgsc_00008811017224_2018-10-26_06-13-33.xml";
		Channel channel=session.openChannel("exec");
		((ChannelExec)channel).setCommand(command);
		
		OutputStream out=channel.getOutputStream();
		InputStream in=channel.getInputStream();
		
		channel.connect();
		
		String lfile = "d:/xmlList/UMG_Metadata_mnetgsc_00008811017224_2018-10-26_06-13-33.xml";
		File _lfile = new File(lfile);
		
		System.out.println("command : "+command);
		
		command="T "+(_lfile.lastModified()/1000)+" 0";
		// The access time should be sent here,
		// but it is not accessible with JavaAPI ;-<
		command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
		out.write(command.getBytes()); out.flush();
		if(checkAck(in)!=0){
			System.exit(0);
		}
		
		System.out.println("command : "+command);
		
		// send "C0644 filesize filename", where filename should not include '/'
		long filesize=_lfile.length();
		command="C0644 "+filesize+" ";
		if(lfile.lastIndexOf('/')>0){
			command+=lfile.substring(lfile.lastIndexOf('/')+1);
		}else{
			command+=lfile;
		}
		command+="\n";
		out.write(command.getBytes());
		out.flush();
		
		System.out.println("command : "+command);
			
		if(checkAck(in)!=0){
			System.exit(0);
		}
		
		// send a content of lfile
		FileInputStream fis=new FileInputStream(lfile);
		byte[] buf=new byte[1024];
		
		while(true){
			int len=fis.read(buf, 0, buf.length);
			if(len<=0) break;
			out.write(buf, 0, len); //out.flush();
		}
		
		fis.close();
		fis=null;
		
		// send '\0'
		buf[0]=0; out.write(buf, 0, 1); out.flush();
		if(checkAck(in)!=0){
			System.exit(0);
		}
		out.close();
		
		channel.disconnect();
		session.disconnect();
		
		System.exit(0);
	}
}

