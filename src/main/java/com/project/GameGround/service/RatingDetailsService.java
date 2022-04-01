package com.project.GameGround.service;

import com.project.GameGround.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingDetailsService {
    @Autowired
    protected RatingRepository repo;
}
