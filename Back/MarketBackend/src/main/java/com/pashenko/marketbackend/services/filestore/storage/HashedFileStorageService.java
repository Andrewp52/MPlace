package com.pashenko.marketbackend.services.filestore.storage;

import com.pashenko.marketbackend.services.filestore.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
public class HashedFileStorageService implements FileStoreService {
    private static final int COUNTER_SIZE = Integer.BYTES;
    private static final String[] userFolders = {"img", "docs", "videos"};
    private final FileHashCalculator fileHashCalculator;
    private final Path ROOT;
    private final Path HASHED_FILES;
    private final Path TEMP_FILES;
    private final Path USER_SPACE;

    @Autowired
    public HashedFileStorageService(FileHashCalculator fileHashCalculator, @Value("${filestore.root}") String root) {
        this.fileHashCalculator = fileHashCalculator;
        this.ROOT = Path.of(root);
        this.HASHED_FILES = this.ROOT.resolve("hashed");
        this.TEMP_FILES = this.ROOT.resolve("temp");
        this.USER_SPACE = this.ROOT.resolve("userspace");
        try {
            checkOrCreateSysFolders();
        } catch (IOException e) {
            log.error("Storage folders check & creation error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String store(String username, String path, MultipartFile file) throws IOException {
        checkOrCreateUserFolders(username);
        Path linkPath = resolveUserFile(username, path, file.getOriginalFilename());
        if(Files.exists(linkPath)){
            throw new FileAlreadyExistsException("file %s/%s/%s is already exist".formatted(username, path, file.getOriginalFilename()));
        }
        Path temp = storeTemp(file);
        storePermanentAndCreateLink(temp, linkPath, Objects.requireNonNull(file.getOriginalFilename()));
        return "%s/%s/%s".formatted(username, path, file.getOriginalFilename());
    }

    @Override
    public InputStream getAsStream(String username, String path, String filename) throws IOException {
        Path p = resolveUserFile(username, path, filename);
        return Files.newInputStream(p);
    }

    @Override
    public void delete(String userName, String filePath, String filename) throws IOException {
        Path symLink = resolveUserFile(userName, filePath, filename);
        if(!Files.exists(symLink)){
            throw new FileNotFoundException("File %s/%s/%s not found".formatted(userName, filePath, filename));
        }
        Path real = symLink.toRealPath();
        Files.delete(symLink);
        afterSymLinkDelete(real);
    }

    @Override
    public FileInfo getFileInfo(String user, String path, String file) throws IOException {
        Path filePath = resolveUserFile(user, path, file);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("File %s/%s/%s not found".formatted(user, path, file));
        }
        return new FileInfo(Files.size(filePath) - COUNTER_SIZE, Files.probeContentType(filePath));
    }

    private Path storeTemp(MultipartFile file) throws IOException {
        Path p = Files.createTempFile(TEMP_FILES, UUID.randomUUID().toString(), file.getOriginalFilename());
        file.transferTo(p);
        return p;
    }

    void storePermanentAndCreateLink(Path temp, Path linkPath, String fileName) throws IOException {
        String hash = this.fileHashCalculator.calculateHash(temp);
        Path hashedFile = HASHED_FILES.resolve(hash + getExtension(fileName));
        if(!Files.exists(hashedFile)){
            Files.move(temp, hashedFile, StandardCopyOption.ATOMIC_MOVE);
            putNewCounterInFile(hashedFile);
        } else {
            Files.delete(temp);
            changeCounterInFile(hashedFile, c -> c + 1);
        }
        Files.createSymbolicLink(linkPath, hashedFile.toAbsolutePath());
    }

    private Path resolveUserFile(String username, String path, String filename){
        return USER_SPACE.resolve(Path.of(username, path, filename));
    }
    @Async
    void afterSymLinkDelete(Path hashed) throws IOException {
        if(getCounterFromFile(hashed) > 1){
            changeCounterInFile(hashed, c -> c - 1);
        } else {
            Files.delete(hashed);
        }
    }
    private String getExtension(String fileName){
        return !fileName.contains(".") ? "" :
                fileName.substring(fileName.lastIndexOf("."));
    }

    public int getCounterFromFile(Path file){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "r")){
            raf.seek(raf.length() - COUNTER_SIZE);
            return raf.readInt();
        } catch (IOException e) {
            log.error("Getting counter from file error: ", e);
            throw new RuntimeException(e);
        }
    }
    private void putNewCounterInFile(Path file){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "rw")){
            raf.seek(raf.length());
            raf.writeInt(1);
        } catch (IOException e) {
            log.error("Putting a new counter to the file error: ", e);
            throw new RuntimeException(e);
        }
    }

    private void changeCounterInFile(Path file, Function<Integer, Integer> countAction){
        try (RandomAccessFile raf = new RandomAccessFile(file.toString(), "rw")){
            long seekPointer = raf.length() - COUNTER_SIZE;
            raf.seek(seekPointer);
            int count = raf.readInt();
            raf.seek(seekPointer);
            raf.writeInt(countAction.apply(count));
        } catch (IOException e) {
            log.error("Changing counter value in file error: ", e);
            throw new RuntimeException(e);
        }
    }

    private void checkOrCreateUserFolders(String username){
        if(Files.exists(USER_SPACE.resolve(username))){
            return;
        }
        for (String s : userFolders){
            try {
                Files.createDirectories(USER_SPACE.resolve(Path.of(username, s)));
            } catch (IOException e) {
                log.error("User's directories creation error: ", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void checkOrCreateSysFolders() throws IOException {
        if(!Files.exists(ROOT)){
                Files.createDirectories(TEMP_FILES);
                Files.createDirectories(HASHED_FILES);
                Files.createDirectories(USER_SPACE);
        } else {
            if(!Files.exists(HASHED_FILES)){
                Files.createDirectories(HASHED_FILES);
            }
            if(!Files.exists(TEMP_FILES)){
                Files.createDirectories(TEMP_FILES);
            }
            if(!Files.exists(USER_SPACE)){
                Files.createDirectories(USER_SPACE);
            }
        }
    }
}
