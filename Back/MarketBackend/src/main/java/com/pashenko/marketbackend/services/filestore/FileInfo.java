package com.pashenko.marketbackend.services.filestore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfo {
    private long size;
    private String contentType;
}
