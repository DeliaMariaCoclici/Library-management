package service.user;

import model.User;
import model.validation.Notification;

import java.util.List;

public interface UserService {
    List<User> findAll();
    Notification<Boolean> saveEmployee(String email, String password);
    Notification<Boolean> deleteByUsername(String email);
}