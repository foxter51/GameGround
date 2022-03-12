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

    @Column(name="review_id", nullable = false)
    private Long reviewID;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "type")
    private String rateType;

    public RatedBy(Long reviewID, User user, String rateType){
        this.reviewID = reviewID;
        this.user = user;
        this.rateType = rateType;
    }

    public RatedBy() {}
}
