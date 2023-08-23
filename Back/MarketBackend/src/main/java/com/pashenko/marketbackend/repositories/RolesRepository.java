package com.pashenko.marketbackend.repositories;

import com.pashenko.marketbackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    Set<Role> getRolesByRole(String role);
}
