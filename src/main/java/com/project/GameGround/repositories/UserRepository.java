package com.project.GameGround.repositories;

import com.project.GameGround.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {  //interface to communicate with database

    User getByEmail(String email);  //request to find by email

    User findUserByVerificationCode(String verificationCode);

    @Modifying
    @Query("UPDATE User user SET user.lastLoginDate = ?2 WHERE user.email = ?1")  //request to modify last login date
    void updateLoginDate(String email, String last_login_date);

    @Modifying
    @Query("UPDATE User user SET user.blocked = 1 WHERE user.id = ?1")  //request to block user
    void blockById(Long id);

    @Modifying
    @Query("UPDATE User user SET user.blocked = 0 WHERE user.id = ?1")  //request to unblock user
    void unblockById(Long id);

    @Modifying
    @Query("UPDATE User user SET user.likesQuantity=user.likesQuantity+1 WHERE user.id = ?1")  //increment user likes
    void incrementLike(Long id);

    @Modifying
    @Query("UPDATE User user SET user.likesQuantity=user.likesQuantity-1 WHERE user.id = ?1")  //increment user likes
    void decrementLike(Long id);
}
