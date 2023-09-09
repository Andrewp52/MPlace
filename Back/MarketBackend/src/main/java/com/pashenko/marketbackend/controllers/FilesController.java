package com.pashenko.marketbackend.controllers;

import com.pashenko.marketbackend.services.filestore.FileInfo;
import com.pashenko.marketbackend.services.filestore.storage.FileStoreService;
import com.pashenko.marketbackend.services.userservice.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FilesController {
    private final FileStoreService fileStoreService;
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
            FileInfo info = fileStoreService.getFileInfo(user, path, file);
            InputStreamResource isr = new InputStreamResource(fileStoreService.getAsStream(user, path, file));
            return ResponseEntity.ok()
                    .contentLength(info.getSize())
                    .contentType(MediaType.valueOf(info.getContentType()))
                    .body(isr);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());        //TODO: USE OWN RUNTIME EXCEPTION
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("!isAnonymous()")
    @PostMapping("/{path}")
    public ResponseEntity<String> storeFile(
            @PathVariable(name = "path", required = true) String path,
            MultipartFile file
    ) {
        try {
            String username = userSecurityService.getCurrentUser().getUsername();
            return ResponseEntity.ok().body(fileStoreService.store(username, path, file));
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());        //TODO: USE OWN RUNTIME EXCEPTION
        } catch (IOException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("!isAnonymous()")
    @DeleteMapping("/{path}/{file}")
    public ResponseEntity<?> deleteFile(
            @PathVariable(name = "path") String path,
            @PathVariable(name = "file") String file
    ) {
        try {
            String username = userSecurityService.getCurrentUser().getUsername();
            fileStoreService.delete(username, path, file);
            return ResponseEntity.ok().build();
        } catch (FileNotFoundException e) {                                                      //TODO: USE OWN RUNTIME EXCEPTION
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
