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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reviews_tags", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(name="review_text", nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "review_rate")
    private float rate;

    @OneToMany
    @JoinColumn(name = "review_id")
    private List<Comment> comments = new ArrayList<>();

    @Column(name="date", nullable = false, length = 20)
    private String publishDate;

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
}
