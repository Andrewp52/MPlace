package com.pashenko.marketbackend.entities.userdata;

import com.pashenko.marketbackend.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;


@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {
    @Column(name = "role")
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role1 = (Role) o;
        return Objects.equals(role, role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role);
    }
}
