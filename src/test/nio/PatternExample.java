package test.nio;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.util.FileUtil;

public class PatternExample {
	/*
	 * 파일내용을 읽어들여 Pattern-Matcher에 맞는 문자열을 출력
	 */
	public void matcherFile() {
		FileUtil	util		= new FileUtil();
		String		br_pattern	= "name=+[\"'](HOME|BIZMANMIZ|RAREVWRSTREG|CURSTATE|DOCNO)[\"']";
		String		org			= util.readFile("d:/BR03A01E01.jsp");
		
		Pattern		p			= Pattern.compile(br_pattern);
		Matcher		m			= p.matcher(org);
		
		Hashtable<String,String> replace= new Hashtable<String,String>();
		replace.put("HOME", "HOMEEX");
		replace.put("BIZMANMIZ", "BIZMANMIZEX");
		replace.put("RAREVWRSTREG", "RAREVWRSTREGEX");
		replace.put("CURSTATE", "CURSTATEEX");
		replace.put("DOCNO", "DOCNOEX");

		String after = org;
		while(m.find()) {
			String temp = m.group();
			temp 		= temp.substring(6,temp.length()-1);
			
			after = m.replaceFirst("name=\""+replace.get(temp)+"\"");
			m.reset(after);
		}
		
		System.out.println(after);
	}
	
	/*
	 * Pattern의 split을 이용하여 문자열을 분할
	 */
	public void split() {
		String org		= "1-2-3-4-5-6-7-8-9-10-";
		String pattern	= "-";
		
		Pattern	p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		
		/*
		 * split뒤의 숫자는 최대 몇조각으로 쪼갤것인지를 지정한다.
		 * 음수	: 더이상 쪼갤수 없을때까지 split을 실행
		 * 0	: 음수와 동일하지만 마지막 split의 결과가 빈값일 경우 결과에 포함시키지 않는다.
		 * 1	: split을 하지 않음(1조각이 최대이기때문에)
		 * 나머지	: 지정한 숫자만큼 쪼갠다.
		 */
		String x[] = p.split(org,2);
		
		for(String a : x) {
			System.out.println("line : "+a);
		}
	}
	
	/*
	 * Pattern-Matcher 기본사용예제
	 */
	public void basic() {
		String org		= "-1-2-3-4-5-6-7-8-9-10-";
		String pattern	= "-|j";
		
		Pattern	p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(org);
		
		if(m.lookingAt()) { //문자열 제일처음과 정규식이 동일한 경우에만 true를 반환
			System.out.println(m.group()); //group()은 lookingAt()/match()/find()가 true일 경우에만 결과를 반환한다 그외의 로직에서 사용하면 예외가 발생함
		}
		/*
		System.out.println(m.matches()); //정규식과 입력한 문자열이 100% 맞아야 true를 반환한다.
		
		while(m.find()) {
			System.out.println("Group : "+m.group(0)); //Group = Pattern임
			System.out.println(org.substring(m.start()-1,m.end()-1));
		}
		
		m.reset("-"); //reset메소드를 이용하여 Matcher를 재생성하지 않고 새로운 문자열을 검증할 수 있다.
		System.out.println(m.matches());*/
	}
	
	public static void main(String[] args) {
		new PatternExample().matcherFile();
	}
}
