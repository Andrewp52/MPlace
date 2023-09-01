package com.pashenko.marketbackend.services.filestore.data;

import com.pashenko.marketbackend.dto.StoredFileDto;
import com.pashenko.marketbackend.entities.StoredFile;
import com.pashenko.marketbackend.factories.dto.DtoFactory;
import com.pashenko.marketbackend.repositories.StoredFilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class FileStoreDatabaseDataService implements FileStoreDataService {

    private final StoredFilesRepository repository;
    private final DtoFactory<StoredFile, StoredFileDto> dtoFactory;

    @Override
    @Transactional
    public void increaseCounterFor(String name){
        repository.increaseLinksCount(name);
    }

    @Override
    @Transactional
    public void decreaseCounter(String name){
        repository.decreaseLinksCount(name);
    }

    @Override
    public StoredFileDto getFileInfo(String name){
        AtomicReference<StoredFileDto> dto = new AtomicReference<>();
        repository.findById(name).ifPresent(storedFile -> dto.set(dtoFactory.getDto(storedFile)));
        return dto.get();
    }

    @Override
    public StoredFileDto saveNewName(String name){
        StoredFile sf = new StoredFile();
        sf.setLinksCount(1);
        sf.setName(name);
        return dtoFactory.getDto(repository.save(sf));
    }

    @Override
    public void deleteByName(String name){
        repository.deleteById(name);
    }

    public void deleteStoredFile(StoredFile storedFile){
        repository.delete(storedFile);
    }
}
