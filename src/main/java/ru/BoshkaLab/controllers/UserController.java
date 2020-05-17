package ru.BoshkaLab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.BoshkaLab.entities.User;
import ru.BoshkaLab.entities.UserType;
import ru.BoshkaLab.repositories.UserRepository;
import ru.BoshkaLab.repositories.UserTypeRepository;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;

    @GetMapping("all")
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("add")
    public User add(@RequestBody Map<String, String> newUser) {
        if (!newUser.containsKey("login") ||
                !newUser.containsKey("email") ||
                !newUser.containsKey("password") ||
                !newUser.containsKey("name") ||
                !newUser.containsKey("surname") ||
                !newUser.containsKey("type_id"))
            return null;

        String login = newUser.get("login");
        String email = newUser.get("email");
        String password = newUser.get("password");
        String name = newUser.get("name");
        String surname = newUser.get("surname");
        long type_id = Long.parseLong(newUser.get("type_id"));

        UserType type = userTypeRepository.getOne(type_id);

        User user = new User(login, password, email, name, surname, type);

        return user;
    }

    @PostMapping("delete")
    public User delete(@RequestBody Map<String, String> userToDelete) {
        if (!userToDelete.containsKey("user_id"))
            return null;

        long user_id = Long.parseLong(userToDelete.get("user_id"));
        User user = userRepository.getOne(user_id);

        return user;
    }

    @GetMapping("auth")
    public String authenticate(@RequestBody Map<String, String> auth) {
        if (!auth.containsKey("email")
                || !auth.containsKey("password"))
            return null;
        String email = auth.get("email");
        String password = auth.get("password");
        return userRepository.findByEmailAndPassword(email, password) != null ? "Ok" : "Fail";
    }
}
