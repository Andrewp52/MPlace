package com.pashenko.marketbackend.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDto {
    protected Long id;
    protected Date created;
    protected Date modified;
}
