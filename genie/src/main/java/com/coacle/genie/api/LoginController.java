package com.coacle.genie.api;

import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.model.User;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /*
     *  Note: Spring boot provides a default "/logout" handler
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(Authentication auth) {
        try {
            User user = userService.getUser(auth.getName());
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
