package edu.fdu.se.test.service.impl;

import edu.fdu.se.test.User;
import edu.fdu.se.test.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Override
    public User getUserByUserName(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }
}
