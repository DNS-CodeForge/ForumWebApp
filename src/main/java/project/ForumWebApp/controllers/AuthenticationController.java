package project.ForumWebApp.controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.LoginDTO;
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser addUser(@RequestBody RegistrationDTO registrationDTO) {

        return authenticationService.registerUser(registrationDTO);
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body) {
        var response = authenticationService.loginUser(body.getUsername(), body.getPassword());
        response.getUser().setPassword("*********");
        return response;
    }
}
