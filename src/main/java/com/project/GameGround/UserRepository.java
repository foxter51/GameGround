package com.project.GameGround;

import com.project.GameGround.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {  //interface to communicate with database

    @Query("SELECT user FROM User user WHERE user.email = ?1")  //request to find by email
    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User user SET user.lastLoginDate = ?2 WHERE user.email = ?1")  //request to modify last login date
    void updateLoginDate(String email, String last_login_date);

    @Modifying
    @Query("DELETE FROM User user WHERE user.id = ?1")  //request to delete user
    void deleteById(Long id);

    @Modifying
    @Query("UPDATE User user SET user.status = 'Blocked' WHERE user.id = ?1")  //request to block user
    void blockById(Long id);

    @Modifying
    @Query("UPDATE User user SET user.status = 'Unblocked' WHERE user.id = ?1")  //request to unblock user
    void unblockById(Long id);

//    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE Role role SET role_id = 1 WHERE user_id = ?1")
//    void setRoleUser(Long id);
}
