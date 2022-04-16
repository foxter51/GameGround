package com.project.GameGround.repositories;

import com.project.GameGround.entities.RatedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface RatingRepository extends JpaRepository<RatedBy, Long> {

    @Query("SELECT ratedBy.rating FROM RatedBy ratedBy WHERE ratedBy.reviewID=?1 AND ratedBy.user.id=?2 AND ratedBy.rateType='RATING'")
    Float getUserRate(Long reviewID, Long userID);

    @Query("SELECT ratedBy FROM RatedBy ratedBy WHERE ratedBy.reviewID=?1 AND ratedBy.user.id=?2 AND ratedBy.rateType='LIKE'")
    RatedBy isUserLiked(Long reviewID, Long userID);

    @Modifying
    @Query("DELETE FROM RatedBy ratedby WHERE ratedby.user.id=?1 AND ratedby.rateType='LIKE'")
    void deleteUserLike(Long userID);

    @Modifying
    @Query("DELETE FROM RatedBy ratedby WHERE ratedby.user.id=?1 AND ratedby.rateType='RATING'")
    void deleteUserRating(Long userID);
}
