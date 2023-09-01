package com.pashenko.marketbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoredFileDto {
    private String name;
    private Integer linksCount;
}
