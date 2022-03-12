package com.project.GameGround.repositories;

import com.project.GameGround.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT review FROM Review review WHERE review.user.id = ?1")
    List<Review> getReviewsByUserID(Long id);

    @Modifying
    @Query("DELETE FROM Review review WHERE review.id = ?1")
    void deleteReviewByID(Long id);

    @Query("SELECT review FROM Review review WHERE review.rate>=4")
    List<Review> getReviewsRatingGE4();

    @Query(value = "SELECT * FROM reviews WHERE MATCH (`review_name`, `group_name`, `review_text`) AGAINST (?1)", nativeQuery = true)
    List<Review> search(String request);
}
