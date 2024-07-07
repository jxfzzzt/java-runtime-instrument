package edu.fdu.se.test.service;

import edu.fdu.se.test.User;

public interface UserService {

    User getUserById(int id);

    User getUserByUserName(String username);
}
