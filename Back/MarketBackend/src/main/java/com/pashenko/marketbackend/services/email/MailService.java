package com.pashenko.marketbackend.services.email;

import com.pashenko.marketbackend.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RequiredArgsConstructor
public abstract class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final MailSender mailSender;
    protected void sendMail(EmailMessage message){
        mailSender.send(message);
    }
}
