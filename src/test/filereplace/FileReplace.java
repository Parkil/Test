package test.filereplace;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import test.util.FileUtil;
import test.util.FindFile;
/*
 * 특정 확장자의 파일을 읽어들여 정규식에 해당하는 문구를 새로운 문구로 치환한후 새경로에 저장
 */
public class FileReplace {
	private Logger log = Logger.getLogger(getClass());
	private static ConvertData cd	   = ConvertData.getInstance();
	private static FileUtil util	   = new FileUtil();
	private static Pattern filepattern = Pattern.compile("(^.*[ui|sql])(.*/)(.*$)");//파일 분할 정규식

	private static Pattern p;

	private ArrayList<File> getFindFile(String rootpath, String ext) {
		File root				 = new File(rootpath);
		ArrayList<File> filelist = FindFile.findFileList(root, ext);

		return filelist;
	}

	public void filereplace(String ext) {
		String root	   = "";
		String newroot = "";

		if(ext.intern() == "jsp".intern()) {
			root	= "d:/DEV_ENV/workspace_kotra/KOTRA_PROJ/WebContent/ui";
			newroot = "d:/newui";
			p = cd.getJspPattern();
		}else if(ext.intern() == "xml".intern()) {
			root	= "d:/DEV_ENV/workspace_kotra/KOTRA_PROJ/src/resources/sql";
			newroot = "d:/newsql";
			p = cd.getSQLPattern();
		}

		Matcher filecon_matcher = p.matcher("");
		Matcher file_matcher	= filepattern.matcher("");

		ArrayList<File> list = getFindFile(root, ext);
		for(File file : list) {
			String orgpath = file.getPath().replace("\\","/");

			String filecon = util.readFile(orgpath);
			filecon_matcher.reset(filecon);
			file_matcher.reset(orgpath);
			file_matcher.find();

			while(filecon_matcher.find()) {
				String prev  = filecon_matcher.group(2); //jsp일 경우에는 1 xml일경우에는 2
				String after = cd.getHashtable().get(prev);
				log.info(file_matcher.group(3)+"[match phrase : "+prev+" | changed phrase : "+after+"]");

				filecon = filecon.replace(prev, after);
			}

			//경로가 존재하지 않을경우 경로를 생성
			file = new File(newroot+file_matcher.group(2));

			if(!file.exists()) {
				file.mkdirs();
			}

			util.writeFile(newroot+file_matcher.group(2)+file_matcher.group(3), filecon);
		}
	}

	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis();
		FileReplace fr = new FileReplace();
		fr.filereplace("xml");

		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}
