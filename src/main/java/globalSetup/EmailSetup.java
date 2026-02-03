package globalSetup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailSetup {
	public static void sendTestReportEmail() {
		final String username = GeneralMethods.getEnvData("SMTP_USERNAME");
		final String appPassword = GeneralMethods.getEnvData("SMTP_PASSWORD");
		final String[] recipients = { "recepient" };

		// Email content
		String subject = GeneralMethods.getEnvData("EMAIL_SUBJECT");
		String body = GeneralMethods.getEnvData("EMAIL_BODY");

		// Files to attach
		String projectPath = System.getProperty("user.dir");
		File pdfReport = new File(projectPath + GeneralMethods.getEnvData("PDF_Report_Path"));
		File htmlReport = new File(projectPath + GeneralMethods.getEnvData("Spark_Report_Path"));

		// Set properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		// Create session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, appPassword);
			}
		});

		try {
			// Compose the message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				recipientAddresses[i] = new InternetAddress(recipients[i]);
			}
			message.setRecipients(Message.RecipientType.TO, recipientAddresses);
			message.setSubject(subject);

			// Email body + attachments
			Multipart multipart = new MimeMultipart();

			// Text part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(body);
			multipart.addBodyPart(textBodyPart);

			// PDF attachment
			if (pdfReport.exists()) {
				MimeBodyPart pdfAttachment = new MimeBodyPart();
				pdfAttachment.attachFile(pdfReport);
				multipart.addBodyPart(pdfAttachment);
			}

			// HTML attachment
			if (htmlReport.exists()) {
				MimeBodyPart htmlAttachment = new MimeBodyPart();
				htmlAttachment.attachFile(htmlReport);
				multipart.addBodyPart(htmlAttachment);
			}
			message.setContent(multipart);

			// Send
			Transport.send(message);
			System.out.println("Email sent successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to send email: " + e.getMessage());
		}
	}

	// Extracts HTML body from multipart or direct HTML email
	private static String extractHtmlFromMessage(Message message) throws Exception {
		if (message.isMimeType("text/html")) {
			return (String) message.getContent();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("text/html")) {
					return (String) part.getContent();
				}
			}
		}
		return null;
	}

	public String ExtractLinkFromEmail(String emailSubject) {
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("EMAIL_USERNAME");
		String password = dotenv.get("EMAIL_PASSWORD");
		String href = null;

		String host = "imap.gmail.com";

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		try {
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imap");
			store.connect(host, username, password);

			System.out.println("Connection to email established");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE); // ✅ READ_WRITE mode for deletion

			Message[] messages = inbox.getMessages();

			boolean found = false;

			for (int i = messages.length - 1; i >= 0; i--) {
				Message msg = messages[i];
				String subject = msg.getSubject();
				if (subject != null && subject.contains(emailSubject)) {
					System.out.println("Found matching email!");
					System.out.println("Subject: " + subject);

					String htmlBody = extractHtmlFromMessage(msg);
					if (htmlBody != null) {
						Document doc = Jsoup.parse(htmlBody);
						Element link = doc.selectFirst("a[href]");

						if (link != null) {
							href = link.attr("href");
							System.out.println("link found: " + href);
						} else {
							System.out.println("❌ No <a href> found in the HTML content.");
						}
					} else {
						System.out.println("❌ No HTML content found.");
					}

					// ✅ Mark email as deleted
					msg.setFlag(Flags.Flag.DELETED, true);

					found = true;
					break;
				}
			}

			if (!found) {
				System.out.println("❌ No email with subject containing: " + emailSubject);
			}

			inbox.close(true); // ✅ expunge = true → deletes flagged emails
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return href;
	}

	public String ExtractOTPFromEmail(String emailSubject) {
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("EMAIL_USERNAME");
		String password = dotenv.get("EMAIL_PASSWORD");
		String OTP = null;

		String host = "imap.gmail.com";

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		try {
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imap");
			store.connect(host, username, password);

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE); // ✅ READ_WRITE mode

			Message[] messages = inbox.getMessages();

			boolean found = false;

			for (int i = messages.length - 1; i >= 0; i--) {
				Message msg = messages[i];
				String subject = msg.getSubject();
				if (subject != null && subject.contains(emailSubject)) {
					System.out.println("Found OTP email!");

					OTP = subject.replaceAll("\\D+", "");
					System.out.println("OTP: " + OTP);

					// ✅ Mark email as deleted
					msg.setFlag(Flags.Flag.DELETED, true);

					found = true;
					break;
				}
			}

			if (!found) {
				System.out.println("❌ No email with OTP found!");
			}

			inbox.close(true); // ✅ expunge deletes it
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return OTP;
	}

	public String ExtractAttachmentFromEmail(String emailSubject) {
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("EMAIL_USERNAME");
		String password = dotenv.get("EMAIL_PASSWORD");
		String savedFilePath = null;

		String host = "imap.gmail.com";

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		try {
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imap");
			store.connect(host, username, password);

			System.out.println("Connection to email established");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Message[] messages = inbox.getMessages();
			boolean found = false;

			for (int i = messages.length - 1; i >= 0; i--) {
				Message msg = messages[i];
				String subject = msg.getSubject();

				if (subject != null && subject.contains(emailSubject)) {
					System.out.println("Found matching email with attachment!");
					System.out.println("Subject: " + subject);

					if (msg.isMimeType("multipart/*")) {
						Multipart multipart = (Multipart) msg.getContent();

						for (int j = 0; j < multipart.getCount(); j++) {
							BodyPart part = multipart.getBodyPart(j);

							String disposition = part.getDisposition();
							if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT)
									|| disposition.equalsIgnoreCase(Part.INLINE))) {

								String fileName = part.getFileName();
								File file = new File(System.getProperty("user.dir") + "/Downloads/" + fileName);

								try (InputStream is = part.getInputStream();
										FileOutputStream fos = new FileOutputStream(file)) {
									byte[] buffer = new byte[4096];
									int bytesRead;
									while ((bytesRead = is.read(buffer)) != -1) {
										fos.write(buffer, 0, bytesRead);
									}
								}

								savedFilePath = file.getAbsolutePath();
								System.out.println("✅ File downloaded: " + savedFilePath);

								// delete email only if successfully saved
								msg.setFlag(Flags.Flag.DELETED, true);

								found = true;
								break; // stop after first attachment
							}
						}
					} else {
						System.out.println("❌ No attachments found in this email.");
					}

					break; // stop after first matching subject
				}
			}

			if (!found) {
				System.out.println("❌ No email with subject containing: " + emailSubject);
			}

			inbox.close(true); // expunge deleted mails
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedFilePath; // return saved file path if successful
	}

	public String ClickYesInEmail(String emailSubject) {
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("EMAIL_USERNAME");
		String password = dotenv.get("EMAIL_PASSWORD");
		String href = null;

		String host = "imap.gmail.com";

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		try {
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imap");
			store.connect(host, username, password);

			System.out.println("Connection to email established");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE); // ✅ READ_WRITE mode for deletion

			Message[] messages = inbox.getMessages();

			boolean found = false;

			for (int i = messages.length - 1; i >= 0; i--) {
				Message msg = messages[i];
				String subject = msg.getSubject();
				if (subject != null && subject.contains(emailSubject)) {
					System.out.println("Found matching email!");
					System.out.println("Subject: " + subject);

					String htmlBody = extractHtmlFromMessage(msg);
					if (htmlBody != null) {
						Document doc = Jsoup.parse(htmlBody);
						Element link = doc.selectFirst("a:contains(Yes)");

						if (link != null) {
							href = link.attr("href");
							System.out.println("link found: " + href);
						} else {
							System.out.println("❌ No <a href> found in the HTML content.");
						}
					} else {
						System.out.println("❌ No HTML content found.");
					}

					// ✅ Mark email as deleted
					msg.setFlag(Flags.Flag.DELETED, true);

					found = true;
					break;
				}
			}

			if (!found) {
				System.out.println("❌ No email with subject containing: " + emailSubject);
			}

			inbox.close(true); // ✅ expunge = true → deletes flagged emails
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return href;
	}

}
