package com.project.GameGround;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class GameGroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameGroundApplication.class, args);
	}

}
