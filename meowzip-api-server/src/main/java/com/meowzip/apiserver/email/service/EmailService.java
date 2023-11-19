package com.meowzip.apiserver.email.service;

import com.meowzip.member.entity.Member;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void send(Member receiver, String subject, String contents) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();

        try {
            String email = receiver.getEmail();
            log.info("member's email: {}", email);

            mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mailMessage.setFrom("meowzip");
            mailMessage.setSubject(subject);
            mailMessage.setContent(contents, "text/html;charset=euc-kr");

        } catch (MessagingException e) {
            log.error("MessagingException: {}", e.getMessage());
        }

        javaMailSender.send(mailMessage);
    }
}
