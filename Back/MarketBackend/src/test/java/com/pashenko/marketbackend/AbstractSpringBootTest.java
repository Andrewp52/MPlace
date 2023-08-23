package com.pashenko.marketbackend;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractSpringBootTest {
    protected final String SIGNUP_URL = "/users/signup";
    protected final String LOGIN_URL = "/users/signin";

}
