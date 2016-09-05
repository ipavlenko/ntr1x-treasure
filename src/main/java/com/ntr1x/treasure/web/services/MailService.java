package com.ntr1x.treasure.web.services;

import java.text.MessageFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.ntr1x.treasure.web.AppException;

public abstract class MailService  implements IMailService {

	private static final String MIME_SUBTYPE = "related"; //$NON-NLS-1$

	private static final String CONTENT_TYPE_HTML = "text/html; charset=utf-8"; //$NON-NLS-1$

	private static final String HEADER_CONTENT_TYPE = "Content-Type"; //$NON-NLS-1$
	private static final String HEADER_CONTENT_ID = "Content-ID"; //$NON-NLS-1$

	private static final String CONTENT_ID_PATTERN = "<{0}>"; //$NON-NLS-1$


	@Override
	public void sendMail(Session session, String email, MailTemplate template) throws AppException {

		try {

			MimeMessage message = new MimeMessage(session);
			message.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_HTML);
			message.addRecipient(RecipientType.TO, new InternetAddress(email));
			message.setSubject(template.subject, "utf-8");

			message.setFrom(new InternetAddress(session.getProperty("mail.smtp.from")));
			message.setSentDate(new Date());

			MimeMultipart multipart = new MimeMultipart(MIME_SUBTYPE);
			BodyPart messageBodyPart = new MimeBodyPart();

//			String content = template.content.call();
			String content = template.content;
			messageBodyPart.setContent(content, CONTENT_TYPE_HTML);
			multipart.addBodyPart(messageBodyPart);

			// Add necessary attachments to the message
			for (MailAttachment attachment : template.attachments) {

				messageBodyPart = new MimeBodyPart();
				DataSource fds = attachment.resource;
				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setHeader(HEADER_CONTENT_ID, MessageFormat.format(CONTENT_ID_PATTERN, attachment.name));
				messageBodyPart.setHeader(HEADER_CONTENT_TYPE, attachment.type);
				messageBodyPart.setFileName(attachment.name);
				messageBodyPart.setDisposition(attachment.disposition);
				multipart.addBodyPart(messageBodyPart);
			}

			message.setContent(multipart, CONTENT_TYPE_HTML);
			Transport.send(message);

		} catch (AddressException e) {
			throw new AppException(e);
		} catch (MessagingException e) {
			throw new AppException(e);
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new AppException(e);
		}
	}
}
