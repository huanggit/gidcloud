package com.cpsdna.gidCloud.web.service;

import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class MailService {
	private String host;
	private String user;
	private String pwd;
	private String from;
	
	@Autowired
	private VelocityEngine velocityEngine;

	private static final Logger logger = LogManager.getLogger(MailService.class
			.getName());

	public boolean send(final String to, final String subject, final String template,
			final Map<String, Object> hTemplateVariables) {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		session.setDebug(false);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart("related");
			BodyPart contentPart = new MimeBodyPart();
			String body = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, template, "UTF-8",hTemplateVariables);
			//contentPart.setText(body);
			contentPart.setContent(body, "text/html;charset=utf-8");  
			multipart.addBodyPart(contentPart);

			// 添加附件
			// BodyPart messageBodyPart = new MimeBodyPart();
			// DataSource source = new FileDataSource(affix);
			// messageBodyPart.setDataHandler(new DataHandler(source));
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			// messageBodyPart.setFileName("=?GBK?B?"+
			// enc.encode(affixName.getBytes()) + "?=");
			// multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, user, pwd);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			logger.error("send email error:{}", e.getMessage());
			return false;
		}
	}

	//setters
	public void setHost(String host) {
		this.host = host;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
}
