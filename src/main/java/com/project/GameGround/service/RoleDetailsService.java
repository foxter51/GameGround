package com.project.GameGround.service;

import com.project.GameGround.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleDetailsService {
    @Autowired
    protected RoleRepository repo;
}
