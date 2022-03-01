package com.project.GameGround;

import com.project.GameGround.entities.Comment;
import com.project.GameGround.repositories.CommentRepository;
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
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateComment(){
        Comment comment = new Comment();
        comment.setText("Test comment");
        comment.setUser(repo.getById((long) 1));
        Comment savedComment = commentRepo.save(comment);
        Comment existComment = entityManager.find(Comment.class, savedComment.getId());
        assertThat(existComment.getId()).isEqualTo(comment.getId());
    }
}
