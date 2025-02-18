package com.example.employee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmployeeRegistrationEmail(String to, String username, String password) {
		String subject = "Welcome to Cahoot Technologies – Access Your Employee Portal";
		String body = "<h3>Dear Employee,</h3>" + "<p>You have been successfully registered in our system.</p>"
				+ "<p><strong>Login Credentials:</strong></p>" + "<p>Username: " + username + "</p>" + "<p>Password: abc123"
				 + " (You can change it after login)</p>"
				+ "<p><a href='http://localhost:8080/login'>Click here to login</a></p>"
				+ "<p>Best Regards,<br>Cahoot Techologies Team</p>";

		sendEmail(to, subject, body);
	}

	private void sendEmail(String to, String subject, String body) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true); // true for HTML content
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
