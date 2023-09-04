package com.pashenko.marketbackend.services.filestore.storage;

import com.pashenko.marketbackend.services.filestore.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStoreService {
    String store(String userName, String path, MultipartFile file) throws IOException;

    InputStream getAsStream(String userName, String path, String filePath) throws IOException;

    void delete(String userName, String filePath, String filename) throws IOException;

    FileInfo getFileInfo(String user, String path, String file) throws IOException;
}
