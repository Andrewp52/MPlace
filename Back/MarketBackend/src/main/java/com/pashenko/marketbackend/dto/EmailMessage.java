package com.pashenko.marketbackend.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class EmailMessage {
    private String from;
    private String to;
    private String subject;
    private String contentType;
    private String body;
    private MultipartFile[] attachments;
}
