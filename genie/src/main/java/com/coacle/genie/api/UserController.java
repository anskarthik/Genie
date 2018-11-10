package com.coacle.genie.api;

import com.coacle.genie.event.OnRegistrationCompleteEvent;
import com.coacle.genie.exception.EmailAlreadyExistsException;
import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.model.User;
import com.coacle.genie.model.UserDto;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addUserAccount(@RequestBody @Valid UserDto userDto,
                                            WebRequest request) {
        try {
            User user = userService.addUserAccount(userDto);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(
                    new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getEmail())
                    .toUri();
            return ResponseEntity.created(location).build();

        } catch (EmailAlreadyExistsException e) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(e.getEmail())
                    .toUri();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .build();
        }
    }

    @GetMapping("/{email}/enable")
    public ResponseEntity<String> enableUserAccount(@PathVariable String email) {
        try {
            userService.enableUserAccount(email);
            return ResponseEntity.ok("Account: " + email + " enabled");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{email}/disable")
    public ResponseEntity<String> disableUserAccount(@PathVariable String email) {
        try {
            userService.disableUserAccount(email);
            return ResponseEntity.ok("Account: " + email + " disabled");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{email}")
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
