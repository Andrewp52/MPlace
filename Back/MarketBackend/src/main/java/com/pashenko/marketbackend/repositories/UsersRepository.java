package com.pashenko.marketbackend.repositories;

import com.pashenko.marketbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String userName);
    User findByUsername(String username);
}
