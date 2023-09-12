package com.pashenko.marketbackend.filestotage;

import java.nio.file.Path;

public class AbstractStorageTest {
    protected final String STORAGE_URL = "/files";
    protected static final Path root = Path.of("test_files");
    protected static final Path testFile = root.resolve("test.txt");
    protected static final String testContent = "Test file content";



}
