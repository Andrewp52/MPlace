package com.pashenko.marketbackend.services.filestore.storage;

import java.io.IOException;
import java.nio.file.Path;

public interface FileHashCalculator {
    String calculateHash(Path filePath) throws IOException;
}
