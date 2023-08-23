package com.pashenko.marketbackend.services.email;

import com.pashenko.marketbackend.dto.EmailMessage;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;

/**
 * Mail sender implementation using JavaMail from starter.
 * Requires JavaMailSender bean configuration to autowire.
 */
@Component
public class JavaMailMailSender implements MailSender{

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void send(EmailMessage message) {
        try {
            MimeMessage preparedMessage = prepareMessage(message, this.mailSender.createMimeMessage());
            mailSender.send(preparedMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage prepareMessage(EmailMessage message, MimeMessage prepared) throws MessagingException {
        prepared.setFrom(message.getFrom());
        prepared.setRecipient(Message.RecipientType.TO, new InternetAddress(message.getTo()));
        prepared.setSubject(message.getSubject());
        prepared.setContent(prepareBody(message));
        prepared.addHeader(HttpHeaders.CONTENT_TYPE, message.getContentType());
        return prepared;
    }

    private Multipart prepareBody(EmailMessage message) throws MessagingException {
        Multipart body = new MimeMultipart();
        body.addBodyPart(prepareTextPart(message));
        prepareAttachmentsPart(body, message);
        return body;
    }

    private BodyPart prepareTextPart(EmailMessage message) throws MessagingException {
        BodyPart textPart = new MimeBodyPart();
        textPart.setText(message.getBody());
        textPart.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        return textPart;
    }

    private void prepareAttachmentsPart(final Multipart body, EmailMessage message){
        if(message.getAttachments() != null){
            Arrays.stream(message.getAttachments()).forEach(file -> {
                try {
                    MimeBodyPart filePart = new MimeBodyPart(file.getInputStream());
                    filePart.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                    filePart.setFileName(filePart.getFileName());
                    body.addBodyPart(filePart);
                } catch (MessagingException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
