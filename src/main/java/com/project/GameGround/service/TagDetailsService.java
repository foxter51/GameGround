package com.project.GameGround.service;

import com.project.GameGround.entities.Tag;
import com.project.GameGround.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TagDetailsService {
    @Autowired
    protected TagRepository repo;

    public Set<Tag> getLast6Tags(){
        Set<Tag> last6tags = repo.findFirst6ByOrderByIdDesc();
        return last6tags.size()>0 ? last6tags : null;
    }
}
