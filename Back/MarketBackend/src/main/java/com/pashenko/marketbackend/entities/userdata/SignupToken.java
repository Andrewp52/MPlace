package com.pashenko.marketbackend.entities.userdata;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    protected LocalDateTime created;

}
