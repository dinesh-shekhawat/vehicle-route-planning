package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;
import edu.northeastern.csye6220.vehiclerouteplanning.properties.EmailProperties;
import edu.northeastern.csye6220.vehiclerouteplanning.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static final String OTP_SUBJECT = "OTP for login";
	
	private final EmailProperties emailProperties;
	
	@Autowired
	public EmailServiceImpl(EmailProperties emailProperties) {
		this.emailProperties = emailProperties;
		LOGGER.info("loaded emailProperties: {}", emailProperties);
	}
	
	@Override
	public void sendOtpMessage(Otp otp, User user) {
		LOGGER.trace("sending otp: {}, user: {}", otp, user);
		
		String toEmail = user.getEmail();
		String subject = OTP_SUBJECT;
		String body = String.format("Your OTP to login is %s", otp.getField());
		
		sendEmail(toEmail, subject, body);
	}

	@Override
	public void sendEmail(String toEmail, String subject, String body) {
		LOGGER.info("sending toEmail: {}, subject: {}, body: {}", toEmail, subject, body);
		
		// SMTP can sometimes cause issue, we don't want to propagate the exception in that case
		try {
			Properties properties = new Properties();
			properties.put(emailProperties.getSmtpHostPropertyKey(), emailProperties.getSmtpHost());
			properties.put(emailProperties.getSmtpPortPropertyKey(), emailProperties.getSmtpPort());
			
			Session session = Session.getInstance(properties);
			
			Message message = new MimeMessage(session);
	        message.setFrom(
	        		new InternetAddress(
	        				emailProperties.getFromEmailAddress(), 
	        				emailProperties.getFromEmailUsername()));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	        // One copy to a backup mail
	        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailProperties.getFromEmailAddress()));
	        message.setSubject(subject);
	        message.setText(body);
	        Transport.send(message);
		} catch (Exception e) {
			LOGGER.error("Excpetion in sendEmail: {}", e.getMessage(), e);
		}
	}

}
