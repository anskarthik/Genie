package com.coacle.genie.service;

import com.coacle.genie.exception.EmailAlreadyExistsException;
import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.jpa.UserRepository;
import com.coacle.genie.model.User;
import com.coacle.genie.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User getUser(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Transactional
    public User addUserAccount(UserDto userDto) throws EmailAlreadyExistsException {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Arrays.asList("ROLE_USER"));
        return userRepository.save(user);
    }

    @Transactional
    public void enableUserAccount(String email) throws UserNotFoundException {
        User user = getUser(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void disableUserAccount(String email) throws UserNotFoundException {
        User user = getUser(email);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public User removeUserByEmail(String email) throws UserNotFoundException {
        User user = getUser(email);
        userRepository.delete(user);
        return user;
    }

}
