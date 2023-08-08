package com.pashenko.marketbackend.factories.entities;

import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserEntityFactory implements EntityFactory<User, UserDto> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getEntity(UserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }
}
