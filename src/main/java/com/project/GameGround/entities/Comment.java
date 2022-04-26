package com.project.GameGround.entities;

import com.project.GameGround.Constants;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Review review;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(updatable = false, nullable = false)
    private String publishDate;

    @PrePersist
    public void onCreate(){
        this.publishDate = LocalDateTime.now().format(Constants.dateTimeFormatter);
    }
}