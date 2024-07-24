package project.ForumWebApp.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.DTOs.UpdateUserDTO;
import project.ForumWebApp.services.UserService;

@RestController
@RequestMapping("user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UpdateUserDTO updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO){
        userService.updateUserInfo(updateUserDTO);
        updateUserDTO.setPassword("********");
        return updateUserDTO;
    }
}
