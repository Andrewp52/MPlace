package com.pashenko.marketbackend.userprofileactions;

import com.pashenko.marketbackend.AbstractSpringBootTest;
import com.pashenko.marketbackend.dto.LoginRequest;
import com.pashenko.marketbackend.entities.Role;
import com.pashenko.marketbackend.entities.User;
import com.pashenko.marketbackend.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

/**
 * Primary authentication test
 */

public class PrimaryAuthenticationTest extends AbstractSpringBootTest {
    private final String TOKEN_PREFIX = "apiToken";
    private final String encryptedPass = "$2a$12$Hy5fBJCBG9jzBARjl2KdCOcHp4xvQ3IpTMI2ORTF2KgYQDyJ6GGXi";
    private final LoginRequest correct = new LoginRequest("user", "user");
    private final LoginRequest incorrect = new LoginRequest("user", "u");

    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    public UsersRepository usersRepository;
    @BeforeAll
    public static void setup(){
        MockitoAnnotations.openMocks(PrimaryAuthenticationTest.class);
    }

    @BeforeEach
    public void initMocks(){
        when(usersRepository.findByUsername(isA(String.class))).then(invocationOnMock -> {
            String username = invocationOnMock.getArgument(0);
            return !username.equals(correct.getUsername())? null : User.builder()
                    .username(username)
                    .password(encryptedPass)
                    .enabled(true)
                    .roles(Set.of(Role.builder().role("ROLE_USER").build()))
                    .id(1L)
                    .build();
        });
    }

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
