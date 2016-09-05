package com.ntr1x.treasure.web.services;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.Session;

import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.services.IMailService;
import com.ntr1x.treasure.web.services.MailService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreMailService extends MailService implements IStoreMailService {

    @Inject
    private MailSender mailSender;

	private final ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	public void sendSignupConfirmation(String email, Map<String, Object> params) {

//		submit(() -> {
//
////			sendMail(getSession(), email, templates.process(
////					portal.template(lang, MailTemplateDesc.Type.SIGNUP_CONFIRMATION.name()),
////					params
////			));
//
//			//TODO брать из списка шаблонов
//			sendMail(
//					session,
//					email,
//					new IMailService.MailTemplate(
//							"VSTORE.RU - Email Confirmation",
//							String.format(
//									"<html>\n" +
//									"<head>\n" +
//									"\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
//									"</head>\n" +
//									"<body>\n" +
//									"\t<h3>Вас приветствует команда <a href=\"http://vstore.ru\">VSTORE.RU</a></h3>\n" +
//									"\t<p>\n" +
//									"\t\tВы получили это письмо потому что ваш адрес электронной\n" +
//									"\t\tпочты был указан при регистрации на сайте\n" +
//									"\t\t<a href=\"http://vstore.ru\">http://vstore.ru</a>\n" +
//									"\t</p>\n" +
//									"\t<p>\n" +
//									"\t\tДля подтверждения регистрации пройдите по ссылке:\n" +
//
//									"\t\t\t<a href=\"http://vstore.ru/signup/confirm/%s\">http://vstore.ru/signup/confirm/%s</a>\n" +
//
//									"\t</p>\n" +
//									"\t<p>\n" +
//									"\t\tЕсли вы не регистрировались на сайте, просто\n" +
//									"\t\tпроигнорируйте данное сообщение. \n" +
//									"\t</p>\n" +
//									"</body>\n" +
//									"</html>",
//									params.get("key"),
//									params.get("key")
//							)
//					)
//			);
//		});
	}

	@Override
	public void sendPasswdInstructions(String email, Map<String, Object> params) {

//		submit(() -> {
//			sendMail(portal.getSession(), email, templates.process(
//					portal.template(lang, MailTemplateDesc.Type.PASSWD_INSTRUCTIONS.name()),
//					params
//			));
//		});
	}

	@FunctionalInterface
	private interface IMailRunnable {
		void run() throws Exception;
	}

	private void submit(IMailRunnable runnable) {

		executor.submit(() -> {

			try {

				runnable.run();

			} catch (Exception e) {

				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt();
				}

				if (log.isWarnEnabled()) {

					log.warn("Cannot send mail message", e);
				}
			}
		});
	}
}
