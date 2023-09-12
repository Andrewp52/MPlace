package com.pashenko.marketbackend.filestotage.hashedstorage;

import com.pashenko.marketbackend.filestotage.AbstractStorageTest;
import com.pashenko.marketbackend.services.filestore.storage.FileHashCalculator;
import com.pashenko.marketbackend.services.filestore.storage.FileStoreService;
import com.pashenko.marketbackend.services.filestore.storage.HashedFileStorageService;
import com.pashenko.marketbackend.services.filestore.storage.MD5FileHashCalculator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class AbstractHashedStorageTest extends AbstractStorageTest {
    protected static final Path hashed = root.resolve("hashed");
    protected static final Path temp = root.resolve("temp");
    protected static final Path userspace = root.resolve("userspace");
    protected static final String hashedName = "ac79653edeb65ab5563585f2d5f14fe9.txt";
    protected static final FileHashCalculator hashCalc = new MD5FileHashCalculator();
    protected static FileStoreService store;
    protected static final String username1 = "test_user";
    protected static final String username2 = "test_user2";
    protected static final String folder = "docs";

    @BeforeAll
    static void setup() throws IOException {
        store = new HashedFileStorageService(hashCalc, root.toString());
        if(!Files.exists(testFile)){
            Files.createFile(testFile.toAbsolutePath());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(testFile.toAbsolutePath().toFile()))){
                bw.write(testContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterAll
    static void cleanup() throws IOException {
        if(!Files.exists(root)){
            return;
        }
        Files.walkFileTree(root, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
