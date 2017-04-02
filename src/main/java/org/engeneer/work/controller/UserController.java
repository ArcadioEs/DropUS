package org.engeneer.work.controller;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller to display specific {@link UserEntity}.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "get/{username}", method = RequestMethod.GET)
    public String getUser(@PathVariable("username") String username) {
        UserEntity user = userService.getUserByUsername(username);

        if (user != null) {
            return user.toString();
        }
        return "User " + username + " could not be found.";
    }

    @RequestMapping(value = "add/{username}", method = RequestMethod.GET)
    public String addUser(@PathVariable("username") String username) {
        userService.saveUser(username);

        return "User " + username + " added to the system.";
    }

    @RequestMapping(value = "/all")
    public String getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();

        if(! users.isEmpty()) {
            return users.toString();
        }
        return "At this moment there are no users in the system.";
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("userId") Long userId) {
        UserEntity user = userService.getUserById(userId);

        if(user != null) {
            final String username = userService.getUserById(userId).getUsername();
            if (userService.deleteUser(userId)) {
                return "User with id " + userId + " (" + username + ") deleted.";
            }
        }
        return "User with id " + userId + " could not be found.";
    }
}
