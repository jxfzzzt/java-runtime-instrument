package edu.fdu.se.test.controller;

import edu.fdu.se.test.User;
import edu.fdu.se.test.service.UserService;
import edu.fdu.se.test.service.impl.UserServiceImpl;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    public void queryUserById(int id) {
        User user = userService.getUserById(id);
        System.out.println(user);
    }

    public void queryUserByName(String username) {
        User user = userService.getUserByUserName(username);
        System.out.println(user);
    }
}
