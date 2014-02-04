package test.smtpsend;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;


public class SendSMTPMail {
	//	private String mSMTP_HOST_NAME = "smtp.gmail.com";
	//	private String mSMTP_PORT = "465";
	private final String mSMTP_HOST_NAME = "spam.kotra.or.kr";
	private final String mSMTP_PORT = "25";
	private final String mLanguage = "utf-8";
	private String mEmailFromAddress = "100000@kotra.or.kr";
	private final String mEmailFromID = "alkain77@gmail.com";

	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();
	/**
	 * 
	 * @param recipients 받는 사람 메일 리스트
	 * @param subject 제목
	 * @param aMailMessage 메일메세지
	 * @param fromName 보내는 사람명
	 * @param toCC 참조 메일 리스트
	 * @param toBCC 숨은 참조 메일 리스트
	 * @param aEMAIL_FILE_LIST 원본파일 리스트 path/tempfile명
	 * @param aEMAIL_FILENAME_LIST 파일명
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendSSLMessage(String recipients[], String subject, String aMailMessage,
			String fromName, String fromEmail, String toCC[], String toBCC[],
			String aEMAIL_FILE_LIST[], String aEMAIL_FILENAME_LIST[] )
	throws AddressException, MessagingException, UnsupportedEncodingException {
		EtmPoint point = SendSMTPMail.etmMonitor.createPoint("main");

		if(fromEmail != null && fromEmail.trim().length() != 0) {
			mEmailFromAddress = fromEmail;
		}

		String tResult = "";
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.host", mSMTP_HOST_NAME);
		if(mSMTP_PORT != null && mSMTP_PORT.trim().length() == 0) {
			mailProps.put("mail.smtp.port", mSMTP_PORT);
		}

		if(mEmailFromID != null && mEmailFromID.trim().length() == 0) {
			mailProps.put("mail.smtp.auth", "false");
		}

		mailProps.put("mail.debug", "true");

		Session mailSession = Session.getInstance(mailProps, null);

		List<String> tMailToList = new ArrayList<String>();
		for (int i = 0; i < recipients.length; i++) {
			if(recipients[i].trim().length() == 0) {
				continue;
			}
			tResult = chkMailServer(recipients[i].trim());
			if(tResult.length()>0) {
				new Exception(tResult);
			}

			tMailToList.add(recipients[i].trim());
		}

		InternetAddress[] addressTo = new InternetAddress[tMailToList.size()];
		for (int i = 0; i < tMailToList.size(); i++) {
			addressTo[i] = new InternetAddress(tMailToList.get(i));
		}

		List<String> tMailToCCList = new ArrayList<String>();
		for (int i = 0; i < toCC.length; i++) {
			tResult = chkMailServer(toCC[i].trim());
			if(tResult.length()>0) {
				new Exception(tResult);
			}

			if(toCC[i].trim().length() == 0) {
				continue;
			}

			tMailToCCList.add(toCC[i].trim());
		}

		InternetAddress[] addressToCC = new InternetAddress[tMailToCCList.size()];
		for (int i = 0; i < tMailToCCList.size(); i++) {
			addressToCC[i] = new InternetAddress(tMailToCCList.get(i));
		}

		List<String> tMailToBCCList = new ArrayList<String>();
		for (int i = 0; i < toBCC.length; i++) {
			tResult = chkMailServer(toBCC[i].trim());
			if(tResult.length()>0) {
				new Exception(tResult);
			}

			if(toBCC[i].trim().length() == 0) {
				continue;
			}
			tMailToBCCList.add(toBCC[i].trim());
		}

		InternetAddress[] addressToBCC = new InternetAddress[tMailToBCCList.size()];
		for (int i = 0; i < tMailToBCCList.size(); i++) {
			addressToBCC[i] = new InternetAddress(tMailToBCCList.get(i));
		}

		InternetAddress fromAddr = new InternetAddress(mEmailFromAddress ,fromName, mLanguage);

		// Create and initialize message
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(fromAddr);
		message.setRecipients(Message.RecipientType.TO, addressTo);

		if(tMailToCCList.size() > 0) {
			message.setRecipients(Message.RecipientType.CC, addressToCC);
		}

		if(tMailToBCCList.size() > 0) {
			message.setRecipients(Message.RecipientType.BCC, addressToBCC);
		}

		message.setSubject(subject, mLanguage);
		message.setSentDate(new Date());
		//2011-10-24 jrkim 아래의 내용이 없으면 특정한 곳에서 메일 오류가 발생함
		message.setHeader("Content-Transfer-Encoding", "quoted-printable");

		//20120901 jrkim 파일 관련 변경
		//message.setContent(aMailMessage.toString(), "text/html; charset="+mLanguage);

		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setContent(aMailMessage.toString(), "text/html; charset="+mLanguage);

		String path = "";

		File[] sendFile = new File[aEMAIL_FILE_LIST.length];
		String[] sendFilename = new String[aEMAIL_FILE_LIST.length];
		int iFileCount = 0;
		for (int i = 0; i < aEMAIL_FILE_LIST.length; i++) {
			sendFile[i]=null;
			if(aEMAIL_FILE_LIST[i] != null && aEMAIL_FILE_LIST[i].length() > 0){
				File tfile = new File(path + aEMAIL_FILE_LIST[i] );
				if(tfile != null && tfile.exists()){
					sendFile[iFileCount] = tfile;
					sendFilename[iFileCount] = aEMAIL_FILENAME_LIST[i];
					iFileCount++;
				}
			}
		}

		MimeBodyPart[] mbp = new MimeBodyPart[iFileCount];
		for (int i = 0; i < iFileCount; i++) {
			mbp[i] = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(sendFile[i]);
			mbp[i].setDataHandler(new DataHandler(fds));
			mbp[i].setFileName(MimeUtility.encodeText(sendFilename[i], "UTF-8", "B"));
		}

		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);

		for (int i = 0; i < iFileCount; i++) {
			mp.addBodyPart(mbp[i]);
		}

		message.setContent(mp);

		// Send message
		try {
			Transport.send(message);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new MessagingException(ex.getMessage());
		}

		//파일 삭제
		for (int i = 0; i < iFileCount; i++) {
			//sendFile[i].delete();
		}
		point.collect();
	}

	//메일 서버 검사
	public String chkMailServer(String aEMail){
		String tResult = "";

		/*
	   //메일송신 시간이 오래 걸림
		int nAtPos = aEMail.indexOf( "@");
		if( aEMail.equals( "") || nAtPos < 0) {
			tResult = "["+aEMail+"] an unusual e-mail address.";
			return tResult;
		}
	  	String strEmailServer = aEMail.substring( nAtPos + 1);
	  	String strMXRecord = null;

	  	String[] strDNSServer =
		{
			"dns://164.124.101.2",	// ns.dacom.co.kr
			"dns://211.47.128.1",	// ns.krline.net
			"dns://210.217.40.1"	// ns.kisc.org
		};
		try {
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
			for( int i = 0; i < strDNSServer.length; i++) {
				env.put("java.naming.provider.url",    strDNSServer[i]);
				DirContext ictx = new InitialDirContext(env);
				Attributes attrs = ictx.getAttributes( strEmailServer, new String[] {"MX"});
				Attribute att = attrs.get("MX");
				if( att != null) {
					strMXRecord = (String)att.get();
					break;
				}
			}
		} catch ( Exception e) {
			//System.out.println( e);
		}
		if( strMXRecord == null) {
			tResult = "["+aEMail+"] an unusual e-mail address.";
		}
		 */
		return tResult;
	}
}
