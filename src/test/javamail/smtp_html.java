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

public class smtp_html {
	public static void main(String[] argv) {
		String mail_from = null, mail_to = null, mail_cc = null, mail_bcc = null; //메일 발신자,메일수신자,CC(carbon copy),BCC(blind carbon copy)
		String mailhost = null, mailhost_user = null, mailhost_pw = null; //메일 호스트, ID, PW
		String record_protocol = null, record_host = null, record_user = null, record_pw = null, record_location = null, record_url = null; //메일저장 프로토콜, 메일저장 ID, 메일저장 PW, 메일저장 경로, 저장URL
		String prot = "smtp";
		String subject = null,contents = null; //제목, 내용
		String file = null;
		String mailer = "sendhtml";
		
		int mailhost_port = -999;
		boolean debug = false;
		boolean verbose = true;
		boolean auth = false;
		boolean isSSL = false; //SMTP접속시 SSL접속여부
		
		//값 설정
		mail_from = "admin@virtualdream.co.kr";
		mail_to = "alkain77@gmail.com";
		mail_bcc = "alkain77@naver.com";
		subject = "제목 - html";
		contents = "내용5";
		
		mailhost = "mail.sports.or.kr";
		mailhost_user = "rjob@sports.or.kr";
		mailhost_pw = "wkqdkf135!";
		auth = true;
		mailhost_port = 465;
		//mailhost_port = 6000; //잘못된 Port
		
		//file = "d:/popup.jpg"; //첨부파일경로 지정 첨부파일명이 한글인경우 구글에서 파일명이 없이 나오는 문제가 있음.
		file = "d:/스티커 메모 백업.txt";
		
		try {
			/*
			 * Initialize the JavaMail Session.
			 */
			Properties props = System.getProperties();
			if (mailhost != null) {
				props.put("mail." + prot + ".host", mailhost);
			}
				
			if (auth) {
				props.put("mail." + prot + ".auth", "true");
			}

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
			Message msg = new MimeMessage(session);
			if (mail_from != null) {
				msg.setFrom(new InternetAddress(mail_from));
			}else {
				msg.setFrom();
			}

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail_to, false));
			if (mail_cc != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail_cc, false));
			}
				
			if (mail_bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail_bcc, false));
			}

			msg.setSubject(subject);

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
			//SMTPTransport t = (SMTPTransport) session.getTransport(prot); //그냥 SMTP서버 접속
			SMTPSSLTransport t = new SMTPSSLTransport(session, null); //SMTP서버에 SSL로 접속
			
			try {
				if (auth) {
					t.connect(mailhost, mailhost_port, mailhost_user, mailhost_pw);
				}else{
					t.connect();
				}
				t.sendMessage(msg, msg.getAllRecipients());
			} finally {
				if (verbose) {
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
		String line;
		String subject = msg.getSubject();
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(subject + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
		
		sb.append("<BODY>\n");
		sb.append("<H1>" + subject + "</H1>" + "\n");
		
		sb.append("<a href='www.google.co.kr'>구글로 이동</a>");
		
		sb.append("</BODY>\n");
		sb.append("</HTML>\n");
		
		msg.setDataHandler(new DataHandler(
		new ByteArrayDataSource(sb.toString(), "text/html")));
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
		sb.append("<h1>" + subject + "</h1>" + "<br></br>");
		
		sb.append("<a href='http://www.google.co.kr'>구글로 이동</a>");
		
		//sb.append("</BODY>\n");
		//sb.append("</HTML>\n");
		
		part.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
	}
}
