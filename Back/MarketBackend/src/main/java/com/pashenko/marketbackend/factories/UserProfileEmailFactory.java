package com.pashenko.marketbackend.factories;

import com.pashenko.marketbackend.dto.EmailMessage;
import com.pashenko.marketbackend.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class UserProfileEmailFactory {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${service.name}")
    private String serviceName;
    @Value("${service.email}")
    private String serviceEmail;
    @Value("${service.url}")
    private String serviceUrl;

    public EmailMessage buildSignupEmailForUser(User user){
        String confirmUrl = "%s%s/users/signup?token=%s"
                .formatted(serviceUrl, contextPath, user.getSignupToken().getTokenString());

        return EmailMessage.builder()
                .from(serviceEmail)
                .to(user.getUsername())
                .subject("%s account confirmation.".formatted(serviceName))
                .contentType(MediaType.MULTIPART_MIXED_VALUE)
                .body("""
                        <p>Hello %s! To confirm your account at %s click the link below</p>
                        <p>%s</p>
                        <p>If you didn't register at %s ignore this email.</p>
                        """.formatted(user.getUsername(), serviceName, confirmUrl, serviceName))
                .build();
    }
}
