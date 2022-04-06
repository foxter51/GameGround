package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private Long reviewID;

    @Column(nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @Column(nullable = false, length = 20)
    private String publishDate;
}