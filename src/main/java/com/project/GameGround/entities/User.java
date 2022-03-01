package com.project.GameGround.entities;

import com.project.GameGround.security.AuthProvider;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(length = 64)
    private String password;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String registrationDate;

    @Column(nullable = false, length = 20)
    private String lastLoginDate;

    @Column(nullable = false, length = 20)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column
    private AuthProvider authProvider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
        this.roles.add(role);
    }
}