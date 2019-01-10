package test.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/*
 * Nio를 이용하여 특정 디렉토리안의 파일을 검색
 * 아래 소스는 다음 사이트에서 가져 왔음 https://javapapers.com/java/search-file-using-nio/
 */
public class NioFileSearch {
	//디렉토리 안에 있는 파일+디렉토리를 삭제
	void deleteDirectoryRecursion(Path path) throws IOException {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
				for (Path entry : entries) {
					deleteDirectoryRecursion(entry);
				}
			}
		}
		Files.deleteIfExists(path);
	}
		
	public static class SearchFileVisitor extends SimpleFileVisitor<Path> {

		private final PathMatcher pathMatcher;
		private int matchCount = 0;
		
		//파일검색 패턴을 지정 파일 검색 패턴에 대해서는 javadoc FileSystem.getPathMatcher 참고
		SearchFileVisitor(String globPattern) {
			pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
		}
		
		/*
		 * root 디렉토리아래의 파일 1개씩 방문할때마다 호출 됨
		 * return 값 
		 * FileVisitResult.CONTINUE - 현재 파일/디렉토리 방문후 계속 진행
		 * FileVisitResult.TERMINATE - 현재 파일/디렉토리 방문후 종료처리
		 */
		@Override
		public FileVisitResult visitFile(Path filePath, BasicFileAttributes basicFileAttrib) {
			if (pathMatcher.matches(filePath.getFileName())) {
				matchCount++;
				System.out.println(filePath+"=="+filePath.getParent());
			}
			
			return FileVisitResult.CONTINUE;
		}
		
		/*
		 * root 디렉토리아래의 디렉토리 1개씩 방문할때마다 호출 됨
		 * return 값 
		 * visitFile과 동일
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path directoryPath, BasicFileAttributes basicFileAttrib) {
			System.out.println("directoryPath : "+directoryPath);
			if (pathMatcher.matches(directoryPath.getFileName())) {
				matchCount++;
			}
			return FileVisitResult.CONTINUE;
		}

		public int getMatchCount() {
			return matchCount;
		}
	}

	public static void main(String[] args) throws IOException {

		Path rootPath = Paths.get("//10.184.56.143/UMG_Content_BACK");
		String globPattern = "*.pdf";

		SearchFileVisitor searchFileVisitor = new SearchFileVisitor(globPattern);
		Files.walkFileTree(rootPath, searchFileVisitor);
		System.out.println("Match Count: " + searchFileVisitor.getMatchCount());
	}
}
