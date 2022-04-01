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

    @Column(name = "review_id")
    private Long reviewID;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    private String rateType;

    public RatedBy(Long reviewID, User user, String rateType){
        this.reviewID = reviewID;
        this.user = user;
        this.rateType = rateType;
    }

    public RatedBy() {}
}