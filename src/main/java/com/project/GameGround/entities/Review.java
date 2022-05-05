package com.project.GameGround.entities;

import com.project.GameGround.Constants;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] reviewPhoto;
    
    @Column(nullable = false, length = 64)
    private String reviewName;

    @Column(nullable = false, length = 32)
    private String groupName;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int authorRate;

    private float rate;

    private int rateCount;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", orphanRemoval = true)
    private List<RatedBy> blockedToRate = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String publishDate;

    @PrePersist
    public void onPublish(){
        this.publishDate = LocalDateTime.now().format(Constants.dateTimeFormatter);
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addBlockedToRate(RatedBy ratedBy){
        blockedToRate.add(ratedBy);
    }

    public String getEncodedPhoto(){
        return Base64.getEncoder().encodeToString(reviewPhoto);
    }
}