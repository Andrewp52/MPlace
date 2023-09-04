package com.pashenko.marketbackend.services.filestore.storage;

import com.pashenko.marketbackend.services.filestore.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class HashedFileStorageService implements FileStoreService {
    private static final int COUNTER_TYPE_LENGTH = 4;
    @Value("${filestore.root}")
    private String ROOT;
    @Value("${filestore.hashed}")
    private String HASHED_FILES;
    @Value("${filestore.temp}")
    private String TEMP_FILES;
    private final FileHashCalculator fileHashCalculator;
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
    public InputStream getAsStream(String userName, String path, String filePath) throws IOException {
        Path p = Path.of(ROOT, userName, path, filePath);
        return Files.newInputStream(p);
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
        return new FileInfo(Files.size(filePath) - COUNTER_TYPE_LENGTH, Files.probeContentType(filePath));
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
            putNewCounterInFile(hashedFile);
        } else {
            Files.delete(temp);
            changeCounterInFile(hashedFile, c -> c + 1);
        }
        Files.createSymbolicLink(linkPath, hashedFile);
    }

    @Async
    void afterSymLinkDelete(Path hashed) throws IOException {
        if(getCountFromFile(hashed) > 1){
            changeCounterInFile(hashed, c -> c - 1);
        } else {
            Files.delete(hashed);
        }
    }
    private String getExtension(String fileName){
        return !fileName.contains(".") ? "" :
                fileName.substring(fileName.lastIndexOf(".") - 1);
    }

    private int getCountFromFile(Path file){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "r")){
            raf.seek(raf.length() - COUNTER_TYPE_LENGTH);
            return raf.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void putNewCounterInFile(Path file){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "rw")){
            raf.seek(raf.length());
            raf.writeInt(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeCounterInFile(Path file, Function<Integer, Integer> countAction){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "rw")){
            long seekPointer = raf.length() - COUNTER_TYPE_LENGTH;
            raf.seek(seekPointer);
            int count = raf.readInt();
            raf.seek(seekPointer);
            raf.writeInt(countAction.apply(count));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
