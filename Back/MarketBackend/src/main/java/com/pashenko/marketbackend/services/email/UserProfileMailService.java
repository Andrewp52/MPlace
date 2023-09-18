package com.pashenko.marketbackend.services.email;

import com.pashenko.marketbackend.dto.EmailMessage;
import com.pashenko.marketbackend.entities.userdata.User;
import com.pashenko.marketbackend.factories.UserProfileEmailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mailing service for user's profile actions.
 */
@Component
public class UserProfileMailService extends AbstractMailService {
    private final UserProfileEmailFactory emailFactory;

    @Autowired
    public UserProfileMailService(MailSender mailSender, UserProfileEmailFactory emailFactory) {
        super(mailSender);
        this.emailFactory = emailFactory;
    }

    public void sendAccountConfirmationEmail(User user){
        EmailMessage message = emailFactory.buildSignupEmailForUser(user);
        super.sendMail(message);
    }

}
