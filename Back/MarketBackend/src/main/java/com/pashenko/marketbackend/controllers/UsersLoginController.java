package com.pashenko.marketbackend.controllers;

import com.pashenko.marketbackend.configs.security.jwt.JwtUtils;
import com.pashenko.marketbackend.dto.LoginRequest;
import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.User;
import com.pashenko.marketbackend.factories.dto.ExtendedDtoFactory;
import com.pashenko.marketbackend.factories.entities.EntityFactory;
import com.pashenko.marketbackend.services.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersLoginController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EntityFactory<User, UserDto> entityFactory;
    private final ExtendedDtoFactory<User, UserDto> dtoFactory;

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserDto dto){
        User newUser = entityFactory.getEntity(dto);
        User registered = userService.save(newUser);
        return dtoFactory.getDto(registered);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(dtoFactory.detExtendedDto(userDetails));
    }
}
