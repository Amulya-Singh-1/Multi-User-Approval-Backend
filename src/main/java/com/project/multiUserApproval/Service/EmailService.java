package com.project.multiUserApproval.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendEmail(String to, String subject, String text) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setTo(to);
      simpleMailMessage.setSubject(subject);
      simpleMailMessage.setText(text);
      mailSender.send(simpleMailMessage);
//      MimeMessage message = mailSender.createMimeMessage();
//      MimeMessageHelper helper = new MimeMessageHelper(message, true);
//      helper.setTo(to);
//      helper.setSubject(subject);
//      helper.setText(text, true);
//      mailSender.send(message);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}

