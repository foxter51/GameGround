package com.project.GameGround.repositories;

import com.project.GameGround.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {}
