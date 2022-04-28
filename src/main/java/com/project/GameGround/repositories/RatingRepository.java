package com.project.GameGround.repositories;

import com.project.GameGround.entities.RatedBy;
import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface RatingRepository extends JpaRepository<RatedBy, Long> {

    Optional<RatedBy> getRatedByReviewAndUserAndRateType(Review review, User user, String rateType);

    void deleteByUserAndRateType(User user, String rateType);
}
