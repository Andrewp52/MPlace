package com.pashenko.marketbackend.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class UserDto extends AbstractDto{
    private String username;
    private String password;
    private Set<RoleDto> roles;
}
