package com.pashenko.marketbackend.factories.dto;

import com.pashenko.marketbackend.dto.RoleDto;
import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.Role;
import com.pashenko.marketbackend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoFactory implements ExtendedDtoFactory<User, UserDto>{
    private final DtoFactory<Role, RoleDto> roleDtoFactory;
    @Override
    public UserDto getDto(User entity) {
        return preAssemble(entity).build();
    }

    @Override
    public UserDto getExtendedDto(User entity) {
        UserDto.UserDtoBuilder<?, ?> builder = preAssemble(entity);
        if(entity.getRoles() != null){
            builder.roles(
                    entity.getRoles().stream()
                            .map(roleDtoFactory::getDto)
                            .collect(Collectors.toSet())
            );
        }

        return builder.build();
    }

    private UserDto.UserDtoBuilder<?, ?> preAssemble(User entity){
        return UserDto.builder()
                .id(entity.getId())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .username(entity.getUsername());
    }
}
