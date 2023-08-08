package com.pashenko.marketbackend.services.filestore;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public interface FileStore {
    String store(String userName, String path, MultipartFile file) throws FileAlreadyExistsException, FileNotFoundException;

    InputStreamResource getAsStreamResource(String userName, String path, String filePath) throws FileNotFoundException;

    void delete(String userName, String filePath);

    FileInfo getFileInfo(String user, String path, String file) throws IOException;
}
