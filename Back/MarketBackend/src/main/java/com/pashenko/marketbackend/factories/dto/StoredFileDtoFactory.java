package com.pashenko.marketbackend.factories.dto;

import com.pashenko.marketbackend.dto.StoredFileDto;
import com.pashenko.marketbackend.entities.StoredFile;
import org.springframework.stereotype.Component;

@Component
public class StoredFileDtoFactory implements DtoFactory<StoredFile, StoredFileDto>{
    @Override
    public StoredFileDto getDto(StoredFile entity) {
        return new StoredFileDto(entity.getName(), entity.getLinksCount());
    }
}
