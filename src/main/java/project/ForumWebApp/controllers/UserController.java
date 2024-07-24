package project.ForumWebApp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.RegistrationDTO;
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

    @PostMapping("/profile/info")
    public UpdateUserDTO updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO){
        userService.updateUserInfo(updateUserDTO);
        updateUserDTO.setPassword("********");
        return updateUserDTO;
    }

    @GetMapping("/profile/info")
    public RegistrationDTO getUserInfo() {
        return userService.viewUserInfo();
    }

    @GetMapping("/{id}")
    public ApplicationUser getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    public List<ApplicationUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("{id}")
    public void deleteUser(int id) {
        userService.deleteUser(id);
    }
}
