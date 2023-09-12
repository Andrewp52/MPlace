package com.pashenko.marketbackend.filestotage.hashedstorage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadFilesTest extends AbstractHashedStorageTest{
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void storeTestFile() throws IOException {
        MultipartFile file = new MockMultipartFile(
                testFile.getFileName().toString(),
                testFile.getFileName().toString(),
                MediaType.TEXT_HTML.getType(),
                Files.newInputStream(testFile)
        );
        store.store(username1, folder, file);
    }
    @Test
    void whenRequestFileThenReturnsTestFileContent() throws IOException {
        String url = "%s/%s/%s/%s".formatted(STORAGE_URL, username1, folder, testFile.getFileName().toString());
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertArrayEquals(Files.readAllBytes(testFile), Objects.requireNonNull(result.getBody()).getBytes());
    }

    @Test
    void whenRequestNonExistentFileThenStatus404(){
        String url = "%s/%s/%s/%s".formatted(STORAGE_URL, username1, folder, "notexist.txt");
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
