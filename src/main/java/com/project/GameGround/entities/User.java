package com.project.GameGround.entities;

import com.project.GameGround.security.AuthProvider;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(nullable = false, length = 32)
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
    private AuthProvider authProvider;

    private int likesQuantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private List<Review> reviews = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private List<RatedBy> blockedToRate = new ArrayList<>();

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void removeAdminRole(){
        this.roles.removeIf(role -> role.getName().equals("ADMIN"));
    }
}