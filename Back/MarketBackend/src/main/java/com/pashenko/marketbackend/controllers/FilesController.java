package com.pashenko.marketbackend.controllers;

import com.pashenko.marketbackend.services.filestore.FileInfo;
import com.pashenko.marketbackend.services.filestore.FileStore;
import com.pashenko.marketbackend.services.userservice.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FilesController {
    private final FileStore fileStore;
    private final UserSecurityService userSecurityService;

    @GetMapping("/{path}")
    public String getFileList(@PathVariable(name = "path") String path){
        return path;
    }

    @GetMapping("/{user}/{path}/{file}")
    public ResponseEntity<?> getFile(
            @PathVariable(name = "user") String user,
            @PathVariable(name = "path") String path,
            @PathVariable(name = "file") String file
    ) {
        try {
            FileInfo info = fileStore.getFileInfo(user, path, file);
            InputStreamResource isr = fileStore.getAsStreamResource(user, path, file);
            return ResponseEntity.ok()
                    .contentLength(info.getSize())
                    .contentType(MediaType.valueOf(info.getContentType()))
                    .body(isr);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{path}")
    public ResponseEntity<String> storeFile(
            @PathVariable(name = "path", required = true) String path,
            MultipartFile file
    ) {
        try {
            return ResponseEntity.ok().body(fileStore.store("test", path, file));
        } catch (FileAlreadyExistsException | FileNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{user}/{path}/{file}")
    public ResponseEntity<?> deleteFile(
            @PathVariable(name = "user") String user,
            @PathVariable(name = "path") String path,
            @PathVariable(name = "file") String file
    ) {
        return ResponseEntity.ok().build();         // IMPLEMENT
    }
}
