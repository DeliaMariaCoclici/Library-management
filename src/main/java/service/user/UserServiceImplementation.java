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
        Notification<Boolean> note = new Notification<>();

        /* 1. validare */
        Role employeeRole = rightsRolesRepository.findRoleByTitle(Constants.Roles.EMPLOYEE);
        if (employeeRole == null) {
            note.addError("Rolul EMPLOYEE nu există în baza de date");
            note.setResult(Boolean.FALSE);
            return note;
        }

        User user = new UserBuilder()
                .setUsername(email)
                .setPassword(password)
                .setRoles(List.of(employeeRole))
                .build();

        UserValidator validator = new UserValidator(user);
        if (!validator.validate()) {
            validator.getErrors().forEach(note::addError);
            note.setResult(Boolean.FALSE);
            return note;
        }

        /* 2. există deja? */
        Notification<Boolean> exists = userRepository.existsByUsername(email);
        if (exists.hasErrors() || Boolean.TRUE.equals(exists.getResult())) {
            exists.getErrors().forEach(note::addError);
            note.setResult(Boolean.FALSE);
            return note;
        }

        /* 3. hash + save */
        user.setPassword(hashPassword(password));
        boolean ok = userRepository.save(user);
        note.setResult(ok);
        if (!ok) note.addError("Database error – employee not saved");
        return note;
    }

    @Override
    public Notification<Boolean> deleteByUsername(String email) {
        Notification<Boolean> note = new Notification<>();

        if (email == null || email.isBlank()) {
            note.addError("Email lipsă");
            note.setResult(Boolean.FALSE);
            return note;
        }

        boolean done = userRepository.deleteByUsername(email);
        note.setResult(done);
        if (!done) note.addError("Nu există employee cu acest email");
        return note;
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