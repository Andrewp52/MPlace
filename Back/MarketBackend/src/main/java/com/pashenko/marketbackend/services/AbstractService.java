package com.pashenko.marketbackend.services;

import com.pashenko.marketbackend.dto.AbstractDto;
import com.pashenko.marketbackend.entities.AbstractEntity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
public abstract class AbstractService <E extends AbstractEntity>{
    protected final JpaRepository<E, Long> repository;

    public E save(E entity){
        if(entity.getId() != null){
            throw new EntityExistsException("New entity ID must be null!");
        }
        return repository.save(entity);
    }

    public E getById(Long id){
        return repository.findById(id).orElseThrow();
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public abstract <D extends AbstractDto> E update(D dto);


}
