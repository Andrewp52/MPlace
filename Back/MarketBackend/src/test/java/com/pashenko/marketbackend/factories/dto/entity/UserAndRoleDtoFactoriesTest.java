package com.pashenko.marketbackend.factories.dto.entity;

import com.pashenko.marketbackend.dto.RoleDto;
import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.userdata.Role;
import com.pashenko.marketbackend.entities.userdata.User;
import com.pashenko.marketbackend.factories.dto.RoleDtoFactory;
import com.pashenko.marketbackend.factories.dto.UserDtoFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Set;

public class UserAndRoleDtoFactoriesTest {
    private final RoleDtoFactory roleDtoFactory = new RoleDtoFactory();
    private final UserDtoFactory userDtoFactory = new UserDtoFactory(roleDtoFactory);
    private static final Role roleEntity = new Role();
    private static final User userEntity = new User();

    @BeforeAll
    public static void initEntities(){
        roleEntity.setRole("USER");
        roleEntity.setId(1L);
        roleEntity.setCreated(LocalDateTime.now());
        roleEntity.setModified(LocalDateTime.now());

        userEntity.setEnabled(true);
        userEntity.setUsername("test");
        userEntity.setPassword("test");
        userEntity.setId(1L);
        userEntity.setRoles(Set.of(roleEntity));
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setModified(LocalDateTime.now());
    }

    @Test
    public void roleDtoFactoryReturnsCorrectSimpleDto(){
        RoleDto dto = roleDtoFactory.getDto(roleEntity);
        assertEquals(roleEntity.getRole(), dto.getRole());
        assertEquals(roleEntity.getId(), dto.getId());
        assertNull(dto.getCreated());
        assertNull(dto.getModified());
    }

    @Test
    public void userDtoFactoryReturnsCorrectSimpleDto(){
        UserDto dto = userDtoFactory.getDto(userEntity);
        assertEquals(userEntity.getId(), dto.getId());
        assertEquals(userEntity.getUsername(), dto.getUsername());
        assertEquals(userEntity.getCreated(), dto.getCreated());
        assertEquals(userEntity.getModified(), dto.getModified());
        assertNull(dto.getPassword());
    }

    @Test
    public void userDtoFactoryReturnsCorrectExtendedDto(){
        UserDto dto = userDtoFactory.getExtendedDto(userEntity);
        assertNotNull(dto.getRoles());
        assertEquals(1, dto.getRoles().size());
    }
}
