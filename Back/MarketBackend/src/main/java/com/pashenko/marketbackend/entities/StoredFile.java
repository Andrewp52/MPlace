package com.pashenko.marketbackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.Modifying;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "stored_files")


@NamedQuery(name = "StoredFile.increaseLinksCount", query = "UPDATE StoredFile sf SET sf.linksCount = sf.linksCount + 1 where sf.name = :fileName")
@NamedQuery(name = "StoredFile.decreaseLinksCount", query = "update StoredFile sf SET sf.linksCount = sf.linksCount - 1 where sf.name = :fileName")

public class StoredFile {
    @Id
    @Column(name = "file_name")
    private String name;
    @Column(name = "links_count")
    private Integer linksCount;
}
