package com.coacle.genie.api;

import com.coacle.genie.event.OnRegistrationCompleteEvent;
import com.coacle.genie.exception.EmailAlreadyExistsException;
import com.coacle.genie.model.User;
import com.coacle.genie.model.UserDto;
import com.coacle.genie.model.VerificationToken;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Calendar;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody @Valid UserDto userDto,
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

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.notFound().build();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() <= cal.getTime().getTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("token expired. Use /resendToken to receive new token");
        }

        user.setEnabled(true);
        userService.saveUser(user);

        return ResponseEntity.ok("Account enabled");
    }
}
