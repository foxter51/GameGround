package com.project.GameGround.repositories;

import com.project.GameGround.entities.RatedBy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<RatedBy, Long> {}
