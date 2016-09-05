package com.ntr1x.treasure.web.services;

import java.util.Collection;
import java.util.LinkedList;

import javax.activation.DataSource;
import javax.mail.Session;
import javax.mail.internet.MimeMultipart;

import com.ntr1x.treasure.web.AppException;

import lombok.Data;

public interface IMailService {

	@Data
	class MailAttachment {

		public final String name;
		public final String type;
		public final String disposition;
		public final DataSource resource;
	}

	@Data
	class MailTemplate {

		//		public final String name;
		public final String subject;
		public final String content;

		public final Collection<MailAttachment> attachments = new LinkedList<>();

		public MailTemplate with(MailAttachment attachment) {
			attachments.add(attachment);
			return this;
		}
	}

	@FunctionalInterface
	interface MultipartFactory {
		MimeMultipart create() throws Exception;
	}

	void sendMail(Session session, String email, MailTemplate template) throws AppException;
}
