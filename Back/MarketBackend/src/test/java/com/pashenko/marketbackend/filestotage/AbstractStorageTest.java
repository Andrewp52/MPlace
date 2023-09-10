package com.pashenko.marketbackend.filestotage;


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

public class AbstractStorageTest {
    protected static final Path root = Path.of("test_files");

    protected static final Path testFile = root.resolve("test.txt");
    protected static final String testContent = "Test file content";



}
