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

    @Column(name="comment_text", nullable = false)
    private String text;

    @OneToOne(mappedBy = "id")
    private Review review;

    @OneToOne(mappedBy = "id")
    private User user;
}
