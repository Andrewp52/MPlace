package com.pashenko.marketbackend.filestotage.hashedstorage;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteFilesTest extends AbstractHashedStorageTest{
    @BeforeAll
    static void storeFileWith2Users() throws IOException {
        MultipartFile mpartFile = new MockMultipartFile(
                testFile.getFileName().toString(),
                testFile.getFileName().toString(),
                MediaType.TEXT_HTML.getType(),
                Files.newInputStream(testFile, StandardOpenOption.READ)
        );
        store.store(username1, folder, mpartFile);
        store.store(username2, folder, mpartFile);
    }

    @Test
    @Order(10)
    void deleteFileWithCoupleLinksDeletesSymLinkAndDecreasesCounterInHashedFile() throws IOException {
        Path hashedFile = hashed.resolve(hashedName);
        Path symlink = userspace.resolve(username1).resolve(folder).resolve(testFile.getFileName());

        store.delete(username1, folder, testFile.getFileName().toString());
        assertFalse(Files.exists(symlink));

        try (RandomAccessFile raf = new RandomAccessFile(hashedFile.toFile(), "r")){
            raf.seek(raf.length() - Integer.BYTES);
            int counter = raf.readInt();
            assertEquals(1, counter);
        }
    }

    @Test
    @Order(20)
    void deleteFileWithOneLinkDeletesSymLinkAndHashedFile() throws IOException {
        Path hashedFile = hashed.resolve(hashedName);
        Path symlink = userspace.resolve(username2).resolve(folder).resolve(testFile.getFileName());

        store.delete(username2, folder, testFile.getFileName().toString());
        assertFalse(Files.exists(symlink));
        assertFalse(Files.exists(hashedFile));
    }
}
