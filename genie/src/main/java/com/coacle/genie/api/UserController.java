package com.coacle.genie.api;

import com.coacle.genie.exception.UserAlreadyExistsExcepton;
import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.model.User;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            String id = userService.addUser(user);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(location).build();

        } catch (UserAlreadyExistsExcepton e) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(e.getUserId())
                    .toUri();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeUser(@PathVariable String id) {
        try {
            userService.removeUserById(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
