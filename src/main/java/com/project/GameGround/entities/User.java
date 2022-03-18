package com.project.GameGround.entities;

import com.project.GameGround.security.AuthProvider;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty
    @Max(32)
    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @NotBlank
    @Max(64)
    @Column(length = 64)
    private String password;

    @NotBlank
    @Max(32)
    @Column(nullable = false, length = 32)
    private String firstName;

    @Max(32)
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

    @Column(name = "likes_quantity")
    private int likesQuantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void removeAdminRole(){
        this.roles.removeIf(role -> role.getName().equals("ADMIN"));
    }
}