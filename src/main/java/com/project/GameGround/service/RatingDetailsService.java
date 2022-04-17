package com.project.GameGround.service;

import com.project.GameGround.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingDetailsService {

    @Autowired
    protected RatingRepository repo;

    public boolean isUserLiked(String reviewID, Long currentUserID){
        return repo.isUserLiked(Long.parseLong(reviewID), currentUserID) != null;
    }

    public Float getRateIfRated(String reviewID, Long currentUserID){
        return repo.getUserRate(Long.parseLong(reviewID), currentUserID);
    }
}
