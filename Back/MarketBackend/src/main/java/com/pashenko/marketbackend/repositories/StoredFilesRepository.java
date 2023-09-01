package com.pashenko.marketbackend.repositories;

import com.pashenko.marketbackend.entities.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredFilesRepository extends JpaRepository<StoredFile, String> {
    @Modifying
    void increaseLinksCount(String fileName);
    @Modifying
    void decreaseLinksCount(String fileName);
}
