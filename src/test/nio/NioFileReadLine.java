package test.nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class NioFileReadLine {
	public static void main(String[] args) throws IOException {
		Path path = FileSystems.getDefault().getPath("d:/", "회전사각형-input.txt");
		Files.lines(path).forEach(line ->
			System.out.println(line)
		);
	}
}
