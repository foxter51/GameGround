package com.project.GameGround.service;

import com.project.GameGround.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentDetailsService {
    @Autowired
    protected CommentRepository repo;
}
