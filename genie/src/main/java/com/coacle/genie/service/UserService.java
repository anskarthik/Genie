package com.coacle.genie.service;

import com.coacle.genie.exception.UserAlreadyExistsExcepton;
import com.coacle.genie.exception.UserNotFoundException;
import com.coacle.genie.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String id) throws UserNotFoundException {
        Optional<User> user = users.stream().filter(u -> id.equals(u.getId())).findFirst();

        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    public String addUser(User user) throws UserAlreadyExistsExcepton {
        try {
            getUser(user.getId());
            throw new UserAlreadyExistsExcepton(user.getId());
        } catch (UserNotFoundException e) {
            users.add(user);
            return user.getId();
        }
    }

    public void removeUserById(String id) throws UserNotFoundException {
        User user = getUser(id);
        users.remove(user);
    }

}
