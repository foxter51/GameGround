package com.project.GameGround.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="review_id")
    private Long reviewID;

    @NotBlank
    @Column(name="comment_text", nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name="date", nullable = false, length = 20)
    private String publishDate;
}
