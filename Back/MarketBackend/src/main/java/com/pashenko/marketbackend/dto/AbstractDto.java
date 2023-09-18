package com.pashenko.marketbackend.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDto {
    protected Long id;
    protected LocalDateTime created;
    protected LocalDateTime modified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto that = (AbstractDto) o;
        return id.equals(that.id) && created.equals(that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created);
    }
}
