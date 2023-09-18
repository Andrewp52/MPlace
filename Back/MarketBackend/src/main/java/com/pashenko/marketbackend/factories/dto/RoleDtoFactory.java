package com.pashenko.marketbackend.factories.dto;

import com.pashenko.marketbackend.dto.RoleDto;
import com.pashenko.marketbackend.entities.userdata.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoFactory implements DtoFactory<Role, RoleDto> {
    @Override
    public RoleDto getDto(Role entity) {
        return RoleDto.builder().id(entity.getId()).role(entity.getRole()).build();
    }

}
