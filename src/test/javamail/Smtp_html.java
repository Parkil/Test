/*
 * javamail api + smpt서버를 이용하여 메일을 보내는 예제(내용을 HTML로 보냄)
 */
package test.javamail;

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import com.sun.mail.smtp.*;

public class Smtp_html {
	public static void main(String[] argv) {
		//메일 기본정보
		String mail_from = null;
		String mail_to = null;
		String mail_cc = null;	//CC(carbon copy)
		String mail_bcc = null; //BCC(blind carbon copy)
		String subject = null; //제목
		String contents = null; //내용
		
		//SMTP서버 정보
		String mailhost = null; //SMTP 서버 도메인
		String mailhost_user = null; //SMTP 서버 ID
		String mailhost_pw = null; //SMTP 서버 PW
		int mailhost_port = -999; //SMPT 포트
		boolean auth = true; //SMTP 접속시 인증필요여부
		boolean isSSL = false; //SMTP접속시 SSL접속여부
		
		//메일 보관시 사용하는 정보
		String record_protocol = null; 
		String record_host = null; 
		String record_user = null;
		String record_pw = null;
		String record_location = null;
		String record_url = null;
		
		//기타 설정 정보
		String prot = "smtp";
		String mailer = "sendhtml";
		
		//javamail option
		boolean debug = true;
		boolean verbose = true;
		
		//========================메일 기본값 설정========================
		mail_from = "info@sk-i.co.kr";
		mail_to = "alkain77@naver.com";
		mail_cc = "";
		subject = "제목";
		contents = "내용";
		
		//========================SMTP 서버 설정========================
		//mailhost = "mw-002.cafe24.com";
		mailhost = "smtp.cafe24.com";
		mailhost_user = "mail@pkw100.cafe24.com";
		mailhost_pw = "pack7601";
		auth = true;
		isSSL = false;
		mailhost_port = 587;
		
		//========================첨부파일==============================
		File file = null;
		
		try {
			Properties props = System.getProperties();
			if (mailhost != null) {
				props.put("mail." + prot + ".host", mailhost);
			}
				
			if (auth) {
				props.put("mail." + prot + ".auth", "true");
			}
			
			//props.put("mail.smtp.connectiontimeout", "3000");
			//props.put("mail.smtp.timeout", "3000");

			/*
			 * Create a Provider representing our extended SMTP transport and
			 * set the property to use our provider.
			 *
			 * Provider p = new Provider(Provider.Type.TRANSPORT, prot,
			 * "smtpsend$SMTPExtension", "JavaMail demo", "no version");
			 * props.put("mail." + prot + ".class", "smtpsend$SMTPExtension");
			 */

			// Get a Session object
			Session session = Session.getInstance(props, null);
			if (debug) {
				session.setDebug(true);
			}

			/*
			 * Register our extended SMTP transport.
			 *
			 * session.addProvider(p);
			 */

			/*
			 * Construct the message and send it.
			 */
			MimeMessage msg = new MimeMessage(session);
			if (mail_from != null) {
				msg.setFrom(new InternetAddress(mail_from));
			}else {
				msg.setFrom();
			}

			
			
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("alkain77@naver.com", false));
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("alkain77@gmail.com,alkain777@daum.net", false));
			//msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse("alkain77@naver.com,alkain777@daum.net", false));
			//msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("alkain77@naver.com", false));
			
			/*
			if (mail_cc != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail_cc, false));
			}
				
			if (mail_bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail_bcc, false));
			}*/
			
			msg.setSubject(subject,"utf-8");
			//msg.setSubject(subject);

			if (file != null) {
				MimeBodyPart html_body_part = new MimeBodyPart();
				collect2("제목222",html_body_part);
				
				
				/*
				단순한 방법이기는 하지만 파일명이 한글일 경우 깨지는 문제가 발생한다.
				MimeBodyPart attach_part = new MimeBodyPart();
				attach_part.attachFile(file);
				*/
				
				/*
				 * 파일명이 한글인 경우 첨부파일명이 깨지는 문제를 수정하기 위해서 
				 * 파일정보자체는 javax.activation 라이브러리를 이용하여 저장하고 
				 * 파일명은 MimeUtility를 이용하여 저장하는 형태로 처리한다.
				 * activation-1.1.1.redhat-4.jar 필요
				 * 밑의 로직을 반복하면 여러개의 첨부파일을 등록하는것도 가능함
				 */
				MimeBodyPart attach_part = new MimeBodyPart();
				javax.activation.DataSource source = new javax.activation.FileDataSource(file);
				attach_part.setDataHandler(new javax.activation.DataHandler(source));
				attach_part.setFileName(MimeUtility.encodeText("첨부파일1.txt"));
				
				MimeBodyPart attach_part2 = new MimeBodyPart();
				javax.activation.DataSource source2 = new javax.activation.FileDataSource("d:/자동진행 로그.txt");
				attach_part2.setDataHandler(new javax.activation.DataHandler(source2));
				attach_part2.setFileName(MimeUtility.encodeText("첨부파일2.txt"));
				
				MimeMultipart mp = new MimeMultipart();
				mp.addBodyPart(html_body_part);
				mp.addBodyPart(attach_part);
				mp.addBodyPart(attach_part2);
				
				
				
				msg.setContent(mp);
				
				
				
				//collect(msg);
			} else {
				// If the desired charset is known, you can use
				// setText(text, charset)
				collect(msg);
			}

			msg.setHeader("X-Mailer", mailer);
			msg.setSentDate(new Date());

			// send the thing off
			/*
			 * The simple way to send a message is this:
			 *
			 * Transport.send(msg);
			 *
			 * But we're going to use some SMTP-specific features for
			 * demonstration purposes so we need to manage the Transport object
			 * explicitly.
			 */
			if(isSSL) {
				
			}else {
				
			}
			SMTPTransport t = (SMTPTransport) session.getTransport(prot); //그냥 SMTP서버 접속
			//SMTPSSLTransport t = new SMTPSSLTransport(session, null); //SMTP서버에 SSL로 접속
			
			try {
				if (auth) {
					t.connect(mailhost, mailhost_port, mailhost_user, mailhost_pw);
				}else{
					t.connect();
				}
				t.sendMessage(msg, msg.getAllRecipients());
			} finally {
				if (verbose) {
					System.out.println(t.getLastReturnCode());
					System.out.println(t.getLastServerResponse());
					System.out.println("Response: " + t.getLastServerResponse());
				}
				t.close();
			}

			System.out.println("\nMail was sent successfully.");

			/*
			 * Save a copy of the message, if requested.
			 */
			if (record_location != null) {
				// Get a Store object
				Store store = null;
				if (record_url != null) {
					URLName urln = new URLName(record_url);
					store = session.getStore(urln);
					store.connect();
				} else {
					if (record_protocol != null)
						store = session.getStore(record_protocol);
					else
						store = session.getStore();

					// Connect
					if (record_host != null || record_user != null || record_pw != null)
						store.connect(record_host, record_user, record_pw);
					else
						store.connect();
				}

				// Get record Folder. Create if it does not exist.
				Folder folder = store.getFolder(record_location);
				if (folder == null) {
					System.err.println("Can't get record folder.");
					System.exit(1);
				}
				if (!folder.exists())
					folder.create(Folder.HOLDS_MESSAGES);

				Message[] msgs = new Message[1];
				msgs[0] = msg;
				folder.appendMessages(msgs);

				System.out.println("Mail was recorded successfully.");
			}

		} catch (Exception e) {
			/*
			 * Handle SMTP-specific exceptions.
			 */
			if (e instanceof SendFailedException) {
				MessagingException sfe = (MessagingException) e;
				if (sfe instanceof SMTPSendFailedException) {
					SMTPSendFailedException ssfe = (SMTPSendFailedException) sfe;
					System.out.println("SMTP SEND FAILED:");
					if (verbose)
						System.out.println(ssfe.toString());
					System.out.println("  Command: " + ssfe.getCommand());
					System.out.println("  RetCode: " + ssfe.getReturnCode());
					System.out.println("  Response: " + ssfe.getMessage());
				} else {
					if (verbose)
						System.out.println("Send failed: " + sfe.toString());
				}
				Exception ne;
				while ((ne = sfe.getNextException()) != null && ne instanceof MessagingException) {
					sfe = (MessagingException) ne;
					if (sfe instanceof SMTPAddressFailedException) {
						SMTPAddressFailedException ssfe = (SMTPAddressFailedException) sfe;
						System.out.println("ADDRESS FAILED:");
						if (verbose)
							System.out.println(ssfe.toString());
						System.out.println("  Address: " + ssfe.getAddress());
						System.out.println("  Command: " + ssfe.getCommand());
						System.out.println("  RetCode: " + ssfe.getReturnCode());
						System.out.println("  Response: " + ssfe.getMessage());
					} else if (sfe instanceof SMTPAddressSucceededException) {
						System.out.println("ADDRESS SUCCEEDED:");
						SMTPAddressSucceededException ssfe = (SMTPAddressSucceededException) sfe;
						if (verbose)
							System.out.println(ssfe.toString());
						System.out.println("  Address: " + ssfe.getAddress());
						System.out.println("  Command: " + ssfe.getCommand());
						System.out.println("  RetCode: " + ssfe.getReturnCode());
						System.out.println("  Response: " + ssfe.getMessage());
					}
				}
			} else {
				System.out.println("Got Exception: " + e);
				if (verbose)
					e.printStackTrace();
			}
		}
	}

	public static void collect(Message msg)
			throws MessagingException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("<style type=\"text/css\">.contents .board01_write table {width:100%;}</style>");
		sb.append("<h1>" + "테스트1111" + "</h1>" + "<br></br>");
		sb.append("<a href='http://www.google.co.kr'>구글로 이동</a>");
		
		msg.setDataHandler(new DataHandler(
		new ByteArrayDataSource(sb.toString(), "text/html; charset=utf-8")));
	}
	
	//<html>부터 작성해도 메일에는 BODY안의 내용만 표시된다.
	public static void collect2(String subject,MimeBodyPart part)
			throws MessagingException, IOException {
		StringBuffer sb = new StringBuffer();
		//sb.append("<HTML>\n");
		//sb.append("<HEAD>\n");
		//sb.append("<TITLE>\n");
		//sb.append(subject + "\n");
		//sb.append("</TITLE>\n");
		//sb.append("</HEAD>\n");
		
		//sb.append("<BODY>\n");
		sb.append("<style type=\"text/css\">11111</style>");
		sb.append("<h1>" + subject + "</h1>" + "<br></br>");
		sb.append("<a href='http://www.google.co.kr'>구글로 이동</a>");
		
		//sb.append("</BODY>\n");
		//sb.append("</HTML>\n");
		
		part.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html; charset=utf-8")));
	}
}
