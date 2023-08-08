package com.pashenko.marketbackend.services.userservice;


import com.pashenko.marketbackend.dto.AbstractDto;
import com.pashenko.marketbackend.dto.UserDto;
import com.pashenko.marketbackend.entities.Role;
import com.pashenko.marketbackend.entities.User;

import com.pashenko.marketbackend.repositories.RolesRepository;
import com.pashenko.marketbackend.repositories.UsersRepository;
import com.pashenko.marketbackend.services.AbstractService;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService extends AbstractService<User> implements UserDetailsService {
    private final RolesRepository rolesRepository;

    public UserService(JpaRepository<User, Long> repository, RolesRepository rolesRepository) {
        super(repository);
        this.rolesRepository = rolesRepository;
    }

    @Override
    public User save(User entity) {
        if(((UsersRepository)repository).existsUserByUsername(entity.getUsername())){
            throw new EntityExistsException("Given username is already exists.");
        }
        Role r = rolesRepository.getRoleByRole("ROLE_USER");
        entity.setRoles(Set.of(r));
        entity.setEnabled(true);        // TEMP (Confirm required)
        return super.save(entity);
    }

    @Override
    public <D extends AbstractDto> User update(D dto) {
        UserDto d = (UserDto) dto;
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ((UsersRepository)repository).findByUsername(username);
    }
}
