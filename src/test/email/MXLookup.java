package test.email;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/*
 * �̸��� ȣ��Ʈ(@ �� ����)�� ����� MX ��������Ʈ�� �����ϴ� ����
 */
public class MXLookup {
	static int doLookUp(String hostName) throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext(env);

		Attributes attrs = ictx.getAttributes(hostName, new String[] {"MX"});
		Attribute attr = attrs.get("MX");

		if(attr == null) {
			return 0;
		}

		return attr.size();
	}

	public static void main(String[] args) throws Exception{
		System.out.println(doLookUp("kotra.or.kr"));
	}
}