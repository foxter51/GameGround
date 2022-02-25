package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;

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

    @Column(name="review_text", nullable = false)
    private String text;

    @Column(name="img1")
    private byte[] img1;

    @Column(name="img2")
    private byte[] img2;

    @Column(name="img3")
    private byte[] img3;

    @OneToOne(mappedBy = "id")
    private User user;
}
