package com.project.GameGround.service;

import com.project.GameGround.entities.Comment;
import com.project.GameGround.entities.Review;
import com.project.GameGround.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentDetailsService {

    @Autowired
    protected CommentRepository repo;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public Comment addComment(Comment comment, Review review, Long userID){
        comment.setUser(userDetailsService.repo.getById(userID));
        comment.setReview(review);
        return repo.save(comment);
    }
}
