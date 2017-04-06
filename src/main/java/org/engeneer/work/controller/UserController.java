package org.engeneer.work.controller;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to display specific {@link UserEntity}.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable("id") Long userId) {
        final UserEntity user = userService.getUserById(userId);

        if (user != null) {
            return user.toString();
        }
        return "User with id: " + userId + " could not be found.";
    }

    @RequestMapping(value = "add/{username}", method = RequestMethod.GET)
    public Map<String, Object> addUser(@PathVariable("username") String username) {
        userService.saveUser(username);
        final Map<String, Object> model = new HashMap<>();

        String message = "User " + username + " added to the system.";

        model.put("message", message);
        return model;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public Map<String, Object> deleteUser(@PathVariable("userId") Long userId) {
        final UserEntity user = userService.getUserById(userId);
        final Map<String, Object> model = new HashMap<>();
        String message = "User not found or could not be deleted";

        if (user != null) {
            final String username = userService.getUserById(userId).getUsername();
            if (userService.deleteUser(userId)) {
                message = "User " + username + " removed from the system";
            }
        }

        model.put("message", message);
        return model;
    }
}
