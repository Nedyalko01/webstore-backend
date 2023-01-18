package com.example.webstore.backend.service;

import com.example.webstore.backend.exception.EmailFailureException;
import com.example.webstore.backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

@Service
public class EmailService {

    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String url;

    private static final String VERIFY_MAIL_MESSAGE = "Verify your email to activate your account.";
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage makeMailMessage() {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromAddress);

        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {

        SimpleMailMessage message = makeMailMessage();

        message.setTo(verificationToken.getLocalUser().getEmail());
        message.setSubject(VERIFY_MAIL_MESSAGE);
        message.setText("Please follow the link bellow to verify your email to activate your account.\n" + url +
                "/auth/verify?token=" + verificationToken.getToken());

        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
             throw new EmailFailureException();
        }
    }
}