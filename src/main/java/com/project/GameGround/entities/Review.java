package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="review_name", nullable = false, length = 64)
    private String reviewName;

    @Column(name="group_name", nullable = false, length = 32)
    private String groupName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reviews_tags", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    @Column(name="review_text", nullable = false)
    private String text;

    @Column(name="img1")
    private byte[] img1;

    @Column(name="img2")
    private byte[] img2;

    @Column(name="img3")
    private byte[] img3;

//    @OneToOne(mappedBy = "id")
//    private User user;

    @Column(name = "review_rate")
    private float rate;
}
