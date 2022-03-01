package com.project.GameGround.repositories;

import com.project.GameGround.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {
}
