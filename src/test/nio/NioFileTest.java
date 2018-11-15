package test.nio;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/*
 * NIO를 이용한 파일 시스템 접근 예제
 */
public class NioFileTest {
	public static void main(String[] args) throws Exception{
		Path path = Paths.get("//10.184.56.143/UMG_Content");
		
		//필터 설정 - newDirectoryStream 호출시 특정 파일시스템만 검색하여 추출
		DirectoryStream.Filter<Path> onlyDirFilter = new DirectoryStream.Filter<Path>() {
			public boolean accept(Path file) throws IOException {
				return Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS);
			}
		};
		
		DirectoryStream<Path> stream = Files.newDirectoryStream(path, onlyDirFilter);
		
		/*
		 * 1.8이상부터는 File.find 메소드를 이용하여 파일검색이 기능하다
		 */
		for(Path temp : stream) {
			DirectoryStream<Path> xmlStream = Files.newDirectoryStream(temp, "*.xml"); //해당 디렉토리안의 파일중 xml 확장자를 가진 파일만 검색
			DirectoryStream<Path> mp3Stream = Files.newDirectoryStream(temp, "*.mp3");
			DirectoryStream<Path> flacStream = Files.newDirectoryStream(temp, "*.flac");
			DirectoryStream<Path> imgStream = Files.newDirectoryStream(temp, "*.{jpg,jpeg,png,gif}"); //여러개의 확장자를 동시에 검색
			
			List<Path> xmlList = IteratorUtils.toList(xmlStream.iterator());
			List<Path> mp3List = IteratorUtils.toList(mp3Stream.iterator());
			List<Path> flacList = IteratorUtils.toList(flacStream.iterator());
			List<Path> imgList = IteratorUtils.toList(imgStream.iterator());
			
			System.out.println(temp.getFileName()+"=="+xmlList.size()+"=="+mp3List.size()+"=="+flacList.size()+"=="+imgList.size());
		}
		
		/*
		//기존 IO를 이용하여 파일을 검색하는 방법 일반 드라이브 접근시에는 별 문제가 없으나 네트워트 드라이브에 접근을 하여 하위 파일이나 디렉토리를 검색하게 되면 성능이 심각하게 저하된다.
		File file = new File("//10.184.56.143/UMG_Content");
		
		FileFilter onlyDirectoryFilter = new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		};
		
		File[] dirList = file.listFiles(onlyDirectoryFilter);
		
		for(File dir : dirList) {
			System.out.println(dir.getPath());
		}*/
	}
}
