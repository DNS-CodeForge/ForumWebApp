package project.ForumWebApp.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.ApplicationUser;

import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.services.UserService;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/profile/info")
    public UpdateUserDTO updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUserInfo(updateUserDTO);

        return updateUserDTO;
    }

    @GetMapping("/profile/info")
    public RegistrationDTO getUserInfo() {
        return userService.viewUserInfo();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApplicationUser getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<ApplicationUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}