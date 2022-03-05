package com.project.GameGround.repositories;

import com.project.GameGround.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT tag FROM Tag tag WHERE tag.tagName = ?1")
    Tag isContains(String tagName);
}
