package com.project.GameGround.service;

import com.project.GameGround.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagDetailsService {
    @Autowired
    protected TagRepository repo;
}
