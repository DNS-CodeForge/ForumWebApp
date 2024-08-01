package project.ForumWebApp.controllers.rest;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "Update user profile information", description = "Updates the profile information of the currently logged-in user")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public UpdateUserDTO updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUserInfo(updateUserDTO);
        return updateUserDTO;
    }

    @GetMapping("/profile/info")
    @Operation(summary = "Retrieve user profile information", description = "Fetches the profile information of the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile information retrieved successfully")
    })
    public RegistrationDTO getUserInfo() {
        return userService.viewUserInfo();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve user by ID", description = "Fetches the details of a specific user based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ApplicationUser getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    @Operation(summary = "Retrieve all users", description = "Fetches a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    public List<ApplicationUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a specific user based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
