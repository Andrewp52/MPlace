package com.pashenko.marketbackend.tasks;

import com.pashenko.marketbackend.services.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


/**
 * SCHEDULED TASK
 * Wipes all newly registered accounts that haven't been activated
 * Activation token timeout and task rate is in application.yml:
 *      signup.token-timeout-mins
 *      signup.wipe-interval
 */
@Component
@RequiredArgsConstructor
public class NotActivatedUsersAndSignupTokensWipe {
private static final Logger logger = LoggerFactory.getLogger(NotActivatedUsersAndSignupTokensWipe.class);
    @Value("${signup.token-timeout-mins}")
    private int tokenTimeout;
    private final UserService userService;


    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRateString = "${signup.wipe-interval}")
    public void wipeNotActivatedAccounts(){
        logger.info("Scheduled unactivated account wipe started");
        userService.wipeUnactivated(LocalDateTime.now().minus(tokenTimeout, ChronoUnit.MINUTES));
        logger.info("Scheduled unactivated account wipe completed");
    }
}
