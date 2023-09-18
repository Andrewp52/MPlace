package com.pashenko.marketbackend.services.userservice;

import com.pashenko.marketbackend.entities.userdata.SignupToken;
import com.pashenko.marketbackend.repositories.SignupTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignupTokenService {
    private final SignupTokensRepository repository;

    public void deleteTokenById(Long id){
        repository.deleteById(id);
    }

    public List<SignupToken> findTokensOlderThan(LocalDateTime dateTime){
        return repository.findByCreatedBefore(dateTime);
    }

}
