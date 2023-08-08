package com.pashenko.marketbackend.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public abstract class AbstractDto {
    protected Long id;
    protected Date created;
    protected Date modified;
}
