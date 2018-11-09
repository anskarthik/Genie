package com.coacle.genie;

import com.coacle.genie.model.UserDto;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenieApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(GenieApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("admin");
        userDto.setMatchingPassword("password");
        userDto.setName("admin");
        userDto.setPassword("password");
        userService.addUserAccount(userDto);
        userService.enableUserAccount(userDto.getEmail());
    }
}
