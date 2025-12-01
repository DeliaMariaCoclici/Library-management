package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.user.UserRepository;

import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.CUSTOMER;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean save(User user) {
        return userRepository.save(user);
    }

/*    public boolean delete(User user) {
        return userRepository.delete(user);
    }*/
}