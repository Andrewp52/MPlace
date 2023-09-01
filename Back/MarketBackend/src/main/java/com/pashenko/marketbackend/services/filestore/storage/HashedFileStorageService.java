package com.pashenko.marketbackend.services.filestore.storage;

import com.pashenko.marketbackend.dto.StoredFileDto;
import com.pashenko.marketbackend.services.filestore.FileInfo;
import com.pashenko.marketbackend.services.filestore.data.FileStoreDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HashedFileStorageService implements FileStoreService {
    @Value("${filestore.root}")
    private String ROOT;
    @Value("${filestore.hashed}")
    private String HASHED_FILES;
    @Value("${filestore.temp}")
    private String TEMP_FILES;
    private final FileHashCalculator fileHashCalculator;
    private final FileStoreDataService fileStoreDataService;
    @Override
    public String store(String userName, String path, MultipartFile file) throws IOException {
        Path linkPath = Path.of(ROOT, userName, path, file.getOriginalFilename());
        if(Files.exists(linkPath)){
            throw new FileAlreadyExistsException("file %s/%s/%s is already exist".formatted(userName, path, file.getOriginalFilename()));
        }
        Path temp = storeTemp(file);
        storePermanentAndCreateLink(temp, linkPath, Objects.requireNonNull(file.getOriginalFilename()));
        return "%s/%s/%s".formatted(userName, path, file.getOriginalFilename());
    }

    @Override
    public InputStreamResource getAsStreamResource(String userName, String path, String filePath) throws IOException {
        Path p = Path.of(ROOT, userName, path, filePath);
        return new InputStreamResource(Files.newInputStream(p));
    }

    @Override
    public void delete(String userName, String filePath, String filename) throws IOException {
        Path symLink = Path.of(ROOT, userName, filePath, filename);
        if(!Files.exists(symLink)){
            throw new FileNotFoundException("File %s/%s/%s not found".formatted(userName, filePath, filename));
        }
        Path real = symLink.toRealPath();
        Files.delete(symLink);
        afterSymLinkDelete(real);
    }

    @Override
    public FileInfo getFileInfo(String user, String path, String file) throws IOException {
        Path filePath = Path.of(ROOT, user, path, file);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("File %s/%s/%s not found".formatted(user, path, file));
        }
        return new FileInfo(Files.size(filePath), Files.probeContentType(filePath));
    }

    private Path storeTemp(MultipartFile file) throws IOException {
        Path p = Files.createTempFile(Path.of(TEMP_FILES), UUID.randomUUID().toString(), file.getOriginalFilename());
        file.transferTo(p);
        return p;
    }

    void storePermanentAndCreateLink(Path temp, Path linkPath, String fileName) throws IOException {
        String hash = this.fileHashCalculator.calculateHash(temp);
        Path hashedFile = Path.of(HASHED_FILES, hash + getExtension(fileName));
        if(!Files.exists(hashedFile)){
            Files.move(temp, hashedFile, StandardCopyOption.ATOMIC_MOVE);
            fileStoreDataService.saveNewName(hashedFile.getFileName().toString());
        } else {
            Files.delete(temp);
            fileStoreDataService.increaseCounterFor(hashedFile.getFileName().toString());
        }
        Files.createSymbolicLink(linkPath, hashedFile);
    }

    @Async
    void afterSymLinkDelete(Path hashed) throws IOException {
        String hashedName = hashed.getFileName().toString();
        StoredFileDto sf = fileStoreDataService.getFileInfo(hashedName);
        if(sf.getLinksCount() > 1){
            fileStoreDataService.decreaseCounter(hashedName);
        } else {
            Files.delete(hashed);
            fileStoreDataService.deleteByName(sf.getName());
        }
    }
    private String getExtension(String fileName){
        return !fileName.contains(".") ? "" :
                fileName.substring(fileName.lastIndexOf(".") - 1);
    }
}
