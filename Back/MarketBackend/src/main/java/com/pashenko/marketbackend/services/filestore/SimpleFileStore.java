package com.pashenko.marketbackend.services.filestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SimpleFileStore implements FileStore{
    @Value("${filestore.root}")
    private String ROOT;

    @Override
    public String store(String userName, String path, MultipartFile file) throws FileAlreadyExistsException, FileNotFoundException {
        Path p = resolvePath(userName, path, file.getOriginalFilename());
        if(!Files.exists(p.getParent())){
            throw new FileNotFoundException("Directory with name %s not found!".formatted(p.getParent().toString()));
        }
        if(Files.exists(p)){
            throw new FileAlreadyExistsException("File with name %s is already exist!".formatted(file.getOriginalFilename()));
        }
        try {
            file.transferTo(p);
        } catch (IOException e) {
            throw new RuntimeException("File transfer failed!", e);
        }
        return "/%s/%s".formatted(path, file.getOriginalFilename());
    }

    @Override
    public InputStreamResource getAsStreamResource(String userName, String path, String fileName) throws FileNotFoundException {
        Path p = resolvePath(userName, path, fileName);
        if(!Files.exists(p)){
            throw new FileNotFoundException("File /%s/%s not found!".formatted(path, fileName));
        }
        return new InputStreamResource(new FileInputStream(p.toFile()));
    }

    @Override
    public void delete(String userName, String filePath) {

    }

    @Override
    public FileInfo getFileInfo(String user, String path, String file) throws IOException {
        Path p = resolvePath(user, path, file);
        return new FileInfo(Files.size(p), Files.probeContentType(p));
    }


    private Path resolvePath(String username, String path){
        return Path.of(ROOT, username, path);
    }

    private Path resolvePath(String username, String path, String fileName){
        return Path.of(ROOT, username, path, fileName);
    }
}
