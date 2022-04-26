package com.project.GameGround.repositories;

import com.project.GameGround.entities.RatedBy;
import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface RatingRepository extends JpaRepository<RatedBy, Long> {

    @Query("SELECT ratedBy.rating FROM RatedBy ratedBy WHERE ratedBy.review=?1 AND ratedBy.user=?2 AND ratedBy.rateType='RATING'")
    Float getUserRate(Review review, User user);

    @Query("SELECT ratedBy FROM RatedBy ratedBy WHERE ratedBy.review=?1 AND ratedBy.user=?2 AND ratedBy.rateType='LIKE'")
    RatedBy isUserLiked(Review review, User user);

    @Modifying
    @Query("DELETE FROM RatedBy ratedby WHERE ratedby.user=?1 AND ratedby.rateType='LIKE'")
    void deleteUserLike(User user);

    @Modifying
    @Query("DELETE FROM RatedBy ratedby WHERE ratedby.user=?1 AND ratedby.rateType='RATING'")
    void deleteUserRating(User user);
}
