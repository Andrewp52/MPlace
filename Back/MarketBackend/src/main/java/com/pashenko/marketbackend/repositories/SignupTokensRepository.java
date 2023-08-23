package com.pashenko.marketbackend.repositories;

import com.pashenko.marketbackend.entities.SignupToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SignupTokensRepository extends JpaRepository<SignupToken, Long> {
    List<SignupToken> findByCreatedBefore(LocalDateTime dateTime);

}
