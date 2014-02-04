package test.regex;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.util.FileUtil;

/*
 * ���ϳ��뿡�� ���ԽĿ� �ش�Ǵ� �κ��� ���ο� �κ����� ġȯ�Ͽ� �����ϴ� ����
 */
public class RegExpExample {
	/*
	 * JSP���� Pattern�� �´� ���ڿ��� ����
	 */
	public void changejsp() {
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
	 * SQL���� Pattern�� �´� ���ڿ��� ����
	 */
	public void changeSQL() {
		FileUtil	util		= new FileUtil();
		String		br_pattern	= "AS( +)(SVCCNSL|SRVCNAME|PROCSTEP|DOCNO)";
		String		org			= util.readFile("d:/BR01A01.xml");

		Pattern		p			= Pattern.compile(br_pattern);
		Matcher		m			= p.matcher(org);

		Hashtable<String,String> replace= new Hashtable<String,String>();
		replace.put("SVCCNSL", "SVCCNSLEX");
		replace.put("SRVCNAME", "SRVCNAMEEX");
		replace.put("PROCSTEP", "PROCSTEPEX");
		replace.put("DOCNO", "DOCNOEX");

		String after = org;
		while(m.find()) {
			String temp		= m.group();
			temp			= temp.substring(3,temp.length());
			after 			= m.replaceFirst(replace.get(temp.trim()));

			m.reset(after);
		}

		System.out.println(after);
	}

	public static void main(String[] args) {
		new RegExpExample().changejsp();
	}
}
