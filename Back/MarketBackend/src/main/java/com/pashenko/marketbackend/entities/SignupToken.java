package com.pashenko.marketbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "signup_tokens")
@EntityListeners(AuditingEntityListener.class)

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupToken {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "token")
    private String tokenString;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    protected LocalDateTime created;

}
