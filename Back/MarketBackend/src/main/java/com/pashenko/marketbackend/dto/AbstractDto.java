package com.pashenko.marketbackend.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDto {
    protected Long id;
    protected LocalDateTime created;
    protected LocalDateTime modified;
}
