package com.project.GameGround;

import com.project.GameGround.entities.Tag;
import com.project.GameGround.repositories.TagRepository;
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
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateTag(){
        Tag tag = new Tag("#game");
        Tag savedTag = tagRepo.save(tag);
        Tag existTag = entityManager.find(Tag.class, savedTag.getId());
        assertThat(existTag.getId()).isEqualTo(tag.getId());
    }
}
