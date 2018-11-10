package com.coacle.genie.api;

import com.coacle.genie.model.User;
import com.coacle.genie.model.VerificationToken;
import com.coacle.genie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = service.getVerificationToken(token);
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
        service.saveUser(user);

        return ResponseEntity.ok("Account enabled");
    }
}
