package com.project.GameGround.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotBlank
    @Max(64)
    @Column(name="review_name", nullable = false, length = 64)
    private String reviewName;

    @NotBlank
    @Max(32)
    @Column(name="group_name", nullable = false, length = 32)
    private String groupName;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "reviews_tags", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @NotBlank
    @Column(name="review_text", nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "author_rate")
    private int authorRate;

    @Column(name = "review_rate")
    private float rate;

    @Column(name = "rate_count")
    private int rateCount;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review_id")
    private List<RatedBy> blockedToRate = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
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

    public void addBlockedToRate(RatedBy ratedBy){
        blockedToRate.add(ratedBy);
    }
}
