package com.project.GameGround;

import com.project.GameGround.entities.Review;
import com.project.GameGround.repositories.CommentRepository;
import com.project.GameGround.repositories.ReviewRepository;
import com.project.GameGround.repositories.TagRepository;
import com.project.GameGround.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private UserRepository repo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateReview(){
        Review review = new Review();
        review.setReviewName("Skyrim");
        review.setGroupName("RPG");
        review.addTag(tagRepo.getById((long) 1));
        review.addTag(tagRepo.getById((long) 2));
        review.setText("<img src=\"https://www.trustedreviews.com/wp-content/uploads/sites/54/2011/11/skyrim1-2.jpg\" data-src=\"https://www.trustedreviews.com/wp-content/uploads/sites/54/2011/11/skyrim1-2.jpg\" alt=\"Skyrim\" title=\"Skyrim\" width=\"300\" height=\"169\" class=\"blur-up align size-medium wp-image-241551 lazyloaded\"><p>The reason is simple: Skyrim hasn’t got the best narrative of any RPG, the best combat, the best magic system or even the best graphics, but it does have one of the biggest, richest and most completely immersive worlds you’ve ever seen. Technically speaking, the visuals aren’t as detailed or beautifully lit as those of Gears of War 3 or Uncharted 3, and while the character models and facial animation are much improved on those of Oblivion or Fallout 3, they still don’t match the work being done in the Mass Effect series. Heck, The Witcher 2 has more photorealistic forests and glistening water. </p>");
        review.setUser(repo.getById((long) 1));
        review.addComment(commentRepo.getById((long) 1));
        review.setRate((float)5.0);
        review.setRateCount(1);
        review.setPublishDate(new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));
        Review savedReview = reviewRepo.save(review);
        Review existReview = entityManager.find(Review.class, savedReview.getId());
        assertThat(existReview.getId()).isEqualTo(review.getId());
    }
}
