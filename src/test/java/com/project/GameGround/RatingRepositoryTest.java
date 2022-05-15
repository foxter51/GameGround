package com.project.GameGround;

import com.project.GameGround.entities.RatedBy;
import com.project.GameGround.repositories.RatingRepository;
import com.project.GameGround.repositories.ReviewRepository;
import com.project.GameGround.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RatingRepositoryTest {
    @Autowired
    private UserRepository repo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private RatingRepository ratingRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateRating(){
        RatedBy ratedBy = new RatedBy(reviewRepo.getById((long)1), repo.getById((long)1), "RATING", 5);
        RatedBy savedRatedBy = ratingRepo.save(ratedBy);
        RatedBy existRatedBy = entityManager.find(RatedBy.class, savedRatedBy.getId());
        assertThat(existRatedBy.getId()).isEqualTo(ratedBy.getId());
    }
}
