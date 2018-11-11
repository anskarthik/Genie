package com.coacle.genie.api;

import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.model.User;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
@RolesAllowed("ROLE_USER")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{email}")
    @PreAuthorize("#email == authentication.principal.username")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("#email == authentication.principal.username")
    public ResponseEntity<?> removeUserAccount(HttpServletRequest request, @PathVariable String email) {
        try {
            User user = userService.removeUserByEmail(email);
            HttpSession session =
                    request.getSession();
            session.invalidate();
            return ResponseEntity.ok("User account: " + email + " deleted");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
