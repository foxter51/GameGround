package com.project.GameGround.service;

import com.project.GameGround.Constants;
import com.project.GameGround.entities.Comment;
import com.project.GameGround.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CommentDetailsService {

    @Autowired
    protected CommentRepository repo;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public Comment addComment(Comment comment, Long reviewID, Long userID){
        comment.setUser(userDetailsService.repo.getById(userID));
        comment.setReviewID(reviewID);
        comment.setPublishDate(new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));
        return repo.save(comment);
    }
}
