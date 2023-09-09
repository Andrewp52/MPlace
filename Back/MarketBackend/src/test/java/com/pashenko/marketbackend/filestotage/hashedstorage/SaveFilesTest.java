package com.pashenko.marketbackend.filestotage.hashedstorage;

import com.pashenko.marketbackend.services.filestore.storage.HashedFileStorageService;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SaveFilesTest extends AbstractHashedStorageTest{


    private final MultipartFile file = new MockMultipartFile(
            testFile.getFileName().toString(),
            testFile.getFileName().toString(),
            MediaType.TEXT_HTML.getType(),
            Files.newInputStream(testFile, StandardOpenOption.READ)
    );

    public SaveFilesTest() throws IOException {
    }

    @Test
    @Order(10)
    void storeNewFileCreatesHashedNameFileWithCounterAndSymLink() throws IOException {
        store.store(username1, folder, file);
        Path hashedFile = hashed.resolve(hashedName);
        Path userLink = userspace.resolve(Path.of(username1, folder, testFile.getFileName().toString()));
        assertTrue(Files.exists(hashedFile));
        assertTrue(Files.exists(userLink));
        assertEquals(hashedFile.toAbsolutePath(), userLink.toRealPath());
    }

    @Test
    @Order(20)
    void hashedFileIsBiggerThanTestAndHasCounter1() throws IOException {
        Path hashedFile = hashed.resolve(hashedName);
        assertEquals(Files.size(hashedFile), Files.size(testFile) + Integer.BYTES);
        assertEquals(1, ((HashedFileStorageService) store).getCounterFromFile(hashedFile));
    }

    @Test
    @Order(30)
    void hashedFileContentExceptCounterIsTheSameAsTestFile(){
        Path hashedFile = hashed.resolve(hashedName);
        try(
                RandomAccessFile orRaf = new RandomAccessFile(testFile.toFile(), "r");
                RandomAccessFile hRaf = new RandomAccessFile(hashedFile.toFile(), "r")
        ){
            byte[] orBytes = new byte[(int) orRaf.length()];                // Original file bytes
            byte[] hBytes = new byte[(int) hRaf.length() - Integer.BYTES];  // Except counter
            orRaf.read(orBytes);
            hRaf.read(hBytes, 0, (int) hRaf.length() - Integer.BYTES);

            assertArrayEquals(orBytes, hBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(40)
    void storeNewFileWithSameUserPathAndNameThrowsException() throws IOException {
        assertThrows(FileAlreadyExistsException.class, () -> store.store(username1, folder, file));
    }

    @Test
    @Order(50)
    void storeExistingFileByAnotherUserCreatesSymLinkAndIncreasesCounterInHashedFile() {

    }


}
