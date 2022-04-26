package com.project.GameGround.service;

import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.User;
import com.project.GameGround.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingDetailsService {

    @Autowired
    protected RatingRepository repo;

    public boolean isUserLiked(Review review, User user){
        return repo.isUserLiked(review, user) != null;
    }

    public Float getRateIfRated(Review review, User user){
        return repo.getUserRate(review, user);
    }
}
