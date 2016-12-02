/*
 * javamail api + smpt서버를 이용하여 메일을 보내는 예제
 */
package test.javamail;

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.*;

public class smtpsend {
	public static void main(String[] argv) {
		String mail_from = null, mail_to = null, mail_cc = null, mail_bcc = null; //메일 발신자,메일수신자,CC(carbon copy),BCC(blind carbon copy)
		String mailhost = null, mailhost_user = null, mailhost_pw = null; //메일 호스트, ID, PW
		String record_protocol = null, record_host = null, record_user = null, record_pw = null, record_location = null, record_url = null; //메일저장 프로토콜, 메일저장 ID, 메일저장 PW, 메일저장 경로, 저장URL
		String prot = "smtp";
		String subject = null,contents = null; //제목, 내용
		String file = null;
		String mailer = "smtpsend";
		
		int mailhost_port = -999;
		boolean debug = false;
		boolean verbose = true;
		boolean auth = false;
		boolean isSSL = false; //SMTP접속시 SSL접속여부
		
		//값 설정
		mail_from = "alkain77@naver.com";
		mail_to = "alkain77@gmail.com";
		subject = "제목3";
		contents = "내용3";
		
		mailhost = "mail.sports.or.kr";
		mailhost_user = "rjob@sports.or.kr";
		mailhost_pw = "wkqdkf135!";
		auth = true;
		mailhost_port = 465;
		
		//file = "d:/popup.jpg"; //첨부파일경로 지정 첨부파일명이 한글인경우 구글에서 파일명이 없이 나오는 문제가 있음.
		file = "d:/개발임시문서.txt";
		
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
				// Attach the specified file.
				// We need a multipart message to hold the attachment.
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(contents);
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.attachFile(file);
				System.out.println(mbp2.getFileName());
				MimeMultipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				mp.addBodyPart(mbp2);
				msg.setContent(mp);
			} else {
				// If the desired charset is known, you can use
				// setText(text, charset)
				msg.setText(contents);
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

	/**
	 * Read the body of the message until EOF.
	 */
	public static String collect(BufferedReader in) throws IOException {
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
}
