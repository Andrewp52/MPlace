package com.pashenko.marketbackend.userprofileactions;

import com.pashenko.marketbackend.AbstractSpringBootTest;
import com.pashenko.marketbackend.dto.EmailMessage;
import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.User;
import com.pashenko.marketbackend.repositories.SignupTokensRepository;
import com.pashenko.marketbackend.repositories.UsersRepository;
import com.pashenko.marketbackend.services.email.MailSender;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSignupProcedureTest extends AbstractSpringBootTest {

    private final UserDto signupDto = UserDto.builder().username("test@test.test").password("test").build();

    @MockBean
    public MailSender mailSender;
    @MockBean
    public UsersRepository usersRepository;

    @MockBean
    public SignupTokensRepository signupTokensRepository;
    @Captor
    ArgumentCaptor<EmailMessage> emailCaptor;
    User savedUser;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void initMocks(){
        savedUser = null;
        when(usersRepository.save(any(User.class))).then(invocationOnMock -> {
            User user = invocationOnMock.getArgument(0);
            user.setId(1L);
            savedUser = user;
            return user;
        });
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void WhenRequestSignupThen200AndUserDtoResponse(){
        ResponseEntity<UserDto> response = restTemplate.postForEntity(SIGNUP_URL, signupDto, UserDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(signupDto.getUsername(), responseBody.getUsername());
        assertNull(responseBody.getPassword());
    }

    @Test
    public void WhenSignupThenSendingConfirmationEmail(){
        restTemplate.postForEntity(SIGNUP_URL, signupDto, UserDto.class);
        verify(mailSender).send(emailCaptor.capture());
        EmailMessage message = emailCaptor.getValue();
        assertNotNull(message);
        assertEquals(signupDto.getUsername(), message.getTo());
        assertTrue(message.getSubject() != null && !message.getSubject().isBlank());
        assertTrue(message.getBody() != null && !message.getBody().isBlank());
    }

    @Test
    public void SigningUpUserHasTokenDisabledNoRoles(){
        restTemplate.postForEntity(SIGNUP_URL, signupDto, UserDto.class);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUsername());
        assertNotNull(savedUser.getPassword());
        assertNull(savedUser.getRoles());
        assertFalse(savedUser.getEnabled());
        assertNotNull(savedUser.getSignupToken());
        String token = savedUser.getSignupToken().getTokenString();
        assertTrue(token != null && !token.isBlank());
    }
}
