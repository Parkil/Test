package test.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;

/*
 * 지정된 root디렉토리 아래의 폴더검색시 마지막 수정시간으로 오름차순 정렬을 하여 표시하는 예제
 */
public class NioFileSearchOrderBy {
	public static void main(String[] args) throws Exception,IOException{
		Path path = Paths.get("d:/");
		
		//필터 설정 - newDirectoryStream 호출시 특정 파일시스템만 검색하여 추출
		DirectoryStream.Filter<Path> onlyDirFilter = new DirectoryStream.Filter<Path>() {
			public boolean accept(Path file) throws IOException {
				return Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS);
			}
		};
		
		DirectoryStream<Path> stream = Files.newDirectoryStream(path, onlyDirFilter);
		
		Path[] arr = IteratorUtils.toArray(stream.iterator(), Path.class);
		
		Arrays.sort(arr, new Comparator<Path>() {
			public int compare(Path f1, Path f2) {
				int compare =0;
				try {
					compare =  Long.compare(Files.getLastModifiedTime(f1).toMillis(), Files.getLastModifiedTime(f2).toMillis());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return compare;
			}
		});
		
		for(Path p : arr) {
			System.out.println(p+"=="+Files.getLastModifiedTime(p));
		}
		
		/*
		for(Path temp : stream) {
			System.out.println(temp+"=="+Files.getLastModifiedTime(temp));
		}*/
	}
}
