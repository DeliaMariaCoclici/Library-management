package service.user;

import database.Constants;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class UserServiceImplementation implements UserService{
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public UserServiceImplementation(UserRepository userRepository,
                                     RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<Boolean> saveEmployee(String email, String password) {
        Notification<Boolean> saveNotification = new Notification<>();

        Role employeeRole = rightsRolesRepository.findRoleByTitle(Constants.Roles.EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(email)
                .setPassword(password)
                .setRoles(List.of(employeeRole))
                .build();

        UserValidator validator = new UserValidator(user);
        if (!validator.validate()) {
            validator.getErrors().forEach(saveNotification::addError);
            saveNotification.setResult(Boolean.FALSE);
            return saveNotification;
        }

        Notification<Boolean> exists = userRepository.existsByUsername(email);
        if (exists.hasErrors() || Boolean.TRUE.equals(exists.getResult())) {
            exists.getErrors().forEach(saveNotification::addError);
            saveNotification.setResult(Boolean.FALSE);
            return saveNotification;
        }

        user.setPassword(hashPassword(password));
        boolean ok = userRepository.save(user);
        saveNotification.setResult(ok);
        if (!ok) saveNotification.addError("Database error – employee not saved");
        return saveNotification;
    }

    @Override
    public Notification<Boolean> deleteByUsername(String email) {
        Notification<Boolean> deleteNotification = new Notification<>();

        if (email == null || email.isBlank()) {
            deleteNotification.addError("Email lipsă");
            deleteNotification.setResult(Boolean.FALSE);
            return deleteNotification;
        }

        boolean done = userRepository.deleteByUsername(email);
        deleteNotification.setResult(done);
        if (!done) deleteNotification.addError("Nu există employee cu acest email");
        return deleteNotification;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}