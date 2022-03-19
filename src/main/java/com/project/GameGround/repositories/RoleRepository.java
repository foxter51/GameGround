package com.project.GameGround.repositories;

import com.project.GameGround.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Role getRoleByName(String name);

}
