package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.User;
import dev.infrastructr.deck.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/me")
    public @ResponseBody User getMe() {
        return userService.getCurrentUser();
    }
}
