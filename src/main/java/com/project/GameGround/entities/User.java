package com.project.GameGround.entities;

import com.project.GameGround.Constants;
import com.project.GameGround.security.AuthProvider;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(length = 64)
    private String password;

    @Column(nullable = false, length = 32)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    @Column(updatable = false, nullable = false)
    private String registrationDate;

    @Column(nullable = false)
    private String lastLoginDate;

    @Column(nullable = false, length = 20)
    private String status;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private int likesQuantity;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<RatedBy> blockedToRate = new ArrayList<>();

    @PrePersist
    public void onCreate(){
        this.registrationDate = LocalDateTime.now().format(Constants.dateTimeFormatter);
        this.lastLoginDate = LocalDateTime.now().format(Constants.dateTimeFormatter);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals("Blocked");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getFullName() {
        return firstName+ " " +lastName;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void removeAdminRole(){
        this.roles.removeIf(role -> role.getName().equals("ADMIN"));
    }
}