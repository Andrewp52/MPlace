package com.pashenko.marketbackend.services.email;

import com.pashenko.marketbackend.dto.EmailMessage;

public interface MailSender {
    void send(EmailMessage message);
}
