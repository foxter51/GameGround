package com.project.GameGround.repositories;

import com.project.GameGround.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> getReviewByUserId(Long id);  //get all user's reviews

    @Query("SELECT review FROM Review review WHERE review.rate>=4")  //get reviews by rating >=4
    List<Review> getReviewsRatingGE4();

    @Query("SELECT review FROM Review review WHERE review.rate>=4 AND review.user.id = ?1")  //get reviews by rating >=4 and user id
    List<Review> getReviewsRatingGE4ByID(Long id);

    List<Review> getReviewsByTagsTagName(String tagName);

    @Query(value = "SELECT * FROM reviews WHERE MATCH (`review_name`, `group_name`, `text`) AGAINST (?1)", nativeQuery = true)
    List<Review> search(String request);

    List<Review> getReviewsByGroupName(String groupName);

    @Query(value = "SELECT DISTINCT group_name FROM reviews WHERE group_name != ?1 ORDER BY id DESC LIMIT 0, 4", nativeQuery = true)
    List<String> getLast4Genres(String withoutGenre);

    @Query(value = "SELECT id FROM reviews WHERE id < ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long getPrevRevID(Long currentRevID);

    @Query(value = "SELECT id FROM reviews WHERE id > ?1 LIMIT 1", nativeQuery = true)
    Long getNextRevID(Long currentRevID);
}
