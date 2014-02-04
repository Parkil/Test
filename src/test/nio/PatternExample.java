package test.nio;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.util.FileUtil;

public class PatternExample {
	/*
	 * ���ϳ����� �о�鿩 Pattern-Matcher�� �´� ���ڿ��� ���
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
	 * Pattern�� split�� �̿��Ͽ� ���ڿ��� ����
	 */
	public void split() {
		String org		= "1-2-3-4-5-6-7-8-9-10-";
		String pattern	= "-";
		
		Pattern	p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		
		/*
		 * split���� ���ڴ� �ִ� ���������� �ɰ��������� �����Ѵ�.
		 * ����	: ���̻� �ɰ��� ���������� split�� ����
		 * 0	: ������ ���������� ������ split�� ����� ���� ��� ����� ���Խ�Ű�� �ʴ´�.
		 * 1	: split�� ���� ����(1������ �ִ��̱⶧����)
		 * ������	: ������ ���ڸ�ŭ �ɰ���.
		 */
		String x[] = p.split(org,2);
		
		for(String a : x) {
			System.out.println("line : "+a);
		}
	}
	
	/*
	 * Pattern-Matcher �⺻��뿹��
	 */
	public void basic() {
		String org		= "-1-2-3-4-5-6-7-8-9-10-";
		String pattern	= "-|j";
		
		Pattern	p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(org);
		
		if(m.lookingAt()) { //���ڿ� ����ó���� ���Խ��� ������ ��쿡�� true�� ��ȯ
			System.out.println(m.group()); //group()�� lookingAt()/match()/find()�� true�� ��쿡�� ����� ��ȯ�Ѵ� �׿��� �������� ����ϸ� ���ܰ� �߻���
		}
		/*
		System.out.println(m.matches()); //���Խİ� �Է��� ���ڿ��� 100% �¾ƾ� true�� ��ȯ�Ѵ�.
		
		while(m.find()) {
			System.out.println("Group : "+m.group(0)); //Group = Pattern��
			System.out.println(org.substring(m.start()-1,m.end()-1));
		}
		
		m.reset("-"); //reset�޼ҵ带 �̿��Ͽ� Matcher�� ��������� �ʰ� ���ο� ���ڿ��� ������ �� �ִ�.
		System.out.println(m.matches());*/
	}
	
	public static void main(String[] args) {
		new PatternExample().matcherFile();
	}
}
