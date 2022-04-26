package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="ratedby")
public class RatedBy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String rateType;

    private Integer rating;

    public RatedBy(Review review, User user, String rateType){
        this.review = review;
        this.user = user;
        this.rateType = rateType;
    }

    public RatedBy(Review review, User user, String rateType, Integer rating){
        this.review = review;
        this.user = user;
        this.rateType = rateType;
        this.rating = rating;
    }

    public RatedBy() {}
}