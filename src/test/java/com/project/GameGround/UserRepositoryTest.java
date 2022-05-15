package com.project.GameGround;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.GameGround.entities.User;
import com.project.GameGround.repositories.RoleRepository;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.security.AuthProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)  //don't roll back after the test method has completed
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private TestEntityManager entityManager;  //useful methods for tests

    @Test
    public void testCreateUser() throws IOException {
        User user = new User();
        user.setEmail("invisiblepanda@gmail.com");
        user.setPassword(new BCryptPasswordEncoder().encode("test2022"));
        user.setFirstName("John");
        user.setLastName("Yeak");
        user.setEnabled(true);
        user.setBlocked(false);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.addRole(roleRepo.getRoleByName("USER"));
        user.setProfilePicture(Files.readAllBytes(Path.of("src/main/resources/images/ava.webp")));
        User savedUser = repo.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());
        assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByEmail(){
        String email = "invisiblepanda04@gmail.com";
        User user =  repo.getByEmail(email);
        assertThat(user).isNotNull();
    }
}