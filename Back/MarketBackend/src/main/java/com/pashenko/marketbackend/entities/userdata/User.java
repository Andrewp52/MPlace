package com.pashenko.marketbackend.entities.userdata;

import com.pashenko.marketbackend.entities.AbstractEntity;
import jakarta.persistence.*;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_login", columnList = "username")
        }
)

public class User extends AbstractEntity implements UserDetails {
    @Column(name = "username")
    protected String username;
    @Column(name = "password")
    protected String password;

    @Column(name = "enabled")
    @BooleanFlag
    private Boolean enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SignupToken signupToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && enabled.equals(user.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, enabled);
    }
}
