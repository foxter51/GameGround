package com.project.GameGround.repositories;

import com.project.GameGround.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> getReviewByUserId(Long id);  //get all user's reviews

    @Modifying
    void deleteById(Long id);  //delete review by id

    @Query("SELECT review FROM Review review WHERE review.rate>=4")  //get reviews by rating >=4
    List<Review> getReviewsRatingGE4();

    @Query("SELECT review FROM Review review WHERE review.rate>=4 AND review.user.id = ?1")  //get reviews by rating >=4 and user id
    List<Review> getReviewsRatingGE4ByID(Long id);

    @Query(value = "SELECT * FROM reviews WHERE id = ANY (SELECT review_id FROM reviews_tags WHERE tag_id = (SELECT id FROM tags WHERE tag_name = ?1))", nativeQuery = true)
    List<Review> getReviewsByTag(String tag);

    @Query(value = "SELECT * FROM reviews WHERE MATCH (`review_name`, `group_name`, `review_text`) AGAINST (?1)", nativeQuery = true)
    List<Review> search(String request);
}
