package com.pashenko.marketbackend.userprofileactions;

import com.pashenko.marketbackend.dto.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * Primary authentication test with existing user (no mocks)
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PrimaryAuthenticationTest {
    private final String LOGIN_URL = "/users/signin";
    private final String TOKEN_PREFIX = "apiToken";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    private final LoginRequest correct = new LoginRequest("user", "user");
    private final LoginRequest incorrect = new LoginRequest("user", "u");


    @Test
    public void authRequestWithCorrectCredsWillSucceedWith200() {
        ResponseEntity<String> result = restTemplate
                .postForEntity(LOGIN_URL, correct, String.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void authRequestWithCorrectCredsWillSetCookieWithJwt() {
        ResponseEntity<String> result = restTemplate
                .postForEntity(LOGIN_URL, correct, String.class);
        List<String> cookies = result.getHeaders().get("Set-Cookie");
        Assertions.assertNotNull(cookies);
        Assertions.assertTrue(cookies.stream().anyMatch(s -> s.contains(TOKEN_PREFIX)));
    }

    @Test
    public void authRequestWithIncorrectCredsWillFailedWith403() {
        ResponseEntity<String> result = restTemplate
                .postForEntity(LOGIN_URL, incorrect, String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }


}
