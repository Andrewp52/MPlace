package com.pashenko.marketbackend.services.filestore.data;

import com.pashenko.marketbackend.dto.StoredFileDto;
import org.springframework.transaction.annotation.Transactional;

public interface FileStoreDataService {
    @Transactional
    void increaseCounterFor(String name);

    @Transactional
    void decreaseCounter(String name);

    StoredFileDto getFileInfo(String name);

    StoredFileDto saveNewName(String name);

    void deleteByName(String name);
}
