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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String reviewName;

    @Column(nullable = false, length = 32)
    private String groupName;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    private int authorRate;

    private float rate;

    private int rateCount;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review_id")
    private List<RatedBy> blockedToRate = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review_id")
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false, length = 20)
    private String publishDate;

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addBlockedToRate(RatedBy ratedBy){
        blockedToRate.add(ratedBy);
    }
}