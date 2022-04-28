package com.project.GameGround.service;

import com.project.GameGround.entities.RatedBy;
import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.User;
import com.project.GameGround.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingDetailsService {

    @Autowired
    protected RatingRepository repo;

    public boolean isUserLiked(Review review, User user){
        return repo.getRatedByReviewAndUserAndRateType(review, user, "LIKE").isPresent();
    }

    public Float getRateIfRated(Review review, User user){
        Optional<RatedBy> ratedBy = repo.getRatedByReviewAndUserAndRateType(review, user, "RATING");
        return ratedBy.isPresent() ? (float)ratedBy.get().getRating() : null;
    }
}
