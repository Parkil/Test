import java.io.File;
import java.io.FilenameFilter;


public class FileFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		boolean result = name.endsWith("uf");
		return result;
	}
}
