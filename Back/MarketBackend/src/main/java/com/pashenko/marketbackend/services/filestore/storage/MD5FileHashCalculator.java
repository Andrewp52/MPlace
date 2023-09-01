package com.pashenko.marketbackend.services.filestore.storage;

import com.twmacinta.util.MD5;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class MD5FileHashCalculator implements FileHashCalculator{
    @Override
    public String calculateHash(Path filePath) throws IOException {
        byte[] hash = MD5.getHash(filePath.toFile());
        return MD5.asHex(hash);
    }
}
