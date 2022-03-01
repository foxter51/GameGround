package com.project.GameGround.repositories;

import com.project.GameGround.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT review FROM Review review WHERE review.user.id = ?1")
    List<Review> getReviewsByUserID(Long id);
}
