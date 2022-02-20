package com.project.GameGround;

import com.project.GameGround.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {  //interface to communicate with database
    @Query("SELECT role FROM Role role WHERE role.name=?1")
    Role findRoleByName(String name);
}
