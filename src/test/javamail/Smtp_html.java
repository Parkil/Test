/*
 * javamail api + smpt������ �̿��Ͽ� ������ ������ ����(������ HTML�� ����)
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
		//���� �⺻����
		String mail_from = null;
		String mail_to = null;
		String mail_cc = null;	//CC(carbon copy)
		String mail_bcc = null; //BCC(blind carbon copy)
		String subject = null; //����
		String contents = null; //����
		
		//SMTP���� ����
		String mailhost = null; //SMTP ���� ������
		String mailhost_user = null; //SMTP ���� ID
		String mailhost_pw = null; //SMTP ���� PW
		int mailhost_port = -999; //SMPT ��Ʈ
		boolean auth = true; //SMTP ���ӽ� �����ʿ俩��
		boolean isSSL = false; //SMTP���ӽ� SSL���ӿ���
		
		//���� ������ ����ϴ� ����
		String record_protocol = null; 
		String record_host = null; 
		String record_user = null;
		String record_pw = null;
		String record_location = null;
		String record_url = null;
		
		//��Ÿ ���� ����
		String prot = "smtp";
		String mailer = "sendhtml";
		
		//javamail option
		boolean debug = true;
		boolean verbose = true;
		
		//========================���� �⺻�� ����========================
		mail_from = "info@sk-i.co.kr";
		mail_to = "alkain77@naver.com";
		mail_cc = "";
		subject = "����";
		contents = "����";
		
		//========================SMTP ���� ����========================
		//mailhost = "mw-002.cafe24.com";
		mailhost = "smtp.cafe24.com";
		mailhost_user = "mail@pkw100.cafe24.com";
		mailhost_pw = "pack7601";
		auth = true;
		isSSL = false;
		mailhost_port = 587;
		
		//========================÷������==============================
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
				collect2("����222",html_body_part);
				
				
				/*
				�ܼ��� ����̱�� ������ ���ϸ��� �ѱ��� ��� ������ ������ �߻��Ѵ�.
				MimeBodyPart attach_part = new MimeBodyPart();
				attach_part.attachFile(file);
				*/
				
				/*
				 * ���ϸ��� �ѱ��� ��� ÷�����ϸ��� ������ ������ �����ϱ� ���ؼ� 
				 * ����������ü�� javax.activation ���̺귯���� �̿��Ͽ� �����ϰ� 
				 * ���ϸ��� MimeUtility�� �̿��Ͽ� �����ϴ� ���·� ó���Ѵ�.
				 * activation-1.1.1.redhat-4.jar �ʿ�
				 * ���� ������ �ݺ��ϸ� �������� ÷�������� ����ϴ°͵� ������
				 */
				MimeBodyPart attach_part = new MimeBodyPart();
				javax.activation.DataSource source = new javax.activation.FileDataSource(file);
				attach_part.setDataHandler(new javax.activation.DataHandler(source));
				attach_part.setFileName(MimeUtility.encodeText("÷������1.txt"));
				
				MimeBodyPart attach_part2 = new MimeBodyPart();
				javax.activation.DataSource source2 = new javax.activation.FileDataSource("d:/�ڵ����� �α�.txt");
				attach_part2.setDataHandler(new javax.activation.DataHandler(source2));
				attach_part2.setFileName(MimeUtility.encodeText("÷������2.txt"));
				
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
			SMTPTransport t = (SMTPTransport) session.getTransport(prot); //�׳� SMTP���� ����
			//SMTPSSLTransport t = new SMTPSSLTransport(session, null); //SMTP������ SSL�� ����
			
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
		sb.append("<h1>" + "�׽�Ʈ1111" + "</h1>" + "<br></br>");
		sb.append("<a href='http://www.google.co.kr'>���۷� �̵�</a>");
		
		msg.setDataHandler(new DataHandler(
		new ByteArrayDataSource(sb.toString(), "text/html; charset=utf-8")));
	}
	
	//<html>���� �ۼ��ص� ���Ͽ��� BODY���� ���븸 ǥ�õȴ�.
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
		sb.append("<a href='http://www.google.co.kr'>���۷� �̵�</a>");
		
		//sb.append("</BODY>\n");
		//sb.append("</HTML>\n");
		
		part.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html; charset=utf-8")));
	}
}
