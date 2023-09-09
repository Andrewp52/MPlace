package com.pashenko.marketbackend.filestotage.hashedstorage;

import com.pashenko.marketbackend.filestotage.AbstractStorageTest;
import com.pashenko.marketbackend.services.filestore.storage.FileHashCalculator;
import com.pashenko.marketbackend.services.filestore.storage.FileStoreService;
import com.pashenko.marketbackend.services.filestore.storage.HashedFileStorageService;
import com.pashenko.marketbackend.services.filestore.storage.MD5FileHashCalculator;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class AbstractHashedStorageTest extends AbstractStorageTest {
    protected static final Path hashed = root.resolve("hashed");
    protected static final Path temp = root.resolve("temp");
    protected static final Path userspace = root.resolve("userspace");
    protected static final String hashedName = "ac79653edeb65ab5563585f2d5f14fe9.txt";
    protected static final FileHashCalculator hashCalc = new MD5FileHashCalculator();
    protected static final FileStoreService store = new HashedFileStorageService(hashCalc, root.toString());
    protected static final String username1 = "test_user";
    protected static final String username2 = "test_user2";
    protected static final String folder = "docs";

    @BeforeAll
    static void setup(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testFile.toFile()))){
            bw.write(testContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
